package com.tms.repository;

import com.tms.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends AbstractRepository<User, Integer> {

    List<User> findByLogin(String login);
}
