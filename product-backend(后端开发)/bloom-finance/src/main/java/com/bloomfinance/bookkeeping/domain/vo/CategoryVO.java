package com.bloomfinance.bookkeeping.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分类视图对象
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "分类响应")
public class CategoryVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 父分类ID（顶级分类为0）
     */
    @Schema(description = "父分类ID", example = "0")
    private Long parentId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称", example = "餐饮")
    private String name;

    /**
     * 分类图标
     */
    @Schema(description = "分类图标", example = "food")
    private String icon;

    /**
     * 类型: 1-收入 2-支出
     */
    @Schema(description = "类型: 1-收入 2-支出", example = "2")
    private Integer type;

    /**
     * 类型名称
     */
    @Schema(description = "类型名称", example = "支出")
    private String typeName;

    /**
     * 子分类列表
     */
    @Schema(description = "子分类列表")
    private List<CategoryVO> children;
}