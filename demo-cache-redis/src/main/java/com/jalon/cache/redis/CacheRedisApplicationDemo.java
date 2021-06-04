package com.jalon.cache.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: jalon2015
 * @date: 2021/3/5 18:01
 */

@SpringBootApplication
public class CacheRedisApplicationDemo {
    public static void main(String[] args) {
        SpringApplication.run(CacheRedisApplicationDemo.class, args);
    }
}
