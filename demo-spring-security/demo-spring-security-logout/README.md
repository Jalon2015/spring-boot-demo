

## 简介

前面我们介绍了[表单登录的入门案例](https://juejin.cn/post/7030306851762176007)；

本篇介绍下**登出**的入门案例，代码基于表单登录的案例进行演示；

代码地址见文末

## 目录



## 正文

### 1. 基本配置

最基本的登出配置如下所示：

```java
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 登出 所有用户都可以访问
            .logout().permitAll();
    }
```

这里默认的登出url为`/logout`，通过在url中访问`http://localhost:8090/logout`就可以登出了。

当然最方便的还是在界面中进行链接跳转，如下所示：

```html
    <a href="logout">退出</a>
```

### 2.  登出跳转

**logoutSuccessUrl配置**：

登出跳转成功后的默认界面是根路径，比如`http://localhost:8090/`；

下面我们可以进行简单的配置，配置成自己指定的界面，如下所示：一般推荐将登出成功后跳转的链接设置为登录界面（习惯）

```java
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 登出 所有用户都可以访问
            .logout().permitAll() 
            .logoutSuccessUrl("/login");
    }
```

**logoutUrl配置：**

登出跳转的默认url为`/logout`，比如`http://localhost:8090/logout`，如果登出成功，就跳转到上面配置的路径；

配置如下所示：

```java
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 登出 所有用户都可以访问
            .logout().permitAll() 
            .logoutUrl("/logout");
    }
```

### 3. 更新缓存

这里的缓存指的就是session和cookie；

在登出之后，需要将session失效处理，并删除对应的cookie；

对应的命令为：`invalidateHttpSession()` 和 `deleteCookies(...name)`;

配置如下所示：

> 其中删除的Cookies名称为`JSESSIONID`，这个就是前后端交互的一个凭证id，是在第一次前端请求后端时，后端返回的id；后续的请求后端会根据JSESSIONID来匹配对应的session

```java
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 登出 所有用户都可以访问
            .logout().permitAll() 
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");
    }
```

### 4. 登出处理器

登出成功后，不仅可以设置特定的url，还可以执行一些自定义的操作；

对应的命令为：`logoutSuccessHandler`

比如我们需要记录登出时访问的最后一个界面，那么可以通过如下的代码来实现；

先定义一个处理器：`CustomLogoutSuccessHandler.java`

```java
public class CustomLogoutSuccessHandler extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String refererUrl = request.getHeader("Referer");
        System.out.println("Logout from: " + refererUrl);

        super.onLogoutSuccess(request, response, authentication);
    }
}
```

然后在配置中注入该处理器，通过方法注入，如下所示：

```java
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }
     @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 登出 所有用户都可以访问
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler());
    }

}

```

这样我们在登出时，就可以看到控制台打印下面的内容：

```bash
Logout from: http://localhost:8090/home
```

## 总结

本篇介绍了登出的相关配置和处理；

配置有：

- logoutUrl(): 登出链接配置
- logoutSuccessUrl(): 登出成功后的跳转链接
- invalidateHttpSession: 失效session
- deleteCookies() : 删除对应cookie，多个cookieName逗号分隔
- LogoutSuccessHandler：登出后执行的自定义操作

[源码地址](https://github.com/Jalon2015/spring-boot-demo/tree/master/demo-spring-security/demo-spring-security-logout)

