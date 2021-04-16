# helloworld项目
SpringBoot经典入门案例
## 知识点
**注解：**
@SpringBootApplication注解
@RestController注解
@GetMappint注解
@RequestParam注解

**工具：hutool**
StrUtil.isBlank() // 是否为空白字符（包括不可显示字符，比如/r/n)
StrUtil.format() // 格式化输出，默认占位符为{}

**yml配置：**

```yaml
server:
  port: 8080
  servlet:
    context-path: "/demo" # 请求根路径
```

**maven配置：**

继承和聚合

## 代码

**主程序** `HelloworldApplication.java`

```java
package com.jalon.helloworld;


import cn.hutool.core.util.StrUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HelloworldApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }
    @GetMapping("hello")
    public String hello(@RequestParam(required = false, name = "name") String name){
        if(StrUtil.isBlank(name)){
            name = "javalover";
        }
        String template = "传递的参数为：{}";
        return StrUtil.format(template, name);
    }
}

```

**配置文件**`pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo</artifactId>
        <groupId>com.jalon</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
	
    <artifactId>helloworld</artifactId>
    <!-- 属性 properties 从父模块继承: 比如<java.version>1.8</java.version> -->
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- 版本在父模块中进行管理 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
    </dependencies>
</project>
```

**资源配置文件** `application.yml`

```yaml
server:
  port: 8080
  servlet:
    context-path: "/demo"
```

