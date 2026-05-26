package com.bloomfinance.bookkeeping.service;

import com.bloomfinance.bookkeeping.domain.vo.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 *
 * @author BloomFinance
 */
public interface CategoryService {

    /**
     * 查询所有分类列表
     *
     * @return 分类列表
     */
    List<CategoryVO> listCategories();

    /**
     * 获取分类树形结构
     *
     * @return 分类树形列表
     */
    List<CategoryVO> getCategoryTree();
}