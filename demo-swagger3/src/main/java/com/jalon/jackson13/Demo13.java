package com.jalon.jackson13;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
注解
 * </p>
 *
 * @author: syj
 * @date: 2022/2/22
 */
public class Demo13 {
    public static void main(String[] args) throws JsonProcessingException {
        String s = "{\"username\": \"jalon\", \"animal\":{\"animalName\": \"tutu\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(s, User.class);
        System.out.println(user);

    }
}
