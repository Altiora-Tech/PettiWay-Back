package com.altioratech.pettiway.image.application;

import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.domain.ImageCategory;
import com.altioratech.pettiway.image.domain.ImageRepository;
import com.altioratech.pettiway.image.domain.StorageServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadMultipleImagesUseCase {

    private final ImageRepository imageRepository;
    private final StorageServicePort storageService;

    public List<Image> execute(UUID userId, ImageCategory category, List<MultipartFile> files) {
        List<Image> uploadedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            String folder = category.name().toLowerCase();
            String url = storageService.uploadFile(file, folder);

            Image image = Image.builder()
                    .userId(userId)
                    .category(category)
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .url(url)
                    .build();

            uploadedImages.add(imageRepository.save(image));
        }

        return uploadedImages;
    }
}

