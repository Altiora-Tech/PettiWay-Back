package com.altioratech.pettiway.user.application.dto.request;

import com.altioratech.pettiway.user.domain.model.Role;

public record GoogleLoginRequest(
        String idToken,
        Role role
) {
}
