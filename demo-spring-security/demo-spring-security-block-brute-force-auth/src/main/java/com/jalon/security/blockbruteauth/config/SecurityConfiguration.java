package com.jalon.security.blockbruteauth.config;

import com.jalon.security.blockbruteauth.handler.CustomAuthenticationFailureHandler;
import com.jalon.security.blockbruteauth.service.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * <p>
 *  安全配置类
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/12 17:39
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler();
//        return new ForwardAuthenticationFailureHandler("");
        return new CustomAuthenticationFailureHandler();
    }

    // 认证相关操作
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("=== SecurityConfiguration.authenticate ===");
        // 数据没有持久化，只是保存在内存中
        auth.userDetailsService(userDetailsService);
//        auth.inMemoryAuthentication()
//                .withUser("javalover").password(passwordEncoder().encode( "123456"))
//                .roles("USER");
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
                .antMatchers("/error").permitAll()
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



}
