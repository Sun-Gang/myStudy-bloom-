package com.bloomfinance.goal.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 目标进度VO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "目标进度响应")
public class GoalProgressVO {

    /**
     * 目标ID
     */
    @Schema(description = "目标ID", example = "1")
    private Long goalId;

    /**
     * 目标名称
     */
    @Schema(description = "目标名称", example = "购买房产")
    private String goalName;

    /**
     * 目标金额
     */
    @Schema(description = "目标金额", example = "500000.00")
    private BigDecimal targetAmount;

    /**
     * 当前金额
     */
    @Schema(description = "当前金额", example = "50000.00")
    private BigDecimal currentAmount;

    /**
     * 进度百分比
     */
    @Schema(description = "进度百分比", example = "10.00")
    private BigDecimal progress;

    /**
     * 剩余金额
     */
    @Schema(description = "剩余金额", example = "450000.00")
    private BigDecimal remainingAmount;

    /**
     * 剩余月份
     */
    @Schema(description = "剩余月份", example = "30")
    private Integer remainingMonths;

    /**
     * 是否按计划进行
     */
    @Schema(description = "是否按计划进行", example = "true")
    private Boolean isOnTrack;

    /**
     * 建议措施列表
     */
    @Schema(description = "建议措施")
    private List<String> suggestions;
}