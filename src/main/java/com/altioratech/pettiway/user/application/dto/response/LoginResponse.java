package com.altioratech.pettiway.user.application.dto.response;

import com.altioratech.pettiway.user.domain.model.Role;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String email,
        String name,
        Role role,
        String token
) {
}
