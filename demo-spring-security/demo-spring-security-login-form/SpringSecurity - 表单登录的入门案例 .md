

## 简介

SpringSecurity的认证机制有多种，比如基于用户名/密码的认证，基于OAuth2.0的认证（OAuth已废弃）。。。

而基于用户名/密码的认证方式，又分多种，比如：

- Form Login，表单登录认证（单体应用，比如SpringMVC） 
- Basic Authentication，基本的http认证（前后端分离应用）
- 【已废弃】Digest Authentication，数字认证（已废弃，不再使用这种认证方式，因为它的加密方式不安全，比如md5加密等；现在比较安全的加密方式有BCrypt等）

本节介绍的就是第一种：**表单登录的认证方式**

## 目录

1. maven配置
2. security配置
3. controller控制器
4. web界面
5. 启动运行

## 正文

在开始之前，需要先了解两个词

- Authenticate认证：就是通过用户名/密码等方式，登入到系统，这个过程就是认证；类似于进入景区的大门
- Authorize授权：就是登入到系统之后，校验用户是否有权限操作某个模块，这个过程就是授权；类似于进入景区后，各个收费区域，只有交了钱（有权限），才能进入指定区域；

项目背景：Spring Boot + SpringMVC + Thymeleaf

项目结构如下：

![image-20210603141803128](https://i.loli.net/2021/06/03/FSsi8QHcnlxAyf3.png)

#### 1.maven配置：

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
    <artifactId>demo-spring-security-login-form</artifactId>
    <properties>
        <java.version>1.8</java.version>
        <spring.boot.version>2.4.3</spring.boot.version>
        <lombok.version>1.18.16</lombok.version>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

#### 2.security配置

这里面主要包含两部分：

- authenticate 认证配置：主要配置用户名，密码，角色（这里基于内存来保存，为了简化）
- authorize 授权配置：主要配置各个角色的权限，即可以访问哪些页面

```java
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 认证相关操作
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 数据没有持久化，只是保存在内存中
        auth.inMemoryAuthentication()
                .withUser("javalover").password(passwordEncoder().encode("123456")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN");
    }

    // 授权相关操作
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            	// admin页面，只有admin角色可以访问
                .antMatchers("/admin").hasRole("ADMIN")
            	// home 页面，ADMIN 和 USER 都可以访问
                .antMatchers("/home").hasAnyRole("USER", "ADMIN")
            	// login 页面，所有用户都可以访问
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
            	// 自定义登录表单
                .formLogin().loginPage("/login")
            	// 登录成功跳转的页面，第二个参数true表示每次登录成功都是跳转到home，如果false则表示跳转到登录之前访问的页面
                .defaultSuccessUrl("/home", true)
            	// 失败跳转的页面（比如用户名/密码错误），这里还是跳转到login页面，只是给出错误提示
                .failureUrl("/login?error=true")
                .and()
                .logout().permitAll()
                .and()
            	// 权限不足时跳转的页面，即访问一个页面时没有对应的权限，会跳转到这个页面
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    // 定义一个密码加密器，这个BCrypt也是Spring默认的加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
```

#### 3. controller控制器

控制器主要任务就是处理请求，下面就是典型的MVC模式

```java
@Controller
@Slf4j
public class SecurityController {

    @RequestMapping("/login")
    public String login(){
        log.info("=== login ===");
        return "login";
    }
    
    @RequestMapping("/home")
    public String home(Model model){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "home";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "admin";
    }
	
    // 权限不足
    @RequestMapping("/accessDenied")
    public String accessDenied(Model model){
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "access_denied";
    }

    // 获取当前登录的用户名
    private String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // 获取当前登录的用户角色：因为有可能一个用户有多个角色，所以需遍历
    private String getAuthority(){
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        ArrayList<String> list = new ArrayList<>();
        for(GrantedAuthority authority: authorities){
            list.add(authority.getAuthority());
        }
        log.info("=== authority：" + list);
        return list.toString();
    }
}
```

#### 4. web界面

界面有4个：

- login.html: 登录界面，所有人都可以访问
- home.html: 主页面，普通用户和管理员可以访问
- admin.html: 管理员页面，只有管理员可以访问
- access_denied.html: 访问被拒绝页面，权限不足时会跳转到该页面；比如普通用户访问admin.html时

login.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Spring Security</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
</head>
<body>
    <div th:if="${param.error}">
        Invalid username and password.
    </div>
    <div th:if="${param.logout}">
        You have been logged out
    </div>
    <form action="/login" method="post">
        <input name="username" placeholder="用户名">
        <input name="password" placeholder="密码">
        <button type="submit">登录</button>
    </form>
</body>
</html>
```

home.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Spring Security Home</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
</head>
<body>
    欢迎<span th:text="${user}"></span>
    你的权限是<span th:text="${role}"></span>
    <a href="admin">admin页面</a>
    <a href="logout">退出</a>
</body>
</html>
```

admin.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Spring Security Admin</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
</head>
<body>
    欢迎<span th:text="${user}"></span>
    你的权限是<span th:text="${role}"></span>
    <a href="logout">退出</a>
</body>
</html>
```

access_denied.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>Access Denied</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
</head>
<body>
    <span th:text="${user}"></span>没有权限访问页面
    你的权限是<span th:text="${role}"></span>
    <a href="logout">退出</a>
</body>
</html>
```

#### 5. 启动运行

访问 http://localhost:8088，会自动跳转到login界面，如下：

![image-20210603143352317](https://i.loli.net/2021/06/03/1ryvFKGR4wTZ8WP.png)

这里先用普通用户的身份来登录，javalover/123456，登陆后进入主页：可以看到，权限是普通用户

![image-20210603143453932](https://i.loli.net/2021/06/03/K24JhxYNAzC1o6L.png)

这时点击`admin页面`就会提示权限不足，如下：

![image-20210603143531292](https://i.loli.net/2021/06/03/3wEOuIRl2iQb8se.png)

此时点击退出，又重新回到登录界面：并附有提示【已退出登录】

![image-20210603143631810](https://i.loli.net/2021/06/03/8HwOQKTMyz5hXiI.png)

最后用管理账户登录，admin/123456，登录进入主页：可以看到，权限是管理员

![image-20210603143901708](https://i.loli.net/2021/06/03/Kvy1TUJPqn5OErc.png)

这时点击`admin页面`，就会正常显示：

![image-20210603144003914](https://i.loli.net/2021/06/03/acVsUEmMuFPpGAy.png)

## 总结

SpringSecurity的表单登录认证，总的来说代码不是很多，因为很多功能SpringSecurity都是自带的（比如登录、登出、权限不足等），我们只需要根据自己的需求来修改一些配置就可以了，这才是真正的开箱即用



源码地址：[**demo-spring-security-login-form**](https://github.com/Jalon2015/spring-boot-demo/tree/master/demo-spring-security/demo-spring-security-login-form)