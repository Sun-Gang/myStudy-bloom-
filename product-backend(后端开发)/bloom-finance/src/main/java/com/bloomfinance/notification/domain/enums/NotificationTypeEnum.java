package com.bloomfinance.notification.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知类型枚举
 *
 * @author BloomFinance
 */
@Getter
@AllArgsConstructor
public enum NotificationTypeEnum {

    /**
     * 账单到期
     */
    BILL_DUE(1, "账单到期"),

    /**
     * 逾期预警
     */
    OVERDUE_WARNING(2, "逾期预警"),

    /**
     * 大额收支
     */
    LARGE_TRANSACTION(3, "大额收支"),

    /**
     * 目标偏离
     */
    GOAL_DEVIATION(4, "目标偏离"),

    /**
     * 系统通知
     */
    SYSTEM(5, "系统通知");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     *
     * @param code 通知类型编码
     * @return 通知类型枚举
     */
    public static NotificationTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (NotificationTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}