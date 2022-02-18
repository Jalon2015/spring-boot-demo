package com.jalon.jackson7;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.*;

/**
 * <p>
 *  Jackson – Unmarshall to Collection/Array
 * </p>
 *
 * @author: syj
 * @date: 2022/2/16
 */
public class Demo7 {
    public static void main(String[] args) throws JsonProcessingException {
        User user = new User("jalon", "xiaowang");
        User user2 = new User("jalon2", "xiaowang2");
        List<User> list = Arrays.asList(user, user2);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(list);

//        TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {
//        };
//        List<User> list1 = objectMapper.readValue(s, typeReference);
//        System.out.println( list1);
        CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        List<User> list2 = objectMapper.readValue(s, collectionType);

    }
}
