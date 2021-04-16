package com.jalon.properties.controller;

import cn.hutool.core.lang.Dict;
import com.jalon.properties.property.ApplicationProperty;
import com.jalon.properties.property.DeveloperProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  属性控制器：显示属性
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/16 20:27
 */
@RestController
public class PropertyController {
    private final ApplicationProperty applicationProperty;
    private final DeveloperProperty developerProperty;

    @Autowired
    public PropertyController(ApplicationProperty applicationProperty, DeveloperProperty developerProperty) {
        this.applicationProperty = applicationProperty;
        this.developerProperty = developerProperty;
    }

    @GetMapping("/property")
    public Dict getProperty(){
        return Dict.create().set("application", applicationProperty).set("developer", developerProperty);
    }
}
