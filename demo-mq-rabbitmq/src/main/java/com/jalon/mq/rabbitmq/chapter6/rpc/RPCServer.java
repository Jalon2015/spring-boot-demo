package com.jalon.mq.rabbitmq.chapter6.rpc;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: jalon2015
 * @date: 2021/3/16 14:17
 */
public class RPCServer {

    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

            DeliverCallback callback = (consumeTag, deliver)->{
                // 1. 解析消息，获取消息内容、消息回复队列、消息关联id
                // 1.1 回复队列：处理完请求数据后，将结果发送到这个队列
                String queueName = deliver.getProperties().getReplyTo();
                // 1.2 获取消息内容
                String msg = new String(deliver.getBody(), "utf-8");
                System.out.println("【server】received fib index: " + msg);
                // 解析出需要执行fib的参数
                int num = Integer.parseInt(msg);
                // 1.3 获取关联id，该id类似于会话id
                String correlationId = deliver.getProperties().getCorrelationId();
                // 1.4 构建回复消息的属性：主要是将关联id包含进去
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(correlationId)
                        .build();

                // 2. 执行fib函数，处理传来的消息内容
                int fibNum = fib(num);
                String fibStr = fibNum + "";
                System.out.println("【server】send fib res: " + fibStr);
                // 3. 回复消息
                channel.basicPublish("", queueName, replyProps, fibStr.getBytes("utf-8"));
            };
            channel.basicConsume(RPC_QUEUE_NAME, true, callback, consumeTag->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public static int fib(int n){
        if(n == 0) return 0;
        if(n == 1) return 1;
        return fib(n-1)+fib(n-2);
    }

}
