# 缓存：整合SpringDataRedis

## 简介

SpringDataRedis简化了Redis的冗余代码和样板代码，为用户提供了低级和高级的抽象

## 示例

1. 添加依赖 spring-data-redis

```xml
 <dependencies>
        <dependency>  
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-redis</artifactId>
            <version>2.4.5</version>
        </dependency>
</dependencies>
```

2. 配置连接器 Lettuce 连接器

```java
@Configuration
public class MyConfig {
    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("ip", 6379);
        configuration.setPassword("password");
        return new LettuceConnectionFactory(configuration);
    }
}
```

3. 存取数据

```java
@RestController
public class DemoController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;

    @GetMapping("/test")
    public String test(){
        listOps.leftPush("name", "jalon");
        System.out.println(listOps.leftPop("name"));
        return "success";
    }

}
```

**知识点**

- @Resource注解：类似@Autowired，区别如下
  - @Autowired根据类型注入
  - @Resource根据名称注入，一般用在setter方法上，符合面向对象思想（不直接操作属性）

## 参考

- [SpringDataRedis官网](https://docs.spring.io/spring-data/redis/docs/2.4.5/reference/html/#redis)

- [@Resource注解](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-resource-annotation)