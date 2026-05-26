package com.bloomfinance.common.domain.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询基类
 *
 * @author BloomFinance
 */
@Data
@Schema(description = "分页查询基类")
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码，从1开始
     */
    @Schema(description = "当前页码，从1开始", example = "1")
    private Integer current = 1;

    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "20")
    private Integer size = 20;

    /**
     * 获取offset
     *
     * @return offset
     */
    public Integer getOffset() {
        if (current == null || current < 1) {
            current = 1;
        }
        if (size == null || size < 1) {
            size = 20;
        }
        return (current - 1) * size;
    }
}