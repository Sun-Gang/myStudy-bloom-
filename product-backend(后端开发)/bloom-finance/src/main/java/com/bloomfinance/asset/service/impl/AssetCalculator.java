package com.bloomfinance.asset.service.impl;

import com.bloomfinance.account.domain.entity.AccountEntity;
import com.bloomfinance.account.domain.enums.AccountTypeEnum;
import com.bloomfinance.account.repository.AccountRepository;
import com.bloomfinance.bookkeeping.repository.BookkeepingTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Asset calculator component for computing various financial metrics.
 *
 * @author BloomFinance
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AssetCalculator {

    private final AccountRepository accountRepository;
    private final BookkeepingTransactionRepository transactionRepository;

    /**
     * Calculate net asset for user.
     * Net asset = total assets - total liabilities
     *
     * @param userId User ID
     * @return Net asset amount
     */
    public BigDecimal calculateNetAsset(Long userId) {
        log.info("Calculate net asset, userId={}", userId);

        BigDecimal totalAsset = calculateTotalAsset(userId);
        BigDecimal totalLiability = calculateTotalLiability(userId);

        return totalAsset.subtract(totalLiability);
    }

    /**
     * Calculate total asset for user.
     *
     * @param userId User ID
     * @return Total asset amount
     */
    public BigDecimal calculateTotalAsset(Long userId) {
        log.info("Calculate total asset, userId={}", userId);

        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return accounts.stream()
            .filter(account -> account.getStatus() != null && account.getStatus() == 1)
            .filter(account -> isAssetAccount(account.getAccountType()))
            .map(account -> account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate total liability for user.
     *
     * @param userId User ID
     * @return Total liability amount
     */
    public BigDecimal calculateTotalLiability(Long userId) {
        log.info("Calculate total liability, userId={}", userId);

        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return accounts.stream()
            .filter(account -> account.getStatus() != null && account.getStatus() == 1)
            .filter(account -> isLiabilityAccount(account.getAccountType()))
            .map(account -> account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate savings rate.
     * Savings rate = (monthly income - monthly expense) / monthly income * 100
     *
     * @param userId User ID
     * @return Savings rate percentage (0-100)
     */
    public BigDecimal calculateSavingsRate(Long userId) {
        log.info("Calculate savings rate, userId={}", userId);

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        BigDecimal monthlyIncome = calculateMonthlyIncome(userId, startOfMonth);
        BigDecimal monthlyExpense = calculateMonthlyExpense(userId, startOfMonth);

        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal savingsRate = monthlyIncome.subtract(monthlyExpense)
            .divide(monthlyIncome, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));

        // Clamp between 0 and 100
        if (savingsRate.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        if (savingsRate.compareTo(new BigDecimal("100")) > 0) {
            return new BigDecimal("100");
        }

        return savingsRate.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate debt ratio.
     * Debt ratio = total liability / total asset * 100
     *
     * @param userId User ID
     * @return Debt ratio percentage (0-100)
     */
    public BigDecimal calculateDebtRatio(Long userId) {
        log.info("Calculate debt ratio, userId={}", userId);

        BigDecimal totalAsset = calculateTotalAsset(userId);
        BigDecimal totalLiability = calculateTotalLiability(userId);

        if (totalAsset.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal debtRatio = totalLiability.divide(totalAsset, 4, RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));

        return debtRatio.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate emergency reserve ratio.
     * Emergency reserve = liquid assets / monthly expense
     * Ideal: 3-6 months of expenses
     *
     * @param userId User ID
     * @return Months of emergency reserve
     */
    public BigDecimal calculateEmergencyReserveRatio(Long userId) {
        log.info("Calculate emergency reserve ratio, userId={}", userId);

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);

        BigDecimal liquidAssets = calculateLiquidAssets(userId);
        BigDecimal monthlyExpense = calculateMonthlyExpense(userId, startOfMonth);

        if (monthlyExpense.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal months = liquidAssets.divide(monthlyExpense, 2, RoundingMode.HALF_UP);

        return months;
    }

    /**
     * Calculate monthly income for user since specified date.
     *
     * @param userId    User ID
     * @param startDate Start date
     * @return Monthly income
     */
    public BigDecimal calculateMonthlyIncome(Long userId, LocalDateTime startDate) {
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal sum = transactionRepository.sumAmountByUserIdAndType(userId, 1, start, new Date());
        return sum != null ? sum : BigDecimal.ZERO;
    }

    /**
     * Calculate monthly expense for user since specified date.
     *
     * @param userId    User ID
     * @param startDate Start date
     * @return Monthly expense
     */
    public BigDecimal calculateMonthlyExpense(Long userId, LocalDateTime startDate) {
        Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
        BigDecimal sum = transactionRepository.sumAmountByUserIdAndType(userId, 2, start, new Date());
        return sum != null ? sum : BigDecimal.ZERO;
    }

    /**
     * Calculate liquid assets (cash, savings, money market).
     *
     * @param userId User ID
     * @return Liquid assets amount
     */
    private BigDecimal calculateLiquidAssets(Long userId) {
        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return accounts.stream()
            .filter(account -> account.getStatus() != null && account.getStatus() == 1)
            .filter(account -> isLiquidAsset(account.getAccountType()))
            .map(account -> account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Check if account type is an asset type.
     */
    private boolean isAssetAccount(Integer accountType) {
        if (accountType == null) {
            return false;
        }
        return accountType == AccountTypeEnum.SAVINGS.getCode()
            || accountType == AccountTypeEnum.SECURITIES.getCode()
            || accountType == AccountTypeEnum.FUND.getCode()
            || accountType == AccountTypeEnum.SOCIAL_SECURITY.getCode()
            || accountType == AccountTypeEnum.HOUSING_FUND.getCode();
    }

    /**
     * Check if account type is a liability type.
     */
    private boolean isLiabilityAccount(Integer accountType) {
        if (accountType == null) {
            return false;
        }
        return accountType == AccountTypeEnum.CREDIT_CARD.getCode()
            || accountType == AccountTypeEnum.LIABILITY.getCode();
    }

    /**
     * Check if account type is a liquid asset.
     */
    private boolean isLiquidAsset(Integer accountType) {
        if (accountType == null) {
            return false;
        }
        return accountType == AccountTypeEnum.SAVINGS.getCode()
            || accountType == AccountTypeEnum.CREDIT_CARD.getCode();
    }
}