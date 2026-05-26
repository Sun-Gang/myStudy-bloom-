package com.bloomfinance.asset.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Asset allocation value object showing distribution of assets by type.
 *
 * @author BloomFinance
 */
@Data
@Builder
@Schema(description = "Asset allocation VO")
public class AssetAllocationVO {

    @Schema(description = "Total assets", example = "100000.00")
    private BigDecimal totalAsset;

    @Schema(description = "Allocation breakdown by asset type")
    private List<AllocationItemVO> allocationList;

    /**
     * Allocation item value object for individual asset type.
     */
    @Data
    @Builder
    @Schema(description = "Allocation item VO")
    public static class AllocationItemVO {

        @Schema(description = "Asset type code", example = "CASH")
        private String type;

        @Schema(description = "Asset type display name", example = "现金及存款")
        private String typeName;

        @Schema(description = "Asset amount", example = "50000.00")
        private BigDecimal amount;

        @Schema(description = "Percentage of total assets", example = "50.00")
        private BigDecimal percentage;
    }
}