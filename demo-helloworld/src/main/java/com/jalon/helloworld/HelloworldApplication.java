package com.jalon.helloworld;


import cn.hutool.core.util.StrUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/16 17:40
 */
@SpringBootApplication
@RestController
public class HelloworldApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }
    @GetMapping("hello")
    public String hello(@RequestParam(name = "name", required = false) String name){
        if(StrUtil.isBlank(name)){
            name = "javalover";
        }
        String template = "传递的参数为：{}";
        return StrUtil.format(template, name);
    }
}
