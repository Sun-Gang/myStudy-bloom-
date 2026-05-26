package com.bloomfinance.notification.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建通知DTO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "创建通知请求")
public class CreateNotificationDTO {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 通知类型: 1-账单到期 2-逾期预警 3-大额收支 4-目标偏离 5-系统通知
     */
    @NotNull(message = "通知类型不能为空")
    @Schema(description = "通知类型", example = "1")
    private Integer type;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 128, message = "标题不能超过128个字符")
    @Schema(description = "标题", example = "账单到期提醒")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容", example = "您的信用卡账单还有5天到期，请及时还款")
    private String content;
}