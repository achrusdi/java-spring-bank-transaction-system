package com.ilu.spring_starter.dto.responses;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private String id;
    private String name;
    private String description;
    private boolean isActive;
    private List<UserResponse> users;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d-M-yyyy HH:mm:ss")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "d-M-yyyy HH:mm:ss")
    private Date updatedAt;
}