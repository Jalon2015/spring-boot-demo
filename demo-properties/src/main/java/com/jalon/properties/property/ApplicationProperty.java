package com.jalon.properties.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  应用的配置属性
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/16 20:17
 */
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class ApplicationProperty {

    private String name;
    private String version;

}
