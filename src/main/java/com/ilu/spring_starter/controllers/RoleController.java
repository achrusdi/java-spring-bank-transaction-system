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
import com.ilu.spring_starter.dto.requests.RoleRequest;
import com.ilu.spring_starter.dto.responses.RoleResponse;
import com.ilu.spring_starter.entities.Role;
import com.ilu.spring_starter.services.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<RoleResponse> register(@RequestBody RoleRequest request) {
        Role role = request.toEntity();
        role.setActive(true);

        role = roleService.create(role);

        if (role == null) {
            return null;
        }

        return ApiResponse.created("Role created successfully", role.toResponse());
    }

    @GetMapping
    public ApiResponse<List<RoleResponse>> findAll() {
        List<Role> roles = roleService.findAll();

        if (roles == null) {
            return null;
        }

        return ApiResponse.ok("Roles fetched successfully", roles.stream().map(Role::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> findById(@PathVariable String id) {
        Role role = roleService.findById(id);

        if (role == null) {
            return null;
        }

        return ApiResponse.ok("Role fetched successfully", role.toResponse());
    }

    @PutMapping
    public ApiResponse<RoleResponse> update(@RequestBody RoleRequest request) {

        Role existingRole = roleService.findById(request.getId());

        if (existingRole == null) {
            return null;
        }

        existingRole.setName(request.getName());
        existingRole.setDescription(request.getDescription());
        existingRole.setActive(request.isActive());

        Role role = roleService.update(existingRole);

        if (role == null) {
            return null;
        }

        System.out.println("=========================================");
        System.out.println("Role" + role);
        System.out.println("=========================================");

        return ApiResponse.ok("Role updated successfully", role.toResponse());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteById(@PathVariable String id) {
        if (!roleService.deleteById(id)) {
            return ApiResponse.badRequest("Role not found");
        }

        return ApiResponse.ok("Role deleted successfully");
    }
}