package com.jalon.cache.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: jalon2015
 * @date: 2021/3/5 17:46
 */

@Configuration
public class MyConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("182.92.78.9", 6379);
        configuration.setPassword("3GBBnBqL@r_2!t8");
        return new LettuceConnectionFactory(configuration);
    }

}
