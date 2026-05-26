package com.bloomfinance.asset.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Asset summary value object containing overall asset status and change rates.
 *
 * @author BloomFinance
 */
@Data
@Builder
@Schema(description = "Asset summary VO")
public class AssetSummaryVO {

    @Schema(description = "Total assets", example = "100000.00")
    private BigDecimal totalAsset;

    @Schema(description = "Total liabilities", example = "20000.00")
    private BigDecimal totalLiability;

    @Schema(description = "Net assets (total assets - total liabilities)", example = "80000.00")
    private BigDecimal netAsset;

    @Schema(description = "Asset change rate percentage", example = "5.50")
    private BigDecimal assetChangeRate;

    @Schema(description = "Liability change rate percentage", example = "-2.30")
    private BigDecimal liabilityChangeRate;
}