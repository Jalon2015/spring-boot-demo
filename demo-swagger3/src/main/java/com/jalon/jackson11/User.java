package com.jalon.jackson11;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 */

@Data
@NoArgsConstructor
public class User {

    private String username;

//    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date birth;

//    @JsonCreator
//    public User(@JsonProperty("name") String username) {
//        this.username = username;
//    }

//    @JsonSetter(value = "name")
//    public void setName(String name){
//        this.username = name;
//    }
}
