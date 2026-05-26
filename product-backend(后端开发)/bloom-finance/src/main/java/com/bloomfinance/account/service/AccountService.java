package com.bloomfinance.account.service;

import com.bloomfinance.account.domain.dto.AccountQuery;
import com.bloomfinance.account.domain.dto.CreateAccountDTO;
import com.bloomfinance.account.domain.dto.UpdateAccountDTO;
import com.bloomfinance.account.domain.vo.AccountSummaryVO;
import com.bloomfinance.account.domain.vo.AccountVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 账户服务接口
 *
 * @author BloomFinance
 */
public interface AccountService {

    /**
     * 创建账户
     *
     * @param userId 用户ID
     * @param dto    创建账户DTO
     * @return 账户ID
     */
    Long createAccount(Long userId, CreateAccountDTO dto);

    /**
     * 更新账户
     *
     * @param userId    用户ID
     * @param accountId 账户ID
     * @param dto       更新账户DTO
     */
    void updateAccount(Long userId, Long accountId, UpdateAccountDTO dto);

    /**
     * 删除账户
     *
     * @param userId    用户ID
     * @param accountId 账户ID
     */
    void deleteAccount(Long userId, Long accountId);

    /**
     * 根据ID获取账户
     *
     * @param userId    用户ID
     * @param accountId 账户ID
     * @return 账户VO
     */
    AccountVO getAccountById(Long userId, Long accountId);

    /**
     * 查询用户账户列表
     *
     * @param userId 用户ID
     * @param query  查询条件
     * @return 账户列表
     */
    List<AccountVO> listAccounts(Long userId, AccountQuery query);

    /**
     * 获取用户净资产
     * 计算公式: 储蓄卡 + 证券 + 基金 + 社保 + 公积金 - 负债 - 信用卡
     *
     * @param userId 用户ID
     * @return 净资产
     */
    BigDecimal getUserNetAsset(Long userId);

    /**
     * 获取账户汇总信息（用于账户页面）
     *
     * @param userId 用户ID
     * @return 账户汇总
     */
    AccountSummaryVO getAccountSummary(Long userId);
}