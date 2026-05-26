-- ============================================
-- 预算设置表
-- ============================================
DROP TABLE IF EXISTS t_budget_setting;
CREATE TABLE t_budget_setting (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id         BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    budget_month    VARCHAR(7) NOT NULL COMMENT '预算月份 (yyyy-MM)',
    category_id     BIGINT NOT NULL COMMENT '分类ID',
    budget_amount  DECIMAL(18,2) NOT NULL DEFAULT 0 COMMENT '预算金额',
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted        TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标记: 0-未删除 1-已删除',
    INDEX idx_user_month (user_id, budget_month),
    INDEX idx_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='预算设置表';
