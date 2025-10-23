package com.altioratech.pettiway.pet.infrastructure.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetJpaRepository extends JpaRepository<PetEntity, UUID> {
    Page<PetEntity> findAllByClientId(UUID clientId, Pageable pageable);
}
