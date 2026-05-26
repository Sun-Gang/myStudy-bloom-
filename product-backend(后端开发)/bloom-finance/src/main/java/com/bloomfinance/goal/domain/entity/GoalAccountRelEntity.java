package com.bloomfinance.goal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 目标账户关联实体类
 *
 * @author BloomFinance
 */
@Data
@TableName("t_goal_account_rel")
public class GoalAccountRelEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 目标ID
     */
    private Long goalId;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}