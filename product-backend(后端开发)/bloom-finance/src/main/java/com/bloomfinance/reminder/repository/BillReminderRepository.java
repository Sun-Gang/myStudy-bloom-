package com.bloomfinance.reminder.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.reminder.domain.entity.BillReminderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 账单仓库
 *
 * @author BloomFinance
 */
@Mapper
public interface BillReminderRepository extends BaseMapper<BillReminderEntity> {

    /**
     * 根据ID和用户ID查询账单
     *
     * @param id     账单ID
     * @param userId 用户ID
     * @return 账单实体
     */
    BillReminderEntity findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 根据用户ID查询账单列表
     *
     * @param userId 用户ID
     * @return 账单列表
     */
    List<BillReminderEntity> findByUserId(@Param("userId") Long userId);

    /**
     * 根据账户ID查询账单列表
     *
     * @param accountId 账户ID
     * @return 账单列表
     */
    List<BillReminderEntity> findByAccountId(@Param("accountId") Long accountId);

    /**
     * 查询即将到期的账单
     *
     * @param userId 用户ID
     * @param start  开始日期
     * @param end    结束日期
     * @return 账单列表
     */
    List<BillReminderEntity> findUpcomingBills(@Param("userId") Long userId,
                                        @Param("start") LocalDate start,
                                        @Param("end") LocalDate end);

    /**
     * 查询即将到期（指定天数内）的账单
     *
     * @param userId     用户ID
     * @param daysBefore 提前天数
     * @return 账单列表
     */
    List<BillReminderEntity> findDueSoonBills(@Param("userId") Long userId, @Param("daysBefore") Integer daysBefore);

    /**
     * 查询已逾期账单
     *
     * @param userId 用户ID
     * @return 账单列表
     */
    List<BillReminderEntity> findOverdueBills(@Param("userId") Long userId);
}