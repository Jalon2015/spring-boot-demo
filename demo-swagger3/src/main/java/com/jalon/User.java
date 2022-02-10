package com.jalon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/7/27 15:30
 */

@Schema
@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonIgnoreProperties(value = { "password"})
public class User {

    @Schema(name = "username", description = "用户名", defaultValue = "javalover", example = "javalover")
    private String username;

    @Schema(name = "password", description = "密码", defaultValue = "123456", example = "123456")
    public String password;
    public String[] password2;

    // 隐藏这个属性，这样接口文档的请求参数中就看不到这个属性
//    @Schema(hidden = true)
//    private String email;

}
