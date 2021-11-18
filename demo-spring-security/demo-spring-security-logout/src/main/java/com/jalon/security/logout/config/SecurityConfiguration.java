package com.jalon.security.logout.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

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

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
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
            .failureUrl("/login?error=true")
            .and()
            // 登出 所有用户都可以访问
            .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout")
                .invalidateHttpSession(false)
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler())
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
