package com.altioratech.pettiway.user.application.dto.request;

public record LoginRequest(
        String email,
        String password
) {}
