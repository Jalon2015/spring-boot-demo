package com.jalon.jackson12;

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
 * @date: 2022/2/22
 */
public class Demo12 {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User jalon = new User("jalon",new Animal("tutu"));
        String s = objectMapper.writerWithView(View.Visible.class).writeValueAsString(jalon);
        System.out.println(s);

//        User user = objectMapper.readValue(s, User.class);
//        System.out.println(user);
    }
}
