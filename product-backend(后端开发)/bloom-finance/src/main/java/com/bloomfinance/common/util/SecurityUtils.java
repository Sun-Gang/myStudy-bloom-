package com.bloomfinance.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 安全工具类
 * <p>
 * 提供JWT令牌的生成、解析等功能
 * </p>
 *
 * @author bloom-finance
 */
@Slf4j
@Component
public class SecurityUtils {

    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    /**
     * AccessToken有效期：2小时
     */
    private static final long ACCESS_TOKEN_EXPIRATION = 2 * 60 * 60 * 1000;

    /**
     * RefreshToken有效期：7天
     */
    private static final long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000;

    /**
     * JWT签名密钥
     */
    private final SecretKey secretKey;

    /**
     * 构造函数
     *
     * @param jwtSecret JWT密钥配置
     */
    public SecurityUtils(@Value("${jwt.secret:bloom-finance-secret-key-change-in-production}") String jwtSecret) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成访问令牌
     *
     * @param userId 用户ID
     * @return JWT令牌
     */
    public String generateAccessToken(Long userId) {
        return generateToken(userId, TOKEN_TYPE_ACCESS, ACCESS_TOKEN_EXPIRATION);
    }

    /**
     * 生成刷新令牌
     *
     * @param userId 用户ID
     * @return JWT令牌
     */
    public String generateRefreshToken(Long userId) {
        return generateToken(userId, TOKEN_TYPE_REFRESH, REFRESH_TOKEN_EXPIRATION);
    }

    /**
     * 生成令牌
     *
     * @param userId   用户ID
     * @param tokenType 令牌类型
     * @param expiration 过期时间（毫秒）
     * @return JWT令牌
     */
    private String generateToken(Long userId, String tokenType, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, userId);
        claims.put(CLAIM_KEY_TYPE, tokenType);

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析令牌
     *
     * @param token JWT令牌
     * @return Claims对象
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("令牌已过期: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.warn("令牌格式错误: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.warn("令牌不支持: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            log.warn("令牌签名验证失败: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.warn("令牌为空或格式错误: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * 从令牌中提取用户ID
     *
     * @param token JWT令牌
     * @return 用户ID
     */
    public Long extractUserId(String token) {
        Claims claims = parseToken(token);
        Object userId = claims.get(CLAIM_KEY_USER_ID);
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        } else if (userId instanceof Long) {
            return (Long) userId;
        }
        return null;
    }

    /**
     * 验证令牌类型是否为访问令牌
     *
     * @param token JWT令牌
     * @return true if access token
     */
    public boolean isAccessToken(String token) {
        try {
            Claims claims = parseToken(token);
            return TOKEN_TYPE_ACCESS.equals(claims.get(CLAIM_KEY_TYPE));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证令牌类型是否为刷新令牌
     *
     * @param token JWT令牌
     * @return true if refresh token
     */
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = parseToken(token);
            return TOKEN_TYPE_REFRESH.equals(claims.get(CLAIM_KEY_TYPE));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证令牌是否有效
     *
     * @param token JWT令牌
     * @return true if valid
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}