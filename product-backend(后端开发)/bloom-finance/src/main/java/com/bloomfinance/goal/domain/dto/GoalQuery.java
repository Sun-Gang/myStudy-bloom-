package com.bloomfinance.goal.domain.dto;

import com.bloomfinance.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 目标查询DTO
 *
 * @author BloomFinance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "目标查询请求")
public class GoalQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 目标类型
     */
    @Schema(description = "目标类型", example = "1")
    private Integer goalType;

    /**
     * 状态: 1-进行中 2-已完成 3-已放弃
     */
    @Schema(description = "状态", example = "1")
    private Integer status;
}