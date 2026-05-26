package com.bloomfinance.ai.service.impl;

import com.bloomfinance.ai.service.AiService;
import com.bloomfinance.asset.service.AssetService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI服务实现 - DeepSeek集成
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final AssetService assetService;

    @Value("${deepseek.api.key:sk-ec00edc7f0764eec982d6f39eb9190ed}")
    private String apiKey;

    @Value("${deepseek.api.url:https://api.deepseek.com/chat/completions}")
    private String apiUrl;

    private static final String MODEL = "deepseek-chat";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // 系统提示词
    private static final String SYSTEM_PROMPT = "你是一位专业的财务助手，名为\"Bloom AI\"。你的职责是帮助用户分析财务状况、提供理财建议和解答财务相关问题。\n" +
            "\n" +
            "请注意：\n" +
            "1. 只回答与财务相关的问题\n" +
            "2. 回答要专业、简洁、易懂\n" +
            "3. 如果用户问的问题与财务无关，请礼貌地引导用户回到财务话题\n" +
            "4. 可以根据用户的资产配置、收支情况等给出个性化建议\n" +
            "5. 回答使用中文\n" +
            "\n" +
            "以下是一些常见问题的回答示例：\n" +
            "- 收支分析：可以根据收入支出数据给出优化建议\n" +
            "- 储蓄建议：建议合理的储蓄率和储蓄方法\n" +
            "- 投资建议：可以根据用户的风险偏好给出配置建议\n" +
            "- 债务管理：可以帮助制定还款计划";

    @Override
    public String chat(Long userId, String message) {
        log.info("AI chat request, userId={}, message={}", userId, message);

        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", MODEL);

            // 构建消息列表
            List<Map<String, String>> messages = new ArrayList<>();

            // 添加系统提示
            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", SYSTEM_PROMPT);
            messages.add(systemMsg);

            // 获取用户财务上下文（可选）
            String financialContext = getUserFinancialContext(userId);
            if (financialContext != null && !financialContext.isEmpty()) {
                Map<String, String> contextMsg = new HashMap<>();
                contextMsg.put("role", "system");
                contextMsg.put("content", "用户当前财务状况：" + financialContext);
                messages.add(contextMsg);
            }

            // 添加用户消息
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", message);
            messages.add(userMsg);

            requestBody.put("messages", messages);
            requestBody.put("max_tokens", 1000);
            requestBody.put("temperature", 0.7);

            // 发送请求
            String requestJson = OBJECT_MAPPER.writeValueAsString(requestBody);
            log.debug("DeepSeek request: {}", requestJson);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("DeepSeek response: {}", response.body());

            if (response.statusCode() == 200) {
                JsonNode responseNode = OBJECT_MAPPER.readTree(response.body());
                JsonNode choices = responseNode.get("choices");
                if (choices != null && choices.isArray() && choices.size() > 0) {
                    JsonNode firstChoice = choices.get(0);
                    JsonNode messageNode = firstChoice.get("message");
                    if (messageNode != null) {
                        String reply = messageNode.get("content").asText();
                        return reply.trim();
                    }
                }
                return "抱歉，AI暂时无法回复，请稍后重试。";
            } else {
                log.error("DeepSeek API error, status={}, body={}", response.statusCode(), response.body());
                return "抱歉，AI服务暂时不可用，请稍后重试。";
            }
        } catch (Exception e) {
            log.error("AI chat error", e);
            return "抱歉，AI服务暂时不可用，请稍后重试。";
        }
    }

    /**
     * 获取用户财务上下文
     */
    private String getUserFinancialContext(Long userId) {
        try {
            StringBuilder context = new StringBuilder();

            // 获取资产汇总
            var assetSummary = assetService.getAssetSummary(userId);
            if (assetSummary != null) {
                context.append(String.format("总资产：%.2f元，", assetSummary.getTotalAsset()));
                context.append(String.format("总负债：%.2f元，", assetSummary.getTotalLiability()));
                context.append(String.format("净资产：%.2f元。", assetSummary.getNetAsset()));
            }

            // 获取资产配置
            var allocation = assetService.getAssetAllocation(userId);
            if (allocation != null && allocation.getAllocationList() != null && !allocation.getAllocationList().isEmpty()) {
                context.append("资产配置：");
                for (var item : allocation.getAllocationList()) {
                    context.append(String.format("%s %.2f%%，",
                            item.getTypeName(), item.getPercentage()));
                }
            }

            return context.toString();
        } catch (Exception e) {
            log.warn("Failed to get user financial context", e);
            return null;
        }
    }
}
