# SR03 聊天应用

基于 Spring Boot + React 构建的实时聊天平台，支持聊天室管理、用户邀请与 WebSocket 实时通信。

---

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端 | Spring Boot 4.0.5、Spring MVC、Spring Data JPA、Spring WebSocket |
| 前端 | React（JSX）|
| 数据库 | MySQL 8+ |
| 模板引擎 | Thymeleaf（管理后台）|
| 构建工具 | Maven |
| Java 版本 | Java 17 |

---

## 功能概览

### 用户认证
- 邮箱 + 密码登录
- 登录失败超过 5 次后账户临时封禁 15 分钟
- Session 会话管理，受保护页面自动跳转登录

### 管理后台（`/admin`）
- 用户列表展示，支持关键字搜索与分页
- 新增 / 编辑 / 禁用 / 重新激活 / 删除用户
- 已禁用用户单独列表管理

### 聊天室（Canal）
- 创建聊天室，设置标题、描述、开始时间与有效时长
- 通过邮箱邀请其他用户加入
- 查看自己创建的聊天室及受邀聊天室

### 实时聊天
- 基于 WebSocket 的实时消息推送
- 显示当前在线用户列表
- 连接 / 断开连接状态通知

---

## 快速开始

### 前置条件

- Java 17+
- MySQL 8+
- Node.js（运行前端）
- Maven

### 1. 配置数据库

创建数据库并修改 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chat_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. 启动后端

```bash
./mvnw spring-boot:run
```

后端默认运行在 `http://localhost:8080`。

### 3. 启动前端

```bash
cd frontend
npm install
npm start
```

前端默认运行在 `http://localhost:3000`。

---

## 项目结构

```
project/
├── src/
│   └── main/
│       ├── java/com/sr03/chat/
│       │   ├── config/          # WebSocket 配置与事件监听
│       │   ├── controller/      # HTTP 控制器
│       │   │   ├── AdminController.java      # 管理后台
│       │   │   ├── CanalController.java      # 聊天室 REST API
│       │   │   ├── ChatController.java       # WebSocket 消息处理
│       │   │   ├── LoginController.java      # 登录 / 登出
│       │   │   └── UtilisateurController.java
│       │   ├── model/           # 实体类（Utilisateur、Canal、Message）
│       │   └── repository/      # Spring Data JPA 数据访问层
│       └── resources/
│           ├── application.properties
│           └── templates/       # Thymeleaf 模板（管理后台页面）
└── frontend/
    └── src/
        ├── App.jsx
        └── components/
            ├── Chat.jsx          # 实时聊天界面
            ├── MesSalons.jsx     # 我的聊天室列表
            ├── MesInvitations.jsx# 我的邀请列表
            ├── Planifier.jsx     # 创建 / 规划聊天室
            └── Navbar.jsx        # 导航栏
```

---

## 主要 API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| `POST` | `/login` | 用户登录 |
| `GET` | `/logout` | 用户登出 |
| `GET` | `/moi` | 获取当前登录用户信息 |
| `POST` | `/canaux` | 创建聊天室 |
| `GET` | `/canaux/proprietaire/{id}` | 获取用户创建的聊天室 |
| `GET` | `/canaux/invite/{id}` | 获取用户受邀的聊天室 |
| `POST` | `/canaux/{id}/inviter` | 邀请用户加入聊天室 |
| `DELETE` | `/canaux/{id}` | 删除聊天室 |

WebSocket 连接地址：`ws://localhost:8080/ws`

---

## 开发团队

本项目为 SR03 课程项目。
