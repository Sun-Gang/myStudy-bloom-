package com.bloomfinance.verification.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码状态枚举
 *
 * @author bloom-finance
 */
@Getter
@AllArgsConstructor
public enum VerificationStatusEnum {

    UNUSED(0, "未使用"),
    USED(1, "已使用"),
    EXPIRED(2, "已过期");

    private final Integer code;
    private final String description;
}