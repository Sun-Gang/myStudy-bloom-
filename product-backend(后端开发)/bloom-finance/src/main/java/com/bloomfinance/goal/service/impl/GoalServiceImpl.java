package com.bloomfinance.goal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.exception.ResourceNotFoundException;
import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.goal.domain.dto.CreateGoalDTO;
import com.bloomfinance.goal.domain.dto.GoalQuery;
import com.bloomfinance.goal.domain.dto.UpdateGoalDTO;
import com.bloomfinance.goal.domain.entity.GoalAccountRelEntity;
import com.bloomfinance.goal.domain.entity.GoalEntity;
import com.bloomfinance.goal.domain.enums.GoalTypeEnum;
import com.bloomfinance.goal.domain.vo.GoalProgressVO;
import com.bloomfinance.goal.domain.vo.GoalVO;
import com.bloomfinance.goal.repository.GoalAccountRelRepository;
import com.bloomfinance.goal.repository.GoalRepository;
import com.bloomfinance.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目标服务实现类
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GoalAccountRelRepository goalAccountRelRepository;

    private static final int MAX_PROBABILITY = 100;
    private static final int INITIAL_PROBABILITY = 30;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGoal(Long userId, CreateGoalDTO dto) {
        log.info("创建目标, userId={}, goalName={}, goalType={}", userId, dto.getGoalName(), dto.getGoalType());

        // 校验目标类型
        GoalTypeEnum goalTypeEnum = GoalTypeEnum.fromCode(dto.getGoalType());
        if (goalTypeEnum == null) {
            throw new BusinessException(400, "无效的目标类型");
        }

        // 计算每月建议金额
        BigDecimal monthlySuggestedAmount = calculateMonthlySuggestedAmount(
                dto.getTargetAmount(),
                dto.getTargetDate()
        );

        // 计算初始概率
        Integer probability = calculateProbability(
                dto.getCurrentAmount() != null ? dto.getCurrentAmount() : BigDecimal.ZERO,
                dto.getTargetAmount(),
                dto.getTargetDate()
        );

        // 构建实体
        GoalEntity entity = new GoalEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        entity.setCurrentAmount(dto.getCurrentAmount() != null ? dto.getCurrentAmount() : BigDecimal.ZERO);
        entity.setMonthlySuggestedAmount(monthlySuggestedAmount);
        entity.setProbability(probability);
        entity.setStatus(1); // 进行中

        // 保存
        goalRepository.insert(entity);
        log.info("创建目标成功, userId={}, goalId={}", userId, entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGoal(Long userId, Long goalId, UpdateGoalDTO dto) {
        log.info("更新目标, userId={}, goalId={}", userId, goalId);

        GoalEntity entity = goalRepository.findByIdAndUserId(goalId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("目标不存在");
        }

        // 更新字段
        if (dto.getGoalName() != null && !dto.getGoalName().isEmpty()) {
            entity.setGoalName(dto.getGoalName());
        }
        if (dto.getTargetAmount() != null) {
            entity.setTargetAmount(dto.getTargetAmount());
        }
        if (dto.getTargetDate() != null) {
            entity.setTargetDate(dto.getTargetDate());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        // 重新计算每月建议金额和概率
        entity.setMonthlySuggestedAmount(calculateMonthlySuggestedAmount(
                entity.getTargetAmount(),
                entity.getTargetDate()
        ));
        entity.setProbability(calculateProbability(
                entity.getCurrentAmount(),
                entity.getTargetAmount(),
                entity.getTargetDate()
        ));

        goalRepository.updateById(entity);
        log.info("更新目标成功, userId={}, goalId={}", userId, goalId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGoal(Long userId, Long goalId) {
        log.info("删除目标, userId={}, goalId={}", userId, goalId);

        GoalEntity entity = goalRepository.findByIdAndUserId(goalId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("目标不存在");
        }

        // 逻辑删除
        goalRepository.deleteById(goalId);

        // 删除关联关系
        List<GoalAccountRelEntity> relations = goalAccountRelRepository.findByGoalId(goalId);
        for (GoalAccountRelEntity rel : relations) {
            goalAccountRelRepository.deleteById(rel.getId());
        }

        log.info("删除目标成功, userId={}, goalId={}", userId, goalId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public GoalVO getGoalById(Long userId, Long goalId) {
        log.info("获取目标详情, userId={}, goalId={}", userId, goalId);

        GoalEntity entity = goalRepository.findByIdAndUserId(goalId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("目标不存在");
        }

        return convertToVO(entity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public PageResult<GoalVO> listGoals(Long userId, GoalQuery query) {
        log.info("查询目标列表, userId={}, query={}", userId, query);

        Page<GoalEntity> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<GoalEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GoalEntity::getUserId, userId);

        if (query.getGoalType() != null) {
            wrapper.eq(GoalEntity::getGoalType, query.getGoalType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(GoalEntity::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(GoalEntity::getCreatedAt);
        IPage<GoalEntity> resultPage = goalRepository.selectPage(page, wrapper);

        List<GoalVO> voList = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<GoalVO> pageResult = new PageResult<>();
        pageResult.setRecords(voList);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setSize(resultPage.getSize());
        pageResult.setCurrent(resultPage.getCurrent());
        pageResult.setPages(resultPage.getPages());
        pageResult.setCode(200);
        pageResult.setMessage("success");

        return pageResult;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public GoalProgressVO getGoalProgress(Long userId, Long goalId) {
        log.info("获取目标进度, userId={}, goalId={}", userId, goalId);

        GoalEntity entity = goalRepository.findByIdAndUserId(goalId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("目标不存在");
        }

        return calculateGoalProgress(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void associateAccount(Long userId, Long goalId, Long accountId) {
        log.info("关联账户到目标, userId={}, goalId={}, accountId={}", userId, goalId, accountId);

        GoalEntity entity = goalRepository.findByIdAndUserId(goalId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("目标不存在");
        }

        // 检查是否已有关联
        if (goalAccountRelRepository.existsByGoalIdAndAccountId(goalId, accountId)) {
            throw new BusinessException(409, "该账户已关联到此目标");
        }

        GoalAccountRelEntity rel = new GoalAccountRelEntity();
        rel.setGoalId(goalId);
        rel.setAccountId(accountId);
        goalAccountRelRepository.insert(rel);

        log.info("关联账户成功, goalId={}, accountId={}", goalId, accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disassociateAccount(Long userId, Long goalId, Long accountId) {
        log.info("取消目标与账户关联, userId={}, goalId={}, accountId={}", userId, goalId, accountId);

        GoalEntity entity = goalRepository.findByIdAndUserId(goalId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("目标不存在");
        }

        goalAccountRelRepository.deleteByGoalIdAndAccountId(goalId, accountId);

        log.info("取消关联成功, goalId={}, accountId={}", goalId, accountId);
    }

    /**
     * 计算每月建议金额
     *
     * @param targetAmount 目标金额
     * @param targetDate   目标日期
     * @return 每月建议金额
     */
    private BigDecimal calculateMonthlySuggestedAmount(BigDecimal targetAmount, LocalDate targetDate) {
        if (targetAmount == null || targetDate == null) {
            return BigDecimal.ZERO;
        }

        long months = ChronoUnit.MONTHS.between(LocalDate.now(), targetDate);
        if (months <= 0) {
            return targetAmount;
        }

        return targetAmount.divide(BigDecimal.valueOf(months), 2, RoundingMode.UP);
    }

    /**
     * 计算完成概率
     *
     * @param currentAmount 当前金额
     * @param targetAmount  目标金额
     * @param targetDate    目标日期
     * @return 完成概率 (0-100)
     */
    private Integer calculateProbability(BigDecimal currentAmount, BigDecimal targetAmount, LocalDate targetDate) {
        if (currentAmount == null || targetAmount == null || targetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return INITIAL_PROBABILITY;
        }

        if (targetDate == null) {
            return INITIAL_PROBABILITY;
        }

        // 基础概率：当前进度
        BigDecimal progress = currentAmount.multiply(BigDecimal.valueOf(100))
                .divide(targetAmount, 2, RoundingMode.HALF_UP);
        int baseProbability = progress.intValue();

        // 时间因素：剩余时间越长，概率越低
        long months = ChronoUnit.MONTHS.between(LocalDate.now(), targetDate);
        if (months <= 0) {
            return Math.min(baseProbability, 50); // 即将到期但未完成，概率不超过50%
        }

        // 每年增加5%基础概率
        int timeAdjustment = (int) (months / 12);
        int adjustedProbability = Math.min(baseProbability + timeAdjustment * 5, MAX_PROBABILITY);

        return Math.max(adjustedProbability, 1);
    }

    /**
     * 计算目标进度
     *
     * @param entity 目标实体
     * @return 目标进度VO
     */
    private GoalProgressVO calculateGoalProgress(GoalEntity entity) {
        GoalProgressVO vo = new GoalProgressVO();
        vo.setGoalId(entity.getId());
        vo.setGoalName(entity.getGoalName());
        vo.setTargetAmount(entity.getTargetAmount());
        vo.setCurrentAmount(entity.getCurrentAmount());

        // 计算进度百分比
        BigDecimal progress = BigDecimal.ZERO;
        if (entity.getTargetAmount() != null && entity.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            progress = entity.getCurrentAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(entity.getTargetAmount(), 2, RoundingMode.HALF_UP);
        }
        vo.setProgress(progress);

        // 计算剩余金额
        BigDecimal remainingAmount = entity.getTargetAmount().subtract(entity.getCurrentAmount());
        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            remainingAmount = BigDecimal.ZERO;
        }
        vo.setRemainingAmount(remainingAmount);

        // 计算剩余月份
        long months = 0;
        if (entity.getTargetDate() != null) {
            months = ChronoUnit.MONTHS.between(LocalDate.now(), entity.getTargetDate());
            if (months < 0) {
                months = 0;
            }
        }
        vo.setRemainingMonths((int) months);

        // 判断是否按计划进行
        boolean isOnTrack = checkIfOnTrack(entity, progress.intValue());

        // 生成建议
        List<String> suggestions = generateSuggestions(entity, progress.intValue(), isOnTrack);
        vo.setSuggestions(suggestions);
        vo.setIsOnTrack(isOnTrack);

        return vo;
    }

    /**
     * 检查是否按计划进行
     */
    private boolean checkIfOnTrack(GoalEntity entity, int currentProgress) {
        if (entity.getTargetDate() == null || entity.getTargetAmount() == null) {
            return true;
        }

        long totalMonths = ChronoUnit.MONTHS.between(LocalDate.now(), entity.getTargetDate());
        if (totalMonths <= 0) {
            return currentProgress >= 100;
        }

        // 期望进度 = (已过时间 / 总时间) * 100
        long now = LocalDate.now().toEpochDay();
        long start = entity.getCreatedAt().toLocalDate().toEpochDay();
        long end = entity.getTargetDate().toEpochDay();

        if (end <= start) {
            return currentProgress >= 100;
        }

        long passedDays = now - start;
        long totalDays = end - start;

        int expectedProgress = (int) ((passedDays * 100) / totalDays);

        return currentProgress >= expectedProgress - 10; // 允许10%的误差
    }

    /**
     * 生成建议
     */
    private List<String> generateSuggestions(GoalEntity entity, int progress, boolean isOnTrack) {
        List<String> suggestions = new ArrayList<>();

        if (!isOnTrack) {
            suggestions.add("当前进度落后于计划，建议增加每月储蓄金额");
        }

        if (entity.getMonthlySuggestedAmount() != null && entity.getMonthlySuggestedAmount().compareTo(BigDecimal.ZERO) > 0) {
            suggestions.add("建议每月储蓄: " + entity.getMonthlySuggestedAmount().toPlainString() + " 元");
        }

        if (progress < 50) {
            suggestions.add("继续保持当前储蓄节奏，按计划达成目标");
        } else if (progress >= 50 && progress < 100) {
            suggestions.add("目标已过半，继续保持并考虑增加投入");
        } else if (progress >= 100) {
            suggestions.add("恭喜！目标已达成");
        }

        return suggestions;
    }

    /**
     * 实体转换为VO
     */
    private GoalVO convertToVO(GoalEntity entity) {
        GoalVO vo = new GoalVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置目标类型名称
        GoalTypeEnum goalTypeEnum = GoalTypeEnum.fromCode(entity.getGoalType());
        if (goalTypeEnum != null) {
            vo.setGoalTypeName(goalTypeEnum.getDescription());
        }

        // 设置状态名称
        vo.setStatusName(getStatusName(entity.getStatus()));

        // 计算进度百分比
        if (entity.getTargetAmount() != null && entity.getTargetAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal progress = entity.getCurrentAmount()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(entity.getTargetAmount(), 2, RoundingMode.HALF_UP);
            vo.setProgress(progress);
        } else {
            vo.setProgress(BigDecimal.ZERO);
        }

        return vo;
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "进行中";
            case 2:
                return "已完成";
            case 3:
                return "已放弃";
            default:
                return "未知";
        }
    }
}