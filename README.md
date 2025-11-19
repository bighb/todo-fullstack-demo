# Todo List - SpringBoot + React Demo

一个用于学习 SpringBoot 后端开发的全栈 Todo List 应用。

## 技术栈

**后端**: Spring Boot 3.2 + Spring Data JPA + MySQL
**前端**: React 18 + Vite + Axios

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- Node.js 18+
- pnpm
- MySQL 8.0

### 1. 创建数据库

```sql
CREATE DATABASE todo_db CHARACTER SET utf8mb4;
```

### 2. 配置环境变量

项目使用环境变量管理敏感信息，启动前需设置：

```bash
# macOS/Linux
export DB_PASSWORD=your_password

# Windows
set DB_PASSWORD=your_password
```

可选环境变量（有默认值）：
- `DB_NAME` - 数据库名（默认：todo_db）
- `DB_USERNAME` - 用户名（默认：root）

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
```

> 查看 `application.properties.example` 了解更多配置方式

### 4. 启动前端

```bash
cd frontend
pnpm install
pnpm dev
```

### 5. 访问应用

- 前端: http://localhost:5173
- API: http://localhost:8080/api/todos

## 项目结构

```
├── backend/          # SpringBoot 后端
├── frontend/         # React 前端
└── DEVELOPMENT_GUIDE.md  # 开发学习指南
```

## 学习目标

- Spring Boot 三层架构
- Spring Data JPA 数据持久化
- RESTful API 设计
- 前后端分离开发

## License

MIT
