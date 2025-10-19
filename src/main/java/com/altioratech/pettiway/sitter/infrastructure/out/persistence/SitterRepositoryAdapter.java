package com.altioratech.pettiway.sitter.infrastructure.out.persistence;

import com.altioratech.pettiway.sitter.domain.model.Sitter;
import com.altioratech.pettiway.sitter.domain.repository.SitterRepository;
import com.altioratech.pettiway.sitter.infrastructure.out.persistence.mapper.SitterEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SitterRepositoryAdapter implements SitterRepository {

    private final SitterJpaRepository sitterJpaRepository;
    private final SitterEntityMapper mapper;

    @Override
    public Optional<Sitter> findById(UUID id) {
        return sitterJpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Sitter save(Sitter sitter) {
        return mapper.toDomain(sitterJpaRepository.save(mapper.toEntity(sitter)));
    }

    @Override
    public List<Sitter> findAll() {
        return sitterJpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Sitter> findByEmail(String email) {
        return sitterJpaRepository.findByUserEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Sitter> findByUserId(UUID userId) {
        return sitterJpaRepository.findByUserId(userId)
                .map(mapper::toDomain);
    }
}
