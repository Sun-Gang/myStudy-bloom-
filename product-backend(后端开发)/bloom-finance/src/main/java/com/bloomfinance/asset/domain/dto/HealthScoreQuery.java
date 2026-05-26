package com.bloomfinance.asset.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Health score query parameters.
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "Health score query parameters")
public class HealthScoreQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Analysis period", example = "MONTH")
    private Period period;

    /**
     * Analysis period enumeration.
     */
    public enum Period {
        /**
         * Monthly analysis
         */
        MONTH,
        /**
         * Quarterly analysis
         */
        QUARTER,
        /**
         * Yearly analysis
         */
        YEAR
    }
}