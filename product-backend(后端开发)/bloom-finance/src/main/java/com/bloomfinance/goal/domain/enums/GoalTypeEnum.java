package com.bloomfinance.goal.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 目标类型枚举
 *
 * @author BloomFinance
 */
@Getter
@AllArgsConstructor
public enum GoalTypeEnum {

    /**
     * 购房
     */
    HOUSING(1, "购房"),

    /**
     * 教育
     */
    EDUCATION(2, "教育"),

    /**
     * 养老
     */
    RETIREMENT(3, "养老"),

    /**
     * 旅游
     */
    TRAVEL(4, "旅游"),

    /**
     * 购车
     */
    CAR(5, "购车"),

    /**
     * 通用
     */
    GENERIC(6, "通用");

    private final Integer code;
    private final String description;

    /**
     * 根据code获取枚举
     *
     * @param code 目标类型编码
     * @return 目标类型枚举
     */
    public static GoalTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (GoalTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}