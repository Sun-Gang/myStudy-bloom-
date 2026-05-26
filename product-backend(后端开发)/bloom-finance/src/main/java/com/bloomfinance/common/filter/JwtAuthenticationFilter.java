package com.bloomfinance.common.filter;

import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.util.SecurityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * <p>
 * 从请求头中提取JWT令牌并验证，验证成功后将用户ID存入UserContext
 * </p>
 *
 * @author bloom-finance
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final SecurityUtils securityUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);

            if (StringUtils.hasText(token) && securityUtils.validateToken(token)) {
                if (securityUtils.isAccessToken(token)) {
                    Long userId = securityUtils.extractUserId(token);

                    if (userId != null) {
                        // 设置用户上下文
                        UserContext.setUserId(userId);

                        // 设置Spring Security认证信息
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.debug("JWT认证成功, userId={}, uri={}", userId, request.getRequestURI());
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            log.warn("JWT令牌已过期: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("JWT令牌格式错误: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT令牌不支持: {}", e.getMessage());
        } catch (SignatureException e) {
            log.warn("JWT签名验证失败: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT认证过程发生异常", e);
        } finally {
            try {
                filterChain.doFilter(request, response);
            } finally {
                // 清理用户上下文
                UserContext.clear();
            }
        }
    }

    /**
     * 从请求头中提取JWT令牌
     *
     * @param request HTTP请求
     * @return JWT令牌，如果没有则返回null
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 跳过认证相关的公开路径
        return path.startsWith("/api/v1/auth/")
                || path.startsWith("/v1/auth/")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/webjars")
                || path.equals("/doc.html")
                || path.equals("/favicon.ico")
                || path.startsWith("/actuator")
                || path.equals("/health");
    }
}