package com.jalon.orm.jpa;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nonnegative;
/**
 * @author: jalon2015
 * @date: 2021/3/11 14:29
 */
@Component
@ConfigurationProperties("jalon")
@Data
public class Test {
    @Nonnegative
    private String name = "n";
    private String first = "a";
    public static void main(String[] args) {
        // 0~10之间的随机整数
        for (int i = 0; i < 10; i++) {
            System.out.println(Math.round(Math.random()*10));
        }

    }

}

