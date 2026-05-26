package com.bloomfinance.dashboard.controller;

import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.result.Result;
import com.bloomfinance.dashboard.domain.vo.DashboardVO;
import com.bloomfinance.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘控制器
 *
 * @author bloom-finance
 */
@Slf4j
@RestController
@RequestMapping("/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "仪表盘管理", description = "首页仪表盘数据接口")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "获取仪表盘数据", description = "获取首页所需的全部数据，包括净资产、收入支出、健康分等")
    public Result<DashboardVO> getDashboard() {
        Long userId = UserContext.getUserId();
        log.info("获取仪表盘数据, userId={}", userId);
        DashboardVO dashboard = dashboardService.getDashboard(userId);
        return Result.success(dashboard);
    }
}