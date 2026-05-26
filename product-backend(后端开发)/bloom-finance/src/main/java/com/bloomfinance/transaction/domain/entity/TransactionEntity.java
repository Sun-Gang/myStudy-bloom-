package com.bloomfinance.transaction.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 流水记录实体
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_transaction")
public class TransactionEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long accountId;

    private BigDecimal amount;

    /**
     * 类型：1-收入 2-支出
     */
    private Integer transactionType;

    private Long categoryId;

    private String categoryName;

    private String merchantName;

    private String mccCode;

    private String description;

    private LocalDateTime transactionDate;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}