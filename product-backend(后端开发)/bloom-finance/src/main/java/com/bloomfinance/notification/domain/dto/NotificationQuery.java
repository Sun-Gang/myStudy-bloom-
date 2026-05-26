package com.bloomfinance.notification.domain.dto;

import com.bloomfinance.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知查询DTO
 *
 * @author BloomFinance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "通知查询请求")
public class NotificationQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 通知类型
     */
    @Schema(description = "通知类型", example = "1")
    private Integer type;

    /**
     * 状态: 1-未读 2-已读 3-已忽略
     */
    @Schema(description = "状态", example = "1")
    private Integer status;
}