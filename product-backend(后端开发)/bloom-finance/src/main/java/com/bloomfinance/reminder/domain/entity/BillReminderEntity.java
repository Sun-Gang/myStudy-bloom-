package com.bloomfinance.reminder.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账单实体
 *
 * @author BloomFinance
 */
@Data
@TableName("t_bill")
public class BillReminderEntity {

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
     * 账单类型: 1-信用卡 2-贷款
     */
    private Integer billType;

    /**
     * 账单日期
     */
    private LocalDate billDate;

    /**
     * 到期日期
     */
    private LocalDate dueDate;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 状态: 1-待还 2-已还 3-逾期
     */
    private Integer status;

    /**
     * 提醒状态: 0-未提醒 1-已提醒
     */
    private Integer remindStatus;

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