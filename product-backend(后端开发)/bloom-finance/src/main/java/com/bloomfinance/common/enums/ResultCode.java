package com.bloomfinance.common.enums;

import lombok.Getter;

/**
 * 响应码枚举
 *
 * @author bloom-finance
 */
@Getter
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "success"),

    /**
     * 已创建
     */
    CREATED(201, "created"),

    /**
     * 无内容
     */
    NO_CONTENT(204, "no content"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "bad request"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "unauthorized"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "forbidden"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "not found"),

    /**
     * 数据冲突
     */
    CONFLICT(409, "conflict"),

    /**
     * 验证错误
     */
    VALIDATION_ERROR(422, "validation error"),

    /**
     * 服务器内部错误
     */
    SERVER_ERROR(500, "server error");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 消息
     */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据状态码获取枚举
     *
     * @param code 状态码
     * @return ResultCode
     */
    public static ResultCode fromCode(int code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        return SERVER_ERROR;
    }
}