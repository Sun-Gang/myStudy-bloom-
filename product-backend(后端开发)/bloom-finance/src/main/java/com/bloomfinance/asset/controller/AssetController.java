package com.bloomfinance.asset.controller;

import com.bloomfinance.asset.domain.dto.HealthScoreQuery;
import com.bloomfinance.asset.domain.vo.AssetAllocationVO;
import com.bloomfinance.asset.domain.vo.AssetSummaryVO;
import com.bloomfinance.asset.domain.vo.DashboardSummaryVO;
import com.bloomfinance.asset.domain.vo.HealthScoreVO;
import com.bloomfinance.asset.service.AssetService;
import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Asset controller for asset-related operations.
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/assets")
@RequiredArgsConstructor
@Tag(name = "资产管理", description = "资产查询与分析接口")
public class AssetController {

    private final AssetService assetService;

    /**
     * Get asset summary.
     *
     * @param userId User ID
     * @return Asset summary including total asset, liability, net asset and change rates
     */
    @GetMapping("/summary")
    @Operation(summary = "获取资产摘要", description = "获取用户资产汇总信息，包括总资产、总负债、净资产及变化率")
    public Result<AssetSummaryVO> getAssetSummary(@Parameter(description = "用户ID", hidden = true) @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        Long userId = getCurrentUserId(headerUserId);
        log.info("Get asset summary, userId={}", userId);
        AssetSummaryVO summary = assetService.getAssetSummary(userId);
        return Result.success(summary);
    }

    /**
     * Get asset allocation breakdown.
     *
     * @param userId User ID
     * @return Asset allocation by type
     */
    @GetMapping("/allocation")
    @Operation(summary = "获取资产配置", description = "获取用户资产配置情况，按类型分组")
    public Result<AssetAllocationVO> getAssetAllocation(@Parameter(description = "用户ID", hidden = true) @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        Long userId = getCurrentUserId(headerUserId);
        log.info("Get asset allocation, userId={}", userId);
        AssetAllocationVO allocation = assetService.getAssetAllocation(userId);
        return Result.success(allocation);
    }

    /**
     * Get dashboard summary.
     *
     * @param userId User ID
     * @return Dashboard summary including assets, liabilities, health score
     */
    @GetMapping("/dashboard")
    @Operation(summary = "获取仪表盘摘要", description = "获取用户仪表盘汇总信息，包括资产、负债、健康评分等")
    public Result<DashboardSummaryVO> getDashboardSummary(@Parameter(description = "用户ID", hidden = true) @RequestHeader(value = "X-User-Id", required = false) Long headerUserId) {
        Long userId = getCurrentUserId(headerUserId);
        log.info("Get dashboard summary, userId={}", userId);
        DashboardSummaryVO dashboard = assetService.getDashboardSummary(userId);
        return Result.success(dashboard);
    }

    /**
     * Get health score with dimension breakdown.
     *
     * @param userId  User ID
     * @param period  Analysis period (MONTH/QUARTER/YEAR), defaults to MONTH
     * @param query   Query parameters
     * @return Health score with dimension scores and suggestions
     */
    @GetMapping("/health-score")
    @Operation(summary = "获取健康评分", description = "获取用户财务健康评分，包含各维度分析及建议")
    public Result<HealthScoreVO> getHealthScore(
            @Parameter(description = "用户ID", hidden = true) @RequestHeader(value = "X-User-Id", required = false) Long headerUserId,
            @Parameter(description = "分析周期", example = "MONTH") @RequestParam(required = false, defaultValue = "MONTH") HealthScoreQuery.Period period) {
        Long userId = getCurrentUserId(headerUserId);
        log.info("Get health score, userId={}, period={}", userId, period);

        HealthScoreQuery query = new HealthScoreQuery();
        query.setUserId(userId);
        query.setPeriod(period);

        HealthScoreVO healthScore = assetService.getHealthScore(userId, query);
        return Result.success(healthScore);
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(Long headerUserId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        return userId;
    }
}