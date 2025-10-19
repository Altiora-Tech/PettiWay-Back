package com.altioratech.pettiway.image.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageJpaRepository extends JpaRepository<ImageJpaEntity, UUID> {
    List<ImageJpaEntity> findByUserId(UUID userId);
}
