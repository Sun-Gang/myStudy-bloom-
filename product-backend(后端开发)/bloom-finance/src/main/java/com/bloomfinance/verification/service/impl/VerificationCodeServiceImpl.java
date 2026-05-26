package com.bloomfinance.verification.service.impl;

import com.bloomfinance.common.exception.ValidationException;
import com.bloomfinance.common.util.SecurityUtils;
import com.bloomfinance.user.repository.UserRepository;
import com.bloomfinance.verification.domain.dto.SendCodeDTO;
import com.bloomfinance.verification.domain.dto.VerifyCodeDTO;
import com.bloomfinance.verification.domain.entity.VerificationCodeEntity;
import com.bloomfinance.verification.domain.enums.VerificationStatusEnum;
import com.bloomfinance.verification.domain.enums.VerificationTypeEnum;
import com.bloomfinance.verification.repository.VerificationCodeRepository;
import com.bloomfinance.verification.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

/**
 * 验证码服务实现类
 *
 * @author bloom-finance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 验证码有效期：5分钟
     */
    private static final int CODE_VALID_MINUTES = 5;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendCode(SendCodeDTO dto) {
        String email = dto.getEmail();
        Integer type = dto.getType();
        VerificationTypeEnum typeEnum = VerificationTypeEnum.fromCode(type);

        log.info("发送验证码, email={}, type={}", email, typeEnum.getDescription());

        // 1. 业务校验
        validateSendCode(email, typeEnum);

        // 2. 生成6位数字验证码
        String code = generateCode();

        // 3. 保存验证码（先软删除过期或未使用的）
        verificationCodeRepository.deleteExpiredByEmailAndType(email, type, LocalDateTime.now());
        verificationCodeRepository.markAsUsedByEmailAndType(email, type, LocalDateTime.now());

        VerificationCodeEntity entity = VerificationCodeEntity.builder()
                .email(email)
                .code(code)
                .type(type)
                .status(VerificationStatusEnum.UNUSED.getCode())
                .expiresAt(LocalDateTime.now().plusMinutes(CODE_VALID_MINUTES))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(0)
                .build();

        verificationCodeRepository.insert(entity);

        // 4. TODO: 发送邮件（实际项目中应接入邮件服务）
        log.info("验证码已生成, email={}, code={}, expiresAt={}", email, code, entity.getExpiresAt());

        // 模拟发送邮件 - 实际项目中替换为真实邮件服务
        sendEmail(email, code, typeEnum);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyAndResetPassword(VerifyCodeDTO dto) {
        String email = dto.getEmail();
        String code = dto.getCode();
        String newPassword = dto.getNewPassword();

        log.info("验证验证码并重置密码, email={}", email);

        // 1. 查询最新未使用的验证码
        VerificationCodeEntity verificationCode =
                verificationCodeRepository.findLatestByEmailAndType(email, VerificationTypeEnum.FORGOT_PASSWORD.getCode(), LocalDateTime.now())
                        .orElseThrow(() -> new ValidationException("验证码不存在，请先获取验证码"));

        // 2. 校验验证码状态
        if (verificationCode.getStatus() == VerificationStatusEnum.USED.getCode()) {
            throw new ValidationException("验证码已被使用，请重新获取");
        }
        if (verificationCode.getStatus() == VerificationStatusEnum.EXPIRED.getCode()) {
            throw new ValidationException("验证码已过期，请重新获取");
        }
        if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ValidationException("验证码已过期，请重新获取");
        }

        // 3. 校验验证码是否匹配
        if (!verificationCode.getCode().equals(code)) {
            throw new ValidationException("验证码不正确");
        }

        // 4. 查询用户
        userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("该邮箱尚未注册"));

        // 5. 更新用户密码
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.updateById(user);
        });

        // 6. 标记验证码为已使用
        verificationCode.setStatus(VerificationStatusEnum.USED.getCode());
        verificationCode.setVerifiedAt(LocalDateTime.now());
        verificationCode.setUpdatedAt(LocalDateTime.now());
        verificationCodeRepository.updateById(verificationCode);

        log.info("密码重置成功, email={}", email);
    }

    /**
     * 校验发送验证码的合法性
     */
    private void validateSendCode(String email, VerificationTypeEnum typeEnum) {
        switch (typeEnum) {
            case REGISTER:
                // 注册时检查邮箱是否已存在
                if (userRepository.existsByEmail(email)) {
                    throw new ValidationException("该邮箱已被注册");
                }
                break;
            case FORGOT_PASSWORD:
                // 找回密码时检查邮箱是否存在
                if (!userRepository.existsByEmail(email)) {
                    throw new ValidationException("该邮箱尚未注册");
                }
                break;
            case LOGIN:
                // 登录时不做强制校验
                break;
            case BIND_EMAIL:
                // 绑定邮箱时检查邮箱是否已被他人使用
                if (userRepository.existsByEmail(email)) {
                    throw new ValidationException("该邮箱已被其他账号绑定");
                }
                break;
        }
    }

    /**
     * 生成6位数字验证码
     */
    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000-999999
        return String.valueOf(code);
    }

    /**
     * 发送邮件
     */
    private void sendEmail(String email, String code, VerificationTypeEnum typeEnum) {
        String subject;
        switch (typeEnum) {
            case REGISTER:
                subject = "【Bloom Finance】注册验证码";
                break;
            case LOGIN:
                subject = "【Bloom Finance】登录验证码";
                break;
            case FORGOT_PASSWORD:
                subject = "【Bloom Finance】找回密码验证码";
                break;
            case BIND_EMAIL:
                subject = "【Bloom Finance】绑定邮箱验证码";
                break;
            default:
                subject = "【Bloom Finance】验证码";
        }

        String content = String.format(
                "您好，\n\n您的验证码是：%s\n有效期为%d分钟。\n\n如非本人操作，请忽略此邮件。\n\nBloom Finance 团队",
                code, CODE_VALID_MINUTES
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("sungang521210@163.com");
            message.setTo(email);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("邮件发送成功 - 收件人: {}, 主题: {}", email, subject);
        } catch (Exception e) {
            log.error("邮件发送失败 - 收件人: {}, 错误: {}", email, e.getMessage());
            // 邮件发送失败不影响业务流程，验证码已保存到数据库
        }
    }
}