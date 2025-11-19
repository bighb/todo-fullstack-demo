# 扩展优化清单

本文档列出 Todo List 项目后续可扩展的功能和知识点，按优先级排序，方便根据学习进度逐步迭代完善。

---

## 一、高优先级（实际开发必备）

这些是实际项目开发中几乎必须具备的基础能力。

### 1. 全局异常处理

**简介**：统一处理应用中的各种异常，返回标准化的错误响应。

**学习目标**：
- 理解 Spring 的异常处理机制
- 掌握全局异常捕获的实现方式
- 学会自定义业务异常

**核心概念**：
- `@ControllerAdvice`：全局控制器增强，可以处理所有 Controller 的异常
- `@ExceptionHandler`：指定处理特定类型的异常
- 自定义异常类：如 `ResourceNotFoundException`、`BusinessException`
- 异常响应体：包含错误码、错误消息、时间戳等

**相关注解**：
- `@ControllerAdvice`
- `@RestControllerAdvice`
- `@ExceptionHandler`
- `@ResponseStatus`

---

### 2. 参数校验

**简介**：对请求参数进行自动校验，确保数据有效性。

**学习目标**：
- 掌握 Bean Validation 规范
- 学会使用常用校验注解
- 理解校验失败的处理流程

**核心概念**：
- JSR-380 (Bean Validation 2.0)：Java 的参数校验规范
- Hibernate Validator：JSR-380 的实现
- 校验分组：不同场景使用不同的校验规则
- 嵌套校验：校验对象中的嵌套对象

**相关注解**：
- `@Valid`、`@Validated`：触发校验
- `@NotNull`、`@NotBlank`、`@NotEmpty`：非空校验
- `@Size`、`@Min`、`@Max`：长度/大小校验
- `@Email`、`@Pattern`：格式校验
- `@Past`、`@Future`：日期校验

**相关依赖**：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

### 3. 统一响应格式

**简介**：封装统一的 API 响应结构，便于前端处理。

**学习目标**：
- 设计标准的响应数据结构
- 实现响应包装器
- 处理成功和失败两种情况

**核心概念**：
- `ApiResponse<T>`：泛型响应包装类
- 响应结构：code（状态码）、message（消息）、data（数据）、timestamp（时间戳）
- 成功响应与错误响应的区分
- 与全局异常处理配合使用

**设计示例**：
```json
// 成功响应
{
  "code": 200,
  "message": "success",
  "data": { ... },
  "timestamp": "2024-01-01T12:00:00"
}

// 错误响应
{
  "code": 400,
  "message": "参数校验失败",
  "errors": ["title 不能为空"],
  "timestamp": "2024-01-01T12:00:00"
}
```

---

### 4. 日志记录

**简介**：记录应用运行日志，便于调试和问题排查。

**学习目标**：
- 理解日志级别和使用场景
- 掌握 SLF4J + Logback 的使用
- 学会配置日志输出格式和文件

**核心概念**：
- 日志级别：TRACE < DEBUG < INFO < WARN < ERROR
- SLF4J：日志门面（接口）
- Logback：日志实现（Spring Boot 默认）
- MDC：用于追踪请求链路
- 日志切面：使用 AOP 记录方法调用日志

**配置要点**：
- 控制台输出格式
- 文件输出（按日期/大小滚动）
- 不同包设置不同日志级别
- 生产环境关闭 DEBUG 日志

**相关依赖**：Spring Boot 已内置，无需额外依赖

---

## 二、中优先级（进阶提升）

这些知识点能提升代码质量和开发效率。

### 5. DTO 模式

**简介**：使用数据传输对象分离内部实体和外部接口。

**学习目标**：
- 理解 DTO 模式的作用和优势
- 掌握 Entity 与 DTO 的转换方式
- 学会为不同场景设计不同的 DTO

**核心概念**：
- DTO（Data Transfer Object）：数据传输对象
- VO（View Object）：视图对象（有时与 DTO 混用）
- 请求 DTO：`CreateTodoRequest`、`UpdateTodoRequest`
- 响应 DTO：`TodoResponse`、`TodoListResponse`
- 对象映射：手动转换 / MapStruct / ModelMapper

**优势**：
- 控制暴露的字段（安全性）
- 前后端解耦（灵活性）
- 支持不同场景的数据结构
- 避免实体类被 JSON 序列化污染

---

### 6. 分页查询

**简介**：实现大数据量的分页查询功能。

**学习目标**：
- 掌握 Spring Data JPA 的分页支持
- 学会处理分页参数和返回分页结果
- 了解排序功能的实现

**核心概念**：
- `Pageable`：分页参数接口
- `PageRequest`：分页参数实现
- `Page<T>`：分页结果
- `Sort`：排序条件
- 页码 vs 偏移量分页

