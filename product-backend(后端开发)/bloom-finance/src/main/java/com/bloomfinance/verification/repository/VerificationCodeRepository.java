package com.bloomfinance.verification.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.verification.domain.entity.VerificationCodeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 验证码Repository接口
 *
 * @author bloom-finance
 */
@Mapper
public interface VerificationCodeRepository extends BaseMapper<VerificationCodeEntity> {

    /**
     * 根据邮箱和类型查找最新未使用的验证码
     */
    @Select("SELECT * FROM t_verification_code WHERE email = #{email} AND type = #{type} AND status = 0 AND expires_at > #{now} ORDER BY created_at DESC LIMIT 1")
    Optional<VerificationCodeEntity> findLatestByEmailAndType(
            @Param("email") String email,
            @Param("type") Integer type,
            @Param("now") LocalDateTime now);

    /**
     * 将指定邮箱和类型的未使用验证码标记为已使用
     */
    @Update("UPDATE t_verification_code SET status = 1, updated_at = #{now} WHERE email = #{email} AND type = #{type} AND status = 0")
    Integer markAsUsedByEmailAndType(
            @Param("email") String email,
            @Param("type") Integer type,
            @Param("now") LocalDateTime now);

    /**
     * 删除指定邮箱和类型的过期验证码
     */
    @Select("DELETE FROM t_verification_code WHERE email = #{email} AND type = #{type} AND expires_at < #{expiresAt}")
    Integer deleteExpiredByEmailAndType(
            @Param("email") String email,
            @Param("type") Integer type,
            @Param("expiresAt") LocalDateTime expiresAt);
}