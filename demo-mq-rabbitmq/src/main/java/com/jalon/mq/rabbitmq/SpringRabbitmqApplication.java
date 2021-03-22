package com.jalon.mq.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author: jalon2015
 * @date: 2021/3/18 11:43
 */
@SpringBootApplication

public class SpringRabbitmqApplication {
    public static void main(String[] args) {

        Integer[] a = new Integer[]{1,2,3};
        SpringApplication.run(SpringRabbitmqApplication.class, args);
    }
}
