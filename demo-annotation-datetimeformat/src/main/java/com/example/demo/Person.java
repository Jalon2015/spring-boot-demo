package com.example.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *  实体类
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/5/14 9:15
 */
@Getter
@Setter
@ToString
public class Person {
    private int age;

    // 解析非JSON数据中的日期字符串
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // 解析JSON数据中的日期字符串
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birth;
}
