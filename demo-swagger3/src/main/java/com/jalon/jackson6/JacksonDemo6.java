package com.jalon.jackson6;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * <p>
 *  Jackson Date
 * </p>
 *
 * @author: syj
 * @date: 2022/2/16
 */
public class JacksonDemo6 {
    public static void main(String[] args) throws JsonProcessingException {
//        User user = new User("jalon", new Date());
//        ObjectMapper objectMapper = new ObjectMapper();
////
//        String s = objectMapper.writeValueAsString(user);
//        System.out.println(s);
////
//        User user1 = objectMapper.readerFor(User.class).readValue(s);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println(user1);
//        System.out.println(dateFormat.format(user1.getBirth()));

        User2 user2 = new User2("jalon", LocalDate.now());
        ObjectMapper objectMapper2 = new ObjectMapper();
//        objectMapper2.registerModule(new JavaTimeModule());
//        objectMapper2.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String s2 = objectMapper2.writeValueAsString(user2);
        System.out.println(s2);

        User2 user21 = objectMapper2.readValue(s2, User2.class);
        System.out.println(user21);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        TemporalAccessor parse = formatter.parse("2021-01-02");
//        System.out.println(parse);
    }
}
