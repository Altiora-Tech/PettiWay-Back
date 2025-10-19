package com.altioratech.pettiway.verification.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VerificationJpaRepository extends JpaRepository<VerificationJpaEntity, UUID> {
    Optional<VerificationJpaEntity> findTopByUserIdOrderByIdDesc(UUID userId);
}
