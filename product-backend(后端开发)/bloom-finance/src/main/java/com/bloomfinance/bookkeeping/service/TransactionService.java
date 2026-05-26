package com.bloomfinance.bookkeeping.service;

import com.bloomfinance.bookkeeping.domain.dto.CreateTransactionDTO;
import com.bloomfinance.bookkeeping.domain.dto.TransactionQuery;
import com.bloomfinance.bookkeeping.domain.vo.BookkeepingSummaryVO;
import com.bloomfinance.bookkeeping.domain.vo.TransactionVO;
import com.bloomfinance.common.result.PageResult;

import java.util.List;

/**
 * 交易记录服务接口
 *
 * @author BloomFinance
 */
public interface TransactionService {

    /**
     * 创建交易记录
     *
     * @param userId 用户ID
     * @param dto    创建交易DTO
     * @return 交易记录ID
     */
    Long createTransaction(Long userId, CreateTransactionDTO dto);

    /**
     * 更新交易记录
     *
     * @param userId        用户ID
     * @param transactionId 交易记录ID
     * @param dto           创建交易DTO
     */
    void updateTransaction(Long userId, Long transactionId, CreateTransactionDTO dto);

    /**
     * 删除交易记录
     *
     * @param userId        用户ID
     * @param transactionId 交易记录ID
     */
    void deleteTransaction(Long userId, Long transactionId);

    /**
     * 根据ID获取交易记录
     *
     * @param userId        用户ID
     * @param transactionId 交易记录ID
     * @return 交易记录VO
     */
    TransactionVO getTransactionById(Long userId, Long transactionId);

    /**
     * 分页查询交易记录
     *
     * @param userId 用户ID
     * @param query  查询条件
     * @return 分页结果
     */
    PageResult<TransactionVO> pageTransactions(Long userId, TransactionQuery query);

    /**
     * 从银行同步交易记录
     *
     * @param userId    用户ID
     * @param accountId 账户ID
     * @return 同步的交易记录列表
     */
    List<TransactionVO> syncFromBank(Long userId, Long accountId);

    /**
     * 获取记账汇总信息（用于记账页面）
     *
     * @param userId 用户ID
     * @param yearMonth 年月，格式：yyyy-MM
     * @return 记账汇总
     */
    BookkeepingSummaryVO getBookkeepingSummary(Long userId, String yearMonth);
}