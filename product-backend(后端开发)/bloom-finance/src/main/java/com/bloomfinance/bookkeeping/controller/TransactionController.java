package com.bloomfinance.bookkeeping.controller;

import com.bloomfinance.bookkeeping.domain.dto.CreateTransactionDTO;
import com.bloomfinance.bookkeeping.domain.dto.TransactionQuery;
import com.bloomfinance.bookkeeping.domain.vo.BookkeepingSummaryVO;
import com.bloomfinance.bookkeeping.domain.vo.TransactionVO;
import com.bloomfinance.bookkeeping.service.TransactionService;
import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 交易记录控制器
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "交易记录管理", description = "交易记录增删改查接口")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 创建交易记录
     *
     * @param dto 创建交易DTO
     * @return 交易记录ID
     */
    @PostMapping
    @Operation(summary = "创建交易记录", description = "创建新的交易记录")
    public Result<Long> createTransaction(@Valid @RequestBody CreateTransactionDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        Long transactionId = transactionService.createTransaction(userId, dto);
        return Result.created(transactionId);
    }

    /**
     * 更新交易记录
     *
     * @param transactionId 交易记录ID
     * @param dto           创建交易DTO
     * @return 无内容
     */
    @PutMapping("/{transactionId}")
    @Operation(summary = "更新交易记录", description = "更新交易记录信息")
    public Result<Void> updateTransaction(
            @Parameter(description = "交易记录ID") @PathVariable Long transactionId,
            @Valid @RequestBody CreateTransactionDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        transactionService.updateTransaction(userId, transactionId, dto);
        return Result.success();
    }

    /**
     * 删除交易记录
     *
     * @param transactionId 交易记录ID
     * @return 无内容
     */
    @DeleteMapping("/{transactionId}")
    @Operation(summary = "删除交易记录", description = "删除交易记录")
    public Result<Void> deleteTransaction(@Parameter(description = "交易记录ID") @PathVariable Long transactionId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        transactionService.deleteTransaction(userId, transactionId);
        return Result.noContent();
    }

    /**
     * 获取交易记录详情
     *
     * @param transactionId 交易记录ID
     * @return 交易记录VO
     */
    @GetMapping("/{transactionId}")
    @Operation(summary = "获取交易记录详情", description = "根据ID获取交易记录信息")
    public Result<TransactionVO> getTransactionById(@Parameter(description = "交易记录ID") @PathVariable Long transactionId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        TransactionVO vo = transactionService.getTransactionById(userId, transactionId);
        return Result.success(vo);
    }

    /**
     * 分页查询交易记录
     *
     * @param query 查询条件
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询交易记录", description = "分页查询交易记录列表")
    public Result<PageResult<TransactionVO>> pageTransactions(@Parameter(description = "查询条件") TransactionQuery query) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        query.setUserId(userId);
        PageResult<TransactionVO> pageResult = transactionService.pageTransactions(userId, query);
        return Result.success(pageResult);
    }

    /**
     * 从银行同步交易记录
     *
     * @param accountId 账户ID
     * @return 同步的交易记录列表
     */
    @PostMapping("/sync/{accountId}")
    @Operation(summary = "从银行同步交易记录", description = "从银行API同步交易记录")
    public Result<List<TransactionVO>> syncFromBank(@Parameter(description = "账户ID") @PathVariable Long accountId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        List<TransactionVO> transactions = transactionService.syncFromBank(userId, accountId);
        return Result.success(transactions);
    }

    /**
     * 获取记账汇总信息
     *
     * @param yearMonth 年月，格式：yyyy-MM
     * @return 记账汇总
     */
    @GetMapping("/summary")
    @Operation(summary = "获取记账汇总", description = "获取记账页面所需的汇总信息")
    public Result<BookkeepingSummaryVO> getBookkeepingSummary(
            @Parameter(description = "年月") @RequestParam(required = false) String yearMonth) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        BookkeepingSummaryVO summary = transactionService.getBookkeepingSummary(userId, yearMonth);
        return Result.success(summary);
    }
}