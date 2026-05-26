package com.bloomfinance.bookkeeping.domain.dto;

import com.bloomfinance.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录查询DTO
 *
 * @author BloomFinance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "交易记录查询请求")
public class TransactionQuery extends PageQuery {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 账户ID
     */
    @Schema(description = "账户ID", example = "1")
    private Long accountId;

    /**
     * 交易类型: 1-收入 2-支出
     */
    @Schema(description = "交易类型: 1-收入 2-支出", example = "1")
    private Integer transactionType;

    /**
     * 分类ID
     */
    @Schema(description = "分类ID", example = "1")
    private Long categoryId;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期", example = "2024-01-01 00:00:00")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @Schema(description = "结束日期", example = "2024-12-31 23:59:59")
    private LocalDateTime endDate;

    /**
     * 关键字（搜索商户名称或描述）
     */
    @Schema(description = "关键字", example = "星巴克")
    private String keyword;

    /**
     * 开始金额
     */
    @Schema(description = "开始金额", example = "0.00")
    private BigDecimal startAmount;

    /**
     * 结束金额
     */
    @Schema(description = "结束金额", example = "1000.00")
    private BigDecimal endAmount;
}