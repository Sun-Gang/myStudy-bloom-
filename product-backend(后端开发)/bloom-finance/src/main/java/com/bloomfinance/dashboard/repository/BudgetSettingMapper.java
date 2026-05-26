package com.bloomfinance.dashboard.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.dashboard.domain.entity.BudgetSettingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预算设置Mapper
 *
 * @author bloom-finance
 */
@Mapper
public interface BudgetSettingMapper extends BaseMapper<BudgetSettingEntity> {

    /**
     * 获取用户本月预算设置
     */
    @Select("SELECT * FROM t_budget_setting WHERE user_id = #{userId} AND budget_month = #{month} AND deleted = 0")
    List<BudgetSettingEntity> findByUserIdAndMonth(@Param("userId") Long userId, @Param("month") String month);
}