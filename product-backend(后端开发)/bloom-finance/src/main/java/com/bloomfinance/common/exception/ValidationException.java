package com.bloomfinance.common.exception;

/**
 * 验证异常（422）
 *
 * @author bloom-finance
 */
public class ValidationException extends BusinessException {

    /**
     * 默认错误码
     */
    private static final int DEFAULT_CODE = 422;

    /**
     * 默认错误消息
     */
    private static final String DEFAULT_MESSAGE = "数据验证失败";

    /**
     * 构造函数
     */
    public ValidationException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * 构造函数
     *
     * @param message 自定义错误消息
     */
    public ValidationException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 自定义错误消息
     * @param cause   原始异常
     */
    public ValidationException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * 工厂方法：创建验证异常
     *
     * @param field   验证失败的字段
     * @param message 验证失败的消息
     * @return ValidationException实例
     */
    public static ValidationException of(String field, String message) {
        return new ValidationException(field + ": " + message);
    }

    /**
     * 工厂方法：创建验证异常
     *
     * @param message 验证失败的消息
     * @return ValidationException实例
     */
    public static ValidationException withMessage(String message) {
        return new ValidationException(message);
    }
}
