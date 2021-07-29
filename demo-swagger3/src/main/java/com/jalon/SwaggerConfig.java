package com.jalon;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/7/29 11:49
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        // 配置OAS 3.0协议
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                // 查找有@Tag注解的类，并生成一个对应的分组；类下面的所有http请求方法，都会生成对应的API接口
                // 通过这个配置，就可以将那些没有添加@Tag注解的控制器类排除掉
                .apis(RequestHandlerSelectors.withClassAnnotation(Tag.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GPS Doc")
                .description("GPS Doc文档")
                .termsOfServiceUrl("http://www.javalover.com")
                .contact(new Contact("javalover", "http://www.javalover.cn", "1121263265@qq.com"))
                .version("2.0.0")
                .build();
    }

}
