package com.bloomfinance.bookkeeping.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 *
 * @author BloomFinance
 */
@Data
@TableName("t_transaction")
public class BookkeepingTransactionEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 交易金额（正数收入，负数支出）
     */
    private BigDecimal amount;

    /**
     * 交易类型: 1-收入 2-支出
     */
    private Integer transactionType;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * MCC码
     */
    private String mccCode;

    /**
     * 交易描述
     */
    private String description;

    /**
     * 交易日期
     */
    private LocalDateTime transactionDate;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除: 0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;
}