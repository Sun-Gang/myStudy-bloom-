-- ============================================
-- Bloom Finance 数据库初始化脚本
-- 版本: 1.0.0
-- 创建时间: 2026-05-21
-- ============================================

CREATE DATABASE IF NOT EXISTS bloom_finance
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE bloom_finance;

-- ============================================
-- 1. 用户表
-- ============================================
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username        VARCHAR(64) NOT NULL COMMENT '用户名',
    password        VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    nickname        VARCHAR(64) NOT NULL COMMENT '昵称',
    phone           VARCHAR(20) COMMENT '手机号',
    email           VARCHAR(128) COMMENT '邮箱',
    avatar          VARCHAR(512) COMMENT '头像URL',
    status          TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0-冻结 1-正常',
    last_login_at   DATETIME COMMENT '最后登录时间',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    UNIQUE KEY uk_username (username),
    KEY idx_phone (phone),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================
-- 2. 账户表
-- ============================================
DROP TABLE IF EXISTS t_account;
CREATE TABLE t_account (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '账户ID',
    user_id         BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    account_type    TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '账户类型：1-储蓄卡 2-信用卡 3-证券 4-基金 5-社保 6-公积金 7-负债 8-其他',
    bank_name       VARCHAR(64) NOT NULL COMMENT '银行/机构名称',
    card_no         VARCHAR(64) NOT NULL COMMENT '卡号（AES加密存储）',
    balance         DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
    status          TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0-冻结 1-正常',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
    KEY idx_user_id (user_id),
    KEY idx_account_type (account_type),
    KEY idx_status (status),
    KEY idx_user_type (user_id, account_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账户表';

-- ============================================
-- 3. 分类表（收支分类）
-- ============================================
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    parent_id       BIGINT UNSIGNED DEFAULT 0 COMMENT '父级ID，0为顶级',
    name            VARCHAR(32) NOT NULL COMMENT '分类名称',
    icon            VARCHAR(64) COMMENT '图标',
    sort_order      INT UNSIGNED DEFAULT 0 COMMENT '排序',
    type            TINYINT UNSIGNED NOT NULL COMMENT '类型：1-收入 2-支出',
    status          TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_type (type),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收支分类表';

-- 初始化收支分类数据
INSERT INTO t_category (id, parent_id, name, icon, sort_order, type, status) VALUES
-- 收入类
(1, 0, '工资', 'payments', 1, 1, 1),
(2, 0, '奖金', 'card_giftcard', 2, 1, 1),
(3, 0, '投资收入', 'trending_up', 3, 1, 1),
(4, 0, '副业', 'work', 4, 1, 1),
(5, 0, '理财收益', 'savings', 5, 1, 1),
-- 支出类
(10, 0, '餐饮', 'restaurant', 10, 2, 1),
(11, 10, '早午晚餐', 'restaurant', 1, 2, 1),
(12, 10, '外卖', 'delivery_dining', 2, 2, 1),
(13, 10, '下午茶', 'local_cafe', 3, 2, 1),
(20, 0, '居住', 'home', 20, 2, 1),
(21, 20, '房租/房贷', 'home', 1, 2, 1),
(22, 20, '水电气', 'bolt', 2, 2, 1),
(23, 20, '物业费', 'cleaning_services', 3, 2, 1),
(30, 0, '交通', 'commute', 30, 2, 1),
(31, 30, '公共交通', 'directions_bus', 1, 2, 1),
(32, 30, '打车', 'local_taxi', 2, 2, 1),
(33, 30, '私家车', 'directions_car', 3, 2, 1),
(40, 0, '购物', 'shopping_bag', 40, 2, 1),
(50, 0, '医疗', 'health_and_safety', 50, 2, 1),
(60, 0, '教育', 'school', 60, 2, 1),
(70, 0, '娱乐', 'sports_esports', 70, 2, 1),
(80, 0, '旅游', 'flight', 80, 2, 1),
(90, 0, '社交', 'people', 90, 2, 1),
(100, 0, '投资', 'analytics', 100, 2, 1);

-- ============================================
-- 4. 流水记录表
-- ============================================
DROP TABLE IF EXISTS t_transaction;
CREATE TABLE t_transaction (
    id                  BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '流水ID',
    user_id             BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    account_id          BIGINT UNSIGNED NOT NULL COMMENT '账户ID',
    amount              DECIMAL(18,2) NOT NULL COMMENT '金额（正数收入，负数支出）',
    transaction_type    TINYINT UNSIGNED NOT NULL COMMENT '类型：1-收入 2-支出',
    category_id         BIGINT UNSIGNED COMMENT '分类ID',
    category_name       VARCHAR(32) COMMENT '分类名称（冗余）',
    merchant_name       VARCHAR(128) COMMENT '商户名称',
    mcc_code            VARCHAR(10) COMMENT 'MCC码',
    description         VARCHAR(255) COMMENT '备注描述',
    transaction_date    DATETIME NOT NULL COMMENT '交易时间',
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted             TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id),
    KEY idx_account_id (account_id),
    KEY idx_category_id (category_id),
    KEY idx_transaction_date (transaction_date),
    KEY idx_type (transaction_type),
    KEY idx_user_date_type (user_id, transaction_date, transaction_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流水记录表';

-- ============================================
-- 5. 周期记账模板表
-- ============================================
DROP TABLE IF EXISTS t_transaction_template;
CREATE TABLE t_transaction_template (
    id                  BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '模板ID',
    user_id             BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    name                VARCHAR(64) NOT NULL COMMENT '模板名称',
    account_id          BIGINT UNSIGNED NOT NULL COMMENT '账户ID',
    amount              DECIMAL(18,2) NOT NULL COMMENT '金额',
    transaction_type    TINYINT UNSIGNED NOT NULL COMMENT '类型：1-收入 2-支出',
    category_id         BIGINT UNSIGNED COMMENT '分类ID',
    frequency           VARCHAR(20) NOT NULL COMMENT '周期：daily/weekly/monthly/yearly',
    next_trigger_date   DATE COMMENT '下次触发日期',
    status              TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：0-停用 1-启用',
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted             TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id),
    KEY idx_next_trigger (next_trigger_date, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='周期记账模板表';

-- ============================================
-- 6. 财务目标表
-- ============================================
DROP TABLE IF EXISTS t_goal;
CREATE TABLE t_goal (
    id                      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '目标ID',
    user_id                 BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    goal_name               VARCHAR(64) NOT NULL COMMENT '目标名称',
    goal_type               TINYINT UNSIGNED NOT NULL COMMENT '类型：1-购房 2-教育 3-养老 4-旅游 5-购车 6-通用',
    target_amount           DECIMAL(18,2) NOT NULL COMMENT '目标金额',
    current_amount          DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '当前已存金额',
    target_date             DATE NOT NULL COMMENT '目标日期',
    monthly_suggested_amount DECIMAL(18,2) COMMENT '建议每月储蓄金额',
    probability             INT UNSIGNED COMMENT '达成概率（0-100）',
    status                  TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：1-进行中 2-已完成 3-已放弃',
    created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted                 TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_target_date (target_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='财务目标表';

-- ============================================
-- 7. 目标-账户关联表
-- ============================================
DROP TABLE IF EXISTS t_goal_account_rel;
CREATE TABLE t_goal_account_rel (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    goal_id     BIGINT UNSIGNED NOT NULL COMMENT '目标ID',
    account_id  BIGINT UNSIGNED NOT NULL COMMENT '账户ID',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_goal_account (goal_id, account_id),
    KEY idx_goal_id (goal_id),
    KEY idx_account_id (account_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='目标账户关联表';

-- ============================================
-- 8. 账单表
-- ============================================
DROP TABLE IF EXISTS t_bill;
CREATE TABLE t_bill (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '账单ID',
    user_id         BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    account_id      BIGINT UNSIGNED NOT NULL COMMENT '账户ID（信用卡或贷款）',
    bill_type       TINYINT UNSIGNED NOT NULL COMMENT '类型：1-信用卡 2-贷款',
    bill_date       DATE NOT NULL COMMENT '账单日',
    due_date        DATE NOT NULL COMMENT '到期日',
    amount          DECIMAL(18,2) NOT NULL COMMENT '账单金额',
    status          TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：1-待还 2-已还 3-逾期',
    remind_status   TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '提醒状态：0-未提醒 1-已提醒',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id),
    KEY idx_account_id (account_id),
    KEY idx_due_date (due_date),
    KEY idx_status (status),
    KEY idx_user_status_due (user_id, status, due_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='账单表';

-- ============================================
-- 9. 通知表
-- ============================================
DROP TABLE IF EXISTS t_notification;
CREATE TABLE t_notification (
    id          BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    type        TINYINT UNSIGNED NOT NULL COMMENT '类型：1-账单到期 2-逾期预警 3-大额收支 4-目标偏离 5-系统通知',
    title       VARCHAR(128) NOT NULL COMMENT '通知标题',
    content     TEXT COMMENT '通知内容',
    status      TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '状态：1-未读 2-已读 3-已忽略',
    sent_at     DATETIME NOT NULL COMMENT '发送时间',
    read_at     DATETIME COMMENT '阅读时间',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    KEY idx_user_id (user_id),
    KEY idx_type (type),
    KEY idx_status (status),
    KEY idx_sent_at (sent_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知表';

-- ============================================
-- 10. 通知偏好表
-- ============================================
DROP TABLE IF EXISTS t_notification_prefs;
CREATE TABLE t_notification_prefs (
    id                      BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id                 BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    bill_due_enabled       TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '账单到期提醒：0-关闭 1-开启',
    overdue_warning_enabled TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '逾期预警：0-关闭 1-开启',
    large_tx_enabled       TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '大额收支提醒：0-关闭 1-开启',
    goal_deviation_enabled TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '目标偏离提醒：0-关闭 1-开启',
    quiet_hours_start      TIME COMMENT '免打扰开始时间',
    quiet_hours_end        TIME COMMENT '免打扰结束时间',
    created_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知偏好表';

-- ============================================
-- 初始化管理员账户（密码: admin123）
-- ============================================
INSERT INTO t_user (username, password, nickname, phone, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', '13800138000', 1);

-- ============================================
-- 初始化测试用户（密码: test123）
-- ============================================
INSERT INTO t_user (username, password, nickname, phone, status) VALUES
('test', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '测试用户', '13900139000', 1);

-- ============================================
-- 初始化测试账户
-- ============================================
INSERT INTO t_account (user_id, account_type, bank_name, card_no, balance, status) VALUES
(2, 1, '中国工商银行', '6222021234567890123', 50000.00, 1),
(2, 2, '招商银行', '6226091234567890', -12000.00, 1),
(2, 4, '天天基金', 'TF000001', 100000.00, 1),
(2, 5, '社保账户', 'SS000001', 0.00, 1);

-- ============================================
-- 初始化测试目标
-- ============================================
INSERT INTO t_goal (user_id, goal_name, goal_type, target_amount, current_amount, target_date, monthly_suggested_amount, probability, status) VALUES
(2, '首套房首付', 1, 1200000.00, 450000.00, '2028-06-01', 15000.00, 85, 1),
(2, '环球旅行', 4, 80000.00, 72000.00, '2027-01-01', 1000.00, 99, 1);

-- ============================================
-- 初始化测试流水
-- ============================================
INSERT INTO t_transaction (user_id, account_id, amount, transaction_type, category_id, category_name, merchant_name, transaction_date) VALUES
(2, 1, 25000.00, 1, 1, '工资', 'XX科技有限公司', '2026-05-01 09:00:00'),
(2, 1, -3500.00, 2, 11, '早午晚餐', '公司食堂', '2026-05-02 12:30:00'),
(2, 1, -3000.00, 2, 21, '房租/房贷', '房东', '2026-05-03 10:00:00'),
(2, 1, -500.00, 2, 31, '公共交通', '地铁', '2026-05-04 08:30:00'),
(2, 1, 1500.00, 1, 4, '副业', '自由职业', '2026-05-05 15:00:00');

-- ============================================
-- 初始化测试账单
-- ============================================
INSERT INTO t_bill (user_id, account_id, bill_type, bill_date, due_date, amount, status, remind_status) VALUES
(2, 2, 1, '2026-05-01', '2026-05-25', 12000.00, 1, 0);