# SpringBoot + React Todo List 开发指南

本指南旨在帮助你通过这个 Todo List 项目学习 SpringBoot 和 Java 后端开发的核心知识。重点介绍技术概念和开发流程，不包含具体代码实现。

---

## 一、Java 开发基础

### 1.1 JDK 与开发环境

**JDK（Java Development Kit）** 是 Java 开发工具包，包含了编译器、运行时环境和核心类库。本项目需要 JDK 17 或更高版本。

**环境变量配置**：
- `JAVA_HOME`：指向 JDK 安装目录
- `PATH`：包含 JDK 的 bin 目录

**版本选择建议**：
- JDK 17 是长期支持版本（LTS），生产环境首选
- JDK 21 是最新 LTS 版本，包含更多新特性

### 1.2 Maven 构建工具

**Maven** 是 Java 项目的构建和依赖管理工具，类似于前端的 npm/pnpm。

**核心概念**：
- **pom.xml**：项目对象模型文件，定义项目信息、依赖和构建配置
- **依赖管理**：自动下载和管理项目所需的第三方库
- **生命周期**：定义了 clean、compile、test、package、install、deploy 等标准构建阶段
- **仓库**：中央仓库存储公共依赖，本地仓库缓存已下载的依赖

**常用命令**：
- `mvn clean`：清理构建产物
- `mvn compile`：编译源代码
- `mvn test`：运行测试
- `mvn package`：打包项目
- `mvn spring-boot:run`：运行 SpringBoot 应用

### 1.3 Java 项目结构

**标准 Maven 项目结构**：
- `src/main/java`：Java 源代码
- `src/main/resources`：配置文件和静态资源
- `src/test/java`：测试代码
- `target`：编译输出目录

**包（Package）命名规范**：
- 通常采用反向域名格式：`com.公司名.项目名.模块名`
- 例如：`com.demo.todo.controller`

---

## 二、Spring Boot 核心概念

### 2.1 什么是 Spring Boot

**Spring Boot** 是基于 Spring 框架的快速开发脚手架，它的核心理念是「约定优于配置」。

**主要特点**：
- **自动配置**：根据类路径中的依赖自动配置 Spring 应用
- **起步依赖（Starter）**：预定义的依赖集合，简化依赖管理
- **内嵌服务器**：内置 Tomcat/Jetty，无需外部部署
- **生产就绪**：提供健康检查、指标监控等功能

**与 Spring Framework 的关系**：
- Spring Boot 不是替代 Spring，而是简化 Spring 应用的创建和配置
- 底层仍然使用 Spring Framework 的核心功能

### 2.2 IoC 与依赖注入（DI）

**IoC（Inversion of Control，控制反转）** 是 Spring 的核心思想，将对象的创建和管理权交给 Spring 容器。

**依赖注入（Dependency Injection）** 是 IoC 的实现方式：
- 对象不再自己创建依赖，而是由容器注入
- 降低了组件间的耦合度
- 便于单元测试和代码维护

**注入方式**：
- **构造器注入**：通过构造函数注入依赖（推荐）
- **Setter 注入**：通过 setter 方法注入
- **字段注入**：直接在字段上使用 @Autowired（简单但不推荐）

**Bean 的概念**：
- Bean 是由 Spring 容器管理的对象
- 默认是单例模式（Singleton）
- 通过注解或配置文件声明

### 2.3 Spring Boot 自动配置原理

**自动配置的工作流程**：
1. 扫描类路径中的依赖
2. 根据条件注解判断是否需要配置
3. 自动创建和配置相关 Bean

**条件注解**：
- `@ConditionalOnClass`：类路径存在某个类时生效
- `@ConditionalOnMissingBean`：不存在某个 Bean 时生效
- `@ConditionalOnProperty`：配置属性满足条件时生效

**配置优先级**：
- 代码中的显式配置优先于自动配置
- application.properties/yaml 可以覆盖默认配置

### 2.4 三层架构

