package com.altioratech.pettiway.user.application.dto.response;

import com.altioratech.pettiway.user.domain.model.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Role role,
        boolean verified
) {}
