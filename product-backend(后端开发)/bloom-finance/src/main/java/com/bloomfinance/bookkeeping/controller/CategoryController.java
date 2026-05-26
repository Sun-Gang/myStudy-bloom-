package com.bloomfinance.bookkeeping.controller;

import com.bloomfinance.bookkeeping.domain.vo.CategoryVO;
import com.bloomfinance.bookkeeping.service.CategoryService;
import com.bloomfinance.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类控制器
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
@Tag(name = "分类管理", description = "分类查询接口")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 查询所有分类列表
     *
     * @param type 类型: 1-收入 2-支出（可选）
     * @return 分类列表
     */
    @GetMapping
    @Operation(summary = "查询分类列表", description = "查询所有启用的分类列表")
    public Result<List<CategoryVO>> listCategories(
            @Parameter(description = "类型: 1-收入 2-支出") @RequestParam(required = false) Integer type) {
        List<CategoryVO> categories = categoryService.listCategories();
        return Result.success(categories);
    }

    /**
     * 获取分类树形结构
     *
     * @return 分类树形列表
     */
    @GetMapping("/tree")
    @Operation(summary = "获取分类树形结构", description = "获取分类的树形层级结构")
    public Result<List<CategoryVO>> getCategoryTree() {
        List<CategoryVO> tree = categoryService.getCategoryTree();
        return Result.success(tree);
    }
}