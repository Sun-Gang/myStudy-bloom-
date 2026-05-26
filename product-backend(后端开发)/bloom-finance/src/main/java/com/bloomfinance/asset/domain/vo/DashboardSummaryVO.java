package com.bloomfinance.asset.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Dashboard summary value object for asset overview.
 *
 * @author BloomFinance
 */
@Data
@Builder
@Schema(description = "Dashboard summary VO")
public class DashboardSummaryVO {

    @Schema(description = "Total assets", example = "100000.00")
    private BigDecimal totalAsset;

    @Schema(description = "Total liabilities", example = "20000.00")
    private BigDecimal totalLiability;

    @Schema(description = "Net assets (total assets - total liabilities)", example = "80000.00")
    private BigDecimal netAsset;

    @Schema(description = "Monthly income", example = "15000.00")
    private BigDecimal monthlyIncome;

    @Schema(description = "Monthly expense", example = "8000.00")
    private BigDecimal monthlyExpense;

    @Schema(description = "Health score (0-100)", example = "85")
    private Integer healthScore;

    @Schema(description = "Health rating: 优秀/良好/预警", example = "优秀")
    private String healthRating;

    @Schema(description = "Income transaction count", example = "5")
    private Integer incomeCount;

    @Schema(description = "Expense transaction count", example = "12")
    private Integer expenseCount;
}