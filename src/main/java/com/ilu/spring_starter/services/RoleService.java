package com.ilu.spring_starter.services;

import java.util.List;

import com.ilu.spring_starter.entities.Role;

public interface RoleService {
    List<Role> findAll();

    Role findById(String id);

    Role findByName(String name);

    Role create(Role role);

    Role update(Role role);

    boolean deleteById(String id);
}