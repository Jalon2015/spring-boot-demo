package com.jalon.jackson12;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @JsonView(View.Visible.class)
    private String username;

//    @JsonUnwrapped
    @JsonView(View.Invisible.class)
    private Animal animal;

//    @JsonProperty(value = "name")
//    public void setUsername(String name){
//        this.username = name;
//    }
//
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date birth;
//
//    @JsonProperty(value = "name")
//    public String getUsername(){
//        return this.username;
//    }

}
@Data
@AllArgsConstructor
@NoArgsConstructor
class Animal{
    private String animalName;
}
