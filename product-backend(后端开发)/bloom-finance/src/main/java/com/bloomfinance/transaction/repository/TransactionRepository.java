package com.bloomfinance.transaction.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.transaction.domain.entity.TransactionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

/**
 * 流水记录Repository
 *
 * @author bloom-finance
 */
@Mapper
public interface TransactionRepository extends BaseMapper<TransactionEntity> {

    @Select("SELECT * FROM t_transaction WHERE user_id = #{userId} AND transaction_date BETWEEN #{startDate} AND #{endDate} AND deleted = 0")
    List<TransactionEntity> findByUserIdAndDateRange(@Param("userId") Long userId,
                                                     @Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM t_transaction WHERE user_id = #{userId} AND transaction_type = 1 AND DATE_FORMAT(transaction_date, '%Y-%m') = #{month} AND deleted = 0")
    BigDecimal sumIncomeByUserIdAndMonth(@Param("userId") Long userId, @Param("month") YearMonth month);

    @Select("SELECT COALESCE(SUM(ABS(amount)), 0) FROM t_transaction WHERE user_id = #{userId} AND transaction_type = 2 AND DATE_FORMAT(transaction_date, '%Y-%m') = #{month} AND deleted = 0")
    BigDecimal sumExpenseByUserIdAndMonth(@Param("userId") Long userId, @Param("month") YearMonth month);

    @Select("SELECT COALESCE(SUM(ABS(amount)), 0) FROM t_transaction WHERE user_id = #{userId} AND category_id = #{categoryId} AND transaction_type = 2 AND DATE_FORMAT(transaction_date, '%Y-%m') = #{month} AND deleted = 0")
    BigDecimal sumExpenseByUserIdAndCategoryAndMonth(@Param("userId") Long userId,
                                                     @Param("categoryId") Long categoryId,
                                                     @Param("month") YearMonth month);
}