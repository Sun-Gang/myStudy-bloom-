package com.bloomfinance.reminder.service;

import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.reminder.domain.dto.BillQuery;
import com.bloomfinance.reminder.domain.dto.CreateBillDTO;
import com.bloomfinance.reminder.domain.dto.UpdateBillDTO;
import com.bloomfinance.reminder.domain.vo.BillVO;

import java.util.List;

/**
 * 账单服务接口
 *
 * @author BloomFinance
 */
public interface BillService {

    /**
     * 创建账单
     *
     * @param userId 用户ID
     * @param dto    创建账单DTO
     * @return 账单ID
     */
    Long createBill(Long userId, CreateBillDTO dto);

    /**
     * 更新账单
     *
     * @param userId  用户ID
     * @param billId  账单ID
     * @param dto 更新账单DTO
     */
    void updateBill(Long userId, Long billId, UpdateBillDTO dto);

    /**
     * 删除账单
     *
     * @param billId 账单ID
     */
    void deleteBill(Long billId);

    /**
     * 标记账单已还款
     *
     * @param userId 用户ID
     * @param billId 账单ID
     */
    void markAsPaid(Long userId, Long billId);

    /**
     * 获取账单详情
     *
     * @param userId 用户ID
     * @param billId 账单ID
     * @return 账单VO
     */
    BillVO getBillById(Long userId, Long billId);

    /**
     * 分页查询账单列表
     *
     * @param userId 用户ID
     * @param query  查询条件
     * @return 分页结果
     */
    PageResult<BillVO> pageBills(Long userId, BillQuery query);

    /**
     * 获取即将到期的账单
     *
     * @param userId 用户ID
     * @param days   提前天数
     * @return 账单列表
     */
    List<BillVO> getUpcomingBills(Long userId, Integer days);

    /**
     * 获取即将到期（7天内）的账单
     *
     * @param userId 用户ID
     * @return 账单列表
     */
    List<BillVO> getDueSoonBills(Long userId);

    /**
     * 获取已逾期账单
     *
     * @param userId 用户ID
     * @return 账单列表
     */
    List<BillVO> getOverdueBills(Long userId);
}