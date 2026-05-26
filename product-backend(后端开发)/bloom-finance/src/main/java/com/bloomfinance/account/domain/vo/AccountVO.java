package com.bloomfinance.account.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户视图对象
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "账户响应")
public class AccountVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 账户类型: 1-储蓄卡 2-信用卡 3-证券 4-基金 5-社保 6-公积金 7-负债 8-其他
     */
    @Schema(description = "账户类型", example = "1")
    private Integer accountType;

    /**
     * 账户类型名称
     */
    @Schema(description = "账户类型名称", example = "储蓄卡")
    private String accountTypeName;

    /**
     * 银行名称
     */
    @Schema(description = "银行名称", example = "中国工商银行")
    private String bankName;

    /**
     * 卡号掩码(显示前四位后四位，中间用*替代)
     */
    @Schema(description = "卡号掩码", example = "6222****0123")
    private String cardNoMasked;

    /**
     * 余额
     */
    @Schema(description = "余额", example = "10000.00")
    private BigDecimal balance;

    /**
     * 状态: 0-冻结 1-正常
     */
    @Schema(description = "状态: 0-冻结 1-正常", example = "1")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2024-01-01 10:00:00")
    private LocalDateTime createdAt;
}