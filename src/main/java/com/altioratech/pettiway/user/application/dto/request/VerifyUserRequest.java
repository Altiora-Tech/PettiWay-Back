package com.altioratech.pettiway.user.application.dto.request;

import java.util.UUID;

public record VerifyUserRequest(
        UUID userId
) {
}
