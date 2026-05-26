package com.bloomfinance.notification.repository;

import com.bloomfinance.notification.domain.entity.NotificationPrefsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通知偏好设置仓库
 *
 * @author BloomFinance
 */
@Mapper
public interface NotificationPrefsRepository {

    /**
     * 根据用户ID查询通知偏好设置
     *
     * @param userId 用户ID
     * @return 通知偏好设置
     */
    NotificationPrefsEntity findByUserId(@Param("userId") Long userId);

    /**
     * 插入通知偏好设置
     *
     * @param entity 通知偏好设置实体
     * @return 影响行数
     */
    int insert(NotificationPrefsEntity entity);

    /**
     * 根据ID更新通知偏好设置
     *
     * @param entity 通知偏好设置实体
     * @return 影响行数
     */
    int updateById(NotificationPrefsEntity entity);
}