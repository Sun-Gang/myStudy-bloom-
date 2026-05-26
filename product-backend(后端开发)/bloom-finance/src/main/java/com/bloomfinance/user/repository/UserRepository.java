package com.bloomfinance.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloomfinance.user.domain.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * 用户Repository接口
 *
 * @author bloom-finance
 */
@Mapper
public interface UserRepository extends BaseMapper<UserEntity> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM t_user WHERE username = #{username} AND deleted = 0 LIMIT 1")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    /**
     * 根据手机号查询用户
     */
    @Select("SELECT * FROM t_user WHERE phone = #{phone} AND deleted = 0 LIMIT 1")
    Optional<UserEntity> findByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM t_user WHERE email = #{email} AND deleted = 0 LIMIT 1")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM t_user WHERE username = #{username} AND deleted = 0")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) > 0 FROM t_user WHERE email = #{email} AND deleted = 0")
    boolean existsByEmail(@Param("email") String email);
}