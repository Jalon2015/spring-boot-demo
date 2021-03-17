package com.jalon.mq.rabbitmq.chapter6.rpc;

import java.util.Timer;

/**
 * @author: jalon2015
 * @date: 2021/3/16 15:55
 */
public class Demo {
    public static void main(String[] args) {
        Object o = new Object();

        while (true){
            synchronized (o){
                try {
                    System.out.println("1");
                    o.wait();
                    System.out.println("2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
