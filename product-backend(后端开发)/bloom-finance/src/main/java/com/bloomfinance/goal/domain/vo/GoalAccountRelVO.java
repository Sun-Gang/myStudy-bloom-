package com.bloomfinance.goal.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 目标账户关联VO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "目标账户关联响应")
public class GoalAccountRelVO {

    /**
     * 目标ID
     */
    @Schema(description = "目标ID", example = "1")
    private Long goalId;

    /**
     * 账户ID
     */
    @Schema(description = "账户ID", example = "100")
    private Long accountId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称", example = "我的储蓄卡")
    private String accountName;

    /**
     * 账户类型名称
     */
    @Schema(description = "账户类型名称", example = "储蓄卡")
    private String accountTypeName;
}