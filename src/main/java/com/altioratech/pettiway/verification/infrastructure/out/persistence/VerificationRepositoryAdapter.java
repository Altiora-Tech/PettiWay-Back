package com.altioratech.pettiway.verification.infrastructure.out.persistence;

import com.altioratech.pettiway.verification.domain.Verification;
import com.altioratech.pettiway.verification.domain.VerificationRepository;
import com.altioratech.pettiway.verification.infrastructure.out.persistence.mapper.VerificationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VerificationRepositoryAdapter implements VerificationRepository {

    private final VerificationJpaRepository jpaRepository;
    private final VerificationEntityMapper mapper;

    @Override
    public Verification save(Verification verification) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(verification)));
    }

    @Override
    public Optional<Verification> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Verification> findLatestByUserId(UUID userId) {
        return jpaRepository.findTopByUserIdOrderByIdDesc(userId).map(mapper::toDomain);
    }
}
