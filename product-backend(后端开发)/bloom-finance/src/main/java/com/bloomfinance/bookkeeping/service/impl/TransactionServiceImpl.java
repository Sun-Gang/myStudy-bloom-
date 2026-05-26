package com.bloomfinance.bookkeeping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloomfinance.account.domain.entity.AccountEntity;
import com.bloomfinance.account.repository.AccountRepository;
import com.bloomfinance.bookkeeping.domain.dto.CreateTransactionDTO;
import com.bloomfinance.bookkeeping.domain.dto.TransactionQuery;
import com.bloomfinance.bookkeeping.domain.entity.BookkeepingTransactionEntity;
import com.bloomfinance.bookkeeping.domain.enums.TransactionTypeEnum;
import com.bloomfinance.bookkeeping.domain.vo.BookkeepingSummaryVO;
import com.bloomfinance.bookkeeping.domain.vo.TransactionVO;
import com.bloomfinance.bookkeeping.repository.BookkeepingTransactionRepository;
import com.bloomfinance.bookkeeping.service.TransactionService;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.exception.ResourceNotFoundException;
import com.bloomfinance.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交易记录服务实现类
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final BookkeepingTransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTransaction(Long userId, CreateTransactionDTO dto) {
        log.info("创建交易记录, userId={}, accountId={}, amount={}, type={}",
                userId, dto.getAccountId(), dto.getAmount(), dto.getTransactionType());

        // 校验交易类型
        TransactionTypeEnum typeEnum = TransactionTypeEnum.fromCode(dto.getTransactionType());
        if (typeEnum == null) {
            throw new BusinessException(400, "无效的交易类型");
        }

        // 校验账户存在且属于该用户
        AccountEntity account = accountRepository.findByIdAndUserId(dto.getAccountId(), userId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        // 构建实体
        BookkeepingTransactionEntity entity = new BookkeepingTransactionEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);

        // 保存
        transactionRepository.insert(entity);
        log.info("创建交易记录成功, userId={}, transactionId={}", userId, entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransaction(Long userId, Long transactionId, CreateTransactionDTO dto) {
        log.info("更新交易记录, userId={}, transactionId={}", userId, transactionId);

        BookkeepingTransactionEntity entity = findByIdAndUserId(transactionId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("交易记录不存在");
        }

        // 更新字段
        if (dto.getAccountId() != null) {
            entity.setAccountId(dto.getAccountId());
        }
        if (dto.getAmount() != null) {
            entity.setAmount(dto.getAmount());
        }
        if (dto.getTransactionType() != null) {
            TransactionTypeEnum typeEnum = TransactionTypeEnum.fromCode(dto.getTransactionType());
            if (typeEnum == null) {
                throw new BusinessException(400, "无效的交易类型");
            }
            entity.setTransactionType(dto.getTransactionType());
        }
        if (dto.getCategoryId() != null) {
            entity.setCategoryId(dto.getCategoryId());
        }
        if (dto.getMerchantName() != null) {
            entity.setMerchantName(dto.getMerchantName());
        }
        if (dto.getMccCode() != null) {
            entity.setMccCode(dto.getMccCode());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getTransactionDate() != null) {
            entity.setTransactionDate(dto.getTransactionDate());
        }

        transactionRepository.updateById(entity);
        log.info("更新交易记录成功, userId={}, transactionId={}", userId, transactionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTransaction(Long userId, Long transactionId) {
        log.info("删除交易记录, userId={}, transactionId={}", userId, transactionId);

        BookkeepingTransactionEntity entity = findByIdAndUserId(transactionId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("交易记录不存在");
        }

        // 逻辑删除
        transactionRepository.deleteById(transactionId);
        log.info("删除交易记录成功, userId={}, transactionId={}", userId, transactionId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public TransactionVO getTransactionById(Long userId, Long transactionId) {
        log.info("获取交易记录详情, userId={}, transactionId={}", userId, transactionId);

        BookkeepingTransactionEntity entity = findByIdAndUserId(transactionId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("交易记录不存在");
        }

        return convertToVO(entity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public PageResult<TransactionVO> pageTransactions(Long userId, TransactionQuery query) {
        log.info("分页查询交易记录, userId={}, query={}", userId, query);

        Page<BookkeepingTransactionEntity> page = new Page<>(query.getCurrent(), query.getSize());
        IPage<BookkeepingTransactionEntity> pageResult = transactionRepository.findByUserId(userId, page, query);

        List<TransactionVO> voList = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, pageResult.getTotal(), query.getCurrent(), query.getSize());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public List<TransactionVO> syncFromBank(Long userId, Long accountId) {
        log.info("从银行同步交易记录, userId={}, accountId={}", userId, accountId);

        // 校验账户存在且属于该用户
        AccountEntity account = accountRepository.findByIdAndUserId(accountId, userId);
        if (account == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        // TODO: 调用银行API获取交易记录
        // 这里模拟返回空列表，实际需要接入银行API
        log.info("从银行同步交易记录完成, userId={}, accountId={}, count=0", userId, accountId);

        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public BookkeepingSummaryVO getBookkeepingSummary(Long userId, String yearMonth) {
        log.info("获取记账汇总信息, userId={}, yearMonth={}", userId, yearMonth);

        YearMonth ym = YearMonth.now();
        if (yearMonth != null && !yearMonth.isEmpty()) {
            try {
                ym = YearMonth.parse(yearMonth);
            } catch (Exception e) {
                log.warn("无效的年月格式: {}", yearMonth);
            }
        }

        LocalDateTime startOfMonth = ym.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = ym.atEndOfMonth().atTime(23, 59, 59);
        Date startDate = Date.from(startOfMonth.atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(endOfMonth.atZone(ZoneId.systemDefault()).toInstant());

        // 计算月度支出
        BigDecimal monthlyExpense = transactionRepository.sumAmountByUserIdAndType(userId, 2, startDate, endDate);
        if (monthlyExpense == null) {
            monthlyExpense = BigDecimal.ZERO;
        }

        // 计算月度收入
        BigDecimal monthlyIncome = transactionRepository.sumAmountByUserIdAndType(userId, 1, startDate, endDate);
        if (monthlyIncome == null) {
            monthlyIncome = BigDecimal.ZERO;
        }

        // 计算日均支出
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        BigDecimal dailyAverageExpense = BigDecimal.ZERO;
        if (dayOfMonth > 0) {
            dailyAverageExpense = monthlyExpense.abs().divide(new BigDecimal(dayOfMonth), 2, java.math.RoundingMode.HALF_UP);
        }

        // 查询当月所有交易记录
        TransactionQuery query = new TransactionQuery();
        query.setStartDate(startOfMonth);
        query.setEndDate(endOfMonth);
        query.setCurrent(1);
        query.setSize(100);

        Page<BookkeepingTransactionEntity> page = new Page<>(1, 100);
        IPage<BookkeepingTransactionEntity> pageResult = transactionRepository.findByUserId(userId, page, query);

        List<TransactionVO> allTransactions = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 按日期分组
        Map<String, List<TransactionVO>> groupedByDate = allTransactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTransactionDate() != null ? t.getTransactionDate().toLocalDate().toString() : "",
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // 转换为DailyTransactionVO列表
        List<BookkeepingSummaryVO.DailyTransactionVO> dailyTransactions = groupedByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = LocalDate.parse(entry.getKey());
                    String dateTitle = formatDateTitle(date);
                    return BookkeepingSummaryVO.DailyTransactionVO.builder()
                            .dateTitle(dateTitle)
                            .dateString(entry.getKey())
                            .transactions(entry.getValue())
                            .build();
                })
                .collect(Collectors.toList());

        // 计算预算状态
        String budgetStatus = calculateBudgetStatus(monthlyExpense);

        // 待报销金额（暂时设为0，实际需要根据业务逻辑计算）
        BigDecimal reimbursableAmount = BigDecimal.ZERO;
        int reimbursableCount = 0;

        return BookkeepingSummaryVO.builder()
                .monthlyExpense(monthlyExpense.abs())
                .monthlyIncome(monthlyIncome)
                .dailyAverageExpense(dailyAverageExpense)
                .reimbursableAmount(reimbursableAmount)
                .reimbursableCount(reimbursableCount)
                .transactions(dailyTransactions)
                .monthInfo(ym.format(DateTimeFormatter.ofPattern("yyyy年MM月")))
                .budgetStatus(budgetStatus)
                .build();
    }

    private String formatDateTitle(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        if (date.equals(today)) {
            return "今天，" + date.format(DateTimeFormatter.ofPattern("MM月dd日"));
        } else if (date.equals(yesterday)) {
            return "昨天，" + date.format(DateTimeFormatter.ofPattern("MM月dd日"));
        } else {
            return date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        }
    }

    private String calculateBudgetStatus(BigDecimal expense) {
        // 简化实现：假设月度预算为10000元
        BigDecimal budget = new BigDecimal("10000");
        BigDecimal percentage = expense.divide(budget, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

        if (percentage.compareTo(new BigDecimal("80")) <= 0) {
            return "进度正常";
        } else if (percentage.compareTo(new BigDecimal("100")) <= 0) {
            return "接近预算";
        } else {
            return "超出预算";
        }
    }

    /**
     * 根据用户ID和交易记录ID查询
     *
     * @param transactionId 交易记录ID
     * @param userId        用户ID
     * @return 交易记录实体
     */
    private BookkeepingTransactionEntity findByIdAndUserId(Long transactionId, Long userId) {
        LambdaQueryWrapper<BookkeepingTransactionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookkeepingTransactionEntity::getId, transactionId)
                .eq(BookkeepingTransactionEntity::getUserId, userId);
        return transactionRepository.selectOne(wrapper);
    }

    /**
     * 实体转换为VO
     *
     * @param entity 交易记录实体
     * @return 交易记录VO
     */
    private TransactionVO convertToVO(BookkeepingTransactionEntity entity) {
        TransactionVO vo = new TransactionVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置交易类型名称
        TransactionTypeEnum typeEnum = TransactionTypeEnum.fromCode(entity.getTransactionType());
        if (typeEnum != null) {
            vo.setTransactionTypeName(typeEnum.getDescription());
        }

        // 获取账户名称
        if (entity.getAccountId() != null) {
            AccountEntity account = accountRepository.selectById(entity.getAccountId());
            if (account != null) {
                vo.setAccountName(account.getBankName());
            }
        }

        return vo;
    }
}