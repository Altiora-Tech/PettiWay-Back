package com.altioratech.pettiway.user.application.dto.request;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword,
        String confirmPassword
) { }
