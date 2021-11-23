

## 简介

前面我们的SpringSecurity文章介绍的登录认证，不管是`form-login-auth`表单登录认证，还是`basic-auth`基本方式的认证，都是通过SpringSecurity系统自动进行的认证，我们并没用人工干预；

比如表单登录认证，我们在表单中提交`login`的表单登录请求后，后台并没有自己写对应的处理器，而是由系统自动认证的；

相应的，基本方式的认证也是直接把用户名密码写入header中，系统自动认证；

那今天我们就来手动进行认证，以此熟悉下认证的过程

## 目录

## 正文

### 1. 安全配置

一如既往，还是简单配置一个用户，一个登录请求；

不过这次的路径匹配多了一个自定义的manually-login：`.antMatchers("/manually-login").permitAll()`；

这个就是我们在提交表单时要请求的路径，如果不开放，请求后会再次跳转到登录界面（因为没有权限）；

```java
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // 认证相关操作
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("=== SecurityConfiguration.authenticate ===");
        // 数据没有持久化，只是保存在内存中
        auth.inMemoryAuthentication()
                .withUser("javalover").password(passwordEncoder().encode("123456")).roles("USER");
    }

    // 授权相关操作
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("=== SecurityConfiguration.authorize ===");
        http
            // home 页面，ADMIN 和 USER 都可以访问
            .antMatchers("/home").hasAnyRole("USER", "ADMIN")
            // login 页面，所有用户都可以访问
            .antMatchers("/manually-login").permitAll()
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
            // 登出 所有用户都可以访问
            .logout().permitAll()
    }

    // 定义一个密码加密器，这个BCrypt也是Spring默认的加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

```

### 2. 表单组件

登录表单如下所示：这里我们跳转的路径改为了`manually-login`，之前是`login`；这样登录请求时，请求登录认证会由我们自己进行处理；

```html
<form action="/manually-login" method="post">
    <input name="username" placeholder="用户名">
    <input name="password" placeholder="密码">
    <button type="submit">登录</button>
</form>
```

### 3. 控制器

这个控制器里有一个`manuallyLogin`就是负责接收上面的登录请求，如下所示：

```java
@Controller
@Slf4j
public class SecurityController {

    @RequestMapping("/login")
    public String login(){
        log.info("=== login ===");
        return "login";
    }

    @Autowired
    AuthenticationManager authManager;

    @PostMapping(path="/manually-login", consumes={APPLICATION_FORM_URLENCODED_VALUE})
    public String manuallyLogin(HttpServletRequest request, String username, String password){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authManager.authenticate(authenticationToken);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
        System.out.println("是否认证通过："+context.getAuthentication());
        return "redirect:/home" ;
    }
	// ...省略其他的

}

```

AuthenticationManager会在安全配置中配置，下面会介绍；

这里先介绍下manuallyLogin方法中的认证步骤：

1. 首先构建一个`UsernamePasswordAuthenticationToken`，根据用户名和密码（这里的用户名和密码就是通过登录表单传入的）
    1. `UsernamePasswordAuthenticationToken`类是`Authentication`的实现类；
    2. 主要实现的功能就是通过用户名/密码来认证用户，构造函数实现
    3. 然后通过`isAuthenticated`判断是否认证成功
2. 然后通过`AuthenticationManager`对上面的`token`进行认证；这一步是关键，如果用户名密码错误或者状态异常，都会在这里报错；
3. 认证通过后，将认证结果`Authentication`保存到`SecurityContext`上下文中（这个上下文主要工作就是负责认证信息的获取和设保存）；
    1. 这里如果不保存，那么认证就没意义了，后续的访问还是会重定向到登录界面；因为后续的权限检测都是通过这个上下文检测的
    2. 这里的`SecurityContext`是线程安全的，也就是说不同用户访问的是不同的`SecurityContext`上下文，互不影响
4. 保存到上下文后，就是session的相关操作，这里先获取session；
    1. 获取的同时会生成JSESSIONID，如果getSession(false)则不会生成JSESSIONID；
    2. 将上下文保存到session中
5. 通过`context.getAuthentication()`验证是否认证通过
6. 最后重定向到home页面；

### 4. AuthenticationManager配置

这个刚开始自己用方法注入了一个，但是死活不成功，报内部错误，大概意思就是用户状态异常；

后来查了下，才知道原来`WebSecurityConfigurerAdapter`配置接口本身就有获取`AuthenticationManager`的方法，直接覆写然后注入@Bean就好了，如下所示：

```java
  @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
```

### 5. 实践

启动程序，访问`http://localhost:8090/login`跳转到登录界面，输入`javalover/123456`发送表单请求到`manually-login`，认证通过重定向到`home`页面

![image-20211123105928146](https://i.loli.net/2021/11/23/6zuweELWQJdpNnk.png)

![image-20211123110142791](https://i.loli.net/2021/11/23/OW3pBoHXcDGIAQZ.png)

后台打印的认证信息如下：Authenticated=true就说明认证通过，还有对应的权限信息

```bash
是否认证通过：UsernamePasswordAuthenticationToken [Principal=org.springframework.security.core.userdetails.User [Username=javalover, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]], Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_USER]]
```

## 总结

本篇主要通过手动认证的方式，熟悉了一下认证的过程：

- 先构造一个认证对象，通过用户名/密码；
- 再通过认证管理器进行认证请求，成功后返回认证信息（包括用户名、密码（不可见）、权限等信息），失败时会抛出各种异常；
- 接下来将认证信息保存到上下文；
- 最后将上下文保存到session中进行管理；



[源码地址]()
