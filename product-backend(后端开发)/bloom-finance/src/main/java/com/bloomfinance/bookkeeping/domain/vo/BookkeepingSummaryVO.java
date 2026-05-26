package com.bloomfinance.bookkeeping.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 记账汇总VO - 记账页面使用
 *
 * @author BloomFinance
 */
@Data
@Builder
@Schema(description = "记账汇总VO")
public class BookkeepingSummaryVO {

    @Schema(description = "月度支出总额")
    private BigDecimal monthlyExpense;

    @Schema(description = "月度收入总额")
    private BigDecimal monthlyIncome;

    @Schema(description = "日均支出")
    private BigDecimal dailyAverageExpense;

    @Schema(description = "待报销金额")
    private BigDecimal reimbursableAmount;

    @Schema(description = "待报销数量")
    private Integer reimbursableCount;

    @Schema(description = "交易记录列表（按日期分组）")
    private List<DailyTransactionVO> transactions;

    @Schema(description = "月份信息")
    private String monthInfo;

    @Schema(description = "预算状态")
    private String budgetStatus;

    @Schema(description = "每日交易记录VO")
    @Data
    @Builder
    public static class DailyTransactionVO {
        @Schema(description = "日期标题，如：今天，10月24日")
        private String dateTitle;

        @Schema(description = "日期字符串，如：2024-10-24")
        private String dateString;

        @Schema(description = "交易记录列表")
        private List<TransactionVO> transactions;
    }
}