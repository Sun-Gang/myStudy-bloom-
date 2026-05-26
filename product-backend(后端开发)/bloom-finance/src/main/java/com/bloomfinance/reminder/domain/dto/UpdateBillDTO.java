package com.bloomfinance.reminder.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 更新账单DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "更新账单请求")
public class UpdateBillDTO {

    /**
     * 账单日期
     */
    @Schema(description = "账单日期", example = "2026-05-01")
    private LocalDate billDate;

    /**
     * 到期日期
     */
    @Schema(description = "到期日期", example = "2026-05-15")
    private LocalDate dueDate;

    /**
     * 金额
     */
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "金额", example = "5000.00")
    private BigDecimal amount;

    /**
     * 状态: 1-待还 2-已还 3-逾期
     */
    @Min(value = 1, message = "状态值无效")
    @Schema(description = "状态", example = "1")
    private Integer status;
}