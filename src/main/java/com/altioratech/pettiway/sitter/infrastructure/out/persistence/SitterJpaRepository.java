package com.altioratech.pettiway.sitter.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SitterJpaRepository extends JpaRepository<SitterEntity, UUID> {
    @Query("SELECT s FROM SitterEntity s WHERE s.user.email = :email")
    Optional<SitterEntity> findByUserEmail(@Param("email") String email);

    Optional<SitterEntity> findByUserId(UUID userId);
}
