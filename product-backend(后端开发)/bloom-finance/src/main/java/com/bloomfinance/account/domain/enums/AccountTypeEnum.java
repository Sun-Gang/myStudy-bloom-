package com.bloomfinance.account.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户类型枚举
 *
 * @author BloomFinance
 */
@Getter
@AllArgsConstructor
public enum AccountTypeEnum {

    /**
     * 储蓄卡
     */
    SAVINGS(1, "储蓄卡"),

    /**
     * 信用卡
     */
    CREDIT_CARD(2, "信用卡"),

    /**
     * 证券
     */
    SECURITIES(3, "证券"),

    /**
     * 基金
     */
    FUND(4, "基金"),

    /**
     * 社保
     */
    SOCIAL_SECURITY(5, "社保"),

    /**
     * 公积金
     */
    HOUSING_FUND(6, "公积金"),

    /**
     * 负债
     */
    LIABILITY(7, "负债"),

    /**
     * 其他
     */
    OTHER(8, "其他");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     *
     * @param code 账户类型编码
     * @return 账户类型枚举
     */
    public static AccountTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (AccountTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}