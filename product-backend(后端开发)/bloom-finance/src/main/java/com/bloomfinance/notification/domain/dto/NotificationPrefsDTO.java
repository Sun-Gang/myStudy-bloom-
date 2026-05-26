package com.bloomfinance.notification.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

/**
 * 通知偏好设置DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "通知偏好设置请求")
public class NotificationPrefsDTO {

    /**
     * 账单到期提醒是否启用
     */
    @Schema(description = "账单到期提醒是否启用", example = "true")
    private Boolean billDueEnabled = true;

    /**
     * 逾期预警提醒是否启用
     */
    @Schema(description = "逾期预警提醒是否启用", example = "true")
    private Boolean overdueWarningEnabled = true;

    /**
     * 大额收支提醒是否启用
     */
    @Schema(description = "大额收支提醒是否启用", example = "true")
    private Boolean largeTxEnabled = true;

    /**
     * 目标偏离提醒是否启用
     */
    @Schema(description = "目标偏离提醒是否启用", example = "true")
    private Boolean goalDeviationEnabled = true;

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