package com.bloomfinance.bookkeeping.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.bookkeeping.domain.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类仓储层
 *
 * @author BloomFinance
 */
@Mapper
public interface CategoryRepository extends BaseMapper<CategoryEntity> {

    /**
     * 查询所有启用的分类
     *
     * @return 分类列表
     */
    List<CategoryEntity> findAll();

    /**
     * 根据类型查询分类列表
     *
     * @param type 类型: 1-收入 2-支出
     * @return 分类列表
     */
    List<CategoryEntity> findByType(@Param("type") Integer type);

    /**
     * 根据父分类ID查询子分类
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<CategoryEntity> findByParentId(@Param("parentId") Long parentId);
}