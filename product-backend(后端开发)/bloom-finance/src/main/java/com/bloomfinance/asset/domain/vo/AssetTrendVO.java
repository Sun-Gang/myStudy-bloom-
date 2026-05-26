package com.bloomfinance.asset.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 资产趋势VO
 *
 * @author BloomFinance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTrendVO {

    /**
     * 月份（格式：2026-01）
     */
    private String month;

    /**
     * 月份显示名称（如：1月）
     */
    private String monthName;

    /**
     * 净资产
     */
    private BigDecimal netAsset;

    /**
     * 月收入
     */
    private BigDecimal income;

    /**
     * 月支出
     */
    private BigDecimal expense;
}
