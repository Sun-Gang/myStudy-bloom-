package com.bloomfinance.dashboard.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 预算设置实体
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_budget_setting")
public class BudgetSettingEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 预算月份 (yyyy-MM)
     */
    private String budgetMonth;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}