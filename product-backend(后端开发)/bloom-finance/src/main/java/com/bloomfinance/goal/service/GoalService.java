package com.bloomfinance.goal.service;

import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.goal.domain.dto.CreateGoalDTO;
import com.bloomfinance.goal.domain.dto.GoalQuery;
import com.bloomfinance.goal.domain.dto.UpdateGoalDTO;
import com.bloomfinance.goal.domain.vo.GoalProgressVO;
import com.bloomfinance.goal.domain.vo.GoalVO;

/**
 * 目标服务接口
 *
 * @author BloomFinance
 */
public interface GoalService {

    /**
     * 创建目标
     *
     * @param userId 用户ID
     * @param dto    创建目标DTO
     * @return 目标ID
     */
    Long createGoal(Long userId, CreateGoalDTO dto);

    /**
     * 更新目标
     *
     * @param userId  用户ID
     * @param goalId  目标ID
     * @param dto 更新目标DTO
     */
    void updateGoal(Long userId, Long goalId, UpdateGoalDTO dto);

    /**
     * 删除目标
     *
     * @param userId 用户ID
     * @param goalId 目标ID
     */
    void deleteGoal(Long userId, Long goalId);

    /**
     * 获取目标详情
     *
     * @param userId 用户ID
     * @param goalId 目标ID
     * @return 目标VO
     */
    GoalVO getGoalById(Long userId, Long goalId);

    /**
     * 分页查询目标列表
     *
     * @param userId 用户ID
     * @param query  查询条件
     * @return 分页结果
     */
    PageResult<GoalVO> listGoals(Long userId, GoalQuery query);

    /**
     * 获取目标进度
     *
     * @param userId 用户ID
     * @param goalId 目标ID
     * @return 目标进度VO
     */
    GoalProgressVO getGoalProgress(Long userId, Long goalId);

    /**
     * 关联账户到目标
     *
     * @param userId   用户ID
     * @param goalId   目标ID
     * @param accountId 账户ID
     */
    void associateAccount(Long userId, Long goalId, Long accountId);

    /**
     * 取消目标与账户的关联
     *
     * @param userId    用户ID
     * @param goalId    目标ID
     * @param accountId 账户ID
     */
    void disassociateAccount(Long userId, Long goalId, Long accountId);
}