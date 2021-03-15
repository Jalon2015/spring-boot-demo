package com.jalon.mq.rabbitmq.chapter3.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: jalon2015
 * @date: 2021/3/15 16:30
 */
public class ReceiveLog {
    // 定义交换机名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            // 声明交换机类型：fanout，即广播模式
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            // 生成临时队列
            String queue = channel.queueDeclare().getQueue();
            // 将队列绑定到交换机上
            channel.queueBind(queue, EXCHANGE_NAME, "");

            System.out.println("waiting for messages, to exit press CTRL+C");

            DeliverCallback callback = (consumerTag, delivery)->{
                String message = new String(delivery.getBody(), "utf-8");
                System.out.println("received: " + message);
            };
            // 从队列中接收消息，收到消息后自动确认
            channel.basicConsume(queue, true, callback, consumerTag->{});

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
