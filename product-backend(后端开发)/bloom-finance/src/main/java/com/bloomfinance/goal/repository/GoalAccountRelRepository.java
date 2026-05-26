package com.bloomfinance.goal.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.goal.domain.entity.GoalAccountRelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 目标账户关联仓库
 *
 * @author BloomFinance
 */
@Mapper
public interface GoalAccountRelRepository extends BaseMapper<GoalAccountRelEntity> {

    /**
     * 根据目标ID查询关联列表
     *
     * @param goalId 目标ID
     * @return 关联列表
     */
    List<GoalAccountRelEntity> findByGoalId(@Param("goalId") Long goalId);

    /**
     * 根据账户ID查询关联列表
     *
     * @param accountId 账户ID
     * @return 关联列表
     */
    List<GoalAccountRelEntity> findByAccountId(@Param("accountId") Long accountId);

    /**
     * 检查目标与账户关联是否存在
     *
     * @param goalId    目标ID
     * @param accountId 账户ID
     * @return 是否存在
     */
    boolean existsByGoalIdAndAccountId(@Param("goalId") Long goalId, @Param("accountId") Long accountId);

    /**
     * 删除目标与账户关联
     *
     * @param goalId    目标ID
     * @param accountId 账户ID
     */
    void deleteByGoalIdAndAccountId(@Param("goalId") Long goalId, @Param("accountId") Long accountId);
}