package com.bloomfinance.common.exception;

/**
 * 资源不存在异常
 *
 * @author BloomFinance
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(404, message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(404, message, cause);
    }
}