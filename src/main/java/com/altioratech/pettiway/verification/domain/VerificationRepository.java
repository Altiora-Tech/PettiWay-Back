package com.altioratech.pettiway.verification.domain;

import java.util.Optional;
import java.util.UUID;

public interface VerificationRepository {
    Verification save(Verification verification);
    Optional<Verification> findById(UUID id);
    Optional<Verification> findLatestByUserId(UUID userId);
}