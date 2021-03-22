package com.jalon.mq.rabbitmq;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author: jalon2015
 * @date: 2021/3/22 16:20
 */
public class Demo {
    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(10);
        BigDecimal b = new BigDecimal(10);
        System.out.println(Objects.equals(a, b));
        Integer a1 = new Integer(10);
        Integer a2 = new Integer(10);
        System.out.println(Objects.equals(a1, a2));
    }
}
