package com.bloomfinance.reminder.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 创建账单DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "创建账单请求")
public class CreateBillDTO {

    /**
     * 账户ID
     */
    @NotNull(message = "账户ID不能为空")
    @Schema(description = "账户ID", example = "1")
    private Long accountId;

    /**
     * 账单类型: 1-信用卡 2-贷款
     */
    @NotNull(message = "账单类型不能为空")
    @Min(value = 1, message = "账单类型无效")
    @Schema(description = "账单类型", example = "1")
    private Integer billType;

    /**
     * 账单日期
     */
    @NotNull(message = "账单日期不能为空")
    @Schema(description = "账单日期", example = "2026-05-01")
    private LocalDate billDate;

    /**
     * 到期日期
     */
    @NotNull(message = "到期日期不能为空")
    @FutureOrPresent(message = "到期日期必须是当前或未来的日期")
    @Schema(description = "到期日期", example = "2026-05-15")
    private LocalDate dueDate;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @DecimalMin(value = "0.01", message = "金额必须大于0")
    @Schema(description = "金额", example = "5000.00")
    private BigDecimal amount;
}