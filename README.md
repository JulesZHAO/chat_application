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

创建数据库并修改 `project/src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/chat_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. 启动后端

```bash
cd project
./mvnw spring-boot:run
```

后端默认运行在 `http://localhost:8080`。

### 3. 启动前端

```bash
cd project/frontend
npm install
npm start
```

前端默认运行在 `http://localhost:3000`。

---

## 项目结构

```
IdeaProjects/
├── project/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/sr03/chat/
│   │       │   ├── config/           # WebSocket 配置与事件监听
│   │       │   ├── controller/       # HTTP 控制器
│   │       │   │   ├── AdminController.java       # 管理后台
│   │       │   │   ├── CanalController.java       # 聊天室 REST API
│   │       │   │   ├── ChatController.java        # WebSocket 消息处理
│   │       │   │   └── LoginController.java       # 登录 / 登出
│   │       │   ├── model/            # 实体类（Utilisateur、Canal、Message）
│   │       │   └── repository/       # Spring Data JPA 数据访问层
│   │       └── resources/
│   │           └── templates/        # Thymeleaf 模板（管理后台页面）
│   └── frontend/
│       └── src/
│           └── components/
│               ├── Chat.jsx           # 实时聊天界面
│               ├── MesSalons.jsx      # 我的聊天室列表
│               ├── MesInvitations.jsx # 我的邀请列表
│               ├── Planifier.jsx      # 创建 / 规划聊天室
│               └── Navbar.jsx         # 导航栏
└── README.md
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

## Add your files

* [Create](https://docs.gitlab.com/user/project/repository/web_editor/#create-a-file) or [upload](https://docs.gitlab.com/user/project/repository/web_editor/#upload-a-file) files
* [Add files using the command line](https://docs.gitlab.com/topics/git/add_files/#add-files-to-a-git-repository) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.utc.fr/petrigas/sr03_app_salon_discussion.git
git branch -M main
git push -uf origin main
```

## Integrate with your tools

* [Set up project integrations](https://gitlab.utc.fr/petrigas/sr03_app_salon_discussion/-/settings/integrations)

## Collaborate with your team

* [Invite team members and collaborators](https://docs.gitlab.com/user/project/members/)
* [Create a new merge request](https://docs.gitlab.com/user/project/merge_requests/creating_merge_requests/)
* [Automatically close issues from merge requests](https://docs.gitlab.com/user/project/issues/managing_issues/#closing-issues-automatically)
* [Enable merge request approvals](https://docs.gitlab.com/user/project/merge_requests/approvals/)
* [Set auto-merge](https://docs.gitlab.com/user/project/merge_requests/auto_merge/)

## Test and Deploy

Use the built-in continuous integration in GitLab.

* [Get started with GitLab CI/CD](https://docs.gitlab.com/ci/quick_start/)
* [Analyze your code for known vulnerabilities with Static Application Security Testing (SAST)](https://docs.gitlab.com/user/application_security/sast/)
* [Deploy to Kubernetes, Amazon EC2, or Amazon ECS using Auto Deploy](https://docs.gitlab.com/topics/autodevops/requirements/)
* [Use pull-based deployments for improved Kubernetes management](https://docs.gitlab.com/user/clusters/agent/)
* [Set up protected environments](https://docs.gitlab.com/ci/environments/protected_environments/)

***

# Editing this README

When you're ready to make this README your own, just edit this file and use the handy template below (or feel free to structure it however you want - this is just a starting point!). Thanks to [makeareadme.com](https://www.makeareadme.com/) for this template.

## Suggestions for a good README

Every project is different, so consider which of these sections apply to yours. The sections used in the template are suggestions for most open source projects. Also keep in mind that while a README can be too long and detailed, too long is better than too short. If you think your README is too long, consider utilizing another form of documentation rather than cutting out information.

## Name
Choose a self-explaining name for your project.

## Description
Let people know what your project can do specifically. Provide context and add a link to any reference visitors might be unfamiliar with. A list of Features or a Background subsection can also be added here. If there are alternatives to your project, this is a good place to list differentiating factors.

## Badges
On some READMEs, you may see small images that convey metadata, such as whether or not all the tests are passing for the project. You can use Shields to add some to your README. Many services also have instructions for adding a badge.

## Visuals
Depending on what you are making, it can be a good idea to include screenshots or even a video (you'll frequently see GIFs rather than actual videos). Tools like ttygif can help, but check out Asciinema for a more sophisticated method.

## Installation
Within a particular ecosystem, there may be a common way of installing things, such as using Yarn, NuGet, or Homebrew. However, consider the possibility that whoever is reading your README is a novice and would like more guidance. Listing specific steps helps remove ambiguity and gets people to using your project as quickly as possible. If it only runs in a specific context like a particular programming language version or operating system or has dependencies that have to be installed manually, also add a Requirements subsection.

## Usage
Use examples liberally, and show the expected output if you can. It's helpful to have inline the smallest example of usage that you can demonstrate, while providing links to more sophisticated examples if they are too long to reasonably include in the README.

## Support
Tell people where they can go to for help. It can be any combination of an issue tracker, a chat room, an email address, etc.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Contributing
State if you are open to contributions and what your requirements are for accepting them.

For people who want to make changes to your project, it's helpful to have some documentation on how to get started. Perhaps there is a script that they should run or some environment variables that they need to set. Make these steps explicit. These instructions could also be useful to your future self.

You can also document commands to lint the code or run tests. These steps help to ensure high code quality and reduce the likelihood that the changes inadvertently break something. Having instructions for running tests is especially helpful if it requires external setup, such as starting a Selenium server for testing in a browser.

## Authors and acknowledgment
Show your appreciation to those who have contributed to the project.

## License
For open source projects, say how it is licensed.

## Project status
If you have run out of energy or time for your project, put a note at the top of the README saying that development has slowed down or stopped completely. Someone may choose to fork your project or volunteer to step in as a maintainer or owner, allowing your project to keep going. You can also make an explicit request for maintainers.
