package com.bloomfinance.asset.service.impl;

import com.bloomfinance.asset.domain.vo.AssetTrendListVO;
import com.bloomfinance.asset.domain.vo.AssetTrendVO;
import com.bloomfinance.asset.repository.AssetTrendRepository;
import com.bloomfinance.asset.service.AssetTrendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 资产趋势Service实现
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetTrendServiceImpl implements AssetTrendService {

    private final AssetTrendRepository assetTrendRepository;

    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @Override
    @Transactional(readOnly = true)
    public AssetTrendListVO getAssetTrend(Long userId, Integer months) {
        if (months == null || months <= 0) {
            months = 6;
        }

        log.info("Get asset trend, userId={}, months={}", userId, months);

        List<AssetTrendVO> monthlyData = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (int i = months - 1; i >= 0; i--) {
            LocalDate monthDate = today.minusMonths(i);
            String monthStr = monthDate.format(MONTH_FORMATTER);
            String monthName = monthDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.CHINESE) + "月";

            // 计算月初和月末
            LocalDateTime monthStart = monthDate.withDayOfMonth(1).atStartOfDay();
            LocalDateTime monthEnd = monthDate.withDayOfMonth(monthDate.lengthOfMonth()).atTime(23, 59, 59);

            Date startDate = Date.from(monthStart.atZone(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(monthEnd.atZone(ZoneId.systemDefault()).toInstant());

            // 获取当月收入和支出
            BigDecimal income = assetTrendRepository.sumIncomeByDateRange(userId, startDate, endDate);
            BigDecimal expense = assetTrendRepository.sumExpenseByDateRange(userId, startDate, endDate);

            // 处理null情况
            if (income == null) income = BigDecimal.ZERO;
            if (expense == null) expense = BigDecimal.ZERO;

            // 支出是负数，取绝对值
            BigDecimal expenseAbs = expense.abs();

            // 计算净资产（简化计算：当前账户余额 + 当月累计收入 - 当月累计支出）
            // 实际上应该计算期末净资产，这里用简化方式
            BigDecimal currentAsset = assetTrendRepository.sumAssetBalanceByUserId(userId);
            BigDecimal currentLiability = assetTrendRepository.sumLiabilityBalanceByUserId(userId);

            if (currentAsset == null) currentAsset = BigDecimal.ZERO;
            if (currentLiability == null) currentLiability = BigDecimal.ZERO;

            // 简化：净资产 = 总资产 - 总负债 + 当月净收支
            BigDecimal netAsset = currentAsset.subtract(currentLiability)
                    .add(income)
                    .subtract(expenseAbs);

            AssetTrendVO trendVO = AssetTrendVO.builder()
                    .month(monthStr)
                    .monthName(monthName)
                    .netAsset(netAsset.setScale(2, RoundingMode.HALF_UP))
                    .income(income.setScale(2, RoundingMode.HALF_UP))
                    .expense(expenseAbs.setScale(2, RoundingMode.HALF_UP))
                    .build();

            monthlyData.add(trendVO);
        }

        return AssetTrendListVO.builder()
                .monthlyData(monthlyData)
                .build();
    }
}
