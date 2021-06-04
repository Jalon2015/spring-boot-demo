package com.jalon.cache.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/6/4 11:28
 */
public class ComponentDemo {

    private RedisTemplate template;
    private String a;

    @Autowired
    public ComponentDemo(@Qualifier("redisTemplate") RedisTemplate template) {
        this.template = template;
    }

    public ComponentDemo(@Qualifier("redisTemplate") RedisTemplate template, String a) {
        this.template = template;
        this.a = a;
    }


    public void test(){
        System.out.println("test");
    }
}
