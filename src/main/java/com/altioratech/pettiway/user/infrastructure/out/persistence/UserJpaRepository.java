package com.altioratech.pettiway.user.infrastructure.out.persistence;

import com.altioratech.pettiway.user.domain.model.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Buscar usuario por email (exact match).
     * Spring Data implementa el método automáticamente.
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Buscar usuario por email ignorando mayúsculas/minúsculas (opcional).
     * Útil si querés evitar problemas con el case sensitive.
     */
    Optional<UserEntity> findByEmailIgnoreCase(String email);

    /**
     * Buscar usuario activo por email (ejemplo de query derivada).
     * Podés usarlo cuando querés asegurarte que la cuenta está activa.
     */
    Optional<UserEntity> findByEmailAndStatus(String email, UserStatus status);
}