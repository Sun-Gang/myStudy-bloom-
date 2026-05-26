package com.bloomfinance.asset.service;

import com.bloomfinance.asset.domain.vo.AssetTrendListVO;

/**
 * 资产趋势Service接口
 *
 * @author BloomFinance
 */
public interface AssetTrendService {

    /**
     * 获取月度资产趋势
     *
     * @param userId 用户ID
     * @param months 月数（默认6个月）
     * @return 资产趋势列表
     */
    AssetTrendListVO getAssetTrend(Long userId, Integer months);
}
