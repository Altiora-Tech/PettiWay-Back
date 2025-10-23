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
import com.altioratech.pettiway.user.application.service.ProfileCompletionService;
import com.altioratech.pettiway.user.domain.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UploadMultipleImagesUseCase {

    private final ImageRepository imageRepository;
    private final StorageServicePort storageService;
    private final UserRepository userRepository;
    private final ProfileCompletionService profileCompletionService;

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

        // ✅ Una vez subidas todas las imágenes, actualizamos el estado del perfil
        userRepository.findById(userId).ifPresent(profileCompletionService::updateProfileCompletion);

        return uploadedImages;
    }
}
