package com.jalon.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: jalon2015
 * @date: 2021/3/5 18:11
 */

@RestController
public class DemoController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;

    @GetMapping("/test")
    public String test(){
        listOps.leftPush("name2", "jalon2");
        System.out.println(listOps.leftPop("name2"));
        return "success";
    }

}
