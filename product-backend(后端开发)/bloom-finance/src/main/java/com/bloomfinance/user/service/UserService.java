package com.bloomfinance.user.service;

import com.bloomfinance.user.domain.dto.LoginDTO;
import com.bloomfinance.user.domain.dto.RegisterDTO;
import com.bloomfinance.user.domain.dto.UpdateProfileDTO;
import com.bloomfinance.user.domain.vo.UserProfileVO;

/**
 * 用户服务接口
 *
 * @author bloom-finance
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param dto 注册信息
     * @return 用户ID
     */
    Long register(RegisterDTO dto);

    /**
     * 用户登录
     *
     * @param dto 登录信息
     * @return 访问令牌
     */
    String login(LoginDTO dto);

    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    String refreshToken(String refreshToken);

    /**
     * 用户登出
     *
     * @param userId 用户ID
     */
    void logout(Long userId);

    /**
     * 获取当前用户资料
     *
     * @param userId 用户ID
     * @return 用户资料
     */
    UserProfileVO getUserProfile(Long userId);

    /**
     * 更新用户资料
     *
     * @param userId 用户ID
     * @param dto    更新信息
     * @return 更新后的用户资料
     */
    UserProfileVO updateProfile(Long userId, UpdateProfileDTO dto);

    /**
     * 根据ID获取用户
     *
     * @param userId 用户ID
     * @return 用户资料
     */
    UserProfileVO getUserById(Long userId);
}