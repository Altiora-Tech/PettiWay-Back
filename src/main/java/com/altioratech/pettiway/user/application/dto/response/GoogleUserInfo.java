package com.altioratech.pettiway.user.application.dto.response;

public record GoogleUserInfo(
        String name,
        String email,
        String sub
) {
}
