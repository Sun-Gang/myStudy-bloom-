package com.bloomfinance.notification.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalTime;
import java.time.LocalDateTime;

/**
 * 通知偏好设置实体
 *
 * @author BloomFinance
 */
@Data
@TableName("t_notification_prefs")
public class NotificationPrefsEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账单到期提醒是否启用
     */
    private Boolean billDueEnabled;

    /**
     * 逾期预警提醒是否启用
     */
    private Boolean overdueWarningEnabled;

    /**
     * 大额收支提醒是否启用
     */
    private Boolean largeTxEnabled;

    /**
     * 目标偏离提醒是否启用
     */
    private Boolean goalDeviationEnabled;

    /**
     * 安静时段开始时间
     */
    private LocalTime quietHoursStart;

    /**
     * 安静时段结束时间
     */
    private LocalTime quietHoursEnd;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}