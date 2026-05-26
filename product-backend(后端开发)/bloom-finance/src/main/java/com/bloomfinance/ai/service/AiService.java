package com.bloomfinance.ai.service;

/**
 * AI服务接口
 *
 * @author BloomFinance
 */
public interface AiService {

    /**
     * 发送聊天消息并获取AI回复
     *
     * @param userId  用户ID
     * @param message 用户消息
     * @return AI回复
     */
    String chat(Long userId, String message);
}
