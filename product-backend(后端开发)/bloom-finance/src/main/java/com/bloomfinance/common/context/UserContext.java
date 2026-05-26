package com.bloomfinance.common.context;

/**
 * 用户上下文Holder
 * <p>
 * 用于在当前线程中存储和获取用户ID
 * </p>
 *
 * @author bloom-finance
 */
public final class UserContext {

    private UserContext() {
    }

    /**
     * 用户ID ThreadLocal
     */
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，如果未设置则返回null
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 清除当前用户ID
     * <p>
     * 在请求处理完毕后务必调用此方法清理ThreadLocal
     * </p>
     */
    public static void clear() {
        USER_ID.remove();
    }

    /**
     * 判断是否已设置用户ID
     *
     * @return true if userId is set
     */
    public static boolean hasUserId() {
        return USER_ID.get() != null;
    }
}