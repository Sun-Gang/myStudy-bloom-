package com.bloomfinance.common.exception;

import lombok.Getter;

/**
 * 业务异常基类
 *
 * @author BloomFinance
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 业务错误码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   原始异常
     */
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    // ==================== 通用业务异常快捷方法 ====================

    /**
     * 创建业务异常
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException of(int code, String message) {
        return new BusinessException(code, message);
    }

    /**
     * 创建参数异常（400）
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException badRequest(String message) {
        return new BusinessException(400, message);
    }

    /**
     * 创建服务器异常（500）
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException serverError(String message) {
        return new BusinessException(500, message);
    }

    /**
     * 创建服务器异常（500）
     *
     * @param message 错误消息
     * @param cause   原始异常
     * @return 业务异常
     */
    public static BusinessException serverError(String message, Throwable cause) {
        return new BusinessException(500, message, cause);
    }

    /**
     * 创建数据冲突异常（409）
     *
     * @param message 错误消息
     * @return 业务异常
     */
    public static BusinessException conflict(String message) {
        return new BusinessException(409, message);
    }
}