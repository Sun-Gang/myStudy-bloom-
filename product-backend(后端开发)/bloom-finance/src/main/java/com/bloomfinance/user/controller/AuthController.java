package com.bloomfinance.user.controller;

import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.result.Result;
import com.bloomfinance.common.util.SecurityUtils;
import com.bloomfinance.user.domain.dto.LoginDTO;
import com.bloomfinance.user.domain.dto.RegisterDTO;
import com.bloomfinance.user.domain.dto.UpdateProfileDTO;
import com.bloomfinance.user.domain.vo.UserProfileVO;
import com.bloomfinance.user.service.UserService;
import com.bloomfinance.verification.domain.dto.SendCodeDTO;
import com.bloomfinance.verification.domain.dto.VerifyCodeDTO;
import com.bloomfinance.verification.service.VerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author bloom-finance
 */
@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户注册、登录、登出、找回密码等认证相关接口")
public class AuthController {

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;
    private final SecurityUtils securityUtils;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "注册新用户账号（需先获取邮箱验证码）")
    public Result<Long> register(@Validated @RequestBody RegisterDTO dto) {
        log.info("收到注册请求, username={}, email={}", dto.getUsername(), dto.getEmail());
        Long userId = userService.register(dto);
        return Result.created(userId);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "使用用户名/邮箱/手机号和密码登录")
    public Result<String> login(@Validated @RequestBody LoginDTO dto) {
        log.info("收到登录请求, username={}", dto.getUsername());
        String accessToken = userService.login(dto);
        return Result.success(accessToken);
    }

    /**
     * 发送验证码
     */
    @PostMapping("/send-code")
    @Operation(summary = "发送验证码", description = "向指定邮箱发送验证码（注册/登录/找回密码）")
    public Result<Void> sendCode(@Validated @RequestBody SendCodeDTO dto) {
        log.info("收到发送验证码请求, email={}, type={}", dto.getEmail(), dto.getType());
        verificationCodeService.sendCode(dto);
        return Result.success();
    }

    /**
     * 验证验证码并重置密码（忘记密码）
     */
    @PostMapping("/reset-password")
    @Operation(summary = "重置密码", description = "验证邮箱验证码后重置密码")
    public Result<Void> resetPassword(@Validated @RequestBody VerifyCodeDTO dto) {
        log.info("收到重置密码请求, email={}", dto.getEmail());
        verificationCodeService.verifyAndResetPassword(dto);
        return Result.success();
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌", description = "使用刷新令牌获取新的访问令牌")
    public Result<String> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        log.info("收到刷新令牌请求");
        String token = refreshToken.replace("Bearer ", "");
        String newAccessToken = userService.refreshToken(token);
        return Result.success(newAccessToken);
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "清除用户登录状态")
    public Result<Void> logout() {
        Long userId = UserContext.getUserId();
        log.info("用户登出, userId={}", userId);
        userService.logout(userId);
        return Result.success();
    }
}