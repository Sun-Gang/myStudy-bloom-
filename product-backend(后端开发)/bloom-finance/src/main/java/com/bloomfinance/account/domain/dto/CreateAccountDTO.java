package com.bloomfinance.account.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建账户DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "创建账户请求")
public class CreateAccountDTO {

    /**
     * 账户类型: 1-储蓄卡 2-信用卡 3-证券 4-基金 5-社保 6-公积金 7-负债 8-其他
     */
    @NotNull(message = "账户类型不能为空")
    @Schema(description = "账户类型", example = "1")
    private Integer accountType;

    /**
     * 银行名称
     */
    @NotBlank(message = "银行名称不能为空")
    @Size(max = 64, message = "银行名称长度不能超过64")
    @Schema(description = "银行名称", example = "中国工商银行")
    private String bankName;

    /**
     * 卡号
     */
    @Schema(description = "卡号", example = "6222021234567890123")
    private String cardNo;

    /**
     * 余额
     */
    @Schema(description = "余额", example = "0.00")
    private BigDecimal balance = BigDecimal.ZERO;
}