package com.bloomfinance.bookkeeping.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易模板实体类
 *
 * @author BloomFinance
 */
@Data
@TableName("t_transaction_template")
public class TransactionTemplateEntity {

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
     * 模板名称
     */
    private String name;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 交易金额
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
     * 触发频率: daily-每天 weekly-每周 monthly-每月
     */
    private String frequency;

    /**
     * 下次触发日期
     */
    private LocalDateTime nextTriggerDate;

    /**
     * 状态: 0-禁用 1-启用
     */
    private Integer status;

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