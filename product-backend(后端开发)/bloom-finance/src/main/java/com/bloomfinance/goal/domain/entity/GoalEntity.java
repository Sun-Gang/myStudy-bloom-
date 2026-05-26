package com.bloomfinance.goal.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 目标实体类
 *
 * @author BloomFinance
 */
@Data
@TableName("t_goal")
public class GoalEntity {

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
     * 目标名称
     */
    private String goalName;

    /**
     * 目标类型: 1-购房 2-教育 3-养老 4-旅游 5-购车 6-通用
     */
    private Integer goalType;

    /**
     * 目标金额
     */
    private BigDecimal targetAmount;

    /**
     * 当前金额
     */
    private BigDecimal currentAmount;

    /**
     * 目标日期
     */
    private LocalDate targetDate;

    /**
     * 每月建议金额
     */
    private BigDecimal monthlySuggestedAmount;

    /**
     * 完成概率 (0-100)
     */
    private Integer probability;

    /**
     * 状态: 1-进行中 2-已完成 3-已放弃
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