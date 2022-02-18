package com.jalon.jackson7;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/7/27 15:30
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String username;

    private String password;

}
