package com.bloomfinance.goal.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 创建目标DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "创建目标请求")
public class CreateGoalDTO {

    /**
     * 目标名称
     */
    @NotBlank(message = "目标名称不能为空")
    @Schema(description = "目标名称", example = "购买房产")
    private String goalName;

    /**
     * 目标类型: 1-购房 2-教育 3-养老 4-旅游 5-购车 6-通用
     */
    @NotNull(message = "目标类型不能为空")
    @Schema(description = "目标类型", example = "1")
    private Integer goalType;

    /**
     * 目标金额
     */
    @NotNull(message = "目标金额不能为空")
    @DecimalMin(value = "0.01", message = "目标金额必须大于0")
    @Schema(description = "目标金额", example = "500000.00")
    private BigDecimal targetAmount;

    /**
     * 目标日期
     */
    @NotNull(message = "目标日期不能为空")
    @Future(message = "目标日期必须是将来的日期")
    @Schema(description = "目标日期", example = "2030-12-31")
    private LocalDate targetDate;

    /**
     * 当前金额
     */
    @DecimalMin(value = "0.00", message = "当前金额不能为负数")
    @Schema(description = "当前金额", example = "50000.00")
    private BigDecimal currentAmount = BigDecimal.ZERO;
}