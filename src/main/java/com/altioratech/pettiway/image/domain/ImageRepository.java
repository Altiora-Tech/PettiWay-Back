package com.altioratech.pettiway.image.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository {
    Image save(Image image);
    Optional<Image> findById(UUID id);
    List<Image> findByUserId(UUID userId);
    void deleteById(UUID id);
    long countByUserId(UUID id);
    long countByUserIdAndCategory(UUID userId, ImageCategory category);

}