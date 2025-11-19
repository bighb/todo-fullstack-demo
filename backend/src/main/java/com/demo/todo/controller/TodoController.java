package com.demo.todo.controller;

import com.demo.todo.model.Todo;
import com.demo.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo REST API 控制器
 *
 * Controller 是 MVC 架构中的 C，负责：
 * - 接收 HTTP 请求
 * - 解析请求参数
 * - 调用 Service 层处理业务
 * - 返回 HTTP 响应
 *
 * @RestController: 组合注解，等价于 @Controller + @ResponseBody
 *   - @Controller: 标识这是一个控制器组件
 *   - @ResponseBody: 方法返回值直接作为响应体（自动转换为 JSON）
 *
 * @RequestMapping("/api/todos"): 定义基础路径
 *   - 所有方法的路径都会加上这个前缀
 *   - 例如：GET /api/todos, POST /api/todos, DELETE /api/todos/1
 *
 * @CrossOrigin: 配置跨域访问
 *   - origins: 允许的来源（前端地址）
 *   - 浏览器的同源策略会阻止不同源的请求，CORS 用于解决这个问题
 *   - 开发环境前端运行在 5173 端口，后端在 8080 端口，属于跨域
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:5173")
public class TodoController {

    /**
     * 注入 TodoService 依赖
     */
    @Autowired
    private TodoService todoService;

    /**
     * 获取所有待办事项
     *
     * @GetMapping: 处理 GET 请求
     *   - 没有指定路径，所以使用类上的基础路径 /api/todos
     *   - GET 请求用于获取资源，不应该有副作用
     *
     * ResponseEntity: Spring 提供的响应包装类
     *   - 可以设置状态码、响应头、响应体
     *   - ResponseEntity.ok() 返回 200 状态码
     *   - 泛型 <List<Todo>> 表示响应体的类型
     *
     * 请求示例：GET http://localhost:8080/api/todos
     * 响应示例：[{"id":1,"title":"学习Spring Boot","completed":false,...}]
     *
     * @return 包含所有待办事项的响应
     */
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.findAll());
    }

    /**
     * 根据 ID 获取单个待办事项
     *
     * @GetMapping("/{id}"): 处理带路径变量的 GET 请求
     *   - {id} 是路径变量占位符
     *   - 实际路径如：/api/todos/1, /api/todos/42
     *
     * @PathVariable: 从 URL 路径中提取变量值
     *   - 自动将路径中的 {id} 映射到方法参数 id
     *   - 如果参数名与占位符名称不同，需要指定：@PathVariable("id") Long todoId
     *
     * 请求示例：GET http://localhost:8080/api/todos/1
     *
     * @param id 待办事项 ID（从 URL 路径获取）
     * @return 包含单个待办事项的响应
     */
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    /**
     * 创建新的待办事项
     *
     * @PostMapping: 处理 POST 请求
     *   - POST 用于创建新资源
     *   - 请求体包含要创建的数据
     *
     * @RequestBody: 将请求体的 JSON 自动反序列化为 Java 对象
     *   - Spring 使用 Jackson 库进行 JSON 转换
     *   - 请求头需要设置 Content-Type: application/json
     *
     * HttpStatus.CREATED (201): 表示资源创建成功
     *   - 比 200 OK 更准确地表达了操作结果
     *
     * 请求示例：
     *   POST http://localhost:8080/api/todos
     *   Content-Type: application/json
     *   Body: {"title":"学习JPA","description":"了解实体映射"}
     *
     * @param todo 待办事项对象（从请求体解析）
     * @return 包含创建结果的响应（201 状态码）
     */
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(todoService.save(todo));
    }

    /**
     * 更新待办事项
     *
     * @PutMapping("/{id}"): 处理 PUT 请求
     *   - PUT 用于完整更新资源（替换）
     *   - PATCH 用于部分更新（本项目简化处理，使用 PUT）
     *
     * 同时使用 @PathVariable 和 @RequestBody：
     *   - @PathVariable: 从 URL 获取要更新的资源 ID
     *   - @RequestBody: 从请求体获取更新内容
     *
     * 请求示例：
     *   PUT http://localhost:8080/api/todos/1
     *   Content-Type: application/json
     *   Body: {"title":"更新后的标题","completed":true}
     *
     * @param id 待办事项 ID
     * @param todoDetails 更新内容
     * @return 包含更新结果的响应
     */
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        return ResponseEntity.ok(todoService.update(id, todoDetails));
    }

    /**
     * 删除待办事项
     *
     * @DeleteMapping("/{id}"): 处理 DELETE 请求
     *   - DELETE 用于删除资源
     *
     * ResponseEntity<Void>: 响应体为空
     *   - 删除操作通常不需要返回数据
     *
     * ResponseEntity.noContent(): 返回 204 No Content
     *   - 表示操作成功，但没有内容返回
     *   - .build() 构建最终的 ResponseEntity 对象
     *
     * 请求示例：DELETE http://localhost:8080/api/todos/1
     * 响应：204 No Content（无响应体）
     *
     * @param id 待办事项 ID
     * @return 空响应（204 状态码）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
