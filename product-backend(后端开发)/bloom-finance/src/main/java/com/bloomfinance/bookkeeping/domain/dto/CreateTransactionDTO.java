package com.bloomfinance.bookkeeping.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 创建交易记录DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "创建交易记录请求")
public class CreateTransactionDTO {

    /**
     * 账户ID
     */
    @NotNull(message = "账户ID不能为空")
    @Schema(description = "账户ID", example = "1")
    private Long accountId;

    /**
     * 交易金额（正数收入，负数支出）
     */
    @NotNull(message = "交易金额不能为空")
    @Schema(description = "交易金额", example = "100.00")
    private BigDecimal amount;

    /**
     * 交易类型: 1-收入 2-支出
     */
    @NotNull(message = "交易类型不能为空")
    @Schema(description = "交易类型: 1-收入 2-支出", example = "1")
    private Integer transactionType;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    /**
     * 商户名称
     */
    @Size(max = 128, message = "商户名称长度不能超过128")
    @Schema(description = "商户名称", example = "星巴克")
    private String merchantName;

    /**
     * MCC码
     */
    @Size(max = 10, message = "MCC码长度不能超过10")
    @Schema(description = "MCC码", example = "5812")
    private String mccCode;

    /**
     * 交易描述
     */
    @Size(max = 500, message = "交易描述长度不能超过500")
    @Schema(description = "交易描述", example = "咖啡消费")
    private String description;

    /**
     * 交易日期
     */
    @NotNull(message = "交易日期不能为空")
    @Schema(description = "交易日期", example = "2024-01-15 10:30:00")
    private LocalDateTime transactionDate;
}