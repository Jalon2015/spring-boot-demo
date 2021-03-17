# 主题

## 定义

**主题交换机**可以指定要接收的消息的来源、种类等附加信息，有点类似于前一节的direct路由，但是要比direct路由更加灵活

> PS：可以把direct交换机的路由看作全字符串匹配，把topic交换机的路由看作正则匹配

## 知识点

### topic交换机

**路由信息(routing key)**

- topic的路由信息是由"."符号拼接成的字符串列表，一般由三个单词组成，比如`quick.gray.rabbit`，其中quick代表速度，gray代表颜色，rabbit代表种类
- 长度：最长255字节

**通配符**

- *：匹配单个单词
  - 比如`*.gray.rabbit`，代表所有灰色的兔子，速度无所谓，可以slow，也可以fast，
  - 但是*****只能**匹配一个**单词，比如`old.fast.gray.rabbit`就**不符合**这个topic
- #：匹配零个或多个单词
  - 比如`#.rabbit`,代表所有的兔子，颜色、速度都无所谓
  - 而且**#**可以**匹配多个**单词，比如`old.fast.gray.rabbit`就**符合**这个topic

> PS：通配符是用在消费者的绑定信息上，生产者用不到通配符

**模仿**

topic主题可以很轻松的模仿其他交换机

- direct交换机：如果消费者的队列绑定信息没有`*`和`#`，则可以实现direct交换机的路由功能（因为没有通配符，所以要全部匹配到才可以接收到消息，这就跟前一节的direct路由功能一致了）
- fanout交换机：如果消费者的队列绑定信息只有`#`，那么就可以实现`fanout`交换机的广播功能（该消费者会接收所有路由信息的消息）



## 代码

生产者`EmitLogTopic.java`

```java

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

```

消费者`ReceiveLogsTopic.java`，该消费者绑定信息为`system.A.*`，表示接收系统A的所有消息

```java

public class ReceiveLogsTopic {
    // 定义交换机名称
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            // 声明交换机类型：topic
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            // 生成临时队列
            String queue = channel.queueDeclare().getQueue();
            // 将队列绑定到交换机上，binding key为system.A.*，表示这个队列接收系统A的所有消息
            channel.queueBind(queue, EXCHANGE_NAME, "system.A.*");

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

消费者`ReceiveLogsTopic2.java`，该消费者绑定消息为`system.#`，表示接收所有系统的所有消息

```java

public class ReceiveLogsTopic2 {
    // 定义交换机名称
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            // 声明交换机类型：topic
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            // 生成临时队列
            String queue = channel.queueDeclare().getQueue();
            // 将队列绑定到交换机上，binding key为system.#，表示这个队列接收所有系统的所有消息
            channel.queueBind(queue, EXCHANGE_NAME, "system.#");

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

- 先运行消费者代码`ReceiveLogsTopic.java`和`ReceiveLogsTopic2.java`，然后再运行生产者代码`EmitLogTopic.java`
- 可以看到，`ReceiveLogsTopic.java`只接收系统A的消息，而`ReceiveLogsTopic2.java`同时接受系统A和系统B的消息

![image-20210317142537591](https://i.loli.net/2021/03/17/veYQqCmf8R2nh54.png)

![image-20210317142604643](https://i.loli.net/2021/03/17/rdCNcB1x2GIailJ.png)