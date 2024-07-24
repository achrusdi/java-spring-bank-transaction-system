package com.ilu.spring_starter.dto.requests;

import com.ilu.spring_starter.entities.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String id;
    private String name;
    private String description;
    private boolean isActive;

    public Role toEntity() {
        return Role.builder()
                .id(id)
                .name(name)
                .description(description)
                .isActive(isActive)
                .build();
    }
}