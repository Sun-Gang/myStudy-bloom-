package com.bloomfinance.goal.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.goal.domain.entity.GoalEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 目标仓库
 *
 * @author BloomFinance
 */
@Mapper
public interface GoalRepository extends BaseMapper<GoalEntity> {

    /**
     * 根据用户ID查询目标列表
     *
     * @param userId 用户ID
     * @return 目标列表
     */
    List<GoalEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID和状态查询目标
     *
     * @param userId 用户ID
     * @param status 状态
     * @return 目标列表
     */
    List<GoalEntity> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);

    /**
     * 根据ID和用户ID查询目标
     *
     * @param id     目标ID
     * @param userId 用户ID
     * @return 目标实体
     */
    GoalEntity findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}