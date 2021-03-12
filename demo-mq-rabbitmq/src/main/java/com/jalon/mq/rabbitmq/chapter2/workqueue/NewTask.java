package com.jalon.mq.rabbitmq.chapter2.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "hello.....";
            for (int i = 0; i < 9; i++) {
                channel.basicPublish("", QUEUE_NAME,null, message.getBytes());
                System.out.println("send:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
