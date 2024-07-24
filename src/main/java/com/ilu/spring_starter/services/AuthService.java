package com.ilu.spring_starter.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ilu.spring_starter.entities.User;

public interface AuthService extends UserDetailsService {
    User findById(String id);
}