Spring Boot 应用通常采用分层架构，每层有明确的职责：

**Controller 层（控制层）**：
- 接收 HTTP 请求
- 参数校验和转换
- 调用 Service 层处理业务
- 返回响应结果

**Service 层（业务逻辑层）**：
- 实现核心业务逻辑
- 事务管理
- 调用 Repository 层访问数据
- 可以调用其他 Service

**Repository 层（数据访问层）**：
- 与数据库交互
- 执行 CRUD 操作
- 封装数据访问细节

**分层的好处**：
- 职责分离，代码结构清晰
- 便于维护和测试
- 支持代码复用
- 可以独立替换某一层的实现

### 2.5 常用注解详解

**启动类注解**：
- `@SpringBootApplication`：组合注解，包含以下三个：
  - `@SpringBootConfiguration`：标识配置类
  - `@EnableAutoConfiguration`：开启自动配置
  - `@ComponentScan`：组件扫描

**组件注解**：
- `@Component`：通用组件
- `@Controller`：控制器组件（处理 HTTP 请求）
- `@RestController`：RESTful 控制器（@Controller + @ResponseBody）
- `@Service`：业务逻辑组件
- `@Repository`：数据访问组件

**依赖注入注解**：
- `@Autowired`：自动注入依赖
- `@Qualifier`：指定注入的 Bean 名称
- `@Value`：注入配置值

**Web 相关注解**：
- `@RequestMapping`：映射请求路径
- `@GetMapping`、`@PostMapping`、`@PutMapping`、`@DeleteMapping`：HTTP 方法映射
- `@PathVariable`：获取路径变量
- `@RequestBody`：获取请求体
- `@RequestParam`：获取查询参数
- `@ResponseBody`：将返回值序列化为 JSON

---

## 三、Spring Data JPA

### 3.1 ORM 概念

**ORM（Object-Relational Mapping，对象关系映射）** 是一种编程技术，用于在面向对象语言和关系型数据库之间建立映射。

**核心思想**：
- Java 类对应数据库表
- 类的属性对应表的列
- 类的实例对应表的行

**优势**：
- 面向对象的方式操作数据库
- 自动处理 SQL 语句生成
- 数据库无关性，易于切换数据库

**JPA（Java Persistence API）** 是 Java 的 ORM 规范，Hibernate 是最流行的实现。

### 3.2 实体映射

**实体（Entity）** 是与数据库表对应的 Java 类。

**常用映射注解**：
- `@Entity`：标识实体类
- `@Table`：指定表名
- `@Id`：标识主键
- `@GeneratedValue`：主键生成策略
- `@Column`：列映射配置
- `@CreationTimestamp`：自动填充创建时间
- `@UpdateTimestamp`：自动填充更新时间

**主键生成策略**：
- `IDENTITY`：数据库自增（MySQL 推荐）
- `SEQUENCE`：序列（PostgreSQL/Oracle）
- `AUTO`：由 JPA 自动选择
- `UUID`：生成 UUID

**字段类型映射**：
- String → VARCHAR
- Integer/Long → INT/BIGINT
- Boolean → TINYINT/BOOLEAN
- LocalDateTime → DATETIME/TIMESTAMP

### 3.3 Repository 模式

**Spring Data JPA** 提供了 Repository 接口，极大简化了数据访问层的开发。

**JpaRepository 接口**：
- 继承自 CrudRepository 和 PagingAndSortingRepository
- 提供了完整的 CRUD 方法
- 支持分页和排序

**内置方法**：
- `findAll()`：查询所有
- `findById()`：根据 ID 查询
- `save()`：保存或更新
- `delete()`：删除
- `count()`：统计数量

**方法名查询（Query Methods）**：
- Spring Data JPA 可以根据方法名自动生成查询
- 例如：`findByTitle(String title)` 自动生成 `WHERE title = ?`
- 支持 And、Or、Between、Like、OrderBy 等关键字

