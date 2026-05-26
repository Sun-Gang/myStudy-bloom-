package com.bloomfinance.ai.controller;

import com.bloomfinance.ai.domain.dto.ChatRequest;
import com.bloomfinance.ai.domain.vo.ChatResponse;
import com.bloomfinance.ai.service.AiService;
import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * AI聊天Controller
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai")
@RequiredArgsConstructor
@Tag(name = "AI聊天", description = "AI智能助手接口")
public class AiController {

    private final AiService aiService;

    /**
     * 发送聊天消息
     *
     * @param request 聊天请求
     * @return AI回复
     */
    @PostMapping("/chat")
    @Operation(summary = "AI聊天", description = "与AI助手对话")
    public Result<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            throw new BusinessException(400, "消息内容不能为空");
        }

        log.info("AI chat request, userId={}, message={}", userId, request.getMessage());
        String reply = aiService.chat(userId, request.getMessage());

        return Result.success(ChatResponse.builder().reply(reply).build());
    }
}
