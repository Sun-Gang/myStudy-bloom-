package com.bloomfinance.user.service.impl;

import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.exception.ResourceNotFoundException;
import com.bloomfinance.common.exception.ValidationException;
import com.bloomfinance.common.util.SecurityUtils;
import com.bloomfinance.user.domain.dto.LoginDTO;
import com.bloomfinance.user.domain.dto.RegisterDTO;
import com.bloomfinance.user.domain.dto.UpdateProfileDTO;
import com.bloomfinance.user.domain.entity.UserEntity;
import com.bloomfinance.user.domain.vo.UserProfileVO;
import com.bloomfinance.user.repository.UserRepository;
import com.bloomfinance.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author bloom-finance
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    /**
     * 用户状态常量：正常
     */
    private static final int STATUS_NORMAL = 1;

    /**
     * 用户状态常量：冻结
     */
    private static final int STATUS_FROZEN = 0;

    @Override
    public Long register(RegisterDTO dto) {
        log.info("用户注册请求, username={}, email={}", dto.getUsername(), dto.getEmail());

        // 校验密码确认
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new ValidationException("两次输入的密码不一致");
        }

        // 校验用户名是否已存在
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ValidationException("用户名已存在");
        }

        // 校验邮箱是否已存在
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ValidationException("邮箱已被注册");
        }

        // 构建用户实体
        UserEntity userEntity = UserEntity.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getUsername()) // 默认昵称为用户名
                .email(dto.getEmail())
                .status(STATUS_NORMAL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(0)
                .build();

        // 保存用户
        userRepository.insert(userEntity);
        Long userId = userEntity.getId();

        log.info("用户注册成功, userId={}", userId);
        return userId;
    }

    @Override
    public String login(LoginDTO dto) {
        log.info("用户登录请求, username={}", dto.getUsername());

        // 查询用户（支持用户名、邮箱、手机号登录）
        UserEntity userEntity = findUserByLoginIdentifier(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("用户名或密码错误"));

        // 检查用户状态
        if (userEntity.getStatus() == STATUS_FROZEN) {
            throw new BusinessException(403, "账号已被冻结，请联系客服");
        }

        // 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), userEntity.getPassword())) {
            throw new ResourceNotFoundException("用户名或密码错误");
        }

        // 生成AccessToken和RefreshToken
        String accessToken = securityUtils.generateAccessToken(userEntity.getId());
        String refreshToken = securityUtils.generateRefreshToken(userEntity.getId());

        // 更新最后登录时间
        userEntity.setLastLoginAt(LocalDateTime.now());
        userRepository.updateById(userEntity);

        log.info("用户登录成功, userId={}", userEntity.getId());

        // 返回组合令牌（实际项目中可分开存储）
        return accessToken + "|" + refreshToken;
    }

    @Override
    public String refreshToken(String refreshToken) {
        log.info("刷新令牌请求");

        // 校验刷新令牌
        if (!securityUtils.isRefreshToken(refreshToken)) {
            throw new BusinessException(401, "无效的刷新令牌");
        }

        Long userId = securityUtils.extractUserId(refreshToken);

        // 生成新的AccessToken
        String newAccessToken = securityUtils.generateAccessToken(userId);

        log.info("令牌刷新成功, userId={}", userId);
        return newAccessToken;
    }

    @Override
    public void logout(Long userId) {
        log.info("用户登出, userId={}", userId);
        // 无需操作Redis，直接返回
        log.info("用户登出成功, userId={}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileVO getUserProfile(Long userId) {
        UserEntity userEntity = getUserEntityById(userId);
        return convertToVO(userEntity);
    }

    @Override
    public UserProfileVO updateProfile(Long userId, UpdateProfileDTO dto) {
        log.info("更新用户资料, userId={}", userId);

        UserEntity userEntity = getUserEntityById(userId);

        // 更新字段
        if (dto.getNickname() != null) {
            userEntity.setNickname(dto.getNickname());
        }
        if (dto.getPhone() != null) {
            userEntity.setPhone(dto.getPhone());
        }
        if (dto.getEmail() != null) {
            userEntity.setEmail(dto.getEmail());
        }
        if (dto.getAvatar() != null) {
            userEntity.setAvatar(dto.getAvatar());
        }
        userEntity.setUpdatedAt(LocalDateTime.now());

        userRepository.updateById(userEntity);

        log.info("用户资料更新成功, userId={}", userId);
        return convertToVO(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileVO getUserById(Long userId) {
        UserEntity userEntity = getUserEntityById(userId);
        return convertToVO(userEntity);
    }

    /**
     * 根据登录标识符（用户名/邮箱/手机号）查找用户
     */
    private java.util.Optional<UserEntity> findUserByLoginIdentifier(String identifier) {
        // 先尝试用户名
        java.util.Optional<UserEntity> user = userRepository.findByUsername(identifier);
        if (user.isPresent()) {
            return user;
        }

        // 再尝试邮箱
        user = userRepository.findByEmail(identifier);
        if (user.isPresent()) {
            return user;
        }

        // 最后尝试手机号
        return userRepository.findByPhone(identifier);
    }

    /**
     * 根据ID获取用户实体
     */
    private UserEntity getUserEntityById(Long userId) {
        return userRepository.selectById(userId);
    }

    /**
     * 将实体转换为VO
     */
    private UserProfileVO convertToVO(UserEntity entity) {
        return UserProfileVO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .nickname(entity.getNickname())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .avatar(entity.getAvatar())
                .status(entity.getStatus())
                .build();
    }
}