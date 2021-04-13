package com.jalon.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <p>
 *  安全配置类
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/12 17:39
 */
@Configuration
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationSuccessHandler authenticationSuccessHandler;
    // 授权相关操作
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("=== SecurityConfiguration.configure(http) ===");
        http.authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/home").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login")
                .successHandler(authenticationSuccessHandler)
                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    // 认证相关操作
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.info("=== SecurityConfiguration.configureGlobal ===");
        auth.inMemoryAuthentication().passwordEncoder(new com.jalon.security.config.MyPasswordEncoder())
                .withUser("javalover").password("123456").roles("USER");
        auth.inMemoryAuthentication().passwordEncoder(new com.jalon.security.config.MyPasswordEncoder())
                .withUser("admin").password("123456").roles("ADMIN");

    }

}
