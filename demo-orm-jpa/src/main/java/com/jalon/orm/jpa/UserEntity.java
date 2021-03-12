package com.jalon.orm.jpa;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.NonNullApi;

import javax.persistence.*;

/**
 * @author: jalon2015
 * @date: 2021/3/10 17:15
 */

@Entity
@Data
@ToString
@Table(name = "user")
public class UserEntity{
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name="lastname")
    private String lastname;
}
