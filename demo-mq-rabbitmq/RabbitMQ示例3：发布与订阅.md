# 发布与订阅

## 定义

生产者负责发布消息，消费者负责接收消息，生产者每发布一条消息，所有订阅的消费者都可以收到相同的消息（不同于之前的消息分配策略，所有的消费者均匀分配所有的消息）

## 知识点

### 交换机

生产者不是直接把消息发布到队列，而是通过**交换机**实现，下图中的P代表生产者，X代表交换机，红色的是队列

![image-20210315170209049](https://i.loli.net/2021/03/15/zwhgMHG6Svf4xjD.png)

交换机有4种：direct, topic, headers, fanout，这里只演示fanout交换机；

**fanout交换机**：fanout会把所有的消息发布到所有已知的队列

```java
// 声明交换机类型：fanout，即广播模式
channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
```

**binding绑定**：交换机和队列之间的关系就是绑定关系，如下图所示

```java
// 将队列绑定到交换机上
channel.queueBind(queue, EXCHANGE_NAME, "");
```

![image-20210315170545616](https://i.loli.net/2021/03/15/9MICBOyzP8icrRA.png)

### 临时队列

顾名思义，就是临时用一下，用完就会删掉（比如程序退出，临时队列就会被清掉）

这里测试的日志demo，很适合用这种临时队列

```java
// 生成临时队列
String queue = channel.queueDeclare().getQueue();
```

生成的队列名为`amq.gen-随机字符串`，比如`amq.gen-JzTY20BRgKO-HjmUJj0wLg`

## 代码

生产者`EmitLog.java`

```java

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
```



消费者`ReceiveLog.java`, `ReceiveLog2.java`（这两个消费者代码一致）

```java

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
```

## 演示效果

- 先运行消费者代码`ReceiveLog.java`和`ReceiveLog2.java`，然后再运行生产者代码`EmitLog.java`
- 可以看到控制台输出如下所示，**两个消费者都收到了相同的消息**

![p8328-3bops](https://i.loli.net/2021/03/15/M2dlrhvSKORGUB5.gif)

