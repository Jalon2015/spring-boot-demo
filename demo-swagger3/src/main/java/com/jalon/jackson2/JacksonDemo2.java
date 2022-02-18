package com.jalon.jackson2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jalon.jackson2.User;

/**
 * <p>
 *  Jackson Ignore Properties on Marshalling
 * </p>
 *
 * @author: syj
 * @date: 2022/2/9
 */
public class JacksonDemo2 {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123");
        ObjectMapper objectMapper = new ObjectMapper();

//        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("password");
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider().addFilter("myFilter", filter);

        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);

        String str2 = "{\"username\":\"admin\",\"password\":\"admin\"}";
        User user1 = objectMapper.readValue(str2, User.class);
        System.out.println(user1);
    }
}
