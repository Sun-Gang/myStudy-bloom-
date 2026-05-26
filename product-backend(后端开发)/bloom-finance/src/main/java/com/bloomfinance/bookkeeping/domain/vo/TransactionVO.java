package com.bloomfinance.bookkeeping.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录视图对象
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "交易记录响应")
public class TransactionVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 账户ID
     */
    @Schema(description = "账户ID", example = "1")
    private Long accountId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称", example = "中国工商银行储蓄卡")
    private String accountName;

    /**
     * 交易金额（正数收入，负数支出）
     */
    @Schema(description = "交易金额", example = "100.00")
    private BigDecimal amount;

    /**
     * 交易类型: 1-收入 2-支出
     */
    @Schema(description = "交易类型: 1-收入 2-支出", example = "1")
    private Integer transactionType;

    /**
     * 交易类型名称
     */
    @Schema(description = "交易类型名称", example = "收入")
    private String transactionTypeName;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称", example = "餐饮")
    private String categoryName;

    /**
     * 商户名称
     */
    @Schema(description = "商户名称", example = "星巴克")
    private String merchantName;

    /**
     * MCC码
     */
    @Schema(description = "MCC码", example = "5812")
    private String mccCode;

    /**
     * 交易描述
     */
    @Schema(description = "交易描述", example = "咖啡消费")
    private String description;

    /**
     * 交易日期
     */
    @Schema(description = "交易日期", example = "2024-01-15 10:30:00")
    private LocalDateTime transactionDate;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-15 10:35:00")
    private LocalDateTime createdAt;
}