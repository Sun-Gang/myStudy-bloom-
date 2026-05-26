package com.bloomfinance.common.exception;

/**
 * 未授权异常（401）
 *
 * @author bloom-finance
 */
public class UnauthorizedException extends BusinessException {

    /**
     * 默认错误码
     */
    private static final int DEFAULT_CODE = 401;

    /**
     * 默认错误消息
     */
    private static final String DEFAULT_MESSAGE = "未授权，请先登录";

    /**
     * 构造函数
     */
    public UnauthorizedException() {
        super(DEFAULT_CODE, DEFAULT_MESSAGE);
    }

    /**
     * 构造函数
     *
     * @param message 自定义错误消息
     */
    public UnauthorizedException(String message) {
        super(DEFAULT_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 自定义错误消息
     * @param cause   原始异常
     */
    public UnauthorizedException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    /**
     * 工厂方法：令牌无效
     *
     * @return UnauthorizedException实例
     */
    public static UnauthorizedException invalidToken() {
        return new UnauthorizedException("令牌无效或已过期");
    }

    /**
     * 工厂方法：登录已过期
     *
     * @return UnauthorizedException实例
     */
    public static UnauthorizedException sessionExpired() {
        return new UnauthorizedException("登录已过期，请重新登录");
    }

    /**
     * 工厂方法：无权限访问
     *
     * @return UnauthorizedException实例
     */
    public static UnauthorizedException accessDenied() {
        return new UnauthorizedException("无权限访问该资源");
    }
}
