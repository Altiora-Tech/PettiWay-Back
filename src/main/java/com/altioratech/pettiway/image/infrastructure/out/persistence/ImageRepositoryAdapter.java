package com.altioratech.pettiway.image.infrastructure.out.persistence;

import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.domain.ImageCategory;
import com.altioratech.pettiway.image.domain.ImageRepository;
import com.altioratech.pettiway.image.infrastructure.out.persistence.mapper.ImageEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageRepositoryAdapter implements ImageRepository {

    private final ImageJpaRepository jpaRepository;
    private final ImageEntityMapper mapper;

    @Override
    public Image save(Image image) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(image)));
    }

    @Override
    public Optional<Image> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Image> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
    @Override
    public long countByUserId(UUID userId) {
        return jpaRepository.countByUserId(userId);
    }
    @Override
    public long countByUserIdAndCategory(UUID userId, ImageCategory category) {
        return jpaRepository.countByUserIdAndCategory(userId, category);
    }

    @Override
    public List<Image> findByReferenceId(UUID referenceId) {
        return jpaRepository.findByReferenceId(referenceId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
