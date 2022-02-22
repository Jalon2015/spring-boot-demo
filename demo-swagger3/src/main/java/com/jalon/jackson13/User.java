package com.jalon.jackson13;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

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

    private String username;

    private String animalName;

    @JsonProperty(value = "animal")
    public void setName(Map<String, Object> animal){
        this.animalName = (String) animal.getOrDefault("animalName", "");
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Animal{
    private String animalName;
}
