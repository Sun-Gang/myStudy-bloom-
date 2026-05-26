package com.bloomfinance.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

/**
 * 链路追踪ID拦截器
 * <p>
 * 为每个请求生成并设置链路追踪ID，便于日志追踪和问题排查
 * </p>
 *
 * @author bloom-finance
 */
@Slf4j
@Component
public class TraceIdInterceptor implements HandlerInterceptor {

    /**
     * 请求头中的链路追踪ID
     */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * 请求属性中的链路追踪ID
     */
    public static final String TRACE_ID_ATTRIBUTE = "traceId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 优先从请求头获取traceId，如果没有则生成新的
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = generateTraceId();
        }

        // 设置到请求属性中
        request.setAttribute(TRACE_ID_ATTRIBUTE, traceId);

        // 设置到响应头中
        response.setHeader(TRACE_ID_HEADER, traceId);

        // 记录日志
        log.debug("请求处理开始, traceId={}, uri={}, method={}",
                traceId, request.getRequestURI(), request.getMethod());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String traceId = (String) request.getAttribute(TRACE_ID_ATTRIBUTE);

        if (ex != null) {
            log.error("请求处理异常, traceId={}, uri={}, method={}",
                    traceId, request.getRequestURI(), request.getMethod(), ex);
        } else {
            log.debug("请求处理完成, traceId={}, uri={}, method={}, status={}",
                    traceId, request.getRequestURI(), request.getMethod(), response.getStatus());
        }
    }

    /**
     * 生成链路追踪ID
     *
     * @return 链路追踪ID
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}