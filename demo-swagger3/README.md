## 目录

- 前言：什么是Swagger
- 起步：（只需简单的3步）
  - 加载依赖
  - 添加注解@EnableOpenApi
  - 启动SpringBoot，访问Swagger后台界面
- 配置：基于Java的配置
- 注解：Swagger2 和 Swagger3做对比
- 源码：
- 问题：踩坑记录（后面再整理）

## 前言

**什么是Swagger：**

​	Swagger 是最流行的 API 开发工具，它遵循 OpenAPI Specification（OpenAPI 规范，也简称 OAS）。 

​	它最方便的地方就在于，API文档可以和服务端保持同步，即服务端更新一个接口，前端的API文档就可以实时更新，而且可以在线测试。

​	这样一来，Swagger就大大降低了前后端的沟通障碍，不用因为一个接口调不通而争论不休

> 之前用的看云文档，不过这种第三方的都需要手动维护，还是不太方便

## 起步

1. 加载依赖

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

2. 添加@EnableOpenApi注解

```java
@EnableOpenApi
@SpringBootApplication
public class SwaggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerApplication.class, args);
    }
}
```

3. 启动项目，访问"http://localhost:8080/swagger-ui/index.html"

![image-20210729112424407](D:\StudyData\github-project\tangyuanxueJava\【文章】\【SpringBoot】\知识点\后台接口文档管理Swagger3\主页.png)

这样一个简单的Swagger后台接口文档就搭建完成了；

下面我们说下配置和注解

## 配置

可以看到，上面那个界面中，默认显示了一个`basic-error-controller`接口分组，但是我们并没有写；

通过在项目中查找我们发现，SpringBoot内部确实有这样一个控制器类，如下所示：

![image-20210729113119350](D:\StudyData\github-project\tangyuanxueJava\【文章】\【SpringBoot】\知识点\后台接口文档管理Swagger3\BasicErrorController.png)

这说明Swagger默认的配置，会自动把@Controller控制器类添加到接口文档中

下面我们就自己配置一下，如下所示：

```java
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        // 配置OAS 3.0协议
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 查找有@Tag注解的类，并生成一个对应的分组；类下面的所有http请求方法，都会生成对应的API接口
            	// 通过这个配置，就可以将那些没有添加@Tag注解的控制器类排除掉
                .apis(RequestHandlerSelectors.withClassAnnotation(Tag.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GPS Doc")
                .description("GPS Doc文档")
                .termsOfServiceUrl("http://www.javalover.com")
                .contact(new Contact("javalover", "http://www.javalover.cn", "1121263265@qq.com"))
                .version("2.0.0")
                .build();
    }

}
```

这样上面那个`basic-error-controller`就看不见了

## 注解

我们先看下Swagger2中的注解，如下所示：

- @Api：用在控制器类上，表示对类的说明    
  - tags="说明该类的作用，可以在UI界面上看到的说明信息的一个好用注解"
  - value="该参数没什么意义，在UI界面上也看到，所以不需要配置"

- @ApiOperation：用在请求的方法上，说明方法的用途、作用
  - value="说明方法的用途、作用"
  - notes="方法的备注说明"

- @ApiImplicitParams：用在请求的方法上，表示一组参数说明
  - @ApiImplicitParam：用在@ApiImplicitParams注解中，指定一个请求参数的各个方面（标注一个指定的参数，详细概括参数的各个方面，例如：参数名是什么？参数意义，是否必填等）
    - name：属性值为方法参数名
    - value：参数意义的汉字说明、解释
    - required：参数是否必须传
    - paramType：参数放在哪个地方
    - header --> 请求参数的获取：@RequestHeader
    - query --> 请求参数的获取：@RequestParam
    - path（用于restful接口）--> 请求参数的获取：@PathVariable
    - dataType：参数类型，默认String，其它值dataType="Integer"       
    - defaultValue：参数的默认值

- @ApiResponses：用在请求的方法上，表示一组响应
  - @ApiResponse：用在@ApiResponses中，一般用于表达一个错误的响应信息
    - code：状态码数字，例如400
    - message：信息，例如"请求参数没填好"
    - response：抛出异常的类

- @ApiModel：用于响应类上（POJO实体类），描述一个返回响应数据的信息（描述POJO类请求或响应的实体说明）
              （这种一般用在post接口的时候，使用@RequestBody接收JSON格式的数据的场景，请求参数无法使用@ApiImplicitParam注解进行描述的时候）
  - @ApiModelProperty：用在POJO属性上，描述响应类的属性说明
- @ApiIgnore：使用该注解忽略这个某个API或者参数；

上面这些是Swagger2的注解，下面我们看下Swagger3和它的简单对比

![Swagger3注解](https://i.loli.net/2021/07/29/s62vJN5XLKdugER.png)

接下来我们就用Swagger3的注解来写一个接口看下效果(其中穿插了Swagger2的注解)

- 控制器UserController.java

```java
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Tag(name = "user-controller", description = "用户接口")
@RestController
public class UserController {

    // 忽略这个api
    @Operation(hidden = true)
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @Operation(summary = "用户接口 - 获取用户详情")
    @GetMapping("/user/detail")
    // 这里的@Parameter也可以不加，Swagger会自动识别到这个name参数
    // 但是加@Parameter注解可以增加一些描述等有用的信息
    public User getUser(@Parameter(in = ParameterIn.QUERY, name = "name", description = "用户名") String name){
        User user = new User();
        user.setUsername(name);
        user.setPassword("123");
        return user;
    }

    @Operation(summary = "用户接口 - 添加用户")
    @PostMapping("/user/add")
    // 这里的user会被Swagger自动识别
    public User addUser(@RequestBody User user){
        System.out.println("添加用户");
        return user;
    }

}

```

实体类User.java：

```java

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class User {

    @Schema(name = "username", description = "用户名", example = "javalover")
    private String username;

    @Schema(name = "password", description = "密码", example = "123456")
    private String password;

    // 隐藏这个属性，这样接口文档的请求参数中就看不到这个属性
    @Schema(hidden = true)
    private String email;

}

```

启动后运行界面如下：

- 首页展示：

![image-20210729132629924](D:\StudyData\github-project\tangyuanxueJava\【文章】\【SpringBoot】\知识点\后台接口文档管理Swagger3\首页展示.png)

- /user/add接口展示：

![image-20210729132730799](D:\StudyData\github-project\tangyuanxueJava\【文章】\【SpringBoot】\知识点\后台接口文档管理Swagger3\user-add接口.png)

- /user/detail接口展示

  ![image-20210729132849933](D:\StudyData\github-project\tangyuanxueJava\【文章】\【SpringBoot】\知识点\后台接口文档管理Swagger3\user-detail接口.png)

## 问题

目前只是简单地体验了下，其实里面还是有很多坑，等后面有空再整理解决，下面列举几个：

- @Paramters参数无效
- @ApiImplicitParamter的body属性无效
- @Tag的name属性：如果name属性不是当前类名的小写连字符格式，则会被识别为一个单独的接口分组
- 等等



**最近整理了一份面试资料《Java面试题-校招版》附答案，无密码无水印，感兴趣的可以关注公众号回复“面试”领取。**

