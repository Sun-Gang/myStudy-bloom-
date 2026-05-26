package com.bloomfinance.account.domain.dto;

import com.bloomfinance.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 账户查询DTO
 *
 * @author BloomFinance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "账户查询请求")
public class AccountQuery extends PageQuery {

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 账户类型: 1-储蓄卡 2-信用卡 3-证券 4-基金 5-社保 6-公积金 7-负债 8-其他
     */
    @Schema(description = "账户类型", example = "1")
    private Integer accountType;

    /**
     * 状态: 0-冻结 1-正常
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期", example = "2024-01-01 00:00:00")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @Schema(description = "结束日期", example = "2024-12-31 23:59:59")
    private LocalDateTime endDate;
}