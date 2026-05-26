package com.bloomfinance.dashboard.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 账单提醒VO
 *
 * @author bloom-finance
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillReminderVO {

    /**
     * 账单ID
     */
    private Long billId;

    /**
     * 账单名称
     */
    private String billName;

    /**
     * 到期天数（负数表示已逾期）
     */
    private Integer daysUntilDue;

    /**
     * 账单金额
     */
    private BigDecimal amount;

    /**
     * 图标
     */
    private String icon;
}