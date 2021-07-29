package com.jalon;

import cn.hutool.extra.mail.MailUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/7/27 17:28
 */

@SpringBootApplication
public class EmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
        MailUtil.send("1121263265@qq.com", "2", "1", false);

    }
}
