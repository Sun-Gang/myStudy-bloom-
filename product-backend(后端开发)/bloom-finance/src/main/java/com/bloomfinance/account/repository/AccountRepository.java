package com.bloomfinance.account.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.account.domain.entity.AccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 账户仓储层
 *
 * @author BloomFinance
 */
@Mapper
public interface AccountRepository extends BaseMapper<AccountEntity> {

    /**
     * 根据用户ID查询账户列表
     *
     * @param userId 用户ID
     * @return 账户列表
     */
    List<AccountEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和账户类型查询账户列表
     *
     * @param userId      用户ID
     * @param accountType 账户类型
     * @return 账户列表
     */
    List<AccountEntity> findByUserIdAndAccountType(@Param("userId") Long userId, @Param("accountType") Integer accountType);

    /**
     * 根据ID和用户ID查询账户
     *
     * @param id     账户ID
     * @param userId 用户ID
     * @return 账户
     */
    AccountEntity findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Select("SELECT COALESCE(SUM(balance), 0) FROM t_account WHERE user_id = #{userId} AND deleted = 0")
    BigDecimal sumBalanceByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(*) FROM t_account WHERE user_id = #{userId} AND deleted = 0")
    Long countByUserId(@Param("userId") Long userId);

    @Select("SELECT COALESCE(SUM(balance), 0) FROM t_account WHERE user_id = #{userId} AND deleted = 0")
    BigDecimal sumNetWorthAtTime(@Param("userId") Long userId, @Param("time") LocalDateTime time);
}