package com.jalon.jackson10;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * <p>
注解
 * </p>
 *
 * @author: syj
 * @date: 2022/2/21
 */
public class Demo10 {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("jalon", new Date());
        String s = objectMapper.writeValueAsString(user);
        System.out.println(s);
        System.out.println(user.getUsername());
    }
}
