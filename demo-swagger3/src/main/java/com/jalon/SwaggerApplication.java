package com.jalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/7/27 14:58
 */
@EnableOpenApi
@SpringBootApplication
public class SwaggerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerApplication.class, args);
    }
}
