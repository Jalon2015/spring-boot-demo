package com.jalon.controller;

import com.jalon.entity.User;
import com.jalon.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/userList")
    public Object getUserList(@RequestParam("name") String name){
        List<User> userList = userMapper.selectAllUser(name);
        return userList;
    }

    @PostMapping("/saveUser")
    public Object saveUser(@RequestBody User user){
        int i = userMapper.saveUser(user);
        return i;
    }
}
