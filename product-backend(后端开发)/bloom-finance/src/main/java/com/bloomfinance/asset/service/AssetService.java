package com.bloomfinance.asset.service;

import com.bloomfinance.asset.domain.dto.HealthScoreQuery;
import com.bloomfinance.asset.domain.vo.AssetAllocationVO;
import com.bloomfinance.asset.domain.vo.AssetSummaryVO;
import com.bloomfinance.asset.domain.vo.DashboardSummaryVO;
import com.bloomfinance.asset.domain.vo.HealthScoreVO;

/**
 * Asset service interface.
 *
 * @author BloomFinance
 */
public interface AssetService {

    /**
     * Get dashboard summary for user.
     * Includes total asset, liability, net asset, monthly income/expense, and health score.
     *
     * @param userId User ID
     * @return Dashboard summary
     */
    DashboardSummaryVO getDashboardSummary(Long userId);

    /**
     * Calculate health score for user based on specified period.
     *
     * @param userId User ID
     * @param query  Health score query parameters
     * @return Health score with dimension breakdown
     */
    HealthScoreVO getHealthScore(Long userId, HealthScoreQuery query);

    /**
     * Get asset allocation breakdown by type.
     *
     * @param userId User ID
     * @return Asset allocation with breakdown
     */
    AssetAllocationVO getAssetAllocation(Long userId);

    /**
     * Get asset summary with change rates.
     *
     * @param userId User ID
     * @return Asset summary
     */
    AssetSummaryVO getAssetSummary(Long userId);
}