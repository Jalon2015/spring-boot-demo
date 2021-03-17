# 工作队列
## 定义
> 工作队列主要是用来解决资源耗时问题，让服务器快速响应

比如web服务器，某个请求的后台操作可能要耗时十几分钟，此时如果没有工作队列，那么请求就会响应超时
但是有了工作队列，就可以先把请求要处理的任务放到工作队列中，然后快速响应请求

## 基础代码
> PS：这里我们用`Thread.sleep()`来模拟耗时操作，同时消息内容中的"."的长度就代表了耗时的秒数
### 生产者
`NewTask.java`

```java
public class NewTask {

    private final static String QUEUE_NAME = "work_queue";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 耗时1S
            String message = "hello.";
            for (int i = 0; i < 9; i++) {
                channel.basicPublish("", QUEUE_NAME,null, message.getBytes());
                System.out.println("send:" + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
```
### 消费者
`Work.java`

```java

public class Work {
    private final static String QUEUE_NAME = "work_queue";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("waiting for messages, to exit press CTRL+C");
            DeliverCallback callback = (s, delivery)->{
                String s1 = new String(delivery.getBody(), "utf-8");
                System.out.println("received: "+s1);
                try {
                    doWork(s1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("done");
                }
            };
            channel.basicConsume(QUEUE_NAME, true, callback, consumeTag->{});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    
    // 有多个".",就耗时多少秒
    private static void doWork(String s1) throws InterruptedException {
        for (String c: s1.split("")) {
            if(c.equals(".")){
                Thread.sleep(1000);
            }
        }
    }
}
```
## 知识点

### 轮询

> 定义：消息分发默认是均匀分发到每个消费者手中，这种分发机制就是轮询机制



比如有一个生产者，三个消费者；生产者发了九条消息，那么三个消费者每人可以拿到三条消息



#### 示例：

将上面的消费者程序Work再复制两份，总共三个消费者`Work1.java`,`Work2.java`,`Work3.java`

#### 步骤：

1. 依次启动这三个消费者

2. 最后启动生产者程序`NewTask.java`

3. 从控制台可以看到，平均每个消费者拿到三条消息

