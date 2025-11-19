package com.demo.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用启动类
 *
 * 这是整个应用的入口点，相当于传统 Java 应用的 main 方法所在类。
 *
 * @SpringBootApplication 是一个组合注解，包含：
 * - @SpringBootConfiguration: 标识这是一个配置类
 * - @EnableAutoConfiguration: 开启自动配置，Spring Boot 会根据依赖自动配置应用
 * - @ComponentScan: 组件扫描，自动发现和注册当前包及子包下的 @Component、@Service、@Repository、@Controller 等
 */
@SpringBootApplication
public class TodoApplication {

    /**
     * 应用入口方法
     *
     * SpringApplication.run() 会：
     * 1. 创建 Spring 应用上下文（ApplicationContext）
     * 2. 扫描并注册所有 Bean
     * 3. 启动内嵌的 Tomcat 服务器
     * 4. 执行自动配置
     *
     * @param args 命令行参数，可以通过它覆盖配置，如 --server.port=9090
     */
    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }
}
