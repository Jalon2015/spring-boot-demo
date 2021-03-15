package com.jalon.mq.rabbitmq.chapter4.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: jalon2015
 * @date: 2021/3/15 16:30
 */
public class EmitLogDirect {

    // 定义交换机名称
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection("localhost");
            Channel channel = connection.createChannel();
            // 声明交换机类型：direct，即直接模式
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 定义一个error消息
            String messageError = "error: this is log error";
            // 发布消息到交换机
            channel.basicPublish(EXCHANGE_NAME, "error", null, messageError.getBytes());
            System.out.println("send: " + messageError);

            // 定义一个info消息
            String messageInfo = "info: this is log info";
            // 发布消息到交换机
            channel.basicPublish(EXCHANGE_NAME, "info", null, messageInfo.getBytes());
            System.out.println("send: " + messageInfo);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
