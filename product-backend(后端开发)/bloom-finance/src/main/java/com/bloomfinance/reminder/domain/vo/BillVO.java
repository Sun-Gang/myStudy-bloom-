package com.bloomfinance.reminder.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账单VO
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "账单响应")
public class BillVO {

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    /**
     * 账户ID
     */
    @Schema(description = "账户ID", example = "1")
    private Long accountId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称", example = "我的信用卡")
    private String accountName;

    /**
     * 账单类型
     */
    @Schema(description = "账单类型", example = "1")
    private Integer billType;

    /**
     * 账单类型名称
     */
    @Schema(description = "账单类型名称", example = "信用卡")
    private String billTypeName;

    /**
     * 账单日期
     */
    @Schema(description = "账单日期", example = "2026-05-01")
    private LocalDate billDate;

    /**
     * 到期日期
     */
    @Schema(description = "到期日期", example = "2026-05-15")
    private LocalDate dueDate;

    /**
     * 金额
     */
    @Schema(description = "金额", example = "5000.00")
    private BigDecimal amount;

    /**
     * 状态: 1-待还 2-已还 3-逾期
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 状态名称
     */
    @Schema(description = "状态名称", example = "待还")
    private String statusName;

    /**
     * 距离到期天数
     */
    @Schema(description = "距离到期天数", example = "5")
    private Integer daysUntilDue;

    /**
     * 是否逾期
     */
    @Schema(description = "是否逾期", example = "false")
    private Boolean isOverdue;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}