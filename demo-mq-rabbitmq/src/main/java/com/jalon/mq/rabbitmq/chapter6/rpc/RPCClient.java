package com.jalon.mq.rabbitmq.chapter6.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author: jalon2015
 * @date: 2021/3/17 10:51
 */
public class RPCClient {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            String message = "10";
            // 1. 定义消息属性
            // 1.1. 定义唯一的correlationId：用来区分消息，服务端处理完会将该id包含到消息属性中
            final String correlationId = UUID.randomUUID().toString();
            // 1.2 定义回复队列：服务端处理完会将结果发送到这个回复队列
            String replyQueueName = channel.queueDeclare().getQueue();
            AMQP.BasicProperties properties = new AMQP.BasicProperties()
                    .builder()
                    .correlationId(correlationId)
                    .replyTo(replyQueueName)
                    .build();
            // 2. 发布消息到RPC队列
            channel.basicPublish("", RPC_QUEUE_NAME, properties, message.getBytes());
            System.out.println("【client】send fib index:" + message);
            // 3. 接收消息的回调函数：接收服务端的消息，并进行处理
            DeliverCallback callback = (consumeTag, deliver)->{
                // 3.1. 取出消息的correlationId
                String correlationId2 = deliver.getProperties().getCorrelationId();
                // 3.2. 跟之前发送消息时的做对比，如果一致，则打印结果
                if(correlationId2.equals(correlationId)){
                    String res = new String(deliver.getBody(), "utf-8");
                    System.out.println("【client】received fib res:" + res);
                }
                // 3.3. 如果不一致，则忽略
            };
            channel.basicConsume(replyQueueName, true, callback, consumeTag->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
