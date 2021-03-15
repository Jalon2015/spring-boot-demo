# 路由

## 定义

路由就是可以自由选择你想要订阅的消息类型

比如日志有info、error类型，通过在发布时指定类型，就可以在接收时，选择性的接收

## 知识点

### 交换机

生产者不是直接把消息发布到队列，而是通过**交换机**实现，下图中的P代表生产者，X代表交换机，红色的是队列

![image-20210315170209049](https://i.loli.net/2021/03/15/zwhgMHG6Svf4xjD.png)

交换机有4种：direct, topic, headers, fanout，这里只演示direct交换机；

**direct交换机**：比前面的fanout更加灵活，可以通过绑定消息类型来过滤消息

```java
// 声明交换机类型：direct，即direct模式
channel.exchangeDeclare(EXCHANGE_NAME, "direct");
```

**发布指定类型的消息**：生产者在发布消息时，可以指定消息的类型，代码如下

```java
// 定义一个error消息
String messageError = "error: this is log error";
// 发布消息到交换机，第二个参数error就是指定消息类型
channel.basicPublish(EXCHANGE_NAME, "error", null, messageError.getBytes());
System.out.println("send: " + messageError);
```

**消费指定类型的消息**：消费者在将队列绑定到交换机时，可以指定消息的类型，代码如下

```java
// 将队列绑定到交换机上，第三个参数就是消息类型，这里为空，表示队列接收交换机传来的所有消息
channel.queueBind(queue, EXCHANGE_NAME, "");
// 将第三个参数改为error，表示队列只接受交换机传来的类型为error的消息
channel.queueBind(queue, EXCHANGE_NAME, "error");
```

![image-20210315181109654](https://i.loli.net/2021/03/15/SqRMG12v4rutNci.png)

>  PS：如果有多个队列和交换机的绑定关系，那么其中的消息类型合并到一起，比如先后绑定了info和error类型,那么该队列会同时接收info和error类型的消息

## 代码

生产者`EmitLogDirect.java`

```java

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

```

消费者`ReceiveLogsDirect.java`，该消费者同时接收`info`,`error`类型的消息

```java

public class ReceiveLogsDirect {
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
            channel.queueBind(queue, EXCHANGE_NAME, "info");
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

```

消费者`ReceiveLogsDirect2.java`，该消费者只接收`error`类型的消息

```java

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

```

## 演示效果

- 先运行消费者代码`ReceiveLogsDirect.java`和`ReceiveLogsDirect2.java`，然后再运行生产者代码`EmitLogDirect.java`
- 可以看到控制台输出如下所示，**`ReceiveLogsDirect.java`同时接收info,error消息，`ReceiveLogsDirect2.java`只接收了error消息**

![h515z-u6n8m](https://i.loli.net/2021/03/15/Nt7xmSz48dnlQ6C.gif)