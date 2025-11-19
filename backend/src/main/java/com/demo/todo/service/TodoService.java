package com.demo.todo.service;

import com.demo.todo.model.Todo;
import com.demo.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Todo 业务逻辑层（Service）
 *
 * Service 层是业务逻辑的核心，位于 Controller 和 Repository 之间。
 * 它的职责包括：
 * - 实现业务逻辑
 * - 事务管理
 * - 数据校验
 * - 调用一个或多个 Repository
 * - 可以调用其他 Service
 *
 * @Service: 标识这是一个业务逻辑组件
 *   - Spring 会自动创建实例并注册为 Bean
 *   - 默认是单例模式（整个应用只有一个实例）
 */
@Service
public class TodoService {

    /**
     * 注入 TodoRepository 依赖
     *
     * @Autowired: 自动注入依赖（依赖注入 DI 的核心）
     *   - Spring 容器会自动找到 TodoRepository 的实现并注入
     *   - 这种方式称为「字段注入」，简单但不推荐用于生产代码
     *   - 推荐使用「构造器注入」，更易于测试
     *
     * 依赖注入的好处：
     * - 解耦：Service 不需要知道 Repository 如何创建
     * - 可测试：测试时可以注入 Mock 对象
     * - 灵活：可以轻松切换实现
     */
    @Autowired
    private TodoRepository todoRepository;

    /**
     * 查询所有待办事项
     *
     * 调用 Repository 的自定义方法，按创建时间降序返回
     *
     * @return 待办事项列表
     */
    public List<Todo> findAll() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 根据 ID 查询待办事项
     *
     * findById() 返回 Optional<Todo>，这是 Java 8 引入的容器类，用于处理可能为空的值。
     *
     * orElseThrow(): 如果值存在则返回，否则抛出异常
     * - 这里使用 RuntimeException，实际项目中应该自定义业务异常
     * - Lambda 表达式 () -> new RuntimeException(...) 是异常的供应者
     *
     * @param id 待办事项 ID
     * @return 待办事项
     * @throws RuntimeException 如果找不到对应的待办事项
     */
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    /**
     * 保存新的待办事项
     *
     * JpaRepository.save() 方法的行为：
     * - 如果实体没有 ID（或 ID 为 null）：执行 INSERT
     * - 如果实体有 ID：执行 UPDATE
     *
     * @param todo 待办事项对象
     * @return 保存后的待办事项（包含生成的 ID 和时间戳）
     */
    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    /**
     * 更新待办事项
     *
     * 更新流程：
     * 1. 先根据 ID 查询出原有记录（如果不存在会抛出异常）
     * 2. 更新需要修改的字段
     * 3. 调用 save() 保存更改
     *
     * 注意：这里只更新了 title、description、completed 三个字段
     * id、createdAt 不会被修改，updatedAt 会自动更新
     *
     * @param id 待办事项 ID
     * @param todoDetails 包含更新内容的对象
     * @return 更新后的待办事项
     */
    public Todo update(Long id, Todo todoDetails) {
        // 先查询确保记录存在
        Todo todo = findById(id);
        // 更新字段
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.getCompleted());
        // 保存并返回
        return todoRepository.save(todo);
    }

    /**
     * 删除待办事项
     *
     * 删除流程：
     * 1. 先查询确保记录存在
     * 2. 执行删除操作
     *
     * 如果直接用 deleteById() 删除不存在的记录，不会报错但也不会有任何操作
     * 这里先查询再删除，可以在记录不存在时给出明确的错误提示
     *
     * @param id 待办事项 ID
     */
    public void delete(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }

    /**
     * 根据完成状态查询待办事项
     *
     * @param completed 完成状态
     * @return 符合条件的待办事项列表
     */
    public List<Todo> findByCompleted(Boolean completed) {
        return todoRepository.findByCompleted(completed);
    }
}
