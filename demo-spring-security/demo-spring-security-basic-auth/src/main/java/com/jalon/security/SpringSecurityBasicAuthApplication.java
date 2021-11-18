package com.jalon.security;

import com.jalon.security.userinfo.SpringSecurityUserinfoApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *  SpringSecurity - 基于basic auth
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/6/2 14:41
 */
@SpringBootApplication
public class SpringSecurityBasicAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityUserinfoApplication.class, args);
    }
}
