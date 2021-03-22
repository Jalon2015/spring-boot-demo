# 整合RabbitMQ
RabbitMQ是基于AMQP协议创建的，轻量级、高可用的消息中间件
## 核心组件
用脑图展示比较靠谱
1. AmqpAdmin:抽象接口，用来定义交换机、队列，并绑定他们之间的关系，使得消息开发更加灵活
2. RabbitAdmin：实现了AmqpAdmin
3. ConnectionFactory接口：创建消息连接，最常用的实现类是 PooledChannelConnectionFactory
4. AmqpTemplate: 消息模板，用来发送、接收消息等基础操作，属于高级别的抽象接口，类似JDBCTemplate
5. RabbitTemplate：AmqpTemplate的实现类
6. MessageListener： 消息监听器，2.0开始有了注解@MessageListen，可以很方便的监听消息
6. Container：
- 容器是消息监听器和队列之间的桥梁，要设置的属性包括ConnectionFactory，Queue, MessageListener
- 默认SimpleMessageListenerContainer，还有新出的DirectMessageListenerContainer，区别见[Choosing Container](https://docs.spring.io/spring-amqp/docs/current/reference/html/#choose-container)
