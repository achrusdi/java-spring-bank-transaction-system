package com.ilu.spring_starter.dto.requests;

import com.ilu.spring_starter.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    private String id;
    private String username;
    private String email;
    private String password;
    private String[] roles;

    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .build();
    }
}