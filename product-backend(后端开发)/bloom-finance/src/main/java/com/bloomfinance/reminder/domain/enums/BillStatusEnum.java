package com.bloomfinance.reminder.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账单状态枚举
 *
 * @author BloomFinance
 */
@Getter
@AllArgsConstructor
public enum BillStatusEnum {

    /**
     * 待还
     */
    PENDING(1, "待还"),

    /**
     * 已还
     */
    PAID(2, "已还"),

    /**
     * 逾期
     */
    OVERDUE(3, "逾期");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     *
     * @param code 账单状态编码
     * @return 账单状态枚举
     */
    public static BillStatusEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BillStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}