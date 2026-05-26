package com.bloomfinance.asset.controller;

import com.bloomfinance.asset.domain.vo.AssetTrendListVO;
import com.bloomfinance.asset.service.AssetTrendService;
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
 * 资产趋势Controller
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/assets")
@RequiredArgsConstructor
@Tag(name = "资产趋势", description = "资产月度趋势接口")
public class AssetTrendController {

    private final AssetTrendService assetTrendService;

    /**
     * 获取月度资产趋势
     *
     * @param months 月数，默认6个月
     * @return 资产趋势列表
     */
    @GetMapping("/trend")
    @Operation(summary = "获取月度资产趋势", description = "获取用户最近N个月的资产趋势数据")
    public Result<AssetTrendListVO> getAssetTrend(
            @Parameter(description = "月数") @RequestParam(required = false, defaultValue = "6") Integer months) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }

        log.info("Get asset trend, userId={}, months={}", userId, months);
        AssetTrendListVO trendData = assetTrendService.getAssetTrend(userId, months);
        return Result.success(trendData);
    }
}
