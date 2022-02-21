package com.jalon.jackson10;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;

    private Date birth;

    @JsonSerialize(using = CustomDateSerializer.class)
    public Date getBirth(){
        return birth;
    }

    @JsonGetter(value = "username")
    public String getMyUsername(){
        return "actual name";
    }
}
