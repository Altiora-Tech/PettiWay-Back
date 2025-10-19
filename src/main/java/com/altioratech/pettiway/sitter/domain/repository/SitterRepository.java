package com.altioratech.pettiway.sitter.domain.repository;

import com.altioratech.pettiway.sitter.domain.model.Sitter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SitterRepository {
    Optional<Sitter> findById(UUID id);

    Optional<Sitter> findByEmail(String email);

    Optional<Sitter> findByUserId(UUID userId);

    Sitter save(Sitter sitter);

    List<Sitter> findAll();
}
