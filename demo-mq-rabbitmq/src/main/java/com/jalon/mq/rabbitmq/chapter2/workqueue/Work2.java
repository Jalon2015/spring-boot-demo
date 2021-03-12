package com.jalon.mq.rabbitmq.chapter2.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 *     消费者：多个消息，定时消费
 * </p>
 * @author: jalon2015
 * @date: 2021/3/12 18:26
 */
public class Work2 {
    private final static String QUEUE_NAME = "work_queue";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("waiting for messages, to exit press CTRL+C");
            DeliverCallback callback = (s, delivery)->{
                String s1 = new String(delivery.getBody(), "utf-8");
                System.out.println("received: "+s1);
                try {
                    doWork(s1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, callback, consumeTag->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void doWork(String s1) throws InterruptedException {
        for (String c: s1.split("")) {
            if(c.equals(".")){
                Thread.sleep(1000);
            }
        }
    }
}
