package com.bloomfinance.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bloomfinance.common.exception.ResourceNotFoundException;
import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.notification.domain.dto.CreateNotificationDTO;
import com.bloomfinance.notification.domain.dto.NotificationPrefsDTO;
import com.bloomfinance.notification.domain.dto.NotificationQuery;
import com.bloomfinance.notification.domain.entity.NotificationEntity;
import com.bloomfinance.notification.domain.entity.NotificationPrefsEntity;
import com.bloomfinance.notification.domain.enums.NotificationTypeEnum;
import com.bloomfinance.notification.domain.vo.NotificationPrefsVO;
import com.bloomfinance.notification.domain.vo.NotificationVO;
import com.bloomfinance.notification.repository.NotificationPrefsRepository;
import com.bloomfinance.notification.repository.NotificationRepository;
import com.bloomfinance.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知服务实现类
 *
 * @author BloomFinance
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPrefsRepository notificationPrefsRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long sendNotification(CreateNotificationDTO dto) {
        log.info("发送通知, userId={}, type={}, title={}", dto.getUserId(), dto.getType(), dto.getTitle());

        // 校验通知类型
        NotificationTypeEnum typeEnum = NotificationTypeEnum.fromCode(dto.getType());
        if (typeEnum == null) {
            throw new com.bloomfinance.common.exception.BusinessException(400, "无效的通知类型");
        }

        // 构建实体
        NotificationEntity entity = new NotificationEntity();
        entity.setUserId(dto.getUserId());
        entity.setType(dto.getType());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setStatus(1); // 未读
        entity.setSentAt(LocalDateTime.now());

        // 保存
        notificationRepository.insert(entity);
        log.info("发送通知成功, userId={}, notificationId={}", dto.getUserId(), entity.getId());

        return entity.getId();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public PageResult<NotificationVO> getNotifications(Long userId, NotificationQuery query) {
        log.info("获取通知列表, userId={}, query={}", userId, query);

        Page<NotificationEntity> page = new Page<>(query.getCurrent(), query.getSize());
        LambdaQueryWrapper<NotificationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NotificationEntity::getUserId, userId);

        if (query.getType() != null) {
            wrapper.eq(NotificationEntity::getType, query.getType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(NotificationEntity::getStatus, query.getStatus());
        }

        wrapper.orderByDesc(NotificationEntity::getSentAt);
        IPage<NotificationEntity> resultPage = notificationRepository.selectPage(page, wrapper);

        List<NotificationVO> voList = resultPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        PageResult<NotificationVO> pageResult = new PageResult<>();
        pageResult.setRecords(voList);
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setSize(resultPage.getSize());
        pageResult.setCurrent(resultPage.getCurrent());
        pageResult.setPages(resultPage.getPages());
        pageResult.setCode(200);
        pageResult.setMessage("success");

        return pageResult;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public Long getUnreadCount(Long userId) {
        log.info("获取未读通知数量, userId={}", userId);

        Long count = notificationRepository.countUnread(userId);
        return count != null ? count : 0L;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long userId, Long notificationId) {
        log.info("标记通知已读, userId={}, notificationId={}", userId, notificationId);

        NotificationEntity entity = notificationRepository.findByIdAndUserId(notificationId, userId);
        if (entity == null) {
            throw new ResourceNotFoundException("通知不存在");
        }

        entity.setStatus(2); // 已读
        entity.setReadAt(LocalDateTime.now());
        notificationRepository.updateById(entity);

        log.info("标记通知已读成功, userId={}, notificationId={}", userId, notificationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long userId) {
        log.info("标记所有通知已读, userId={}", userId);

        LambdaQueryWrapper<NotificationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NotificationEntity::getUserId, userId)
               .eq(NotificationEntity::getStatus, 1); // 未读

        List<NotificationEntity> unreadList = notificationRepository.selectList(wrapper);
        for (NotificationEntity entity : unreadList) {
            entity.setStatus(2); // 已读
            entity.setReadAt(LocalDateTime.now());
            notificationRepository.updateById(entity);
        }

        log.info("标记所有通知已读完成, userId={}, count={}", userId, unreadList.size());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public NotificationPrefsVO getPreferences(Long userId) {
        log.info("获取通知偏好设置, userId={}", userId);

        NotificationPrefsEntity entity = notificationPrefsRepository.findByUserId(userId);
        if (entity == null) {
            // 返回默认设置
            NotificationPrefsVO vo = new NotificationPrefsVO();
            vo.setUserId(userId);
            vo.setBillDueEnabled(true);
            vo.setOverdueWarningEnabled(true);
            vo.setLargeTxEnabled(true);
            vo.setGoalDeviationEnabled(true);
            return vo;
        }

        return convertToPrefsVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePreferences(Long userId, NotificationPrefsDTO dto) {
        log.info("更新通知偏好设置, userId={}", userId);

        NotificationPrefsEntity entity = notificationPrefsRepository.findByUserId(userId);
        if (entity == null) {
            // 创建新记录
            entity = new NotificationPrefsEntity();
            entity.setUserId(userId);
        }

        // 更新字段
        if (dto.getBillDueEnabled() != null) {
            entity.setBillDueEnabled(dto.getBillDueEnabled());
        }
        if (dto.getOverdueWarningEnabled() != null) {
            entity.setOverdueWarningEnabled(dto.getOverdueWarningEnabled());
        }
        if (dto.getLargeTxEnabled() != null) {
            entity.setLargeTxEnabled(dto.getLargeTxEnabled());
        }
        if (dto.getGoalDeviationEnabled() != null) {
            entity.setGoalDeviationEnabled(dto.getGoalDeviationEnabled());
        }
        if (dto.getQuietHoursStart() != null) {
            entity.setQuietHoursStart(dto.getQuietHoursStart());
        }
        if (dto.getQuietHoursEnd() != null) {
            entity.setQuietHoursEnd(dto.getQuietHoursEnd());
        }

        if (entity.getId() == null) {
            notificationPrefsRepository.insert(entity);
        } else {
            notificationPrefsRepository.updateById(entity);
        }

        log.info("更新通知偏好设置成功, userId={}", userId);
    }

    /**
     * 实体转换为VO
     */
    private NotificationVO convertToVO(NotificationEntity entity) {
        NotificationVO vo = new NotificationVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置通知类型名称
        NotificationTypeEnum typeEnum = NotificationTypeEnum.fromCode(entity.getType());
        if (typeEnum != null) {
            vo.setTypeName(typeEnum.getDescription());
        }

        // 设置状态名称
        vo.setStatusName(getStatusName(entity.getStatus()));

        return vo;
    }

    /**
     * 实体转换为偏好设置VO
     */
    private NotificationPrefsVO convertToPrefsVO(NotificationPrefsEntity entity) {
        NotificationPrefsVO vo = new NotificationPrefsVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "未读";
            case 2:
                return "已读";
            case 3:
                return "已忽略";
            default:
                return "未知";
        }
    }
}