![3dkgo-zbkn7](https://i.loli.net/2021/03/15/uvFjJAcS1iyVx7w.gif)

### 消息确认

> 定义：生产者发送消息，消费者接收消息；当消费者收到消息，会给生产者回复**确认收到**（默认手动），然后生产者就会立即删除该消息



之所以默认手动，是因为**自动存在**的**隐患**（消息丢失）：

- 如果消费者执行的任务比较耗时，那么有可能在任务执行过程中，该**消费者挂掉**；
- 但是此时消费者已经删除了这条消息，那么结果就是这条**消息丢失**；
- 而且给这个消费者发送的其他**后续消息**（已确认收到，但是还没处理）也**一并丢失**了。



但是**手动不存在**这个**隐患**，手动会把**挂掉的**消费者里面的**消息**，**重新分配**到其他消费者

>  PS：手动存在的隐患是：如果消费者忘记确认消息，那么RabbitMQ将不会释放那些没有收到回复的消息，从而导致占用内存越来越大，直到该消费者退出；此时那些未应答的消息会分配给其他消费者（类似随机分配）



下面用表格列出：手动确认和自动确认的区别

|          | 手动确认                                                     | 自动确认                                     |
| -------- | ------------------------------------------------------------ | -------------------------------------------- |
| 消息丢失 | 不会                                                         | 会（已被确认的消息，会被删除，导致消息丢失） |
| 消息阻塞 | 会（忘记确认消息，导致该消费者接收的其他后续消息也无法处理） | 不会                                         |

**推荐**用**手动**确认的方式来告诉生产者，我收到消息了（一般在任务完成后，手动回复确认消息）

#### 示例1：

> 演示内容：手动确认时，忘记确认，会发生什么？

修改`Work3.java`，其中1. 和2. 是修改的部分

```java

// 2. === begin ===
// 注释掉手动确认的代码，假设忘记了手动确认
// channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
System.out.println("forget ack");
// 2. === end ===
               
// 1. === begin ===
// 这里将之前的自动确认改为手动确认
boolean autoAck = false;
channel.basicConsume(QUEUE_NAME, autoAck, callback, consumeTag->{});
// 1. === end ===

```

#### 步骤

- 三个消费者顺序启动(Work1, Work2, Work3)，然后启动生产者(NewTask)

- 观察控制台，如下所示

  ![l72uu-46ra9](https://i.loli.net/2021/03/15/zqOjupUrYvHewl5.gif)

> 结论：可以看到，每个消费者都是接收均等的三条消息，但是Work3的三条消息都没有确认，此时这三条消息就会一直阻塞

#### 示例2：

>  演示内容：手动确认时，如果中断消费者，会发生什么？

继续上面的示例1，代码不变

#### 步骤：

- 上面的例子中，每个消费者都有三条消息，其中Work3的三条消息都还没确认

- 此时中断Work3，可以看到，Work1又执行了1条（总共4条），Work2又执行了2条（总共5条）

  ![fyewy-z0etk](https://i.loli.net/2021/03/15/AhefpLWx6OC5H7R.gif)

  


> 结论：手动确认时，如果中断消费者，那么该消费者已接受但尚未确认的消息，会分配给其他消费者(伪随机，不是完全随机)

### 消息持久化

上面的手动确认，只是保证了消费者如果挂掉，消息不会丢失
但是如果是RabbitMQ服务挂了呢？

可以通过设置参数来保证队列和消息基本不会丢失（不能完全保证不丢失,要完全保证不丢失，可以参考[publisher confirms](https://www.rabbitmq.com/confirms.html)）

```java
// 1. 第二个参数设置为true，生产者和消费者都要改
channel.queueDeclare(QUEUE_NAME, true, false, false, null); 
// 2. basic_properties设置为PERSISTENT_TEXT_PLAIN
channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes()); 
```

#### 示例

修改`NewTask.java`

```java
// 1. 重新定义一个队列：因为RabbitMQ不允许以不同的参数 重复定义同一个队列
private final static String QUEUE_NAME_DURABLE = "work_queue_durable";

// 2. 队列持久化：如果RabbitMQ服务挂了，保证队列还存在
boolean durable = true;
channel.queueDeclare(QUEUE_NAME_DURABLE, durable, false, false, null);

// 3.消息持久化：如果RabbitMQ服务挂了，保证消息还存在；
// 这里将basicProperties属性设置为 PERSISTENT_TEXT_PLAIN
channel.basicPublish("", QUEUE_NAME_DURABLE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

```

修改`Work1.java`,`Work2.java`,`Work3.java`，下面的代码同步修改到三个消费者中

```java
// 1. 重新定义一个队列：因为RabbitMQ不允许以不同的参数 重复定义同一个队列
private final static String QUEUE_NAME_DURABLE = "work_queue_durable";

// 2. 队列持久化：如果RabbitMQ服务挂了，保证队列还存在
boolean durable = true;
channel.queueDeclare(QUEUE_NAME_DURABLE, durable, false, false, null);

// 3. 修改消费时的队列名称
channel.basicConsume(QUEUE_NAME_DURABLE, true, callback, consumeTag->{});

```

### 公平分配

前面的例子，我们看到，就算消费者忘记手动确认，RabbitMQ还是将消息均匀的分配给了每个消费者（即忘记确认的消费者后续还会收到消息）
其实这是不合理的，因为这样就导致，那些忘记确认的消费者一直占着消息不去处理，造成消息阻塞，RabbitMQ占用内存也会越来越大

> 定义：当消费者确认了一个消息后，RabbitMQ才会给它分配下一个消息，从而充分利用消费者的工作线程

#### 示例

修改刚才的`Work3.java`，因为`Work3.java`中忘记了手动确认，符合这个测试场景

```java
// 设置公平分配策略，即消费者确认了一个消息后，RabbitMQ才会给它分配下一个消息
int prefetchCount = 1;
channel.basicQos(prefetchCount);
```

#### 步骤

- 三个消费者顺序启动(Work1, Work2, Work3)，然后启动生产者(NewTask)
- 观察控制台，可以看到，Work3只接受了一条消息，Work1和Work2分别接收了4条消息
- ![2h1pu-urzvu](https://i.loli.net/2021/03/15/1As7oZKjqLryTY5.gif)



> 结论：通过在消费者中设置`channel.basicQos(1);`，可以保证消息的公平分配



## 参考

[RabbitMQ官网教程：第二节](https://www.rabbitmq.com/tutorials/tutorial-two-java.html)