package com.jalon.mq.rabbitmq.chapter1.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 *     消息接收者
 * </p>
 * @author: jalon2015
 * @date: 2021/3/12 17:30
 */
public class Recv {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println("waiting for  messages, To exit press CTRL+C");
        DeliverCallback callback = (s, delivery) -> {
            String message = new String(delivery.getBody(), "utf-8");
            System.out.println("received:" + message);
        };
        channel.basicConsume(QUEUE_NAME, true, callback, new CancelCallback() {
            public void handle(String consumerTag) throws IOException {
            }
        });
    }

}
