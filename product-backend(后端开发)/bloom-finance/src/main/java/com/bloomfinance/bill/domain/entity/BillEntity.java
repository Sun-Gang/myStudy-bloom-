package com.bloomfinance.bill.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账单实体
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_bill")
public class BillEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long accountId;

    /**
     * 账单类型：1-信用卡 2-贷款
     */
    private Integer billType;

    private LocalDate billDate;

    private LocalDate dueDate;

    private BigDecimal amount;

    /**
     * 状态：1-待还 2-已还 3-逾期
     */
    private Integer status;

    private Integer remindStatus;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}