package com.demo.todo.repository;

import com.demo.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Todo 数据访问层（Repository）
 *
 * Repository 是数据访问层的抽象，负责与数据库交互。
 * Spring Data JPA 通过接口继承的方式，自动实现常用的 CRUD 操作，无需编写实现类。
 *
 * @Repository: 标识这是一个数据访问组件
 *   - Spring 会自动创建它的实现类并注册为 Bean
 *   - 会自动处理数据库异常，转换为 Spring 的 DataAccessException
 *
 * JpaRepository<Todo, Long> 泛型参数：
 *   - Todo: 实体类类型
 *   - Long: 主键类型
 *
 * 继承 JpaRepository 后自动获得的方法：
 *   - save(entity): 保存或更新实体
 *   - findById(id): 根据 ID 查询
 *   - findAll(): 查询所有
 *   - delete(entity): 删除实体
 *   - count(): 统计数量
 *   - existsById(id): 判断是否存在
 *   等等...
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 根据完成状态查询待办事项
     *
     * 这是 Spring Data JPA 的「方法名查询」特性：
     * - 方法名以 findBy 开头，后面跟字段名
     * - JPA 会自动解析方法名并生成对应的 SQL 查询
     * - findByCompleted(true) 等价于: SELECT * FROM todos WHERE completed = true
     *
     * 常用的方法名关键字：
     * - findBy: 查询
     * - countBy: 统计
     * - deleteBy: 删除
     * - And/Or: 多条件组合
     * - OrderBy: 排序
     * - Like/Contains: 模糊匹配
     * - Between: 范围查询
     * - GreaterThan/LessThan: 比较
     *
     * @param completed 完成状态
     * @return 符合条件的待办事项列表
     */
    List<Todo> findByCompleted(Boolean completed);

    /**
     * 查询所有待办事项，按创建时间降序排列
     *
     * 方法名解析：
     * - findAll: 查询所有
     * - By: 分隔符（这里没有条件）
     * - OrderBy: 排序
     * - CreatedAt: 排序字段
     * - Desc: 降序（Asc 为升序）
     *
     * 生成的 SQL: SELECT * FROM todos ORDER BY created_at DESC
     *
     * @return 按创建时间降序排列的待办事项列表
     */
    List<Todo> findAllByOrderByCreatedAtDesc();
}
