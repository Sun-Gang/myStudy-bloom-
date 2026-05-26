package com.bloomfinance.bookkeeping.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.bookkeeping.domain.entity.TransactionTemplateEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易模板仓储层
 *
 * @author BloomFinance
 */
@Mapper
public interface TransactionTemplateRepository extends BaseMapper<TransactionTemplateEntity> {

    /**
     * 根据用户ID查询交易模板列表
     *
     * @param userId 用户ID
     * @return 模板列表
     */
    List<TransactionTemplateEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 查询所有启用的模板
     *
     * @return 启用的模板列表
     */
    List<TransactionTemplateEntity> findActiveTemplates();
}