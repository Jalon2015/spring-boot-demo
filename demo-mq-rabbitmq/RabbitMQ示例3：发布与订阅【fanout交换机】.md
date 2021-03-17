# 发布与订阅

## 概念

生产者负责发布消息，消费者负责接收消息，生产者每**发布**一条消息，所有**订阅**的消费者都可以收到**相同**的消息（不同于之前的消息分配策略，所有的消费者均匀分配所有的消息）

## 知识点

### 交换机

在Rabbit'MQ中，生产者不是直接把消息发布到队列（它甚至不知道队列的存在），而是通过**交换机**实现，下图中的P代表生产者，X代表交换机，红色的是队列

![image-20210315170209049](https://i.loli.net/2021/03/15/zwhgMHG6Svf4xjD.png)

> PS：前面的章节，没有涉及到交换机的概念，都是直接对队列进行发送和接收，是因为用了默认的交换机direct

**默认的交换机**

默认的交换机类型是`direct`，交换机名字为空字符串`""`；

默认交换机有个很有用的功能：只要将路由信息`routing key`绑定到默认交换机`""`，那么交换机就会自动创建一个跟路由信息`routing key`同名的队列，并把交换机路由到该队列（如果队列已经存在，则直接路由到该队列）

比如下面的代码，默认交换机`""`会自动创建一个`hello`队列(这里的`routing key` = `hello`），并将消息发送到`hello`队列

```java
channel.basicPublish("", "hello", null, message.getBytes());
```

上面的代码也可以看作是生产者`P`直接将消息`message`发送到队列`hello`（但是从技术上来讲是不成立的，还是要经过默认交换机）

**交换机种类**

交换机总共有4种：direct, topic, headers, fanout

| 交换机类型       | 交换机默认名称                          |
| :--------------- | :-------------------------------------- |
| Direct exchange  | (Empty string) and amq.direct           |
| Fanout exchange  | amq.fanout                              |
| Topic exchange   | amq.topic                               |
| Headers exchange | amq.match (and amq.headers in RabbitMQ) |

**fanout交换机**：fanout会把所有的消息发布到所有已知的队列（即绑定到fanout交换机的队列），类似广播机制

```java
// 声明交换机类型：fanout，即广播模式
channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
```

**binding绑定**：交换机和队列之间的关系就是绑定关系，如下图所示

```java
// 将队列绑定到交换机上，第三个参数就是消息类型，这里为空，表示队列接收交换机传来的所有消息
channel.queueBind(queue, EXCHANGE_NAME, "");
```

![image-20210315170545616](https://i.loli.net/2021/03/15/9MICBOyzP8icrRA.png)

### 临时队列

顾名思义，就是临时用一下，用完就会删掉（比如程序退出，临时队列就会被清掉）

临时队列可以直接从系统创建，生成的队列名为`amq.gen-随机字符串`，比如`amq.gen-JzTY20BRgKO-HjmUJj0wLg`

比如这里要测试的日志示例，就很适合用这种临时队列

```java
// 生成临时队列
String queue = channel.queueDeclare().getQueue();
```



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

![image-20210317142005653](https://i.loli.net/2021/03/17/G6hn7ZasCdEuQO3.png)

![image-20210317142035145](https://i.loli.net/2021/03/17/Q6u9lez4EKpH5nm.png)