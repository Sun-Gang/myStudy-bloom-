package com.bloomfinance.notification.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知VO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "通知响应")
public class NotificationVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 通知类型
     */
    @Schema(description = "通知类型", example = "1")
    private Integer type;

    /**
     * 通知类型名称
     */
    @Schema(description = "通知类型名称", example = "账单到期")
    private String typeName;

    /**
     * 标题
     */
    @Schema(description = "标题", example = "账单到期提醒")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容", example = "您的信用卡账单还有5天到期")
    private String content;

    /**
     * 状态: 1-未读 2-已读 3-已忽略
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 状态名称
     */
    @Schema(description = "状态名称", example = "未读")
    private String statusName;

    /**
     * 发送时间
     */
    @Schema(description = "发送时间")
    private LocalDateTime sentAt;

    /**
     * 阅读时间
     */
    @Schema(description = "阅读时间")
    private LocalDateTime readAt;
}