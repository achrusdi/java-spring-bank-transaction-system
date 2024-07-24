package com.ilu.spring_starter.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilu.spring_starter.apis.ApiResponse;
import com.ilu.spring_starter.configs.securities.JwtSecurity;
import com.ilu.spring_starter.dto.requests.AuthRequest;
import com.ilu.spring_starter.dto.responses.AuthResponse;
import com.ilu.spring_starter.dto.responses.UserResponse;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtSecurity jwtSecurity;

    @PostMapping("/sign-up")
    public ApiResponse<UserResponse> register(@RequestBody AuthRequest request) {
        User user = userService.create(request.toEntity(), new String[] {});

        if (user == null) {
            return null;
        }

        return ApiResponse.created("User registered successfully", user.toResponse());
    }

    @PostMapping("/sign-in")
    public ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername() == null ? request.getEmail() : request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String token = jwtSecurity.generateToken(user);

        return ApiResponse.ok("User logged in successfully", AuthResponse.builder().token(token).roles(user.getRoles().stream().map(role -> role.getName()).toList()).build());
    }
}