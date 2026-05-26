package com.bloomfinance.user.controller;

import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.result.Result;
import com.bloomfinance.user.domain.dto.UpdateProfileDTO;
import com.bloomfinance.user.domain.vo.UserProfileVO;
import com.bloomfinance.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author bloom-finance
 */
@Slf4j
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户资料查询、更新等接口")
public class UserController {

    private final UserService userService;

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户资料
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详情")
    public Result<UserProfileVO> getById(@PathVariable Long id) {
        log.info("获取用户详情, userId={}", id);
        UserProfileVO userProfile = userService.getUserById(id);
        return Result.success(userProfile);
    }

    /**
     * 获取当前用户资料
     *
     * @return 当前用户资料
     */
    @GetMapping("/profile")
    @Operation(summary = "获取当前用户资料", description = "获取当前登录用户的资料")
    public Result<UserProfileVO> getProfile() {
        // TODO: 从上下文获取当前用户ID
        Long currentUserId = getCurrentUserId();
        log.info("获取当前用户资料, userId={}", currentUserId);
        UserProfileVO userProfile = userService.getUserProfile(currentUserId);
        return Result.success(userProfile);
    }

    /**
     * 更新当前用户资料
     *
     * @param dto 更新信息
     * @return 更新后的用户资料
     */
    @PutMapping("/profile")
    @Operation(summary = "更新当前用户资料", description = "更新当前登录用户的资料")
    public Result<UserProfileVO> updateProfile(@Validated @RequestBody UpdateProfileDTO dto) {
        Long currentUserId = getCurrentUserId();
        log.info("更新用户资料, userId={}", currentUserId);
        UserProfileVO updatedProfile = userService.updateProfile(currentUserId, dto);
        return Result.success(updatedProfile);
    }

    /**
     * 获取当前登录用户ID
     * TODO: 实际实现应从安全上下文/Token中获取
     *
     * @return 当前用户ID
     */
    private Long getCurrentUserId() {
        // TODO: 从SecurityContext或Token解析获取真实用户ID
        // 此处临时返回固定值，实际需实现
        return 1L;
    }
}