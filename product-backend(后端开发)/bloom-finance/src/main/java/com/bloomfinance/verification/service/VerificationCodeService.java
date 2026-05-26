package com.bloomfinance.verification.service;

import com.bloomfinance.verification.domain.dto.SendCodeDTO;
import com.bloomfinance.verification.domain.dto.VerifyCodeDTO;

/**
 * 验证码服务接口
 *
 * @author bloom-finance
 */
public interface VerificationCodeService {

    /**
     * 发送验证码
     *
     * @param dto 发送验证码请求
     */
    void sendCode(SendCodeDTO dto);

    /**
     * 验证验证码并重置密码（忘记密码流程）
     *
     * @param dto 验证验证码请求
     */
    void verifyAndResetPassword(VerifyCodeDTO dto);
}