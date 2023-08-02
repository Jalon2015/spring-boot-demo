package com.jalon.properties.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  开发者的配置属性：通过@Value注解配置
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/16 20:17
 */
@Data
@Component
public class DeveloperProperty {

    @Value("${developer.name}")
    private String name;
    @Value("${developer.website}")
    private String website;
    @Value("${developer.nickname}")
    private String nickname;


}
