package com.jalon.mq.rabbitmq.chapter1.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>
 *     消息生产者
 *     查看消息队列：在powershell中运行命令 rabbitmqctl.bat list_queues
 * </p>
 * @author: jalon2015
 * @date: 2021/3/12 17:23
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try{
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("send:" + message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
