package com.jalon.orm.jpa;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * @author: jalon2015
 * @date: 2021/3/10 16:37
 */

@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackages = "com.jalon.orm.jpa")

public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    @Autowired
    UserRepository repository;
    @Autowired
    Test test;
    @GetMapping("/demo")
    public String demo(){
        long count = repository.countByLastname("jalon");
        System.out.println(count);
        // 删除
        long c = repository.deleteByLastname("jalon");
        System.out.println(c);

        //        List<UserEntity> userEntity = repository.findByLastnameOrderByFirstnameDesc("jalon");
//        System.out.println( userEntity.stream().map(m->m.toString()).collect(joining()));
        List<Test> list = Lists.newArrayList(new Test());
        String a = A.A1.toString();
        String b = A.A1.name();
        System.out.println("a:"+a);
        System.out.println("b:"+b);

        return test.getName();
    }

}

enum A{
    A1(1);
    int n;

    A(int i) {
        n = i;
    }
}
