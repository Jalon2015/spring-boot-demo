package com.jalon.security.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *  权限类
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/13 14:59
 */
@Entity
@Table(name = "tb_role")
@Data
@NoArgsConstructor
@ToString
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "authority")
    private String authority;


}
