package com.bloomfinance.reminder.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账单类型枚举
 *
 * @author BloomFinance
 */
@Getter
@AllArgsConstructor
public enum BillTypeEnum {

    /**
     * 信用卡
     */
    CREDIT_CARD(1, "信用卡"),

    /**
     * 贷款
     */
    LOAN(2, "贷款");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     *
     * @param code 账单类型编码
     * @return 账单类型枚举
     */
    public static BillTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BillTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}