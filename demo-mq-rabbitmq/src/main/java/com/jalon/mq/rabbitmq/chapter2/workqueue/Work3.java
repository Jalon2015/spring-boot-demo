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
public class Work3 {
    private final static String QUEUE_NAME = "work_queue";
    // 重新定义一个队列：因为RabbitMQ不允许以不同的参数 重复定义同一个队列
    private final static String QUEUE_NAME_DURABLE = "work_queue_durable";
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            // 设置公平分配策略，即消费者确认了一个消息后，RabbitMQ才会给它分配下一个消息
            int prefetchCount = 1;
            channel.basicQos(prefetchCount);

            // 队列持久化：如果RabbitMQ服务挂了，保证队列还存在
            boolean durable = true;
            channel.queueDeclare(QUEUE_NAME_DURABLE, durable, false, false, null);

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
                    // 注释掉手动确认的代码，假设忘记了手动确认
//                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    System.out.println("forget ack");
                }
            };
            // 这里将之前的自动确认改为手动确认
            boolean autoAck = false;
            channel.basicConsume(QUEUE_NAME_DURABLE, autoAck, callback, consumeTag->{});

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