**自定义查询**：
- `@Query`：使用 JPQL 或原生 SQL
- `@Modifying`：标识更新或删除操作

### 3.4 数据库配置

**application.properties 配置项**：

**数据源配置**：
- `spring.datasource.url`：数据库连接 URL
- `spring.datasource.username`：用户名
- `spring.datasource.password`：密码
- `spring.datasource.driver-class-name`：驱动类

**JPA/Hibernate 配置**：
- `spring.jpa.hibernate.ddl-auto`：DDL 生成策略
  - `none`：不执行任何操作
  - `validate`：验证表结构
  - `update`：自动更新表结构（开发常用）
  - `create`：每次启动创建表
  - `create-drop`：启动创建，关闭删除
- `spring.jpa.show-sql`：显示 SQL 语句
- `spring.jpa.properties.hibernate.format_sql`：格式化 SQL

---

## 四、REST API 开发

### 4.1 RESTful 设计原则

**REST（Representational State Transfer）** 是一种软件架构风格，用于设计网络应用的 API。

**核心原则**：
- **资源导向**：URI 表示资源，而非动作
- **统一接口**：使用标准 HTTP 方法
- **无状态**：每个请求包含所有必要信息
- **分层系统**：客户端无法判断是否直连服务器

**URI 设计规范**：
- 使用名词复数：`/api/todos`（而非 `/api/getTodos`）
- 使用小写字母和连字符
- 避免文件扩展名
- 层级关系用路径表示：`/api/users/{id}/todos`

### 4.2 HTTP 方法与状态码

**HTTP 方法对应 CRUD 操作**：
- `GET`：读取资源（Read）
- `POST`：创建资源（Create）
- `PUT`：完整更新资源（Update）
- `PATCH`：部分更新资源
- `DELETE`：删除资源（Delete）

**常用状态码**：
- `200 OK`：请求成功
- `201 Created`：资源创建成功
- `204 No Content`：成功但无返回内容
- `400 Bad Request`：请求参数错误
- `401 Unauthorized`：未认证
- `403 Forbidden`：无权限
- `404 Not Found`：资源不存在
- `500 Internal Server Error`：服务器内部错误

### 4.3 ResponseEntity

**ResponseEntity** 是 Spring 提供的响应包装类，可以完整控制 HTTP 响应。

**可控制的内容**：
- 响应状态码
- 响应头
- 响应体

**使用场景**：
- 需要返回特定状态码
- 需要添加自定义响应头
- 需要更精细地控制响应

### 4.4 跨域配置（CORS）

**跨域问题**：浏览器的同源策略限制了不同源之间的请求。

**同源的定义**：协议、域名、端口都相同。

**CORS 解决方案**：
- `@CrossOrigin` 注解：在 Controller 或方法级别配置
- 全局配置：通过 WebMvcConfigurer 配置类
- Filter 方式：自定义过滤器

**CORS 配置项**：
- `origins`：允许的来源
- `methods`：允许的 HTTP 方法
- `allowedHeaders`：允许的请求头
- `maxAge`：预检请求缓存时间

---

## 五、配置安全与环境管理

### 5.1 application.properties 简介

**application.properties** 是 Spring Boot 的核心配置文件，位于 `src/main/resources` 目录下。

**主要用途**：
- 数据库连接配置
- 服务器端口设置
- 日志级别配置
- 第三方服务密钥
- 应用自定义参数

**配置格式**：
- `.properties` 格式：`key=value`
- `.yaml/.yml` 格式：层级结构，更易读

### 5.2 敏感信息的安全风险

**常见的敏感信息**：
- 数据库密码
- API 密钥（第三方服务）
- JWT 签名密钥
- OAuth 客户端密钥
- 云服务凭证（AWS、阿里云等）

**直接写入配置文件的风险**：
- 代码提交到 Git 仓库后，密码会被永久记录在历史中
- 公开仓库会导致密码泄露
- 即使是私有仓库，团队成员也能看到所有密码
- 不同环境（开发/测试/生产）需要不同的密码

