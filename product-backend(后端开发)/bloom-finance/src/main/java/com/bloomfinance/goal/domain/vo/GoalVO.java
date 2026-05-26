package com.bloomfinance.goal.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 目标VO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "目标响应")
public class GoalVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 目标名称
     */
    @Schema(description = "目标名称", example = "购买房产")
    private String goalName;

    /**
     * 目标类型
     */
    @Schema(description = "目标类型", example = "1")
    private Integer goalType;

    /**
     * 目标类型名称
     */
    @Schema(description = "目标类型名称", example = "购房")
    private String goalTypeName;

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
     * 目标日期
     */
    @Schema(description = "目标日期", example = "2030-12-31")
    private LocalDate targetDate;

    /**
     * 每月建议金额
     */
    @Schema(description = "每月建议金额", example = "15000.00")
    private BigDecimal monthlySuggestedAmount;

    /**
     * 完成概率 (0-100)
     */
    @Schema(description = "完成概率", example = "65")
    private Integer probability;

    /**
     * 状态: 1-进行中 2-已完成 3-已放弃
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 状态名称
     */
    @Schema(description = "状态名称", example = "进行中")
    private String statusName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}