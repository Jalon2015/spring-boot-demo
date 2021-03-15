package com.jalon.mq.rabbitmq.chapter4.routing;

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
public class ReceiveLogsDirect2 {
    // 定义交换机名称
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            // 声明交换机类型：direct，即直接模式
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            // 生成临时队列
            String queue = channel.queueDeclare().getQueue();
            // 将队列绑定到交换机上，添加路由名称 info, error
            channel.queueBind(queue, EXCHANGE_NAME, "error");

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
