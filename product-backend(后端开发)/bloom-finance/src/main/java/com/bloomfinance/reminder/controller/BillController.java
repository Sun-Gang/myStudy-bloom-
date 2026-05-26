package com.bloomfinance.reminder.controller;

import com.bloomfinance.common.result.PageResult;
import com.bloomfinance.common.result.Result;
import com.bloomfinance.reminder.domain.dto.BillQuery;
import com.bloomfinance.reminder.domain.dto.CreateBillDTO;
import com.bloomfinance.reminder.domain.dto.UpdateBillDTO;
import com.bloomfinance.reminder.domain.vo.BillVO;
import com.bloomfinance.reminder.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 账单控制器
 *
 * @author BloomFinance
 */
@Slf4j
@RestController
@RequestMapping("/v1/bills")
@RequiredArgsConstructor
@Tag(name = "账单管理", description = "账单相关接口")
public class BillController {

    private final BillService billService;

    /**
     * 创建账单
     */
    @PostMapping
    @Operation(summary = "创建账单", description = "创建一个新的账单")
    public Result<Long> createBill(@Valid @RequestBody CreateBillDTO dto) {
        Long userId = getCurrentUserId();
        Long billId = billService.createBill(userId, dto);
        return Result.created(billId);
    }

    /**
     * 更新账单
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新账单", description = "更新指定账单的信息")
    public Result<Void> updateBill(@PathVariable Long id, @Valid @RequestBody UpdateBillDTO dto) {
        Long userId = getCurrentUserId();
        billService.updateBill(userId, id, dto);
        return Result.success();
    }

    /**
     * 标记账单已还款
     */
    @PutMapping("/{id}/paid")
    @Operation(summary = "标记已还款", description = "标记指定账单为已还款")
    public Result<Void> markAsPaid(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        billService.markAsPaid(userId, id);
        return Result.success();
    }

    /**
     * 删除账单
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除账单", description = "删除指定的账单")
    public Result<Void> deleteBill(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        billService.deleteBill(id);
        return Result.noContent();
    }

    /**
     * 获取账单详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取账单详情", description = "获取指定账单的详细信息")
    public Result<BillVO> getBillById(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        BillVO vo = billService.getBillById(userId, id);
        return Result.success(vo);
    }

    /**
     * 分页查询账单列表
     */
    @GetMapping
    @Operation(summary = "查询账单列表", description = "分页查询账单列表")
    public Result<PageResult<BillVO>> pageBills(BillQuery query) {
        Long userId = getCurrentUserId();
        PageResult<BillVO> result = billService.pageBills(userId, query);
        return Result.success(result);
    }

    /**
     * 获取即将到期的账单
     */
    @GetMapping("/upcoming")
    @Operation(summary = "获取即将到期账单", description = "获取指定天数内即将到期的账单")
    public Result<List<BillVO>> getUpcomingBills(@RequestParam(defaultValue = "30") Integer days) {
        Long userId = getCurrentUserId();
        List<BillVO> bills = billService.getUpcomingBills(userId, days);
        return Result.success(bills);
    }

    /**
     * 获取即将到期（7天内）的账单
     */
    @GetMapping("/due-soon")
    @Operation(summary = "获取7天内到期账单", description = "获取7天内即将到期的账单")
    public Result<List<BillVO>> getDueSoonBills() {
        Long userId = getCurrentUserId();
        List<BillVO> bills = billService.getDueSoonBills(userId);
        return Result.success(bills);
    }

    /**
     * 获取已逾期账单
     */
    @GetMapping("/overdue")
    @Operation(summary = "获取已逾期账单", description = "获取已逾期的账单列表")
    public Result<List<BillVO>> getOverdueBills() {
        Long userId = getCurrentUserId();
        List<BillVO> bills = billService.getOverdueBills(userId);
        return Result.success(bills);
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        return 1L;
    }
}