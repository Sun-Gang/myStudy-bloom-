package com.bloomfinance.ai.domain.dto;

import lombok.Data;

/**
 * AI聊天请求DTO
 *
 * @author BloomFinance
 */
@Data
public class ChatRequest {

    /**
     * 用户消息
     */
    private String message;
}
