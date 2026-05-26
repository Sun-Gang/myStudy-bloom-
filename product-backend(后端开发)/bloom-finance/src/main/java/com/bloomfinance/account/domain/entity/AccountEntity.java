package com.bloomfinance.account.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户实体类
 *
 * @author BloomFinance
 */
@Data
@TableName("t_account")
public class AccountEntity {

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
     * 账户类型: 1-储蓄卡 2-信用卡 3-证券 4-基金 5-社保 6-公积金 7-负债 8-其他
     */
    private Integer accountType;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 卡号(加密存储)
     */
    private String cardNo;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 状态: 0-冻结 1-正常
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