package com.bloomfinance.asset.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.account.domain.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产趋势Repository
 *
 * @author BloomFinance
 */
@Mapper
public interface AssetTrendRepository extends BaseMapper<AccountEntity> {

    /**
     * 查询指定月份的账户总资产
     *
     * @param userId    用户ID
     * @param accountTypes 账户类型（1,3,4,5,6 为资产类型）
     * @return 总资产
     */
    @Select("SELECT COALESCE(SUM(balance), 0) FROM t_account " +
           "WHERE user_id = #{userId} " +
           "AND status = 1 " +
           "AND deleted = 0 " +
           "AND account_type IN (1, 3, 4, 5, 6)")
    BigDecimal sumAssetBalanceByUserId(@Param("userId") Long userId);

    /**
     * 查询指定月份的账户总负债
     *
     * @param userId    用户ID
     * @return 总负债
     */
    @Select("SELECT COALESCE(SUM(balance), 0) FROM t_account " +
           "WHERE user_id = #{userId} " +
           "AND status = 1 " +
           "AND deleted = 0 " +
           "AND account_type IN (2, 7)")
    BigDecimal sumLiabilityBalanceByUserId(@Param("userId") Long userId);

    /**
     * 查询指定时间范围内的收入金额
     *
     * @param userId    用户ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 收入金额（正数）
     */
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_transaction " +
           "WHERE user_id = #{userId} " +
           "AND deleted = 0 " +
           "AND transaction_type = 1 " +
           "AND transaction_date >= #{startDate} " +
           "AND transaction_date <= #{endDate}")
    BigDecimal sumIncomeByDateRange(@Param("userId") Long userId,
                                    @Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate);

    /**
     * 查询指定时间范围内的支出金额
     *
     * @param userId    用户ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 支出金额（负数）
     */
    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_transaction " +
           "WHERE user_id = #{userId} " +
           "AND deleted = 0 " +
           "AND transaction_type = 2 " +
           "AND transaction_date >= #{startDate} " +
           "AND transaction_date <= #{endDate}")
    BigDecimal sumExpenseByDateRange(@Param("userId") Long userId,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);
}
