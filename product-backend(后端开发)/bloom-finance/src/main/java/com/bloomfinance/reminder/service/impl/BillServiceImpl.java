package com.bloomfinance.reminder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.exception.ResourceNotFoundException;
import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.reminder.domain.dto.BillQuery;
import com.bloomfinance.reminder.domain.dto.CreateBillDTO;
import com.bloomfinance.reminder.domain.dto.UpdateBillDTO;
import com.bloomfinance.reminder.domain.entity.BillReminderEntity;
import com.bloomfinance.reminder.domain.enums.BillTypeEnum;
import com.bloomfinance.reminder.domain.vo.BillVO;
import com.bloomfinance.reminder.repository.BillReminderRepository;
import com.bloomfinance.reminder.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账单服务实现类
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillReminderRepository billRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBill(Long userId, CreateBillDTO dto) {
        log.info("创建账单, userId={}, accountId={}, billType={}, amount={}",
                userId, dto.getAccountId(), dto.getBillType(), dto.getAmount());

        // 校验账单类型
        BillTypeEnum billTypeEnum = BillTypeEnum.fromCode(dto.getBillType());
        if (billTypeEnum == null) {
            throw new BusinessException(400, "无效的账单类型");
        }

        // 构建实体
        BillReminderEntity entity = new BillReminderEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        entity.setStatus(1); // 待还
        entity.setRemindStatus(0); // 未提醒

        // 保存
        billRepository.insert(entity);
        log.info("创建账单成功, userId={}, billId={}", userId, entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBill(Long userId, Long billId, UpdateBillDTO dto) {
        log.info("更新账单, userId={}, billId={}", userId, billId);

        BillReminderEntity entity = billRepository.findByIdAndUserId(billId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("账单不存在");
        }

        // 更新字段
        if (dto.getBillDate() != null) {
            entity.setBillDate(dto.getBillDate());
        }
        if (dto.getDueDate() != null) {
            entity.setDueDate(dto.getDueDate());
        }
        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        billRepository.updateById(entity);
        log.info("更新账单成功, userId={}, billId={}", userId, billId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBill(Long billId) {
        log.info("删除账单, billId={}", billId);

        BillReminderEntity entity = billRepository.selectById(billId);
        if (entity == null) {
            throw new ResourceNotFoundException("账单不存在");
        }

        billRepository.deleteById(billId);
        log.info("删除账单成功, billId={}", billId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsPaid(Long userId, Long billId) {
        log.info("标记账单已还款, userId={}, billId={}", userId, billId);

        BillReminderEntity entity = billRepository.findByIdAndUserId(billId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("账单不存在");
        }

        entity.setStatus(2); // 已还
        billRepository.updateById(entity);
        log.info("标记账单已还款成功, userId={}, billId={}", userId, billId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public BillVO getBillById(Long userId, Long billId) {
        log.info("获取账单详情, userId={}, billId={}", userId, billId);

        BillReminderEntity entity = billRepository.findByIdAndUserId(billId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("账单不存在");
        }

        return convertToVO(entity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public PageResult<BillVO> pageBills(Long userId, BillQuery query) {
        log.info("分页查询账单, userId={}, query={}", userId, query);

        Page<BillReminderEntity> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<BillReminderEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BillReminderEntity::getUserId, userId);

        if (query.getAccountId() != null) {
            wrapper.eq(BillReminderEntity::getAccountId, query.getAccountId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(BillReminderEntity::getStatus, query.getStatus());
        }
        if (query.getStartDueDate() != null) {
            wrapper.ge(BillReminderEntity::getDueDate, query.getStartDueDate());
        }
        if (query.getEndDueDate() != null) {
            wrapper.le(BillReminderEntity::getDueDate, query.getEndDueDate());
        }

        wrapper.orderByDesc(BillReminderEntity::getDueDate);
        IPage<BillReminderEntity> resultPage = billRepository.selectPage(page, wrapper);

        List<BillVO> voList = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<BillVO> pageResult = new PageResult<>();
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
    public List<BillVO> getUpcomingBills(Long userId, Integer days) {
        log.info("获取即将到期的账单, userId={}, days={}", userId, days);

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(days);

        List<BillReminderEntity> entities = billRepository.findUpcomingBills(userId, start, end);
        return entities.stream()
                .filter(e -> e.getStatus() == 1) // 只返回待还账单
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<BillVO> getDueSoonBills(Long userId) {
        log.info("获取即将到期（7天内）的账单, userId={}", userId);

        List<BillReminderEntity> entities = billRepository.findDueSoonBills(userId, 7);
        return entities.stream()
                .filter(e -> e.getStatus() == 1) // 只返回待还账单
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<BillVO> getOverdueBills(Long userId) {
        log.info("获取已逾期账单, userId={}", userId);

        List<BillReminderEntity> entities = billRepository.findOverdueBills(userId);
        return entities.stream()
                .filter(e -> e.getStatus() != 2) // 排除已还账单
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 实体转换为VO
     */
    private BillVO convertToVO(BillReminderEntity entity) {
        BillVO vo = new BillVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置账单类型名称
        BillTypeEnum billTypeEnum = BillTypeEnum.fromCode(entity.getBillType());
        if (billTypeEnum != null) {
            vo.setBillTypeName(billTypeEnum.getDescription());
        }

        // 设置状态名称
        vo.setStatusName(getStatusName(entity.getStatus()));

        // 计算距离到期天数
        if (entity.getDueDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), entity.getDueDate());
            vo.setDaysUntilDue((int) days);
            vo.setIsOverdue(days < 0 && entity.getStatus() != 2);
        } else {
            vo.setDaysUntilDue(0);
            vo.setIsOverdue(false);
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
                return "待还";
            case 2:
                return "已还";
            case 3:
                return "逾期";
            default:
                return "未知";
        }
    }
}