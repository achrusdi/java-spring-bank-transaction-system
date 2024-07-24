package com.ilu.spring_starter.entities;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ilu.spring_starter.dto.responses.RoleResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("deprecation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@SQLDelete(sql = "UPDATE roles SET deleted_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Instant updatedAt;

    @Column(name = "deleted_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Instant deletedAt;

    public RoleResponse toResponse() {
        return RoleResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .isActive(isActive)
                .users(users == null ? List.of() : users.stream().map(User::toResponse).toList())
                .createdAt(createdAt == null ? null : Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant()))
                .updatedAt(updatedAt == null ? null : Date.from(updatedAt.atZone(ZoneId.systemDefault()).toInstant()))
                .build();
    }
}