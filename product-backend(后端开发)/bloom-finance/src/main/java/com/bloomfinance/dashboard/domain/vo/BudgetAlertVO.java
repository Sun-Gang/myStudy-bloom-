package com.bloomfinance.dashboard.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 预算提醒VO
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAlertVO {

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 预算金额
     */
    private BigDecimal budgetAmount;

    /**
     * 已使用金额
     */
    private BigDecimal usedAmount;

    /**
     * 使用百分比 (0-100)
     */
    private Integer usedPercent;

    /**
     * 图标
     */
    private String icon;
}