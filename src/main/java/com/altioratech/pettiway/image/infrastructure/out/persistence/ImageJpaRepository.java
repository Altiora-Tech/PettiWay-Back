package com.altioratech.pettiway.image.infrastructure.out.persistence;

import com.altioratech.pettiway.image.domain.ImageCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, UUID> {
    List<ImageEntity> findByUserId(UUID userId);
    long countByUserId(UUID userId);
    long countByUserIdAndCategory(UUID userId, ImageCategory category);
    List<ImageEntity> findByReferenceId(UUID referenceId);
}
