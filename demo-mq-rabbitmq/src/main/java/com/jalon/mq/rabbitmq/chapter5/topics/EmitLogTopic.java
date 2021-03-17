package com.jalon.mq.rabbitmq.chapter5.topics;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: jalon2015
 * @date: 2021/3/15 16:30
 */
public class EmitLogTopic {

    // 定义交换机名称
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection("localhost");
            Channel channel = connection.createChannel();
            // 声明交换机类型：topic，即topic模式
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");

            // 定义一个error消息
            String messageError = "system.A.error: this is log error";
            // 发布消息到交换机，routing key为system.A.error，说明这个错误是系统A造成的
            channel.basicPublish(EXCHANGE_NAME, "system.A.error", null, messageError.getBytes());
            System.out.println("send: " + messageError);

            // 定义一个error消息
            String messageError2 = "system.B.error: this is log error";
            // 发布消息到交换机，routing key为system.B.error，说明这个错误是系统B造成的
            channel.basicPublish(EXCHANGE_NAME, "system.B.error", null, messageError2.getBytes());
            System.out.println("send: " + messageError2);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
