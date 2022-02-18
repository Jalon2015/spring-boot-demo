package com.jalon.jackson1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author: syj
 * @date: 2022/2/9
 */
public class JacksonDemo {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User("admin", "123");
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);

        User readValue = objectMapper.readValue(str, User.class);
        System.out.println(readValue);

        JsonNode jsonNode = objectMapper.readTree(str);
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();
        System.out.println("username:"+username+", password:"+password);

        Map map = objectMapper.readValue(str, Map.class);
        Object username1 = map.get("username");
        System.out.println(username1);

        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        users.add(user);
        String jsonArrStr = objectMapper.writeValueAsString(users);
        System.out.println(jsonArrStr);
        List<User> userList = objectMapper.readValue(jsonArrStr, new TypeReference<List<User>>(){});
        System.out.println(userList);

        String strNew = "{\"username\":\"admin1\",\"password\":\"123\", \"email\": \"xxx.mail\"}";
        User user1 = objectMapper.readValue(strNew, User.class);
        System.out.println(user1);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user2 = objectMapper.readValue(strNew, User.class);
        System.out.println(user2);
    }
}
