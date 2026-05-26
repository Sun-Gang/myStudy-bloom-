package com.bloomfinance.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户账号状态枚举
 *
 * @author bloom-finance
 */
@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    /**
     * 冻结
     */
    FROZEN(0, "账号已冻结"),

    /**
     * 正常
     */
    NORMAL(1, "正常");

    private final Integer code;
    private final String description;
}