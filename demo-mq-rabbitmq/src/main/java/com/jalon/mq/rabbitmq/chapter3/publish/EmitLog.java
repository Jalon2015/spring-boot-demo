package com.jalon.mq.rabbitmq.chapter3.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: jalon2015
 * @date: 2021/3/15 16:30
 */
public class EmitLog {

    // 定义交换机名称
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection("localhost");
            Channel channel = connection.createChannel();
            // 声明交换机类型：fanout，即广播模式
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = "log: this is log info";
            // 发布消息到交换机
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("send: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
