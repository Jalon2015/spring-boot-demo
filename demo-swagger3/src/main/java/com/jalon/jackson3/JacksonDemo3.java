package com.jalon.jackson3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalon.jackson3.User;
/**
 * <p>
 *  Ignore Null Fields with Jackson
 * </p>
 *
 * @author: syj
 * @date: 2022/2/14
 */
public class JacksonDemo3 {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);



    }
}
