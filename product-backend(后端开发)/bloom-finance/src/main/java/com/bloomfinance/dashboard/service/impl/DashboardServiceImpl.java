package com.bloomfinance.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bloomfinance.account.repository.AccountRepository;
import com.bloomfinance.bill.repository.BillRepository;
import com.bloomfinance.bill.domain.entity.BillEntity;
import com.bloomfinance.dashboard.domain.entity.BudgetSettingEntity;
import com.bloomfinance.dashboard.domain.vo.*;
import com.bloomfinance.dashboard.repository.BudgetSettingMapper;
import com.bloomfinance.dashboard.service.DashboardService;
import com.bloomfinance.transaction.repository.TransactionRepository;
import com.bloomfinance.transaction.domain.entity.TransactionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 仪表盘服务实现类
 *
 * @author bloom-finance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BillRepository billRepository;
    private final BudgetSettingMapper budgetSettingMapper;

    @Override
    public DashboardVO getDashboard(Long userId) {
        log.info("获取仪表盘数据, userId={}", userId);

        // 1. 计算总净资产
        BigDecimal totalNetWorth = calculateTotalNetWorth(userId);

        // 2. 计算本月增长百分比
        BigDecimal monthGrowthRate = calculateMonthGrowthRate(userId, totalNetWorth);

        // 3. 计算财务健康分
        Integer healthScore = calculateHealthScore(userId);
        String healthDescription = getHealthDescription(healthScore);

        // 4. 获取本月收入支出
        BigDecimal monthIncome = BigDecimal.ZERO;
        BigDecimal monthExpense = BigDecimal.ZERO;
        Integer incomeCount = 0;
        Integer expenseCount = 0;
        List<TransactionEntity> monthTransactions = getMonthTransactions(userId);
        for (TransactionEntity tx : monthTransactions) {
            if (tx.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                monthIncome = monthIncome.add(tx.getAmount());
                incomeCount++;
            } else {
                monthExpense = monthExpense.add(tx.getAmount().abs());
                expenseCount++;
            }
        }

        // 5. 获取近期账单
        List<BillReminderVO> billReminders = getBillReminders(userId);

        // 6. 获取预算提醒
        List<BudgetAlertVO> budgetAlerts = getBudgetAlerts(userId);

        // 7. 获取净资产趋势（近7天）
        List<TrendPointVO> netWorthTrend = getNetWorthTrend(userId, totalNetWorth);

        return DashboardVO.builder()
                .totalNetWorth(totalNetWorth)
                .monthGrowthRate(monthGrowthRate)
                .healthScore(healthScore)
                .healthDescription(healthDescription)
                .monthIncome(monthIncome)
                .monthExpense(monthExpense)
                .incomeCount(incomeCount)
                .expenseCount(expenseCount)
                .billReminders(billReminders)
                .budgetAlerts(budgetAlerts)
                .netWorthTrend(netWorthTrend)
                .build();
    }

    /**
     * 计算总净资产
     */
    private BigDecimal calculateTotalNetWorth(Long userId) {
        BigDecimal total = accountRepository.sumBalanceByUserId(userId);
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * 计算本月增长百分比
     */
    private BigDecimal calculateMonthGrowthRate(Long userId, BigDecimal currentNetWorth) {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal lastMonthEndNetWorth = accountRepository.sumNetWorthAtTime(userId, endOfLastMonth);

        if (lastMonthEndNetWorth == null || lastMonthEndNetWorth.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return currentNetWorth.subtract(lastMonthEndNetWorth)
                .divide(lastMonthEndNetWorth, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(1, RoundingMode.HALF_UP);
    }

    /**
     * 计算财务健康分
     */
    private Integer calculateHealthScore(Long userId) {
        int score = 75; // 基础分

        // 1. 储蓄率评分 (满分20)
        BigDecimal monthIncome = getMonthTotalIncome(userId);
        BigDecimal monthExpense = getMonthTotalExpense(userId);
        if (monthIncome.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal savingRate = monthIncome.subtract(monthExpense).divide(monthIncome, 4, RoundingMode.HALF_UP);
            if (savingRate.compareTo(new BigDecimal("0.2")) >= 0) {
                score += 20;
            } else if (savingRate.compareTo(new BigDecimal("0.1")) >= 0) {
                score += 10;
            } else {
                score += 5;
            }
        }

        // 2. 账户多样性评分 (满分10)
        Long accountCount = accountRepository.countByUserId(userId);
        if (accountCount >= 3) {
            score += 10;
        } else if (accountCount >= 2) {
            score += 5;
        }

        // 3. 账单管理评分 (满分10)
        Integer overdueBills = billRepository.countOverdueBills(userId);
        if (overdueBills == 0) {
            score += 10;
        } else {
            score -= overdueBills * 5;
        }

        return Math.max(0, Math.min(100, score));
    }

    /**
     * 获取健康状态描述
     */
    private String getHealthDescription(Integer score) {
        if (score >= 90) {
            return "太棒了！你的财务状况非常健康。";
        } else if (score >= 80) {
            return "太棒了！你的储蓄正在稳步增长。";
        } else if (score >= 70) {
            return "不错！你的财务状况良好。";
        } else if (score >= 60) {
            return "还可以，但有进步空间。";
        } else {
            return "需要改善，建议增加储蓄比例。";
        }
    }

    /**
     * 获取本月所有交易
     */
    private List<TransactionEntity> getMonthTransactions(Long userId) {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);
        return transactionRepository.findByUserIdAndDateRange(userId, startOfMonth, endOfMonth);
    }

    /**
     * 获取本月总收入
     */
    private BigDecimal getMonthTotalIncome(Long userId) {
        BigDecimal total = transactionRepository.sumIncomeByUserIdAndMonth(userId, YearMonth.now());
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * 获取本月总支出
     */
    private BigDecimal getMonthTotalExpense(Long userId) {
        BigDecimal total = transactionRepository.sumExpenseByUserIdAndMonth(userId, YearMonth.now());
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * 获取近期账单提醒
     */
    private List<BillReminderVO> getBillReminders(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);

        List<BillEntity> upcomingBills = billRepository.findUpcomingBills(userId, today, oneWeekLater);
        List<BillReminderVO> reminders = new ArrayList<>();

        for (BillEntity bill : upcomingBills) {
            long daysUntilDue = ChronoUnit.DAYS.between(today, bill.getDueDate());
            String billName = bill.getBillType() == 1 ? "信用卡" : "贷款";
            String icon = bill.getBillType() == 1 ? "credit_card" : "account_balance";

            reminders.add(BillReminderVO.builder()
                    .billId(bill.getId())
                    .billName(billName)
                    .daysUntilDue((int) daysUntilDue)
                    .amount(bill.getAmount())
                    .icon(icon)
                    .build());
        }

        return reminders;
    }

    /**
     * 获取预算提醒
     */
    private List<BudgetAlertVO> getBudgetAlerts(Long userId) {
        try {
            String currentMonth = YearMonth.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            List<BudgetSettingEntity> budgets = budgetSettingMapper.findByUserIdAndMonth(userId, currentMonth);
            List<BudgetAlertVO> alerts = new ArrayList<>();

            for (BudgetSettingEntity budget : budgets) {
                BigDecimal used = transactionRepository.sumExpenseByUserIdAndCategoryAndMonth(
                        userId, budget.getCategoryId(), YearMonth.now());

                if (used == null) {
                    used = BigDecimal.ZERO;
                }

                int usedPercent = budget.getBudgetAmount().compareTo(BigDecimal.ZERO) > 0
                        ? used.multiply(new BigDecimal("100")).divide(budget.getBudgetAmount(), 0, RoundingMode.HALF_UP).intValue()
                        : 0;

                // 只返回使用率超过50%的预算提醒
                if (usedPercent >= 50) {
                    String icon = getCategoryIcon(budget.getCategoryId());
                    alerts.add(BudgetAlertVO.builder()
                            .categoryId(budget.getCategoryId())
                            .categoryName(getCategoryName(budget.getCategoryId()))
                            .budgetAmount(budget.getBudgetAmount())
                            .usedAmount(used)
                            .usedPercent(usedPercent)
                            .icon(icon)
                            .build());
                }
            }

            return alerts;
        } catch (Exception e) {
            log.warn("获取预算提醒失败, userId={}, error={}", userId, e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 获取净资产趋势
     */
    private List<TrendPointVO> getNetWorthTrend(Long userId, BigDecimal currentNetWorth) {
        List<TrendPointVO> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            BigDecimal netWorth = accountRepository.sumNetWorthAtTime(userId, date.atTime(23, 59, 59));
            if (netWorth == null) {
                netWorth = currentNetWorth; // 如果某天没有数据，用当前值
            }
            trend.add(TrendPointVO.builder()
                    .date(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .netWorth(netWorth)
                    .build());
        }

        return trend;
    }

    private String getCategoryIcon(Long categoryId) {
        Map<Long, String> iconMap = Map.of(
                11L, "restaurant",
                12L, "delivery_dining",
                13L, "local_cafe",
                21L, "home",
                22L, "bolt",
                31L, "directions_bus"
        );
        return iconMap.getOrDefault(categoryId, "category");
    }

    private String getCategoryName(Long categoryId) {
        Map<Long, String> nameMap = Map.of(
                11L, "餐饮-早午晚餐",
                12L, "餐饮-外卖",
                13L, "餐饮-下午茶",
                21L, "居住-房租/房贷",
                22L, "居住-水电气",
                31L, "交通-公共交通"
        );
        return nameMap.getOrDefault(categoryId, "其他");
    }
}