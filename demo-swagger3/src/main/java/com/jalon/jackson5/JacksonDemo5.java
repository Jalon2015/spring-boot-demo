package com.jalon.jackson5;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 *  JsonMappingException (No serializer found for class)
 * </p>
 *
 * @author: syj
 * @date: 2022/2/16
 */
public class JacksonDemo5 {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User("jalon","xiaowang");
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor., JsonAutoDetect.Visibility.ANY);
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String s = objectMapper.writeValueAsString(user);
        System.out.println(s);

    }
}
