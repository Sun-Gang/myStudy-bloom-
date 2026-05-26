package com.bloomfinance.notification.controller;

import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.common.result.Result;
import com.bloomfinance.notification.domain.dto.NotificationPrefsDTO;
import com.bloomfinance.notification.domain.dto.NotificationQuery;
import com.bloomfinance.notification.domain.vo.NotificationPrefsVO;
import com.bloomfinance.notification.domain.vo.NotificationVO;
import com.bloomfinance.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 通知控制器
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "通知管理", description = "通知相关接口")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取通知列表
     */
    @GetMapping
    @Operation(summary = "获取通知列表", description = "分页查询通知列表")
    public Result<PageResult<NotificationVO>> getNotifications(NotificationQuery query) {
        Long userId = getCurrentUserId();
        PageResult<NotificationVO> result = notificationService.getNotifications(userId, query);
        return Result.success(result);
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/unread-count")
    @Operation(summary = "获取未读数量", description = "获取当前用户未读通知数量")
    public Result<Map<String, Long>> getUnreadCount() {
        Long userId = getCurrentUserId();
        Long count = notificationService.getUnreadCount(userId);
        return Result.success(Map.of("unreadCount", count));
    }

    /**
     * 标记通知已读
     */
    @PutMapping("/{id}/read")
    @Operation(summary = "标记已读", description = "标记指定通知为已读")
    public Result<Void> markAsRead(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        notificationService.markAsRead(userId, id);
        return Result.success();
    }

    /**
     * 标记所有通知已读
     */
    @PutMapping("/read-all")
    @Operation(summary = "全部已读", description = "标记所有通知为已读")
    public Result<Void> markAllAsRead() {
        Long userId = getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return Result.success();
    }

    /**
     * 获取通知偏好设置
     */
    @GetMapping("/prefs")
    @Operation(summary = "获取偏好设置", description = "获取当前用户的通知偏好设置")
    public Result<NotificationPrefsVO> getPreferences() {
        Long userId = getCurrentUserId();
        NotificationPrefsVO vo = notificationService.getPreferences(userId);
        return Result.success(vo);
    }

    /**
     * 更新通知偏好设置
     */
    @PutMapping("/prefs")
    @Operation(summary = "更新偏好设置", description = "更新当前用户的通知偏好设置")
    public Result<Void> updatePreferences(@Valid @RequestBody NotificationPrefsDTO dto) {
        Long userId = getCurrentUserId();
        notificationService.updatePreferences(userId, dto);
        return Result.success();
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        return userId;
    }
}