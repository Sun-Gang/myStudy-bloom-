package com.bloomfinance.asset.service.impl;

import com.bloomfinance.asset.domain.dto.HealthScoreQuery;
import com.bloomfinance.asset.domain.vo.AssetAllocationVO;
import com.bloomfinance.asset.domain.vo.AssetSummaryVO;
import com.bloomfinance.asset.domain.vo.DashboardSummaryVO;
import com.bloomfinance.asset.domain.vo.HealthScoreVO;
import com.bloomfinance.asset.repository.AssetRepository;
import com.bloomfinance.asset.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Asset service implementation.
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final AssetCalculator assetCalculator;

    private static final String CACHE_NAME_DASHBOARD = "asset:dashboard";
    private static final String CACHE_NAME_HEALTH = "asset:health";

    // Health score dimension weights per PRD
    private static final int WEIGHT_SAVINGS_RATE = 25;
    private static final int WEIGHT_EMERGENCY_RESERVE = 20;
    private static final int WEIGHT_DEBT_RATIO = 20;
    private static final int WEIGHT_ASSET_ALLOCATION = 20;
    private static final int WEIGHT_INCOME_EXPENSE_BALANCE = 15;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = CACHE_NAME_DASHBOARD, key = "#userId", unless = "#result == null", cacheManager = "cacheManager")
    public DashboardSummaryVO getDashboardSummary(Long userId) {
        log.info("Get dashboard summary, userId={}", userId);

        BigDecimal totalAsset = assetCalculator.calculateTotalAsset(userId);
        BigDecimal totalLiability = assetCalculator.calculateTotalLiability(userId);
        BigDecimal netAsset = totalAsset.subtract(totalLiability);

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        BigDecimal monthlyIncome = assetCalculator.calculateMonthlyIncome(userId, startOfMonth);
        BigDecimal monthlyExpense = assetCalculator.calculateMonthlyExpense(userId, startOfMonth);

        HealthScoreVO healthScoreVO = calculateHealthScore(userId, null);

        return DashboardSummaryVO.builder()
            .totalAsset(totalAsset)
            .totalLiability(totalLiability)
            .netAsset(netAsset)
            .monthlyIncome(monthlyIncome)
            .monthlyExpense(monthlyExpense)
            .healthScore(healthScoreVO.getTotalScore())
            .healthRating(healthScoreVO.getRating())
            .incomeCount(null) // TODO: implement count query if needed
            .expenseCount(null) // TODO: implement count query if needed
            .build();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = CACHE_NAME_HEALTH, key = "#userId + ':' + #query.hashCode()", unless = "#result == null", cacheManager = "cacheManager")
    public HealthScoreVO getHealthScore(Long userId, HealthScoreQuery query) {
        log.info("Get health score, userId={}, query={}", userId, query);
        return calculateHealthScore(userId, query);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public AssetAllocationVO getAssetAllocation(Long userId) {
        log.info("Get asset allocation, userId={}", userId);

        BigDecimal totalAsset = assetCalculator.calculateTotalAsset(userId);
        if (totalAsset.compareTo(BigDecimal.ZERO) == 0) {
            return AssetAllocationVO.builder()
                .totalAsset(BigDecimal.ZERO)
                .allocationList(new ArrayList<>())
                .build();
        }

        List<AssetAllocationVO.AllocationItemVO> allocationItems = calculateAssetAllocationByType(userId, totalAsset);

        return AssetAllocationVO.builder()
            .totalAsset(totalAsset)
            .allocationList(allocationItems)
            .build();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public AssetSummaryVO getAssetSummary(Long userId) {
        log.info("Get asset summary, userId={}", userId);

        BigDecimal totalAsset = assetCalculator.calculateTotalAsset(userId);
        BigDecimal totalLiability = assetCalculator.calculateTotalLiability(userId);
        BigDecimal netAsset = totalAsset.subtract(totalLiability);

        // Calculate change rates (simplified - comparing current month to previous month)
        BigDecimal assetChangeRate = calculateChangeRate(userId, true);
        BigDecimal liabilityChangeRate = calculateChangeRate(userId, false);

        return AssetSummaryVO.builder()
            .totalAsset(totalAsset)
            .totalLiability(totalLiability)
            .netAsset(netAsset)
            .assetChangeRate(assetChangeRate)
            .liabilityChangeRate(liabilityChangeRate)
            .build();
    }

    /**
     * Calculate health score with dimension breakdown.
     *
     * @param userId User ID
     * @param query  Query (can be null for default monthly)
     * @return Health score VO
     */
    private HealthScoreVO calculateHealthScore(Long userId, HealthScoreQuery query) {
        List<HealthScoreVO.DimensionScoreVO> dimensions = new ArrayList<>();

        // 1. Savings Rate (25%)
        BigDecimal savingsRate = assetCalculator.calculateSavingsRate(userId);
        int savingsRateScore = calculateSavingsRateScore(savingsRate);
        dimensions.add(HealthScoreVO.DimensionScoreVO.builder()
            .dimension("savings_rate")
            .dimensionName("储蓄率")
            .score(savingsRateScore)
            .weight(WEIGHT_SAVINGS_RATE)
            .suggestion(getSavingsRateSuggestion(savingsRateScore))
            .build());

        // 2. Emergency Reserve (20%)
        BigDecimal emergencyMonths = assetCalculator.calculateEmergencyReserveRatio(userId);
        int emergencyScore = calculateEmergencyScore(emergencyMonths);
        dimensions.add(HealthScoreVO.DimensionScoreVO.builder()
            .dimension("emergency_reserve")
            .dimensionName("应急储备")
            .score(emergencyScore)
            .weight(WEIGHT_EMERGENCY_RESERVE)
            .suggestion(getEmergencySuggestion(emergencyScore))
            .build());

        // 3. Debt Ratio (20%)
        BigDecimal debtRatio = assetCalculator.calculateDebtRatio(userId);
        int debtScore = calculateDebtScore(debtRatio);
        dimensions.add(HealthScoreVO.DimensionScoreVO.builder()
            .dimension("debt_ratio")
            .dimensionName("负债率")
            .score(debtScore)
            .weight(WEIGHT_DEBT_RATIO)
            .suggestion(getDebtSuggestion(debtScore))
            .build());

        // 4. Asset Allocation (20%)
        BigDecimal totalAsset = assetCalculator.calculateTotalAsset(userId);
        int allocationScore = calculateAssetAllocationScore(userId, totalAsset);
        dimensions.add(HealthScoreVO.DimensionScoreVO.builder()
            .dimension("asset_allocation")
            .dimensionName("资产配置")
            .score(allocationScore)
            .weight(WEIGHT_ASSET_ALLOCATION)
            .suggestion(getAllocationSuggestion(allocationScore))
            .build());

        // 5. Income Expense Balance (15%)
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        BigDecimal monthlyIncome = assetCalculator.calculateMonthlyIncome(userId, startOfMonth);
        BigDecimal monthlyExpense = assetCalculator.calculateMonthlyExpense(userId, startOfMonth);
        int incomeExpenseScore = calculateIncomeExpenseScore(monthlyIncome, monthlyExpense);
        dimensions.add(HealthScoreVO.DimensionScoreVO.builder()
            .dimension("income_expense_balance")
            .dimensionName("收支平衡")
            .score(incomeExpenseScore)
            .weight(WEIGHT_INCOME_EXPENSE_BALANCE)
            .suggestion(getIncomeExpenseSuggestion(incomeExpenseScore))
            .build());

        // Calculate total weighted score
        int totalScore = calculateTotalScore(dimensions);
        String rating = calculateRating(totalScore);

        return HealthScoreVO.builder()
            .totalScore(totalScore)
            .rating(rating)
            .dimensions(dimensions)
            .build();
    }

    /**
     * Calculate savings rate score (0-100).
     */
    private int calculateSavingsRateScore(BigDecimal savingsRate) {
        if (savingsRate.compareTo(new BigDecimal("30")) >= 0) {
            return 100;
        } else if (savingsRate.compareTo(new BigDecimal("20")) >= 0) {
            return 80;
        } else if (savingsRate.compareTo(new BigDecimal("10")) >= 0) {
            return 60;
        } else if (savingsRate.compareTo(new BigDecimal("0")) >= 0) {
            return 40;
        } else {
            return 20;
        }
    }

    /**
     * Calculate emergency reserve score (0-100).
     * Ideal: 3-6 months
     */
    private int calculateEmergencyScore(BigDecimal months) {
        if (months.compareTo(new BigDecimal("6")) >= 0) {
            return 100;
        } else if (months.compareTo(new BigDecimal("3")) >= 0) {
            return 80;
        } else if (months.compareTo(new BigDecimal("1")) >= 0) {
            return 50;
        } else if (months.compareTo(new BigDecimal("0")) > 0) {
            return 30;
        } else {
            return 0;
        }
    }

    /**
     * Calculate debt ratio score (0-100).
     * Lower is better
     */
    private int calculateDebtScore(BigDecimal debtRatio) {
        if (debtRatio.compareTo(new BigDecimal("20")) <= 0) {
            return 100;
        } else if (debtRatio.compareTo(new BigDecimal("40")) <= 0) {
            return 80;
        } else if (debtRatio.compareTo(new BigDecimal("60")) <= 0) {
            return 60;
        } else if (debtRatio.compareTo(new BigDecimal("80")) <= 0) {
            return 40;
        } else {
            return 20;
        }
    }

    /**
     * Calculate asset allocation score (0-100).
     */
    private int calculateAssetAllocationScore(Long userId, BigDecimal totalAsset) {
        // Simplified: check if assets are diversified across multiple types
        if (totalAsset.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }

        // Check diversification through account types
        // This is a simplified check - in production, would analyze actual diversification
        return 70; // Default moderate score
    }

    /**
     * Calculate income-expense balance score (0-100).
     */
    private int calculateIncomeExpenseScore(BigDecimal income, BigDecimal expense) {
        if (income.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }

        BigDecimal ratio = expense.divide(income, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));

        if (ratio.compareTo(new BigDecimal("50")) <= 0) {
            return 100;
        } else if (ratio.compareTo(new BigDecimal("70")) <= 0) {
            return 80;
        } else if (ratio.compareTo(new BigDecimal("90")) <= 0) {
            return 60;
        } else if (ratio.compareTo(new BigDecimal("100")) <= 0) {
            return 40;
        } else {
            return 20;
        }
    }

    /**
     * Calculate total weighted score.
     */
    private int calculateTotalScore(List<HealthScoreVO.DimensionScoreVO> dimensions) {
        int totalScore = 0;
        for (HealthScoreVO.DimensionScoreVO dimension : dimensions) {
            totalScore += dimension.getScore() * dimension.getWeight() / 100;
        }
        return totalScore;
    }

    /**
     * Calculate rating based on total score.
     */
    private String calculateRating(int totalScore) {
        if (totalScore >= 80) {
            return "优秀";
        } else if (totalScore >= 60) {
            return "良好";
        } else {
            return "预警";
        }
    }

    /**
     * Calculate asset allocation by account type.
     */
    private List<AssetAllocationVO.AllocationItemVO> calculateAssetAllocationByType(Long userId, BigDecimal totalAsset) {
        List<AssetAllocationVO.AllocationItemVO> items = new ArrayList<>();

        // Asset types with their codes
        items.add(createAllocationItem("SAVINGS", "储蓄", userId, totalAsset, 1));
        items.add(createAllocationItem("SECURITIES", "证券", userId, totalAsset, 3));
        items.add(createAllocationItem("FUND", "基金", userId, totalAsset, 4));
        items.add(createAllocationItem("SOCIAL_SECURITY", "社保", userId, totalAsset, 5));
        items.add(createAllocationItem("HOUSING_FUND", "公积金", userId, totalAsset, 6));

        // Remove items with zero amount
        items.removeIf(item -> item.getAmount().compareTo(BigDecimal.ZERO) == 0);

        return items;
    }

    /**
     * Create allocation item for specific account type.
     */
    private AssetAllocationVO.AllocationItemVO createAllocationItem(
            String type, String typeName, Long userId, BigDecimal totalAsset, Integer accountType) {
        // This would need to query by account type - simplified for now
        return AssetAllocationVO.AllocationItemVO.builder()
            .type(type)
            .typeName(typeName)
            .amount(BigDecimal.ZERO)
            .percentage(BigDecimal.ZERO)
            .build();
    }

    /**
     * Calculate change rate for assets or liabilities.
     */
    private BigDecimal calculateChangeRate(Long userId, boolean isAsset) {
        // Simplified implementation - would need historical data comparison
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    // Suggestions
    private String getSavingsRateSuggestion(int score) {
        if (score >= 80) {
            return "储蓄率良好，继续保持";
        } else if (score >= 60) {
            return "建议将储蓄率提高至30%以上";
        } else {
            return "储蓄率偏低，建议每月储蓄至少30%收入";
        }
    }

    private String getEmergencySuggestion(int score) {
        if (score >= 80) {
            return "应急储备充足";
        } else if (score >= 60) {
            return "建议保持3-6个月开销作为应急储备";
        } else {
            return "应急储备不足，建议紧急建立3个月开销的储备金";
        }
    }

    private String getDebtSuggestion(int score) {
        if (score >= 80) {
            return "负债率控制良好";
        } else if (score >= 60) {
            return "负债率偏高，建议优化债务结构";
        } else {
            return "负债率过高，建议尽快偿还债务";
        }
    }

    private String getAllocationSuggestion(int score) {
        if (score >= 80) {
            return "资产配置合理";
        } else if (score >= 60) {
            return "资产配置较合理，可考虑适度多元化";
        } else {
            return "建议分散投资，优化资产配置";
        }
    }

    private String getIncomeExpenseSuggestion(int score) {
        if (score >= 80) {
            return "收支平衡良好";
        } else if (score >= 60) {
            return "建议控制不必要的开支";
        } else {
            return "支出偏高，建议制定预算并严格执行";
        }
    }
}