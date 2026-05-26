package com.bloomfinance.common.constant;

/**
 * Redis键常量定义
 *
 * <p>格式：{module}:{entity}:{identifier}:{field}</p>
 *
 * @author bloom-finance
 */
public final class RedisKeys {

    private RedisKeys() {
    }

    // ==================== 用户相关 ====================

    /**
     * 用户资料缓存
     * 格式：bloom:user:{userId}:profile
     * TTL：30分钟
     */
    public static final String USER_PROFILE = "bloom:user:%s:profile";

    /**
     * 用户认证信息
     * 格式：bloom:user:%s:auth
     * TTL：与Token有效期一致
     */
    public static final String USER_AUTH = "bloom:user:%s:auth";

    // ==================== 账户相关 ====================

    /**
     * 账户余额缓存
     * 格式：bloom:account:{accountId}:balance
     * TTL：5分钟
     */
    public static final String ACCOUNT_BALANCE = "bloom:account:%s:balance";

    /**
     * 账户详情缓存
     * 格式：bloom:account:{accountId}:info
     * TTL：10分钟
     */
    public static final String ACCOUNT_INFO = "bloom:account:%s:info";

    // ==================== 认证相关 ====================

    /**
     * 访问令牌
     * 格式：bloom:auth:token:{token}
     * TTL：2小时
     */
    public static final String AUTH_TOKEN = "bloom:auth:token:%s";

    /**
     * 刷新令牌
     * 格式：bloom:auth:refresh:{userId}
     * TTL：7天
     */
    public static final String AUTH_REFRESH = "bloom:auth:refresh:%s";

    /**
     * 验证码
     * 格式：bloom:auth:code:{phone}
     * TTL：5分钟
     */
    public static final String AUTH_CODE = "bloom:auth:code:%s";

    // ==================== 目标相关 ====================

    /**
     * 目标进度缓存
     * 格式：bloom:goal:{goalId}:progress
     * TTL：10分钟
     */
    public static final String GOAL_PROGRESS = "bloom:goal:%s:progress";

    /**
     * 目标详情缓存
     * 格式：bloom:goal:{goalId}:info
     * TTL：10分钟
     */
    public static final String GOAL_INFO = "bloom:goal:%s:info";

    // ==================== 资产统计相关 ====================

    /**
     * 资产汇总缓存
     * 格式：bloom:asset:summary:{userId}
     * TTL：5分钟
     */
    public static final String ASSET_SUMMARY = "bloom:asset:summary:%s";

    /**
     * 资产明细缓存
     * 格式：bloom:asset:detail:{userId}
     * TTL：5分钟
     */
    public static final String ASSET_DETAIL = "bloom:asset:detail:%s";

    // ==================== 账单相关 ====================

    /**
     * 账单列表缓存
     * 格式：bloom:bills:{userId}:{accountId}:list
     * TTL：5分钟
     */
    public static final String BILLS_LIST = "bloom:bills:%s:%s:list";

    /**
     * 账单统计缓存
     * 格式：bloom:bills:{userId}:stats
     * TTL：10分钟
     */
    public static final String BILLS_STATS = "bloom:bills:%s:stats";

    // ==================== 分布式锁 ====================

    /**
     * 账户余额锁
     * 格式：bloom:lock:account:{accountId}
     * TTL：10秒
     */
    public static final String LOCK_ACCOUNT_BALANCE = "bloom:lock:account:%s";

    /**
     * 用户操作锁
     * 格式：bloom:lock:user:{userId}
     * TTL：30秒
     */
    public static final String LOCK_USER = "bloom:lock:user:%s";

    // ==================== 工具方法 ====================

    /**
     * 格式化键
     *
     * @param pattern 格式模式
     * @param args    参数
     * @return 格式化后的键
     */
    public static String format(String pattern, Object... args) {
        return String.format(pattern, args);
    }

    /**
     * 构建用户资料键
     *
     * @param userId 用户ID
     * @return Redis键
     */
    public static String userProfile(Long userId) {
        return format(USER_PROFILE, userId);
    }

    /**
     * 构建账户余额键
     *
     * @param accountId 账户ID
     * @return Redis键
     */
    public static String accountBalance(Long accountId) {
        return format(ACCOUNT_BALANCE, accountId);
    }

    /**
     * 构建认证令牌键
     *
     * @param token 令牌
     * @return Redis键
     */
    public static String authToken(String token) {
        return format(AUTH_TOKEN, token);
    }

    /**
     * 构建目标进度键
     *
     * @param goalId 目标ID
     * @return Redis键
     */
    public static String goalProgress(Long goalId) {
        return format(GOAL_PROGRESS, goalId);
    }
}