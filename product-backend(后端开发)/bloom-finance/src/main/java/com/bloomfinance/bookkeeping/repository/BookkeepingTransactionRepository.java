package com.bloomfinance.bookkeeping.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bloomfinance.bookkeeping.domain.dto.TransactionQuery;
import com.bloomfinance.bookkeeping.domain.entity.BookkeepingTransactionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 交易记录仓储层
 *
 * @author BloomFinance
 */
@Mapper
public interface BookkeepingTransactionRepository extends BaseMapper<BookkeepingTransactionEntity> {

    /**
     * 根据用户ID分页查询交易记录
     *
     * @param userId 用户ID
     * @param page   分页对象
     * @param query  查询条件
     * @return 分页交易记录
     */
    IPage<BookkeepingTransactionEntity> findByUserId(
            @Param("userId") Long userId,
            IPage<BookkeepingTransactionEntity> page,
            @Param("query") TransactionQuery query);

    /**
     * 根据账户ID查询交易记录列表
     *
     * @param accountId 账户ID
     * @return 交易记录列表
     */
    List<BookkeepingTransactionEntity> findByAccountId(@Param("accountId") Long accountId);

    /**
     * 根据用户ID和交易类型统计金额
     *
     * @param userId         用户ID
     * @param transactionType 交易类型
     * @param startDate      开始日期
     * @param endDate        结束日期
     * @return 金额总和
     */
    BigDecimal sumAmountByUserIdAndType(
            @Param("userId") Long userId,
            @Param("transactionType") Integer transactionType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}