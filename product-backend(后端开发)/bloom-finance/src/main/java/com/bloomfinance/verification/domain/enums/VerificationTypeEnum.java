package com.bloomfinance.verification.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 验证码类型枚举
 *
 * @author bloom-finance
 */
@Getter
@AllArgsConstructor
public enum VerificationTypeEnum {

    REGISTER(1, "注册"),
    LOGIN(2, "登录"),
    FORGOT_PASSWORD(3, "找回密码"),
    BIND_EMAIL(4, "绑定邮箱");

    private final Integer code;
    private final String description;

    public static VerificationTypeEnum fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}