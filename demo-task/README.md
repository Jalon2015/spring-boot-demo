# 定时任务：Spring自带的TaskScheduler接口

## 简介

Spring自带的`TaskScheduler`主要用来执行一些定时任务，比如每天的23点执行一次任务（固定时间点）、每隔10分钟执行一次任务等（固定频率）

## 示例

- 首先写一个定时任务

  `MyTask.java`

  ```java
  @Component
  public class MyTask {
  
      @Scheduled(cron = "*/5 * * * * *")
      public void task1(){
          System.out.println("this is task1");
      }
  }
  
  ```

- 然后在主程序中添加注解`@EnableScheduling`

  ```java
  @SpringBootApplication
  @EnableScheduling
  public class TaskApplication {
      public static void main(String[] args) {
          SpringApplication.run(TaskApplication.class, args);
      }
  }
  
  ```

- 最后启动程序，就可以看到控制台的任务执行情况，每隔5s打印一次

## 知识点

- 注解`@Scheduled`：设置定时任务，支持cron表达式、fixedRate固定频率触发等；
- 注解`@EnableScheduling `：开启定时任务，可以加在主类或者配置类中
- 配置线程池大小`spring.task.scheduling.pool.size=20`：默认是1

## 参考

- [@Scheduled](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-annotation-support-scheduled)注解

- [cron表达式](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-cron-expression)

- [@EnableScheduling](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-annotation-support)注解