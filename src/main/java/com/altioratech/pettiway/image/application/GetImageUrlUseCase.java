package com.altioratech.pettiway.image.application;

import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.domain.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetImageUrlUseCase {

    private final ImageRepository imageRepository;

    public String execute(UUID imageId) {
        return imageRepository.findById(imageId)
                .map(Image::getUrl)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
    }
}
