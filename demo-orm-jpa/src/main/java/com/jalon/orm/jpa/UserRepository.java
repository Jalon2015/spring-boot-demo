package com.jalon.orm.jpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author: jalon2015
 * @date: 2021/3/10 16:44
 */
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    long countByLastname(String lastname);

    @Modifying
    @Transactional
    long deleteByLastname(String lastname);

    List<UserEntity> findByLastnameOrderByFirstnameDesc(String lastname);

}
