package com.jalon.mq.rabbitmq.chapter2.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 *     生产者：多个消息
 * </p>
 * @author: jalon2015
 * @date: 2021/3/12 18:09
 */
public class NewTask {

    private final static String QUEUE_NAME = "work_queue";
    // 重新定义一个队列：因为RabbitMQ不允许以不同的参数 重复定义同一个队列
    private final static String QUEUE_NAME_DURABLE = "work_queue_durable";
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            // 队列持久化：如果RabbitMQ服务挂了，保证队列还存在
            boolean durable = true;
            channel.queueDeclare(QUEUE_NAME_DURABLE, durable, false, false, null);
            // 耗时1S
            String message = "hello.";
            for (int i = 0; i < 9; i++) {
                // 消息持久化：如果RabbitMQ服务挂了，保证消息还存在
                // 这里将basicProperties属性设置为 PERSISTENT_TEXT_PLAIN
                channel.basicPublish("", QUEUE_NAME_DURABLE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("send:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
