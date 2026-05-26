package com.bloomfinance.asset.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Health score value object containing overall score and dimension breakdown.
 *
 * @author BloomFinance
 */
@Data
@Builder
@Schema(description = "Health score VO")
public class HealthScoreVO {

    @Schema(description = "Total health score (0-100)", example = "85")
    private Integer totalScore;

    @Schema(description = "Health rating", example = "优秀")
    private String rating;

    @Schema(description = "Dimension scores breakdown")
    private List<DimensionScoreVO> dimensions;

    /**
     * Dimension score value object for individual health metric.
     */
    @Data
    @Builder
    @Schema(description = "Dimension score VO")
    public static class DimensionScoreVO {

        @Schema(description = "Dimension key", example = "savings_rate")
        private String dimension;

        @Schema(description = "Dimension display name", example = "储蓄率")
        private String dimensionName;

        @Schema(description = "Dimension score (0-100)", example = "80")
        private Integer score;

        @Schema(description = "Dimension weight percentage", example = "25")
        private Integer weight;

        @Schema(description = "Improvement suggestion", example = "建议提高储蓄率至30%以上")
        private String suggestion;
    }
}