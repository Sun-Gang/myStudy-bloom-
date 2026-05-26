package com.bloomfinance.bookkeeping.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易类型枚举
 *
 * @author BloomFinance
 */
@Getter
@AllArgsConstructor
public enum TransactionTypeEnum {

    /**
     * 收入
     */
    INCOME(1, "收入"),

    /**
     * 支出
     */
    EXPENSE(2, "支出");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     *
     * @param code 交易类型编码
     * @return 交易类型枚举
     */
    public static TransactionTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (TransactionTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}