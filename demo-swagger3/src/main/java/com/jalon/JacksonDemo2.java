package com.jalon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        User user = new User("admin", "123", new String[]{"jalon"});
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(String[].class, MyIgnoreType.class);
        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);
    }
}
