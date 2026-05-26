# Bloom Finance 后端

个人财务管理系统后端服务，基于 Spring Boot 3.2 + Java 17。

## 技术栈

| 类别 | 技术 |
|------|------|
| 框架 | Spring Boot 3.2 |
| ORM | MyBatis-Plus 3.5 |
| 安全 | Spring Security + JWT |
| 缓存 | Redis (Spring Data Redis) |
| 数据库 | MySQL 8.0 |
| 对象转换 | MapStruct |
| 文档 | SpringDoc OpenAPI 3 (Swagger) |

## 项目结构

```
bloom-finance/
├── src/main/java/com/bloomfinance/
│   ├── common/                    # 通用层
│   │   ├── config/                # 配置类
│   │   ├── constant/              # 常量
│   │   ├── context/               # 上下文
│   │   ├── enums/                 # 枚举
│   │   ├── exception/             # 异常
│   │   ├── result/                # 统一响应
│   │   └── util/                  # 工具类
│   ├── user/                      # 用户/认证模块
│   ├── account/                   # 账户管理模块
│   ├── bookkeeping/              # 智能记账模块
│   ├── asset/                     # 资产分析模块
│   ├── goal/                      # 财务目标模块
│   ├── reminder/                  # 账单提醒模块
│   └── notification/              # 推送通知模块
├── src/main/resources/
│   ├── application.yml            # 主配置
│   ├── application-dev.yml        # 开发环境
│   └── application-prod.yml       # 生产环境
├── scripts/sql/
│   └── init.sql                   # 数据库初始化脚本
└── pom.xml
```

## 快速启动

### 前置要求

- JDK 17+
- Maven 3.9+
- MySQL 8.0
- Redis 7+

### 1. 初始化数据库

```sql
-- 登录 MySQL 后执行
CREATE DATABASE bloom_finance DEFAULT CHARSET utf8mb4;

-- 导入初始化脚本
source /path/to/scripts/sql/init.sql;
```

### 2. 配置环境

```bash
# 修改 application-dev.yml 中的数据库连接信息
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bloom_finance?useSSL=false...
    username: root
    password: your_password

  data:
    redis:
      host: localhost
      port: 6379
```

### 3. 编译运行

```bash
cd bloom-finance
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. 访问接口文档

开发环境启动后访问：
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- API Docs: http://localhost:8080/api/v3/api-docs

## 默认账户

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 测试用户 | test | test123 |

## 接口规范

详见 [后端开发规范.md](../后端开发规范.md)

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": 1716200000000,
  "traceId": "abc123"
}
```

### 认证方式

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/v1/...
```

## 模块说明

| 模块 | 说明 | 主要功能 |
|------|------|----------|
| user | 用户/认证 | 登录注册、Token刷新、用户信息 |
| account | 账户管理 | 账户CRUD、余额管理、卡号加密 |
| bookkeeping | 智能记账 | 流水管理、分类引擎、周期模板 |
| asset | 资产分析 | 财务健康分、资产配置、收支统计 |
| goal | 财务目标 | 目标CRUD、进度跟踪、账户关联 |
| reminder | 账单提醒 | 账单管理、到期提醒、逾期预警 |
| notification | 推送通知 | 消息推送、已读管理、偏好设置 |

## 开发规范

- 命名遵循 camelCase（变量/方法）/ PascalCase（类）
- 数据库表/字段使用 snake_case
- 所有接口须通过 Token 认证（除 /v1/auth/**）
- 敏感信息（密码、卡号）加密存储
- 事务边界在 Service 层控制

## 相关文档

- [后端开发规范.md](../后端开发规范.md) - 详细的开发规范文档
- [财务管家系统PRD.md](../../product(产品经理)/财务管家系统PRD.md) - 产品需求文档