package com.jalon.jackson4;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 *  Using Optional with Jackson
 * </p>
 *
 * @author: syj
 * @date: 2022/2/15
 */
public class JacksonDemo4 {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User("jalon", Optional.of("xiaowang"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);

        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String s = objectMapper.writeValueAsString(map);
        String s1 = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);

        System.out.println(s);
        System.out.println(s1);
        Map map1 = objectMapper.readValue(s1, Map.class);
        System.out.println(map1);


    }
}
