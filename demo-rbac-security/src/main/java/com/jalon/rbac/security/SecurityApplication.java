package com.jalon.rbac.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: jalon2015
 * @date: 2021/3/9 18:27
 */
@SpringBootApplication
@RestController
public class SecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @GetMapping("/demo")
    public String demo(){
        System.out.println("demo");
        return "demo";
    }
}
