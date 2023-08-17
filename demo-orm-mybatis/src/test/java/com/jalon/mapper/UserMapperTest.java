package com.jalon.mapper;


import com.jalon.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;
    @Test
    void selectAllUser() {
        List<User> userList = userMapper.selectAllUser("ja2");
        Assertions.assertFalse(userList.isEmpty(), "列表为空");
    }
}