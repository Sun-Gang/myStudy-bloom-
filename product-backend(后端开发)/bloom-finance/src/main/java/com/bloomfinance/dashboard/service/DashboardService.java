package com.bloomfinance.dashboard.service;

import com.bloomfinance.dashboard.domain.vo.DashboardVO;

/**
 * 仪表盘服务接口
 *
 * @author bloom-finance
 */
public interface DashboardService {

    /**
     * 获取仪表盘数据
     *
     * @param userId 用户ID
     * @return 仪表盘数据
     */
    DashboardVO getDashboard(Long userId);
}