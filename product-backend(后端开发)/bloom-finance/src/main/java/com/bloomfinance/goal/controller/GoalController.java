package com.bloomfinance.goal.controller;

import com.bloomfinance.common.context.UserContext;
import com.bloomfinance.common.exception.BusinessException;
import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.common.result.Result;
import com.bloomfinance.goal.domain.dto.CreateGoalDTO;
import com.bloomfinance.goal.domain.dto.GoalQuery;
import com.bloomfinance.goal.domain.dto.UpdateGoalDTO;
import com.bloomfinance.goal.domain.vo.GoalAccountRelVO;
import com.bloomfinance.goal.domain.vo.GoalProgressVO;
import com.bloomfinance.goal.domain.vo.GoalVO;
import com.bloomfinance.goal.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 目标控制器
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/goals")
@RequiredArgsConstructor
@Tag(name = "目标管理", description = "目标相关接口")
public class GoalController {

    private final GoalService goalService;

    /**
     * 创建目标
     */
    @PostMapping
    @Operation(summary = "创建目标", description = "创建一个新的理财目标")
    public Result<Long> createGoal(@Valid @RequestBody CreateGoalDTO dto) {
        Long userId = getCurrentUserId();
        Long goalId = goalService.createGoal(userId, dto);
        return Result.created(goalId);
    }

    /**
     * 更新目标
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新目标", description = "更新指定目标的信息")
    public Result<Void> updateGoal(@PathVariable Long id, @Valid @RequestBody UpdateGoalDTO dto) {
        Long userId = getCurrentUserId();
        goalService.updateGoal(userId, id, dto);
        return Result.success();
    }

    /**
     * 删除目标
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除目标", description = "删除指定的目标")
    public Result<Void> deleteGoal(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        goalService.deleteGoal(userId, id);
        return Result.noContent();
    }

    /**
     * 获取目标详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取目标详情", description = "获取指定目标的详细信息")
    public Result<GoalVO> getGoalById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        GoalVO vo = goalService.getGoalById(userId, id);
        return Result.success(vo);
    }

    /**
     * 分页查询目标列表
     */
    @GetMapping
    @Operation(summary = "查询目标列表", description = "分页查询目标列表")
    public Result<PageResult<GoalVO>> listGoals(GoalQuery query) {
        Long userId = getCurrentUserId();
        PageResult<GoalVO> result = goalService.listGoals(userId, query);
        return Result.success(result);
    }

    /**
     * 获取目标进度
     */
    @GetMapping("/{id}/progress")
    @Operation(summary = "获取目标进度", description = "获取指定目标的进度信息和建议")
    public Result<GoalProgressVO> getGoalProgress(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        GoalProgressVO vo = goalService.getGoalProgress(userId, id);
        return Result.success(vo);
    }

    /**
     * 关联账户到目标
     */
    @PostMapping("/{id}/accounts")
    @Operation(summary = "关联账户", description = "将账户关联到目标")
    public Result<Void> associateAccount(@PathVariable Long id, @RequestParam Long accountId) {
        Long userId = getCurrentUserId();
        goalService.associateAccount(userId, id, accountId);
        return Result.success();
    }

    /**
     * 取消目标与账户的关联
     */
    @DeleteMapping("/{id}/accounts/{accountId}")
    @Operation(summary = "取消账户关联", description = "取消目标与账户的关联关系")
    public Result<Void> disassociateAccount(@PathVariable Long id, @PathVariable Long accountId) {
        Long userId = getCurrentUserId();
        goalService.disassociateAccount(userId, id, accountId);
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