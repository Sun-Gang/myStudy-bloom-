package com.bloomfinance.dashboard.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘数据VO
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardVO {

    /**
     * 总净资产
     */
    private BigDecimal totalNetWorth;

    /**
     * 本月增长百分比
     */
    private BigDecimal monthGrowthRate;

    /**
     * 财务健康分 (0-100)
     */
    private Integer healthScore;

    /**
     * 健康状态描述
     */
    private String healthDescription;

    /**
     * 本月收入
     */
    private BigDecimal monthIncome;

    /**
     * 本月支出
     */
    private BigDecimal monthExpense;

    /**
     * 收入笔数
     */
    private Integer incomeCount;

    /**
     * 支出笔数
     */
    private Integer expenseCount;

    /**
     * 近期账单列表
     */
    private List<BillReminderVO> billReminders;

    /**
     * 预算提醒列表
     */
    private List<BudgetAlertVO> budgetAlerts;

    /**
     * 净资产趋势图数据（近7天）
     */
    private List<TrendPointVO> netWorthTrend;
}