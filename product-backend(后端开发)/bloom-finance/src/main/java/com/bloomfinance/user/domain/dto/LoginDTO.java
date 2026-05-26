package com.bloomfinance.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录DTO
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录请求")
public class LoginDTO {

    @NotBlank(message = "用户名/邮箱/手机号不能为空")
    @Schema(description = "用户名、邮箱或手机号", example = "13800138000")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "P@ssw0rd123")
    private String password;
}