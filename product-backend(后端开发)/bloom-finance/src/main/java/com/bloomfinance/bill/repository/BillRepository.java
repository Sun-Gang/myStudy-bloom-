package com.bloomfinance.bill.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.bill.domain.entity.BillEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 账单Repository
 *
 * @author bloom-finance
 */
@Mapper
public interface BillRepository extends BaseMapper<BillEntity> {

    @Select("SELECT * FROM t_bill WHERE user_id = #{userId} AND due_date BETWEEN #{startDate} AND #{endDate} AND status IN (1, 3) AND deleted = 0 ORDER BY due_date ASC")
    List<BillEntity> findUpcomingBills(@Param("userId") Long userId,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Select("SELECT COUNT(*) FROM t_bill WHERE user_id = #{userId} AND status = 3 AND deleted = 0")
    Integer countOverdueBills(@Param("userId") Long userId);
}