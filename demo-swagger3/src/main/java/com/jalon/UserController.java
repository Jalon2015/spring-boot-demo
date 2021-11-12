package com.jalon;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/7/27 15:20
 */
@Tag(name = "user-controller", description = "用户接口")
@RestController
public class UserController {

    // 忽略这个api
    @ApiIgnore
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @Operation(summary = "用户接口 - 获取用户详情")
    @GetMapping("/user/detail")
    // 这里的@Parameter也可以不加，Swagger会自动识别到这个name参数
    // 但是加@Parameter注解可以增加一些描述等有用的信息
    public User getUser(@Parameter(in = ParameterIn.QUERY, name = "name", description = "用户名") String name){
        User user = new User();
        user.setUsername(name);
        user.setPassword("123");
        return user;
    }

    @Operation(summary = "用户接口 - 添加用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username1", value = "用户名1", paramType = "body"),
            @ApiImplicitParam(name = "password1", value = "密码1", paramType = "body")
    })
    @PostMapping("/user/add")
    // 这里的user会被Swagger自动识别
    public User addUser(@ApiIgnore @RequestBody User user){
        System.out.println("添加用户");
        return user;
    }

}
