package com.bloomfinance.asset.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.account.domain.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * Asset repository for asset-related custom queries.
 *
 * @author BloomFinance
 */
@Mapper
public interface AssetRepository extends BaseMapper<AccountEntity> {

    /**
     * Calculate total assets for a user.
     * Assets include: SAVINGS(1), SECURITIES(3), FUND(4), SOCIAL_SECURITY(5), HOUSING_FUND(6)
     *
     * @param userId User ID
     * @return Total asset amount
     */
    @Select("SELECT COALESCE(SUM(balance), 0) FROM t_account " +
           "WHERE user_id = #{userId} " +
           "AND status = 1 " +
           "AND deleted = 0 " +
           "AND account_type IN (1, 3, 4, 5, 6)")
    BigDecimal calculateUserTotalAsset(@Param("userId") Long userId);

    /**
     * Calculate total liabilities for a user.
     * Liabilities include: CREDIT_CARD(2), LIABILITY(7)
     *
     * @param userId User ID
     * @return Total liability amount
     */
    @Select("SELECT COALESCE(SUM(balance), 0) FROM t_account " +
           "WHERE user_id = #{userId} " +
           "AND status = 1 " +
           "AND deleted = 0 " +
           "AND account_type IN (2, 7)")
    BigDecimal calculateUserTotalLiability(@Param("userId") Long userId);
}