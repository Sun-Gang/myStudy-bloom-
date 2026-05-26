package com.bloomfinance.account.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新账户DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "更新账户请求")
public class UpdateAccountDTO {

    /**
     * 银行名称
     */
    @Size(max = 64, message = "银行名称长度不能超过64")
    @Schema(description = "银行名称", example = "中国建设银行")
    private String bankName;

    /**
     * 余额
     */
    @DecimalMin(value = "0", message = "余额不能为负数")
    @Schema(description = "余额", example = "10000.00")
    private BigDecimal balance;

    /**
     * 状态: 0-冻结 1-正常
     */
    @Schema(description = "状态: 0-冻结 1-正常", example = "1")
    private Integer status;
}