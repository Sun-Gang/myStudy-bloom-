package com.bloomfinance.notification.service;

import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.notification.domain.dto.CreateNotificationDTO;
import com.bloomfinance.notification.domain.dto.NotificationPrefsDTO;
import com.bloomfinance.notification.domain.dto.NotificationQuery;
import com.bloomfinance.notification.domain.vo.NotificationPrefsVO;
import com.bloomfinance.notification.domain.vo.NotificationVO;

import java.util.List;

/**
 * 通知服务接口
 *
 * @author BloomFinance
 */
public interface NotificationService {

    /**
     * 发送通知
     *
     * @param dto 创建通知DTO
     * @return 通知ID
     */
    Long sendNotification(CreateNotificationDTO dto);

    /**
     * 获取通知列表
     *
     * @param userId 用户ID
     * @param query  查询条件
     * @return 分页结果
     */
    PageResult<NotificationVO> getNotifications(Long userId, NotificationQuery query);

    /**
     * 获取未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 标记通知已读
     *
     * @param userId         用户ID
     * @param notificationId 通知ID
     */
    void markAsRead(Long userId, Long notificationId);

    /**
     * 标记所有通知已读
     *
     * @param userId 用户ID
     */
    void markAllAsRead(Long userId);

    /**
     * 获取通知偏好设置
     *
     * @param userId 用户ID
     * @return 通知偏好设置VO
     */
    NotificationPrefsVO getPreferences(Long userId);

    /**
     * 更新通知偏好设置
     *
     * @param userId 用户ID
     * @param dto    通知偏好设置DTO
     */
    void updatePreferences(Long userId, NotificationPrefsDTO dto);
}