**安全原则**：永远不要将明文密码提交到版本控制系统。

### 5.3 环境变量配置（推荐方案）

**基本语法**：
```
${环境变量名}           # 必须设置，否则启动失败
${环境变量名:默认值}    # 未设置时使用默认值
```

**示例配置**：
```properties
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
```

**设置环境变量的方式**：

**macOS/Linux**：
```bash
# 临时设置（当前终端有效）
export DB_PASSWORD=your_password

# 永久设置（添加到 ~/.zshrc 或 ~/.bashrc）
echo 'export DB_PASSWORD=your_password' >> ~/.zshrc
source ~/.zshrc
```

**Windows**：
```cmd
# 临时设置
set DB_PASSWORD=your_password

# 永久设置（通过系统环境变量设置）
```

**IDE 中配置**：
- VS Code：在 launch.json 中添加 env 配置
- IntelliJ IDEA：Run Configuration → Environment variables

### 5.4 Spring Profiles 多环境配置

**Profiles 机制**允许为不同环境定义不同的配置。

**配置文件命名**：
- `application.properties` - 通用配置
- `application-dev.properties` - 开发环境
- `application-test.properties` - 测试环境
- `application-prod.properties` - 生产环境

**激活 Profile**：
```bash
# 命令行参数
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 环境变量
export SPRING_PROFILES_ACTIVE=dev

# 在 application.properties 中指定
spring.profiles.active=dev
```

**配置继承**：
- 特定 Profile 的配置会覆盖通用配置
- 可以同时激活多个 Profile

### 5.5 .gitignore 配置

**应该忽略的文件**：
```gitignore
# 包含敏感信息的配置
application-local.properties
application-dev.properties
.env
.env.local

# IDE 配置
.idea/
.vscode/

# 构建产物
target/
node_modules/
```

**配置模板文件**：
- 提交 `application.properties.example` 作为模板
- 说明需要配置的环境变量
- 其他开发者复制模板并填入自己的配置

### 5.6 生产环境安全方案

**方案一：密钥管理服务**
- HashiCorp Vault
- AWS Secrets Manager
- Azure Key Vault
- 阿里云密钥管理服务

**方案二：Spring Cloud Config**
- 集中式配置管理
- 配置加密存储
- 动态刷新配置

**方案三：Kubernetes Secrets**
- 容器化部署时使用
- Secret 挂载为环境变量或文件

**方案四：配置加密**
- Jasypt 加密配置值
- Spring Cloud Config 加密

### 5.7 本项目的配置方式

本项目采用**环境变量**方式管理敏感信息：

**必须设置的环境变量**：
- `DB_PASSWORD`：数据库密码

**可选环境变量（有默认值）**：
- `DB_NAME`：数据库名称（默认：todo_db）
- `DB_USERNAME`：数据库用户名（默认：root）

**启动项目**：
```bash
# 设置环境变量
export DB_PASSWORD=your_password

# 启动应用
cd backend
mvn spring-boot:run
```

---

## 六、开发流程

### 6.1 环境准备

**第一步：验证开发环境**

确认以下工具已正确安装：
- JDK 17+：运行 Java 应用的基础
- Maven 3.6+：项目构建和依赖管理
- MySQL 8.0：数据存储
- Node.js 18+：前端运行环境
- pnpm：前端包管理器

**第二步：创建数据库**

1. 启动 MySQL 服务
2. 登录 MySQL 客户端
3. 创建项目数据库
4. 配置字符集为 utf8mb4

**第三步：配置项目**

1. 修改 application.properties 中的数据库连接信息
2. 设置正确的用户名和密码
3. 确认端口号没有冲突

### 6.2 后端开发顺序

按照以下顺序开发后端功能，这是 Spring Boot 项目的标准开发流程：

**第一步：定义实体类（Model）**

这是数据模型的核心：
- 分析业务需求，确定需要哪些字段
- 设计表结构和字段类型
- 添加 JPA 注解进行映射配置
- 考虑字段约束（非空、唯一等）

