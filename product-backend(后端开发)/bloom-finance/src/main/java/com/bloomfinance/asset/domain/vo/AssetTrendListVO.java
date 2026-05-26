package com.bloomfinance.asset.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 资产趋势列表VO
 *
 * @author BloomFinance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetTrendListVO {

    /**
     * 月度趋势数据列表
     */
    private List<AssetTrendVO> monthlyData;
}
