package com.jalon.jackson8;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 Compare Two JSON Objects with Jackson
 * </p>
 *
 * @author: syj
 * @date: 2022/2/18
 */
public class Demo8 {
    public static void main(String[] args) throws JsonProcessingException {

        String s1 = "{\"name\":\"jalon\", \"weight\": 100}";
        String s2 = "{\"name\":\"jalon\", \"weight\": 100.0}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(s1);
        JsonNode jsonNode2 = objectMapper.readTree(s2);
        System.out.println(jsonNode.equals(jsonNode2));
        NumericNodeComparator comparator = new NumericNodeComparator();
        System.out.println(jsonNode.equals(comparator, jsonNode2));

        String s3 = "{\"name\":\"jalon\"}";
        String s4 = "{\"name\":\"Jalon\"}";

        boolean equals = objectMapper.readTree(s3).equals(objectMapper.readTree(s4));
        System.out.println(equals);

        TextNodeComparator comparator1 = new TextNodeComparator();
        boolean equals2 = objectMapper.readTree(s3).equals(comparator1, objectMapper.readTree(s4));
        System.out.println(equals2);

        String s6 = "{\"name\":\"jalon\", \"friend\": {\"name\":\"xiao\"}}";
        String s7 = "{\"name\":\"jalon\", \"friend\": {\"name\":\"da\"}}";
        JsonNode jsonNode6 = objectMapper.readTree(s6);
        JsonNode jsonNode7 = objectMapper.readTree(s7);
        System.out.println(jsonNode6.equals(jsonNode7));

        String s8 = "{\"name\":\"jalon\", \"girlFriend\": [{\"name\":\"xiaomei\"}]}";
        String s9 = "{\"name\":\"jalon\", \"girlFriend\": [{\"name\":\"lili\"}]}";
        JsonNode jsonNode8 = objectMapper.readTree(s8);
        JsonNode jsonNode9 = objectMapper.readTree(s9);
        System.out.println(jsonNode8.equals(jsonNode9));


    }
}
