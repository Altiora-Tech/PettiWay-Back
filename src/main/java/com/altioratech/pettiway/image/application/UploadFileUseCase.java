package com.altioratech.pettiway.image.application;

import com.altioratech.pettiway.image.domain.*;
import com.altioratech.pettiway.user.application.service.ProfileCompletionService;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadFileUseCase {

    private final ImageRepository imageRepository;
    private final StorageServicePort storageService;
    private final ProfileCompletionService profileCompletionService;
    private final UserRepository userRepository;

    public Image execute(UUID userId, UUID referenceId, ImageCategory category, MultipartFile file) {

        // ===== Validaciones =====
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        if (file.getSize() > 5_000_000) { // 5 MB
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido (5 MB)");
        }
        if (!AllowedMimeType.isAllowed(file.getContentType())) {
            throw new IllegalArgumentException("Tipo de archivo no permitido: " + file.getContentType());
        }

        // ===== Carpeta destino =====
        String folder = switch (category) {
            // ===== Usuarios =====
            case USER_PROFILE, USER_GALLERY -> "users";
            // ===== Verificaciones =====
            case VERIFICATION_DOCUMENT, VERIFICATION_PDF -> "verification";
            // ===== Mascotas =====
            case PET_PROFILE, PET_GALLERY -> "pets";
            // ===== Negocios =====
            case BUSINESS_LOGO, BUSINESS_GALLERY, BUSINESS_CERTIFICATE -> "business";
            // ===== Productos =====
            case PRODUCT_MAIN, PRODUCT_GALLERY, PRODUCT_BANNER -> "products";
            // ===== Servicios =====
            case SERVICE_GALLERY, SERVICE_ICON -> "services";
            // ===== Otros =====
            default -> "others";
        };

        // ===== Subida del archivo =====
        String url = storageService.uploadFile(file, folder);

        // ===== Construcción de la entidad =====
        Image image = Image.builder()
                .userId(userId)
                .referenceId(referenceId) // <- Pet, Product, Business, etc.
                .category(category)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .url(url)
                .createdAt(LocalDateTime.now())
                .build();

        imageRepository.save(image);

        // ===== Actualización de perfil solo si aplica =====
        if (category.name().startsWith("USER_") || category == ImageCategory.VERIFICATION_DOCUMENT || category == ImageCategory.VERIFICATION_PDF) {
            userRepository.findById(userId).ifPresent(profileCompletionService::updateProfileCompletion);
        }

        return image;
    }
}
