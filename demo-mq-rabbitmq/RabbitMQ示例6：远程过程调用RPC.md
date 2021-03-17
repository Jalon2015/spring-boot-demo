# 远程过程调用RPC

## 定义

远程过程调用，就是在一个机器上调用另一个远程机器上的函数，并返回结果

## 知识点

### 消息属性

**回复队列(`replyTo`)**

>  定义：用来接收远程处理结果的队列

比如本地向远程请求调用函数fun，同时传递了这个`replyTo`属性，那么远程机器执行完后，会将结果返回到这个`replyTo`队列

**关联ID(`correlationId`)**

> 唯一性：所有消息的关联ID都是唯一的，类似我们的身份证ID

比如本地向远程请求调用函数fun，同时传递了`correlationId`属性，那么远程机器执行完后，会将`correlationId`添加到消息属性中；

当本地收到消息后，会校验收到的`correlationId`是否和本地的一致

**RPC工作流程**

![image-20210317114944317](https://i.loli.net/2021/03/17/1HCfgrD8eu5K93c.png)

- 客户端发送消息到`rpc_queue`队列，其中消息包含了两个属性`reply_to`和`correlation_id`
- 服务端从`rpc_queue`队列接收到消息，进行处理，将结果和`correlation_id`属性发送到`reply_to`队列
- 客户端从`reply_to`队列接收到消息，比对`correlation_id`关联ID，如果一致，则进行对应的处理

## 代码

> PS：这里的代码做了简化，主要是为了介绍RPC工作流程，完整代码可参考官网[RPCServer.java](https://github.com/rabbitmq/rabbitmq-tutorials/blob/master/java/RPCServer.java)、[RPCClient](https://github.com/rabbitmq/rabbitmq-tutorials/blob/master/java/RPCClient.java)

客户端`RPCClient.java`

```java

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

```

服务端`RPCServer.java`

```java

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

```

## 演示效果

- 先运行服务端代码`RPCServer.java`，然后再运行客户端代码`RPCClient.java`

- 可以看到，客户端发送了10，收到了55；服务端接收到了10，发送了55；


![image-20210317142802139](https://i.loli.net/2021/03/17/1F2XdNul6OB5AGx.png)

![image-20210317142834587](https://i.loli.net/2021/03/17/6yUsuCf7eNt4cEQ.png)