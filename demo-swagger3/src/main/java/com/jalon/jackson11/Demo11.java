package com.jalon.jackson11;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
注解
 * </p>
 *
 * @author: syj
 * @date: 2022/2/21
 */
public class Demo11 {
    public static void main(String[] args) throws JsonProcessingException {
        String jsonStr = "{\"username\":\"jalon\", \"birth\":\"2021-01-01 00:00:00\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonStr, User.class);
        System.out.println(user);
        SimpleDateFormat formatter
                = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(user.getBirth()));
    }
}
