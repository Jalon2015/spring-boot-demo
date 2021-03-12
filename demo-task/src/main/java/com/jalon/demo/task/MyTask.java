package com.jalon.demo.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: jalon2015
 * @date: 2021/3/9 13:06
 */
@Component
public class MyTask {

    @Scheduled(cron = "*/5 * * 9 * ?")
    public void task1(){
        System.out.println("this is task1");
    }

    @Scheduled(cron = "0/5 * * 9 * *")
    public void task2(){
        System.out.println("this is task2");
    }

}
