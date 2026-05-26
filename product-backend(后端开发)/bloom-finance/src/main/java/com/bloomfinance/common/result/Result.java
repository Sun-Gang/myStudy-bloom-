package com.bloomfinance.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * 统一响应封装
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "统一响应封装")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    @Schema(description = "状态码", example = "200")
    protected int code;

    /**
     * 消息
     */
    @Schema(description = "消息", example = "success")
    protected String message;

    /**
     * 数据
     */
    @Schema(description = "数据")
    protected T data;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳", example = "1716200000000")
    protected long timestamp;

    /**
     * 链路ID
     */
    @Schema(description = "链路ID")
    protected String traceId;

    protected Result() {
        this.timestamp = System.currentTimeMillis();
        this.traceId = generateTraceId();
    }

    protected Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.traceId = generateTraceId();
    }

    /**
     * 生成链路追踪ID
     *
     * @return 链路追踪ID
     */
    private static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 成功响应
     *
     * @param <T>  数据类型
     * @return Result
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return Result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    /**
     * 成功响应（带消息）
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return Result
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 创建成功响应（资源创建）
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return Result
     */
    public static <T> Result<T> created(T data) {
        return new Result<>(201, "created", data);
    }

    /**
     * 无内容响应（删除成功）
     *
     * @param <T> 数据类型
     * @return Result
     */
    public static <T> Result<T> noContent() {
        return new Result<>(204, "no content", null);
    }

    /**
     * 失败响应
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     数据类型
     * @return Result
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败响应（带数据）
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return Result
     */
    public static <T> Result<T> fail(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 失败响应（默认500）
     *
     * @param message 消息
     * @param <T>     数据类型
     * @return Result
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }
}