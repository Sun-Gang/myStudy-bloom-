package com.bloomfinance.reminder.domain.dto;

import com.bloomfinance.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 账单查询DTO
 *
 * @author BloomFinance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "账单查询请求")
public class BillQuery extends PageQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 账户ID
     */
    @Schema(description = "账户ID", example = "1")
    private Long accountId;

    /**
     * 状态: 1-待还 2-已还 3-逾期
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 到期日期开始
     */
    @Schema(description = "到期日期开始", example = "2026-05-01")
    private LocalDate startDueDate;

    /**
     * 到期日期结束
     */
    @Schema(description = "到期日期结束", example = "2026-05-31")
    private LocalDate endDueDate;
}