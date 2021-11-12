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
        String a = "asdf";
        String b = "asd";

        String str = "{\n" +
                "  \"a\": 10,\n" +
                "  \"b\": 20\n" +
                "}";

        if(2>1) {
            System.out.println(1);
        }
        SpringApplication.run(SwaggerApplication.class, args);
    }
}
