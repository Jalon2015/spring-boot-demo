package com.jalon.security.controller;

import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/6/2 14:55
 */
@RestController
public class UserController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }
}