**第二步：创建 Repository 接口**

数据访问层的实现：
- 继承 JpaRepository 接口
- 根据业务需求定义自定义查询方法
- 利用方法名查询简化开发

**第三步：实现 Service 层**

业务逻辑的核心：
- 注入 Repository 依赖
- 实现 CRUD 业务方法
- 处理业务异常
- 添加必要的数据校验

**第四步：开发 Controller**

API 接口层：
- 定义 RESTful 路由
- 调用 Service 层方法
- 处理请求参数
- 返回标准响应格式

**第五步：测试 API**

验证接口功能：
- 使用 Postman 或 REST Client 测试
- 检查各个端点的响应
- 验证数据库数据变化
- 测试异常情况的处理

### 6.3 前端开发顺序（简略）

前端开发相对简单，主要步骤：

1. **创建 API 服务**：封装后端接口调用
2. **开发组件**：TodoForm、TodoItem、TodoList
3. **状态管理**：使用 React Hooks 管理数据
4. **样式美化**：添加 CSS 样式

### 6.4 联调测试

**启动顺序**：
1. 先启动 MySQL 数据库
2. 启动后端服务（8080 端口）
3. 启动前端服务（5173 端口）

**测试要点**：
- 创建待办：验证数据写入数据库
- 查询列表：验证数据正确显示
- 更新状态：验证完成状态切换
- 删除待办：验证数据从数据库删除
- 跨域请求：确认 CORS 配置正确

---

## 七、进阶学习路径

完成基础项目后，建议按以下路径深入学习：

### 7.1 异常处理

**全局异常处理**：
- 使用 `@ControllerAdvice` 统一处理异常
- 自定义业务异常类
- 返回标准错误响应格式

### 7.2 参数校验

**Bean Validation**：
- 使用 `@Valid` 触发校验
- 常用校验注解：`@NotNull`、`@Size`、`@Email` 等
- 自定义校验注解

### 7.3 DTO 模式

**数据传输对象（DTO）**：
- 分离内部实体和外部接口
- 控制数据暴露范围
- 支持不同场景的数据转换

### 7.4 日志记录

**SLF4J + Logback**：
- 日志级别：TRACE、DEBUG、INFO、WARN、ERROR
- 日志格式配置
- 日志文件输出

### 7.5 安全认证

**Spring Security**：
- 用户认证和授权
- JWT Token 机制
- 密码加密存储

### 7.6 单元测试

**JUnit 5 + Mockito**：
- Repository 层测试
- Service 层测试
- Controller 层测试（MockMvc）

---

## 八、常见问题排查

### 8.1 后端启动问题

**数据库连接失败**：
- 检查 MySQL 服务是否运行
- 确认连接 URL、用户名、密码正确
- 检查数据库是否存在

**端口被占用**：
- 查找占用端口的进程
- 修改 application.properties 中的端口配置

**依赖下载失败**：
- 检查网络连接
- 配置 Maven 镜像源（阿里云等）

### 8.2 API 测试问题

**404 Not Found**：
- 检查请求路径是否正确
- 确认 Controller 的 RequestMapping 配置

**500 Internal Server Error**：
- 查看控制台错误日志
- 检查数据库查询是否正确

### 8.3 前后端联调问题

**CORS 错误**：
- 检查 @CrossOrigin 注解配置
- 确认 origins 包含前端地址

**数据格式问题**：
- 检查请求体 JSON 格式
- 确认字段名与实体类一致

---

## 九、学习资源推荐

### 官方文档
- Spring Boot 官方文档
- Spring Data JPA 参考指南
- Maven 官方文档

### 在线教程
- Spring 官方指南系列
- Baeldung Spring 教程
- 掘金/思否 Spring Boot 专栏

### 书籍推荐
- 《Spring Boot 实战》
- 《Spring 实战（第 5 版）》
- 《Java 编程思想》

---

祝你学习顺利！
