package com.bloomfinance.notification.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

/**
 * 通知偏好设置VO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "通知偏好设置响应")
public class NotificationPrefsVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 账单到期提醒是否启用
     */
    @Schema(description = "账单到期提醒是否启用", example = "true")
    private Boolean billDueEnabled;

    /**
     * 逾期预警提醒是否启用
     */
    @Schema(description = "逾期预警提醒是否启用", example = "true")
    private Boolean overdueWarningEnabled;

    /**
     * 大额收支提醒是否启用
     */
    @Schema(description = "大额收支提醒是否启用", example = "true")
    private Boolean largeTxEnabled;

    /**
     * 目标偏离提醒是否启用
     */
    @Schema(description = "目标偏离提醒是否启用", example = "true")
    private Boolean goalDeviationEnabled;

    /**
     * 安静时段开始时间
     */
    @Schema(description = "安静时段开始时间", example = "22:00")
    private LocalTime quietHoursStart;

    /**
     * 安静时段结束时间
     */
    @Schema(description = "安静时段结束时间", example = "08:00")
    private LocalTime quietHoursEnd;
}