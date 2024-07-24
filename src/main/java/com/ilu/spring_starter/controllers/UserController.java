package com.ilu.spring_starter.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ilu.spring_starter.apis.ApiResponse;
import com.ilu.spring_starter.dto.requests.UserRequest;
import com.ilu.spring_starter.dto.responses.UserResponse;
import com.ilu.spring_starter.entities.User;
import com.ilu.spring_starter.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> findAll() {
        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            return ApiResponse.ok("Users not found", List.of());
        }
        return ApiResponse.ok("Users fetched successfully", users.stream().map(User::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable String id) {
        User user = userService.findById(id);
        if (user == null) {
            return ApiResponse.error("User not found");
        }
        return ApiResponse.ok("User fetched successfully", user.toResponse());
    }

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody UserRequest user) {
        User createdUser = userService.create(user.toEntity(), user.getRoles());

        if (createdUser == null) {
            return ApiResponse.error("User not created");
        }
        
        return ApiResponse.created("User created successfully", createdUser.toResponse());
    }

    @PutMapping
    public ApiResponse<UserResponse> update(@RequestBody UserRequest user) {

        User existingUser = userService.findById(user.getId());
        if (existingUser == null) {
            return ApiResponse.error("User not found");
        }

        User updatedUser = userService.update(user.toEntity(), user.getRoles());

        if (updatedUser == null) {
            return ApiResponse.error("User not updated");
        }
        return ApiResponse.ok("User updated successfully", updatedUser.toResponse());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<UserResponse> delete(@PathVariable String id) {
        if (userService.deleteById(id)) {
            return ApiResponse.ok("User deleted successfully");
        }

        return ApiResponse.error("User not deleted");
    }
}