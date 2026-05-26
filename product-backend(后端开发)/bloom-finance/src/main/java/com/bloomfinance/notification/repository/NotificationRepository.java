package com.bloomfinance.notification.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.notification.domain.entity.NotificationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知仓库
 *
 * @author BloomFinance
 */
@Mapper
public interface NotificationRepository extends BaseMapper<NotificationEntity> {

    /**
     * 根据ID和用户ID查询通知
     *
     * @param id     通知ID
     * @param userId 用户ID
     * @return 通知实体
     */
    NotificationEntity findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 根据用户ID查询通知列表
     *
     * @param userId 用户ID
     * @return 通知列表
     */
    List<NotificationEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 统计未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    Long countUnread(@Param("userId") Long userId);
}