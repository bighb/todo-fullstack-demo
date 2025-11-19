package com.demo.todo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Todo 实体类 - 对应数据库中的 todos 表
 *
 * 实体类是 ORM（对象关系映射）的核心，它定义了 Java 对象与数据库表之间的映射关系。
 * JPA（Java Persistence API）会根据这个类自动生成对应的数据库表结构。
 *
 * Lombok 注解说明：
 * @Data: 自动生成 getter、setter、toString、equals、hashCode 方法
 * @NoArgsConstructor: 生成无参构造函数（JPA 要求实体类必须有无参构造函数）
 * @AllArgsConstructor: 生成包含所有字段的构造函数
 *
 * JPA 注解说明：
 * @Entity: 标识这是一个 JPA 实体类，会被持久化到数据库
 * @Table: 指定对应的数据库表名，如果不指定则默认使用类名
 */
@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    /**
     * 主键字段
     *
     * @Id: 标识这是主键字段
     * @GeneratedValue: 主键生成策略
     *   - IDENTITY: 使用数据库的自增机制（MySQL 推荐）
     *   - SEQUENCE: 使用数据库序列（PostgreSQL/Oracle）
     *   - AUTO: 由 JPA 自动选择策略
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 待办事项标题
     *
     * @Column: 定义列的属性
     *   - nullable = false: 不允许为空，对应数据库的 NOT NULL 约束
     *   - 其他常用属性：length（长度）、unique（唯一）、columnDefinition（列定义）
     */
    @Column(nullable = false)
    private String title;

    /**
     * 待办事项描述（可选字段）
     *
     * 没有 @Column 注解时，JPA 会使用默认配置：
     * - 列名与字段名相同
     * - 允许为空
     * - 字符串类型默认长度 255
     */
    private String description;

    /**
     * 完成状态
     *
     * 使用 Boolean 包装类而非 boolean 基本类型，可以区分 null 和 false
     * 默认值为 false，表示新创建的待办事项未完成
     */
    @Column(nullable = false)
    private Boolean completed = false;

    /**
     * 创建时间
     *
     * @CreationTimestamp: Hibernate 特有注解，在插入记录时自动设置当前时间
     * @Column(updatable = false): 创建后不允许更新，保持记录的原始创建时间
     *
     * LocalDateTime: Java 8 引入的日期时间类，比 Date 更易用
     */
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     *
     * @UpdateTimestamp: Hibernate 特有注解，每次更新记录时自动设置当前时间
     * 用于跟踪记录的最后修改时间
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
