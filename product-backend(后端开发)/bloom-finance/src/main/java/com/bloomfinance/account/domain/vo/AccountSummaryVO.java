package com.bloomfinance.account.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 账户汇总VO - 账户页面使用
 *
 * @author BloomFinance
 */
@Data
@Builder
@Schema(description = "账户汇总VO")
public class AccountSummaryVO {

    @Schema(description = "总净资产")
    private BigDecimal totalNetWorth;

    @Schema(description = "总资产")
    private BigDecimal totalAsset;

    @Schema(description = "总负债")
    private BigDecimal totalLiability;

    @Schema(description = "月度增长率")
    private BigDecimal monthlyGrowthRate;

    @Schema(description = "安全健康状态")
    private String healthStatus;

    @Schema(description = "银行卡汇总")
    private AccountTypeSummary bankCardSummary;

    @Schema(description = "投资账户汇总")
    private AccountTypeSummary investmentSummary;

    @Schema(description = "社保公积金汇总")
    private AccountTypeSummary socialSecuritySummary;

    @Schema(description = "负债汇总")
    private LiabilitySummary liabilitySummary;

    @Schema(description = "固定资产汇总")
    private FixedAssetSummary fixedAssetSummary;

    @Data
    @Builder
    public static class AccountTypeSummary {
        @Schema(description = "余额")
        private BigDecimal balance;

        @Schema(description = "数量")
        private Integer count;

        @Schema(description = "占比百分比")
        private BigDecimal percentage;

        @Schema(description = "最近缴存日期（社保公积金用）")
        private String lastPaymentDate;
    }

    @Data
    @Builder
    public static class LiabilitySummary {
        @Schema(description = "负债总额")
        private BigDecimal totalAmount;

        @Schema(description = "负债类型")
        private String liabilityType;

        @Schema(description = "负债占比")
        private BigDecimal percentage;
    }

    @Data
    @Builder
    public static class FixedAssetSummary {
        @Schema(description = "固定资产总额")
        private BigDecimal totalAmount;

        @Schema(description = "房产数量")
        private Integer propertyCount;

        @Schema(description = "车辆数量")
        private Integer vehicleCount;
    }
}