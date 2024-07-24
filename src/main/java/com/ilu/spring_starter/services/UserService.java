package com.ilu.spring_starter.services;

import java.util.List;

import com.ilu.spring_starter.entities.User;

public interface UserService {
    List<User> findAll();

    User create(User user, String[] roles);

    User findById(String id);

    User findByUsernameOrEmail(String usernameOrEmail);

    User update(User user, String[] roles);

    boolean deleteById(String id);
}