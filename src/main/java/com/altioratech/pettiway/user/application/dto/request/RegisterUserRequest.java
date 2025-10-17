package com.altioratech.pettiway.user.application.dto.request;

import com.altioratech.pettiway.user.domain.model.Role;

public record RegisterUserRequest(String name, String email, String password, Role role) {}
