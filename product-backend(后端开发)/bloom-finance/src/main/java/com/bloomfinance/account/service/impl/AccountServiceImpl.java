package com.bloomfinance.account.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bloomfinance.account.domain.dto.AccountQuery;
import com.bloomfinance.account.domain.dto.CreateAccountDTO;
import com.bloomfinance.account.domain.dto.UpdateAccountDTO;
import com.bloomfinance.account.domain.entity.AccountEntity;
import com.bloomfinance.account.domain.enums.AccountTypeEnum;
import com.bloomfinance.account.domain.vo.AccountSummaryVO;
import com.bloomfinance.account.domain.vo.AccountVO;
import com.bloomfinance.account.repository.AccountRepository;
import com.bloomfinance.account.service.AccountService;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 账户服务实现类
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private static final String CACHE_NAME = "account";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAccount(Long userId, CreateAccountDTO dto) {
        log.info("创建账户, userId={}, accountType={}, bankName={}", userId, dto.getAccountType(), dto.getBankName());

        // 校验账户类型
        AccountTypeEnum accountTypeEnum = AccountTypeEnum.fromCode(dto.getAccountType());
        if (accountTypeEnum == null) {
            throw new BusinessException(400, "无效的账户类型");
        }

        // 构建实体
        AccountEntity entity = new AccountEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setUserId(userId);
        entity.setStatus(1); // 默认正常状态

        // 保存
        accountRepository.insert(entity);
        log.info("创建账户成功, userId={}, accountId={}", userId, entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, key = "#accountId")
    public void updateAccount(Long userId, Long accountId, UpdateAccountDTO dto) {
        log.info("更新账户, userId={}, accountId={}", userId, accountId);

        AccountEntity entity = accountRepository.findByIdAndUserId(accountId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        // 更新字段
        if (StringUtils.hasText(dto.getBankName())) {
            entity.setBankName(dto.getBankName());
        }
        if (dto.getBalance() != null) {
            entity.setBalance(dto.getBalance());
        }
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        accountRepository.updateById(entity);
        log.info("更新账户成功, userId={}, accountId={}", userId, accountId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, key = "#accountId")
    public void deleteAccount(Long userId, Long accountId) {
        log.info("删除账户, userId={}, accountId={}", userId, accountId);

        AccountEntity entity = accountRepository.findByIdAndUserId(accountId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        // 逻辑删除
        accountRepository.deleteById(accountId);
        log.info("删除账户成功, userId={}, accountId={}", userId, accountId);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = CACHE_NAME, key = "#accountId", unless = "#result == null")
    public AccountVO getAccountById(Long userId, Long accountId) {
        log.info("获取账户详情, userId={}, accountId={}", userId, accountId);

        AccountEntity entity = accountRepository.findByIdAndUserId(accountId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("账户不存在");
        }

        return convertToVO(entity);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = CACHE_NAME, key = "'list:' + #userId + ':' + #query.hashCode()", unless = "#result.isEmpty()")
    public List<AccountVO> listAccounts(Long userId, AccountQuery query) {
        log.info("查询账户列表, userId={}, query={}", userId, query);

        LambdaQueryWrapper<AccountEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountEntity::getUserId, userId);

        if (query.getAccountType() != null) {
            wrapper.eq(AccountEntity::getAccountType, query.getAccountType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(AccountEntity::getStatus, query.getStatus());
        }
        if (query.getStartDate() != null) {
            wrapper.ge(AccountEntity::getCreatedAt, query.getStartDate());
        }
        if (query.getEndDate() != null) {
            wrapper.le(AccountEntity::getCreatedAt, query.getEndDate());
        }

        wrapper.orderByDesc(AccountEntity::getCreatedAt);

        List<AccountEntity> entities = accountRepository.selectList(wrapper);
        return entities.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public BigDecimal getUserNetAsset(Long userId) {
        log.info("计算用户净资产, userId={}", userId);

        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal netAsset = BigDecimal.ZERO;

        for (AccountEntity account : accounts) {
            if (account.getStatus() != 1) {
                continue; // 跳过冻结账户
            }

            AccountTypeEnum typeEnum = AccountTypeEnum.fromCode(account.getAccountType());
            if (typeEnum == null) {
                continue;
            }

            BigDecimal balance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;

            switch (typeEnum) {
                case SAVINGS:
                case SECURITIES:
                case FUND:
                case SOCIAL_SECURITY:
                case HOUSING_FUND:
                    netAsset = netAsset.add(balance);
                    break;
                case CREDIT_CARD:
                case LIABILITY:
                    netAsset = netAsset.subtract(balance);
                    break;
                case OTHER:
                    // 其他类型账户不计入净资产计算
                    break;
            }
        }

        log.info("计算用户净资产完成, userId={}, netAsset={}", userId, netAsset);
        return netAsset;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public AccountSummaryVO getAccountSummary(Long userId) {
        log.info("获取账户汇总信息, userId={}", userId);

        List<AccountEntity> accounts = accountRepository.findByUserId(userId);
        if (accounts == null || accounts.isEmpty()) {
            return buildEmptySummary();
        }

        BigDecimal totalAsset = BigDecimal.ZERO;
        BigDecimal totalLiability = BigDecimal.ZERO;
        BigDecimal bankCardBalance = BigDecimal.ZERO;
        int bankCardCount = 0;
        BigDecimal investmentBalance = BigDecimal.ZERO;
        int investmentCount = 0;
        BigDecimal socialSecurityBalance = BigDecimal.ZERO;
        int socialSecurityCount = 0;
        BigDecimal liabilityBalance = BigDecimal.ZERO;
        int liabilityCount = 0;

        for (AccountEntity account : accounts) {
            if (account.getStatus() != 1) {
                continue;
            }

            AccountTypeEnum typeEnum = AccountTypeEnum.fromCode(account.getAccountType());
            if (typeEnum == null) {
                continue;
            }

            BigDecimal balance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;

            switch (typeEnum) {
                case SAVINGS:
                    totalAsset = totalAsset.add(balance);
                    bankCardBalance = bankCardBalance.add(balance);
                    bankCardCount++;
                    break;
                case CREDIT_CARD:
                    totalLiability = totalLiability.add(balance);
                    liabilityBalance = liabilityBalance.add(balance);
                    liabilityCount++;
                    break;
                case SECURITIES:
                case FUND:
                    totalAsset = totalAsset.add(balance);
                    investmentBalance = investmentBalance.add(balance);
                    investmentCount++;
                    break;
                case SOCIAL_SECURITY:
                case HOUSING_FUND:
                    totalAsset = totalAsset.add(balance);
                    socialSecurityBalance = socialSecurityBalance.add(balance);
                    socialSecurityCount++;
                    break;
                case LIABILITY:
                    totalLiability = totalLiability.add(balance);
                    liabilityBalance = liabilityBalance.add(balance);
                    liabilityCount++;
                    break;
                default:
                    break;
            }
        }

        BigDecimal totalNetWorth = totalAsset.subtract(totalLiability);
        BigDecimal monthlyGrowthRate = BigDecimal.ZERO;
        if (totalNetWorth.compareTo(BigDecimal.ZERO) > 0) {
            monthlyGrowthRate = BigDecimal.ZERO;
        }

        return AccountSummaryVO.builder()
                .totalNetWorth(totalNetWorth)
                .totalAsset(totalAsset)
                .totalLiability(totalLiability)
                .monthlyGrowthRate(monthlyGrowthRate)
                .healthStatus(totalLiability.compareTo(BigDecimal.ZERO) == 0 ? "安全且健康" : "有负债")
                .bankCardSummary(AccountSummaryVO.AccountTypeSummary.builder()
                        .balance(bankCardBalance)
                        .count(bankCardCount)
                        .percentage(totalAsset.compareTo(BigDecimal.ZERO) > 0
                                ? bankCardBalance.divide(totalAsset, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                                : BigDecimal.ZERO)
                        .build())
                .investmentSummary(AccountSummaryVO.AccountTypeSummary.builder()
                        .balance(investmentBalance)
                        .count(investmentCount)
                        .percentage(totalAsset.compareTo(BigDecimal.ZERO) > 0
                                ? investmentBalance.divide(totalAsset, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                                : BigDecimal.ZERO)
                        .build())
                .socialSecuritySummary(AccountSummaryVO.AccountTypeSummary.builder()
                        .balance(socialSecurityBalance)
                        .count(socialSecurityCount)
                        .percentage(totalAsset.compareTo(BigDecimal.ZERO) > 0
                                ? socialSecurityBalance.divide(totalAsset, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                                : BigDecimal.ZERO)
                        .lastPaymentDate(null)
                        .build())
                .liabilitySummary(AccountSummaryVO.LiabilitySummary.builder()
                        .totalAmount(liabilityBalance)
                        .liabilityType("信用卡与个人贷款")
                        .percentage(totalAsset.compareTo(BigDecimal.ZERO) > 0
                                ? liabilityBalance.divide(totalAsset, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                                : BigDecimal.ZERO)
                        .build())
                .fixedAssetSummary(AccountSummaryVO.FixedAssetSummary.builder()
                        .totalAmount(BigDecimal.ZERO)
                        .propertyCount(0)
                        .vehicleCount(0)
                        .build())
                .build();
    }

    private AccountSummaryVO buildEmptySummary() {
        return AccountSummaryVO.builder()
                .totalNetWorth(BigDecimal.ZERO)
                .totalAsset(BigDecimal.ZERO)
                .totalLiability(BigDecimal.ZERO)
                .monthlyGrowthRate(BigDecimal.ZERO)
                .healthStatus("暂无数据")
                .bankCardSummary(AccountSummaryVO.AccountTypeSummary.builder()
                        .balance(BigDecimal.ZERO).count(0).percentage(BigDecimal.ZERO).build())
                .investmentSummary(AccountSummaryVO.AccountTypeSummary.builder()
                        .balance(BigDecimal.ZERO).count(0).percentage(BigDecimal.ZERO).build())
                .socialSecuritySummary(AccountSummaryVO.AccountTypeSummary.builder()
                        .balance(BigDecimal.ZERO).count(0).percentage(BigDecimal.ZERO).build())
                .liabilitySummary(AccountSummaryVO.LiabilitySummary.builder()
                        .totalAmount(BigDecimal.ZERO).liabilityType("信用卡与个人贷款").percentage(BigDecimal.ZERO).build())
                .fixedAssetSummary(AccountSummaryVO.FixedAssetSummary.builder()
                        .totalAmount(BigDecimal.ZERO).propertyCount(0).vehicleCount(0).build())
                .build();
    }

    /**
     * 实体转换为VO
     *
     * @param entity 账户实体
     * @return 账户VO
     */
    private AccountVO convertToVO(AccountEntity entity) {
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置账户类型名称
        AccountTypeEnum typeEnum = AccountTypeEnum.fromCode(entity.getAccountType());
        if (typeEnum != null) {
            vo.setAccountTypeName(typeEnum.getDescription());
        }

        // 卡号掩码处理
        vo.setCardNoMasked(maskCardNo(entity.getCardNo()));

        return vo;
    }

    /**
     * 卡号掩码处理
     * 显示前四位后四位，中间用*替代
     *
     * @param cardNo 原始卡号
     * @return 掩码卡号
     */
    private String maskCardNo(String cardNo) {
        if (!StringUtils.hasText(cardNo) || cardNo.length() < 8) {
            return cardNo;
        }
        return cardNo.substring(0, 4) + "****" + cardNo.substring(cardNo.length() - 4);
    }
}