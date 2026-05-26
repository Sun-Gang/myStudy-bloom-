package com.bloomfinance.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新用户资料DTO
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新用户资料请求")
public class UpdateProfileDTO {

    @Size(max = 32, message = "昵称长度不能超过32个字符")
    @Schema(description = "昵称", example = "Bloom User Updated")
    private String nickname;

    @Schema(description = "手机号", example = "13800138001")
    private String phone;

    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    @Schema(description = "头像URL")
    private String avatar;
}