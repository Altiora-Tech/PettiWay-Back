package com.altioratech.pettiway.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class VerificationToken {
    private UUID id;
    private UUID userId;
    private String token;
    private LocalDateTime expiresAt;

    public VerificationToken(UUID userId, String token) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusHours(24);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}
