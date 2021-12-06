## 简介

通常一个后台管理系统，会包含不同的角色和权限；

然后不同的角色，登录后会根据权限的不同，跳转到不同的界面；

这里我们还是设定有两个角色：普通用户和管理员；

- 普通用户：登录成功跳转到 home 页面；
- 管理员：登录成功跳转到 admin 页面；

## 目录

- 基本配置
- 配置用户和角色
- 创建处理器类
- 配置处理器
- 修改控制器
- 运行

## 正文

### 1. 基本配置

先配置一个默认的登录成功界面，如下所示：

```java
@Configuration
@EnableWebSecurity
@Slf4j
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .formLogin()
                .loginPage("/login")
            	// 登录成功跳转的页面，第二个参数true表示每次登录成功都是跳转到home，如果false则表示跳转到登录之前访问的页面
                .defaultSuccessUrl("/home.html", true)
            // ... 其他配置
            
    }
}
```

这样当用户登录成功后，会默认跳转到home.html界面；

但是本节我们要做的就是修改这个地方，使得不同角色跳转不到不同的界面；

下面开始进入主体

### 2. 配置用户和角色

这里我们用 全局配置：

```java
 @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // 数据没有持久化，只是保存在内存中
        auth.inMemoryAuthentication()
                .withUser("javalover").password(passwordEncoder().encode("123456")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("123456")).roles("ADMIN");
    }
```

这里配置了两个用户：

- 普通用户：javalover
- 管理员：admin

### 3. 创建处理器类

第一步中的配置是：两个角色登录后，默认都是跳转到`home.html`界面；

接下来就开始修改，使他们跳转到不同的界面；

先定义一个处理器类，实现了`AuthenticationSuccessHandler`接口：

```java
public class MySimpleUrlAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {


    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {

         String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            logger.debug(
                    "Response has already been committed. Unable to redirect to "
                            + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
        }

}

```

覆写的`onAuthenticationSuccess`方法：登录成功会先到这个地方，然后我们就可以在这里控制下一步要跳转的界面（当然其他的一些操作也可以）；



`determineTargetUrl`方法：就是这篇文章的核心；

它会根据不同的权限，获取到不同的跳转url，然后重定向；

```java
protected String determineTargetUrl(final Authentication authentication) {

    Map<String, String> roleTargetUrlMap = new HashMap<>();
    roleTargetUrlMap.put("ROLE_USER", "/home");
    roleTargetUrlMap.put("ROLE_ADMIN", "/admin");

    final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (final GrantedAuthority grantedAuthority : authorities) {
        String authorityName = grantedAuthority.getAuthority();
        if(roleTargetUrlMap.containsKey(authorityName)) {
            return roleTargetUrlMap.get(authorityName);
        }
    }

    throw new IllegalStateException();
}
```

### 4. 配置处理器

上面我们定义了一个处理器，用来控制登录成功后的跳转界面；

这里我们将其配置到config类中；

先在配置类中注入一个Bean：`AuthenticationSuccessHandler `，返回的是刚才创建的处理器类

```java
@Bean
public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
    return new MySimpleUrlAuthenticationSuccessHandler();
}
```

然后替换掉开头设置的跳转url参数，如下所示：

```java
@Override
protected void configure(final HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .formLogin()
            .loginPage("/login")
            .successHandler(myAuthenticationSuccessHandler())
        // ...其他配置
}
```

### 5. 修改控制器

上面我们跳转home和admin，是通过控制器进行跳转的，下面我们配置一下控制器：

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

    // 获取当前登录的用户角色
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

### 6. 运行

接下来我们就可以启动程序，访问`http://localhost:8090/`进行测试了

> 前端代码就不贴了，就是三个界面：login,.html, home.html, admin.html。完整源码见文末

- 普通用户登录：

![image-20211119123413681](https://i.loli.net/2021/11/19/Zt6Ds9gBxhOqH2A.png)

跳转到home

![image-20211119123425793](https://i.loli.net/2021/11/19/GDZEaugkNMBIK97.png)

- 管理员登录：

![image-20211119123533315](https://i.loli.net/2021/11/19/jWm1UARyIxfNpuL.png)

跳转到admin

![image-20211119123541475](https://i.loli.net/2021/11/19/AJGNECQVonU6mse.png)

## 

## 总结

重定向的核心就是那个处理器中的`determineTargetUrl`方法，根据角色的不同，跳转到不同的界面；



[源码地址](https://github.com/Jalon2015/spring-boot-demo/tree/master/demo-spring-security/demo-spring-security-login-redirect)

