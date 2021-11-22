## 前言

前面介绍了基于SpringSecurity的表单登录例子；

本篇介绍怎么给表单登录添加一个**记住我**的功能；

有了这个功能，那么在token失效后，系统会自动获取最新token，而不用重新登录；

这里需要注意一点：这里的token并不是普通的token，而是JSESSIONID；这个JSESSIONID会在前端第一次请求后端时返回，以后这个JSESSION就是前后台通讯的凭证

## 目录

## 正文

### 1. 安全配置

这里我们做一个最简单的配置，如下所示：添加一个rememberMe()方法

```java
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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
        log.info("=== SecurityConfiguration.authorize ===");
        http
            // 登出 所有用户都可以访问
            .logout().permitAll()
                .deleteCookies("JSESSIONID")
            .and()
                .rememberMe()
            // ...省略其他配置
            ;
    }


}

```

可以看到，我们要做的只是提供一个rememberMe()方法，前台就可以在token失效后自动获取最新的token，而不用再重新输入用户名密码进行登录操作；

### 2. 前端组件

这里我们前端也尽可能简化，在之前的表单登录基础上，只增加一个复选框 rememberMe，代码如下：

```html
<form action="/login" method="post">
    <table>
        <tr>
            <input name="username" placeholder="用户名">
        </tr>
        <tr>
            <input name="password" placeholder="密码">
        </tr>
        <tr>
            <td>记住我:</td>
            <td><input type="checkbox" name="remember-me" /></td>
        </tr>
    </table>
        <button type="submit">登录</button>
    </form>
```

### 3. 实践-不勾选rememberMe

下面我们就可以基于上面的代码，进行一个简单的测试：看一下 勾选rememberMe和不勾选的差别

> 完整代码见文末地址

第一步：启动程序，界面如下所示：javalover/123456

![image-20211122152245002](https://i.loli.net/2021/11/22/HJfNM4xW7p2z6PV.png)

这里我们先不勾选**记住我**，点击**登录**，跳转到如下的主页：

![image-20211122152555706](https://i.loli.net/2021/11/22/yMzTQc1YvmiuC5W.png)

第二步：**接下来是重点**，这里我们删除本地cookie中的JSESSIONID，如下所示：F12->应用程序->cookies->JSESSION->右键-删除

![image-20211122152731372](https://i.loli.net/2021/11/22/UJi4FWH9qo7kv6V.png)

第三步：刷新页面，可以看到，自动跳转到登录页面，因为token失效了，前后端通讯的凭证没了：

![image-20211122152901019](https://i.loli.net/2021/11/22/JazEfBAciKCy7xk.png)

> 其实上面我们也可以不删除cookie，等着session失效（默认30分钟，可以在application.yml中配置：server.servlet.session.timeout=60，默认单位秒）
>
> 后端的session都失效了，那session产生的JSESSIONID肯定也无效了；

### 4. 实践-勾选rememberMe

下面我们勾选**记住我**，重复上面的步骤，会发现在删除cookie中的JSESSIONID时，看到多了一个remember-me；

![image-20211122153751334](https://i.loli.net/2021/11/22/x6aBAdEGWgvVT1t.png)

其实这里我们可以换个角度来理解：虽然删了JSESSIONID，但是因为还有一个remember-me，所以前后端的通讯还是没有断开；

所以此时我们刷新页面，还是停留在主页，不会跳转到登录界面；

**但是如果我们把remember-me也一起删掉，那么结果很明显，还是会跳到登陆界面**。

### 5. 更多配置

**失效时间：**

上面我们配置的remember-me，默认的token失效时间是两周，下面我们可以配置的短一点，比如一天：

```java
.logout().permitAll()
    .deleteCookies("JSESSIONID")
    .and()
    .rememberMe()
    .tokenValiditySeconds(86400)
    .and()
```

> 失效时间：严格意义上来说，上面这个失效时间 应该是remember-me的失效时间；

这样的话，如果超过一天后，你再去删除JSESSIONID或者session失效，那么刷新页面还是会跳转到登录界面；

**加密的密钥：**

前面我们在调试界面看到的remember-me cookie值，它的值是由：MD5(用户名+过期时间+密码+密钥）合成的；

这里的密钥我们可以自己配置，如下所示：

```java
.rememberMe()
    .key("privateKey")
    .tokenValiditySeconds(86400)
```

## 总结

上面介绍了**rememberMe**的相关知识，了解了其实**rememberMe**就是：用新的通讯凭证`remember-me`来管理旧的通讯凭证`JSESSIONID`；

当`JSESSIONID`被删除或者`session`过期时，如果`rememberMe cookie`还没过期（默认两周），那么系统就可以自动登录

`remember-me`真实的过期时间可以在调试界面看到，如下所示：

![image-20211122162553634](https://i.loli.net/2021/11/22/bQH12ZA3k8dy4nT.png)

