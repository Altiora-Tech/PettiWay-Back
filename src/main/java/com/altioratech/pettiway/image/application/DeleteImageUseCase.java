package com.altioratech.pettiway.image.application;

import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.domain.ImageRepository;
import com.altioratech.pettiway.image.domain.StorageServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteImageUseCase {

    private final ImageRepository imageRepository;
    private final StorageServicePort storageService;

    public void execute(UUID imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        storageService.deleteFile(image.getUrl());
        imageRepository.deleteById(imageId);
    }
}
