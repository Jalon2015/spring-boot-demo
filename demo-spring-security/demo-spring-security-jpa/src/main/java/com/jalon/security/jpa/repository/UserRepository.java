package com.jalon.security.jpa.repository;

import com.jalon.security.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <p>
 *  用户映射接口
 * </p>
 *
 * @author: JavaLover
 * @date: 2021/4/13 15:31
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginName(String loginName);
}
