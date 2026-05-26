package com.bloomfinance.verification.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 邮箱验证码实体
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_verification_code")
public class VerificationCodeEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 验证码
     */
    private String code;

    /**
     * 类型：1-注册 2-登录 3-找回密码 4-绑定邮箱
     */
    private Integer type;

    /**
     * 状态：0-未使用 1-已使用 2-已过期
     */
    private Integer status;

    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;

    /**
     * 验证时间
     */
    private LocalDateTime verifiedAt;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 逻辑删除标识：0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;
}