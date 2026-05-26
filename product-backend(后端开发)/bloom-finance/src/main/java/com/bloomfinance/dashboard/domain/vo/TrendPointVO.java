package com.bloomfinance.dashboard.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 趋势点VO
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendPointVO {

    /**
     * 日期 (yyyy-MM-dd)
     */
    private String date;

    /**
     * 净资产
     */
    private BigDecimal netWorth;
}