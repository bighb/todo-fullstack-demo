# SpringBoot + React Todo List - Demo 项目

## 项目概述

这是一个用于学习 SpringBoot 后端框架的 Demo 项目，通过实现经典的 Todo List 应用来掌握 SpringBoot 的核心概念：REST API 开发、JPA 数据持久化、依赖注入等。前端使用 React 构建简洁的用户界面，后端使用 SpringBoot 提供 RESTful API，数据存储在 MySQL 数据库中。

## 核心功能

实现一个完整的 CRUD（增删改查）Todo 管理系统：

- ✅ 创建新的待办事项
- 📝 查看所有待办事项列表
- ✏️ 更新待办事项的完成状态
- 🗑️ 删除待办事项

## 技术栈

### 后端（SpringBoot）

- **框架**: Spring Boot 3.2.x
- **构建工具**: Maven
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA (Hibernate)
- **API**: Spring Web (RESTful)
- **工具**: Lombok (减少样板代码)

### 前端（React）

- **框架**: React 18
- **构建工具**: Vite
- **包管理器**: pnpm（根据你的偏好）
- **HTTP 客户端**: Axios
- **样式**: CSS Modules 或 Tailwind CSS（可选）
- **类型检查**: 可选 TypeScript

### 开发工具

- **IDE**: VS Code
- **数据库管理**: MySQL Workbench 或 DBeaver
- **API 测试**: Postman 或 Insomnia

## 项目文件结构

```
todo-fullstack-demo/
├── backend/                          # SpringBoot 后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/demo/todo/
│   │   │   │   ├── TodoApplication.java        # 启动类
│   │   │   │   ├── controller/
│   │   │   │   │   └── TodoController.java     # REST API 控制器
│   │   │   │   ├── model/
│   │   │   │   │   └── Todo.java               # 实体类
│   │   │   │   ├── repository/
│   │   │   │   │   └── TodoRepository.java     # JPA Repository
│   │   │   │   └── service/
│   │   │   │       └── TodoService.java        # 业务逻辑层
│   │   │   └── resources/
│   │   │       └── application.properties      # 配置文件
│   │   └── test/                               # 测试文件（可选）
│   ├── pom.xml                                 # Maven 依赖配置
│   └── README.md
│
├── frontend/                         # React 前端
│   ├── src/
│   │   ├── components/
│   │   │   ├── TodoList.jsx                    # 待办列表组件
│   │   │   ├── TodoItem.jsx                    # 单个待办项组件
│   │   │   └── TodoForm.jsx                    # 添加待办表单
│   │   ├── services/
│   │   │   └── todoApi.js                      # API 调用封装
│   │   ├── App.jsx                             # 主应用组件
│   │   └── main.jsx                            # 入口文件
│   ├── package.json
│   ├── pnpm-lock.yaml
│   ├── vite.config.js
│   └── README.md
│
└── README.md                         # 项目总体说明
```

## 核心实现要点

### 1. SpringBoot 后端核心代码

#### Todo 实体类（JPA Entity）

```java
@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

#### TodoRepository（数据访问层）

```java
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // JpaRepository 已提供基础 CRUD 方法
    // 可添加自定义查询方法，例如：
    List<Todo> findByCompleted(Boolean completed);
}
```

#### TodoController（REST API）

```java
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173") // 允许前端跨域
public class TodoController {

    @Autowired
    private TodoService todoService;

    // 获取所有待办
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.findAll());
    }

    // 创建待办
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(todoService.save(todo));
    }

    // 更新待办
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
        @PathVariable Long id,
        @RequestBody Todo todoDetails
    ) {
        return ResponseEntity.ok(todoService.update(id, todoDetails));
    }

    // 删除待办
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

#### application.properties 配置

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

# JPA/Hibernate 配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# 服务器端口
server.port=8080
```

### 2. React 前端核心代码

#### API 服务封装

```javascript
// src/services/todoApi.js
import axios from "axios";

const API_BASE_URL = "http://localhost:8080/api/todos";

export const todoApi = {
  // 获取所有待办
  getAll: () => axios.get(API_BASE_URL),

  // 创建待办
  create: (todo) => axios.post(API_BASE_URL, todo),

  // 更新待办
  update: (id, todo) => axios.put(`${API_BASE_URL}/${id}`, todo),

  // 删除待办
  delete: (id) => axios.delete(`${API_BASE_URL}/${id}`),
};
```

#### 主应用组件

```jsx
// src/App.jsx
import { useState, useEffect } from "react";
import { todoApi } from "./services/todoApi";
import TodoForm from "./components/TodoForm";
import TodoList from "./components/TodoList";

