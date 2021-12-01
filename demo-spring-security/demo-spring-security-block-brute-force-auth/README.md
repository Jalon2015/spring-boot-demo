## 简介

在我们之前演示的表单登录例子中，登录失败后默认会再次跳转到登录界面，没有任何提示；

这里我们可以做一些小改动，在登录失败后增加一些提示信息，使其更加符合我们真实使用的场景；

## 目录

1. 自定义的认证失败处理器
2. 内置的认证失败处理器
3. 配置异常处理器
4. 实践

## 正文

### 1. 自定义的认证失败处理器

这里我们自己定义一个**认证失败处理器**，用来处理认证失败时的相关操作；这里我们返回一个时间戳和异常的提示信息

```java
public class CustomAuthenticationFailureHandler
        implements AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put(
                "timestamp",
                Calendar.getInstance().getTime());
        data.put(
                "exception",
                exception.getMessage());

        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }
}

```

### 2. 内置的认证失败处理器

其实SpringSecurity内部也有提供几种认证失败的处理方式；

1. `SimpleUrlAuthenticationFailureHandler `：这个就是默认的处理方式，即设定一个认证失败跳转的url；那么在认证失败时，就会跳转到特定的url；

    - 通过`failureUrl(url)`来设定跳转的url；

    - 如果没有设定，那么会重定向到默认的跳转页面：登录页面login并携带?error参数，比如`http://localhost:8090/login?error`；

    - 如果我们直接设定url为null，程序启动不了，因为设定的url不允许null，那源码中的null检测有啥用呢？其实是有办法设置null的，那就是通过重新构造一个空的`SimpleUrlAuthenticationFailureHandler`来实现，此时认证失败会返回401未授权的错误；

        - 不过前端的表现是重定向到login页面，因为401会被系统监测到，然后自动跳转到error页面；
        - 但是因为没有`/error`的访问需要权限，所以会重定向到login页面；
        - 如果配置一个`.antMatchers("/error").permitAll()`那么就会跳转到error页面；
        - 并且通过F12可看到返回的错误码401；

      下面是`SimpleUrlAuthenticationFailureHandler`的核心处理方法：

      ```java
      @Override
          public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                  AuthenticationException exception) throws IOException, ServletException {
              if (this.defaultFailureUrl == null) {
                  if (this.logger.isTraceEnabled()) {
                      this.logger.trace("Sending 401 Unauthorized error since no failure URL is set");
                  }
                  else {
                      this.logger.debug("Sending 401 Unauthorized error");
                  }
                  response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
                  return;
              }
              saveException(request, exception);
              if (this.forwardToDestination) {
                  this.logger.debug("Forwarding to " + this.defaultFailureUrl);
                  request.getRequestDispatcher(this.defaultFailureUrl).forward(request, response);
              }
              else {
                  this.redirectStrategy.sendRedirect(request, response, this.defaultFailureUrl);
              }
          }
      ```

2. `ForwardAuthenticationFailureHandler`：这个处理器跟上面的类似，也是设定一个url，然后跳转；

    - 不同的是，他会把错误信息设置到request的SPRING_SECURITY_LAST_EXCEPTION属性中，然后进行**页面跳转**；
    - 而上面的处理器是设置到response中，进行**页面的重定向**；不过上面的处理器如果有设置`forwardToDestination`属性，那么就跟`ForwardAuthenticationFailureHandler`一样了；

   下面是`ForwardAuthenticationFailureHandler`的核心处理方法：

   ```java
   	@Override
   	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
   			AuthenticationException exception) throws IOException, ServletException {
   		request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
   		request.getRequestDispatcher(this.forwardUrl).forward(request, response);
   	}
   ```

3. `ExceptionMappingAuthenticationFailureHandler `：这个处理器继承自默认的`SimpleUrlAuthenticationFailureHandler`，它的功能就比较丰富了，它可以根据异常的不同，跳转到不同的url；

    - 前提是要定义好map映射，其中map的key就是异常类的名称，map的value就是对应的异常要跳转的url；

    - 如果没有找到对应的url，就会回滚到`SimpleUrlAuthenticationFailureHandler`处理器进行处理；

4. `DelegatingAuthenticationFailureHandler`：委托处理器，它自己不实现异常处理，而是根据异常的不同，委托给不同的处理器（就是上面的这些，还有其他的一些）；这种方式会更加灵活；

### 3. 配置异常处理器

配置的方式还是跟往常一样，先注入，再配置，如下所示：

```java
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    // 认证相关操作
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("=== SecurityConfiguration.authenticate ===");
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
            // 关闭csrf，此时登出logout接收任何形式的请求；（默认开启，logout只接受post请求）
            .csrf().disable()
            .authorizeRequests()
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
//            .failureUrl("/login?error=true")
                .failureHandler(authenticationFailureHandler())
            .and()
            // 登出 所有用户都可以访问
            .logout().permitAll();
    }

    // 定义一个密码加密器，这个BCrypt也是Spring默认的加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

```

可以看到，我们注释了之前的`.failureUrl("/login?error=true")`方法，改用`                .failureHandler(authenticationFailureHandler())`方法；

这样当认证失败时，会使用自定义的`CustomAuthenticationFailureHandler`处理器，此时会返回一个错误信息，包括时间戳和异常的提示信息；

### 4. 实践

其他的登录和控制器代码就不贴了，都是跟之前的一样；

下面我们就启动测试一下，访问`http://localhost:8090/login`，输入错误的用户名/密码，如下所示：

![image-20211124115717289](https://i.loli.net/2021/11/24/CQPKOAveLDUWSwg.png)

点击登录后，会按照预期的设定进行提示，如下所示：

![image-20211124115745548](https://i.loli.net/2021/11/24/Ffk3EHvCaLmZp52.png)

这里需要的话，可以在前端进行判断，然后弹框进行相应的提示

## 总结

这里介绍了多种认证失败的异常处理方式，包括自定义的，以及官方内置的；

一般我们用默认的`SimpleUrlAuthenticationFailureHandler`就可以了；

这里自定义的认证失败处理器，主要是为了了解认证失败的相关运行机制；

[源码地址](https://github.com/Jalon2015/spring-boot-demo/tree/master/demo-spring-security/demo-spring-security-auth-failure-handler)

