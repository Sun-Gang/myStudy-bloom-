package com.bloomfinance.account.controller;

import com.bloomfinance.account.domain.dto.AccountQuery;
import com.bloomfinance.account.domain.dto.CreateAccountDTO;
import com.bloomfinance.account.domain.dto.UpdateAccountDTO;
import com.bloomfinance.account.domain.vo.AccountSummaryVO;
import com.bloomfinance.account.domain.vo.AccountVO;
import com.bloomfinance.account.service.AccountService;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户控制器
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "账户管理", description = "账户增删改查接口")
public class AccountController {

    private final AccountService accountService;

    /**
     * 创建账户
     *
     * @param dto 创建账户DTO
     * @return 账户ID
     */
    @PostMapping
    @Operation(summary = "创建账户", description = "创建新账户")
    public Result<Long> createAccount(@Valid @RequestBody CreateAccountDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        Long accountId = accountService.createAccount(userId, dto);
        return Result.created(accountId);
    }

    /**
     * 更新账户
     *
     * @param accountId 账户ID
     * @param dto       更新账户DTO
     * @return 无内容
     */
    @PutMapping("/{accountId}")
    @Operation(summary = "更新账户", description = "更新账户信息")
    public Result<Void> updateAccount(
            @Parameter(description = "账户ID") @PathVariable Long accountId,
            @Valid @RequestBody UpdateAccountDTO dto) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        accountService.updateAccount(userId, accountId, dto);
        return Result.success();
    }

    /**
     * 删除账户
     *
     * @param accountId 账户ID
     * @return 无内容
     */
    @DeleteMapping("/{accountId}")
    @Operation(summary = "删除账户", description = "删除账户")
    public Result<Void> deleteAccount(
            @Parameter(description = "账户ID") @PathVariable Long accountId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        accountService.deleteAccount(userId, accountId);
        return Result.noContent();
    }

    /**
     * 获取账户详情
     *
     * @param accountId 账户ID
     * @return 账户VO
     */
    @GetMapping("/{accountId}")
    @Operation(summary = "获取账户详情", description = "根据ID获取账户信息")
    public Result<AccountVO> getAccountById(
            @Parameter(description = "账户ID") @PathVariable Long accountId) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        AccountVO vo = accountService.getAccountById(userId, accountId);
        return Result.success(vo);
    }

    /**
     * 查询账户列表
     *
     * @param query 查询条件
     * @return 账户列表
     */
    @GetMapping
    @Operation(summary = "查询账户列表", description = "查询用户账户列表")
    public Result<PageResult<AccountVO>> listAccounts(
            @Parameter(description = "查询条件") AccountQuery query) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        query.setUserId(userId);
        List<AccountVO> accounts = accountService.listAccounts(userId, query);
        PageResult<AccountVO> pageResult = PageResult.of(accounts, accounts.size(), query.getCurrent(), query.getSize());
        return Result.success(pageResult);
    }

    /**
     * 获取用户净资产
     *
     * @return 净资产
     */
    @GetMapping("/net-asset")
    @Operation(summary = "获取用户净资产", description = "计算用户净资产")
    public Result<BigDecimal> getUserNetAsset() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        BigDecimal netAsset = accountService.getUserNetAsset(userId);
        return Result.success(netAsset);
    }

    /**
     * 获取账户汇总信息
     *
     * @return 账户汇总
     */
    @GetMapping("/summary")
    @Operation(summary = "获取账户汇总", description = "获取账户页面所需的汇总信息")
    public Result<AccountSummaryVO> getAccountSummary() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        AccountSummaryVO summary = accountService.getAccountSummary(userId);
        return Result.success(summary);
    }
}