**接口示例**：
```
GET /api/todos?page=0&size=10&sort=createdAt,desc
```

**返回结构**：
```json
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10,
  "number": 0,
  "size": 10
}
```

---

### 7. API 文档（Swagger/OpenAPI）

**简介**：自动生成 API 接口文档，便于前后端协作。

**学习目标**：
- 集成 Swagger/OpenAPI 3.0
- 学会使用注解描述接口
- 了解文档 UI 的使用

**核心概念**：
- OpenAPI 3.0：API 描述规范
- Swagger UI：可视化文档界面
- Springdoc：Spring Boot 3.x 推荐的文档库
- 接口分组和标签

**相关注解**：
- `@Tag`：接口分组
- `@Operation`：接口描述
- `@Parameter`：参数描述
- `@Schema`：模型描述
- `@ApiResponse`：响应描述

**相关依赖**：
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

**访问地址**：`http://localhost:8080/swagger-ui.html`

---

### 8. 单元测试

**简介**：为业务代码编写自动化测试，保证代码质量。

**学习目标**：
- 理解单元测试的基本概念
- 掌握 JUnit 5 的使用
- 学会使用 Mockito 模拟依赖
- 了解 Spring Boot 测试支持

**核心概念**：
- 单元测试 vs 集成测试
- AAA 模式：Arrange-Act-Assert
- Mock 对象：模拟依赖的行为
- 测试切片：`@WebMvcTest`、`@DataJpaTest`

**测试层次**：
- Repository 层测试：`@DataJpaTest`
- Service 层测试：`@ExtendWith(MockitoExtension.class)`
- Controller 层测试：`@WebMvcTest` + MockMvc

**相关注解**：
- `@Test`：标识测试方法
- `@BeforeEach`、`@AfterEach`：测试前后执行
- `@Mock`、`@InjectMocks`：Mockito 注解
- `@SpringBootTest`：完整集成测试

**相关依赖**：Spring Boot Starter Test 已包含 JUnit 5 和 Mockito

---

## 三、低优先级（可选扩展）

这些是更高级的主题，可以在掌握基础后选择性学习。

### 9. Spring Security 基础

**简介**：实现用户认证和授权功能。

**学习目标**：
- 理解认证（Authentication）和授权（Authorization）
- 掌握 Spring Security 的基本配置
- 学会 JWT Token 的使用

**核心概念**：
- SecurityFilterChain：安全过滤器链
- UserDetailsService：用户信息加载
- PasswordEncoder：密码加密
- JWT：无状态的身份验证令牌
- RBAC：基于角色的访问控制

**功能扩展**：
- 用户注册/登录接口
- Token 刷新机制
- 接口权限控制
- 当前用户信息获取

**相关依赖**：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
```

---

### 10. Actuator 监控

**简介**：监控应用运行状态，提供健康检查和指标数据。

**学习目标**：
- 理解应用监控的重要性
- 掌握 Actuator 的基本使用
- 了解常用的监控端点

**核心概念**：
- Endpoint：监控端点
- Health Indicator：健康指示器
- Metrics：应用指标
- Info：应用信息

**常用端点**：
- `/actuator/health`：健康检查
- `/actuator/info`：应用信息
- `/actuator/metrics`：性能指标
- `/actuator/env`：环境变量
- `/actuator/loggers`：日志级别管理

**相关依赖**：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**安全注意**：生产环境需要限制端点访问权限

---

## 四、实施建议

### 推荐实施顺序

1. **第一阶段**：全局异常处理 + 参数校验 + 统一响应格式
   - 这三个功能相互配合，建议一起实现
   - 实现后可以大幅提升 API 的规范性

2. **第二阶段**：日志记录 + DTO 模式
   - 日志对于调试非常重要
   - DTO 模式是架构优化的基础

3. **第三阶段**：分页查询 + API 文档
   - 分页是实际项目的常见需求
   - API 文档便于前后端协作

4. **第四阶段**：单元测试
   - 测试是保证代码质量的重要手段
   - 建议在功能稳定后补充测试

5. **第五阶段**：Security + Actuator
   - 这些是生产环境需要的功能
   - 可以根据实际需求选择性实现

### 每个功能的实施步骤

1. 阅读相关文档和教程
2. 添加必要的依赖
3. 编写核心代码
4. 测试功能是否正常
5. 更新 DEVELOPMENT_GUIDE.md 添加说明
6. 提交代码并记录变更

---

## 五、学习资源

### 官方文档
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Bean Validation Specification](https://beanvalidation.org/2.0/spec/)

### 推荐教程
- Baeldung Spring 系列教程
- Spring 官方 Guides
- 掘金/思否 Spring Boot 专栏

---

祝学习顺利！
