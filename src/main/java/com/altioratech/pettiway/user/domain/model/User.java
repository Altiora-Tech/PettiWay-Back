package com.altioratech.pettiway.user.domain.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private UUID id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private Role role;
    private UserStatus status;
    private AuthProvider provider;
    private boolean verified;
    private boolean profileComplete;
    private UUID profilePhotoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ======================
    // Métodos de dominio puro
    // ======================

    public void verifyAccount() {
        this.verified = true;
    }

    public void deactivate() {
        this.status = UserStatus.INACTIVE;
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public boolean canManageUsers() {
        return this.role == Role.SUPER_ADMIN || this.role == Role.BUSINESS;
    }

    public boolean isProfessional() {
        return this.role == Role.SITTER || this.role == Role.BUSINESS;
    }
}