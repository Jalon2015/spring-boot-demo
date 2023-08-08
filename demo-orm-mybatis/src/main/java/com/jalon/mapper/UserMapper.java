package com.jalon.mapper;

import com.jalon.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("select * from orm_user where name like concat('%',#{name},'%')")
    List<User> selectAllUser(String name);
    int saveUser(User user);
    int deleteById(Long id);
}