function App() {
  const [todos, setTodos] = useState([]);
  const [loading, setLoading] = useState(true);

  // 加载所有待办
  useEffect(() => {
    fetchTodos();
  }, []);

  const fetchTodos = async () => {
    try {
      const response = await todoApi.getAll();
      setTodos(response.data);
    } catch (error) {
      console.error("Failed to fetch todos:", error);
    } finally {
      setLoading(false);
    }
  };

  // 添加待办
  const handleAddTodo = async (todoData) => {
    try {
      const response = await todoApi.create(todoData);
      setTodos([...todos, response.data]);
    } catch (error) {
      console.error("Failed to create todo:", error);
    }
  };

  // 切换完成状态
  const handleToggleTodo = async (id) => {
    const todo = todos.find((t) => t.id === id);
    try {
      const response = await todoApi.update(id, {
        ...todo,
        completed: !todo.completed,
      });
      setTodos(todos.map((t) => (t.id === id ? response.data : t)));
    } catch (error) {
      console.error("Failed to update todo:", error);
    }
  };

  // 删除待办
  const handleDeleteTodo = async (id) => {
    try {
      await todoApi.delete(id);
      setTodos(todos.filter((t) => t.id !== id));
    } catch (error) {
      console.error("Failed to delete todo:", error);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="app">
      <h1>Todo List</h1>
      <TodoForm onSubmit={handleAddTodo} />
      <TodoList
        todos={todos}
        onToggle={handleToggleTodo}
        onDelete={handleDeleteTodo}
      />
    </div>
  );
}

export default App;
```

### 3. 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS todo_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE todo_db;

-- Hibernate 会自动创建表，但也可以手动创建：
CREATE TABLE IF NOT EXISTS todos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 学习目标

通过这个项目，你将学习到：

### SpringBoot 核心概念

- [ ] 理解 SpringBoot 的自动配置和依赖注入（DI/IoC）
- [ ] 掌握 Spring MVC 的 REST API 开发
- [ ] 学习 Spring Data JPA 的使用和 ORM 映射
- [ ] 了解 SpringBoot 的三层架构（Controller-Service-Repository）
- [ ] 掌握 `@RestController`, `@Service`, `@Repository` 等注解

### 全栈集成

- [ ] 理解前后端分离架构
- [ ] 掌握 CORS 跨域问题的处理
- [ ] 学习 RESTful API 的设计原则
- [ ] 了解 HTTP 请求方法（GET、POST、PUT、DELETE）

### 数据库操作

- [ ] MySQL 数据库的连接配置
- [ ] JPA 实体映射和关系定义
- [ ] 基本的 CRUD 操作实现

## 运行步骤

### 1. 准备工作

```bash
# 确保已安装：
# - Java 17 或更高版本
# - Maven 3.6+
# - Node.js 18+
# - pnpm
# - MySQL 8.0

# 验证安装
java -version
mvn -version
node -version
pnpm -version
mysql -version
```

### 2. 数据库设置

```bash
# 启动 MySQL 服务（macOS）
brew services start mysql

# 登录 MySQL 并创建数据库
mysql -u root -p
# 然后执行上面的 SQL 初始化脚本
```

### 3. 启动后端

```bash
cd backend

# 首次运行：下载依赖
mvn clean install

# 启动 SpringBoot 应用
mvn spring-boot:run

# 应用将运行在 http://localhost:8080
# 测试 API：curl http://localhost:8080/api/todos
```

### 4. 启动前端

```bash
cd frontend

# 安装依赖
pnpm install

# 启动开发服务器
pnpm dev

# 前端将运行在 http://localhost:5173
```

### 5. 访问应用

打开浏览器访问 `http://localhost:5173`，你应该能看到 Todo List 应用界面。

## API 接口文档

| 方法   | 路径              | 描述         | 请求体                            | 响应                                                          |
| ------ | ----------------- | ------------ | --------------------------------- | ------------------------------------------------------------- |
| GET    | `/api/todos`      | 获取所有待办 | -                                 | `[{id, title, description, completed, createdAt, updatedAt}]` |
| POST   | `/api/todos`      | 创建待办     | `{title, description}`            | `{id, title, description, completed, createdAt, updatedAt}`   |
| PUT    | `/api/todos/{id}` | 更新待办     | `{title, description, completed}` | `{id, title, description, completed, createdAt, updatedAt}`   |
| DELETE | `/api/todos/{id}` | 删除待办     | -                                 | `204 No Content`                                              |

## 下一步扩展建议

完成基础功能后，可以尝试以下扩展：

### 功能扩展

- 添加用户认证（Spring Security + JWT）
- 实现待办分类/标签功能
- 添加截止日期和优先级
- 实现待办搜索和过滤

### 技术提升

- 添加单元测试（JUnit + Mockito）
- 使用 Spring Validation 进行参数校验
- 实现全局异常处理（@ControllerAdvice）
- 添加日志记录（Logback/SLF4J）
- 使用 DTO 模式分离数据层和展示层

### 部署相关

- Docker 容器化部署
- 使用 Docker Compose 管理多容器应用
- 部署到云平台（AWS、阿里云等）

## 常见问题排查

### 后端启动失败

- 检查 MySQL 服务是否运行
- 确认数据库配置（用户名、密码、数据库名）
- 查看端口 8080 是否被占用

### 前端无法连接后端

- 确认后端已启动且运行在 8080 端口
- 检查 CORS 配置是否正确
- 查看浏览器控制台的网络请求错误

### 数据库连接错误

- 确认 MySQL 时区设置：`serverTimezone=UTC`
- 检查数据库用户权限
- 验证数据库是否已创建

## 学习资源

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Spring Data JPA 指南](https://spring.io/guides/gs/accessing-data-jpa/)
- [RESTful API 设计最佳实践](https://restfulapi.net/)
- [React 官方文档](https://react.dev/)

---

祝你学习愉快！有问题随时问我 🚀
