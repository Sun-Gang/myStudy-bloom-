package com.bloomfinance.goal.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 更新目标DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "更新目标请求")
public class UpdateGoalDTO {

    /**
     * 目标名称
     */
    @Size(max = 64, message = "目标名称不能超过64个字符")
    @Schema(description = "目标名称", example = "购买房产")
    private String goalName;

    /**
     * 目标金额
     */
    @Min(value = 1, message = "目标金额必须大于0")
    @Schema(description = "目标金额", example = "600000.00")
    private BigDecimal targetAmount;

    /**
     * 目标日期
     */
    @Schema(description = "目标日期", example = "2031-12-31")
    private LocalDate targetDate;

    /**
     * 状态: 1-进行中 2-已完成 3-已放弃
     */
    @Min(value = 1, message = "状态值无效")
    @Schema(description = "状态", example = "1")
    private Integer status;
}