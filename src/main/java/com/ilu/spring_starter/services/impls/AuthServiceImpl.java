package com.ilu.spring_starter.services.impls;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.services.AuthService;
import com.ilu.spring_starter.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsernameOrEmail(username);

        return user;
    }

    @Override
    public User findById(String id) {
        return userService.findById(id);
    }
    
}