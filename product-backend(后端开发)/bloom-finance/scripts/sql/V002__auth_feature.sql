-- ============================================
-- Bloom Finance 认证功能扩展脚本
-- 版本: V002
-- 创建时间: 2026-05-21
-- ============================================

USE bloom_finance;

-- 1. 新增邮箱唯一索引（t_user.email）
ALTER TABLE t_user ADD UNIQUE KEY uk_email (email);

-- 2. 新增验证码表
DROP TABLE IF EXISTS t_verification_code;
CREATE TABLE t_verification_code (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    email           VARCHAR(128) NOT NULL COMMENT '邮箱地址',
    code            VARCHAR(10) NOT NULL COMMENT '验证码',
    type            TINYINT UNSIGNED NOT NULL COMMENT '类型：1-注册 2-登录 3-找回密码 4-绑定邮箱',
    status          TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态：0-未使用 1-已使用 2-已过期',
    expires_at      DATETIME NOT NULL COMMENT '过期时间',
    verified_at     DATETIME COMMENT '验证时间',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_email_type_status (email, type, status),
    KEY idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮箱验证码表';

-- 3. 新增 last_login_ip 字段（可选，用于安全审计）
ALTER TABLE t_user ADD COLUMN last_login_ip VARCHAR(64) COMMENT '最后登录IP' AFTER last_login_at;

-- 4. 为 test 用户设置邮箱（方便测试）
UPDATE t_user SET email = 'test@bloomfinance.com' WHERE username = 'test';
