package com.altioratech.pettiway.image.application;

import com.altioratech.pettiway.image.domain.*;
import com.altioratech.pettiway.user.application.service.ProfileCompletionService;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileUseCase {

    private final ImageRepository imageRepository;
    private final StorageServicePort storageService;
    private final ProfileCompletionService profileCompletionService;
    private final UserRepository userRepository;

    public Image execute(UUID userId, ImageCategory category, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        if (file.getSize() > 5_000_000) { // 5 MB
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido (5 MB)");
        }

        if (!AllowedMimeType.isAllowed(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido: " + file.getContentType());
        }

        String folder = switch (category) {
            case VERIFICATION_DOCUMENT, VERIFICATION_PDF -> "verification";
            case USER_PROFILE -> "profiles";
            case PET_PHOTO -> "pets";
            case BUSINESS_LOGO -> "business";
            default -> "others";
        };

        String url = storageService.uploadFile(file, folder);

        Image image = Image.builder()
                .userId(userId)
                .category(category)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .url(url)
                .build();

        imageRepository.save(image);
        User user = userRepository.findById(userId).orElseThrow();
        profileCompletionService.updateProfileCompletion(user);
        return image;
    }
}
