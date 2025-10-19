package com.altioratech.pettiway.image.infrastructure.in.rest;

import com.altioratech.pettiway.image.application.DeleteImageUseCase;
import com.altioratech.pettiway.image.application.GetImageUrlUseCase;
import com.altioratech.pettiway.image.application.UploadFileUseCase;
import com.altioratech.pettiway.image.application.UploadMultipleImagesUseCase;
import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.domain.ImageCategory;
import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class ImageController {

    private final UploadFileUseCase uploadUseCase;
    private final DeleteImageUseCase deleteUseCase;
    private final GetImageUrlUseCase getUrlUseCase;
    private final UploadMultipleImagesUseCase uploadMultipleImagesUseCase;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Image> upload(
            @RequestParam ImageCategory category,
            @RequestParam MultipartFile file
    ) {
        var user = authenticatedUserService.getAuthenticatedUser();
        return ResponseEntity.ok(uploadUseCase.execute(user.getId(), category, file));
    }

    @PostMapping(value = "/upload/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<Image>> uploadMultiple(
            @RequestParam ImageCategory category,
            @Parameter(
                    description = "Hasta 5 imágenes",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("files") MultipartFile[] files
    ) {
        var user = authenticatedUserService.getAuthenticatedUser();
        List<MultipartFile> fileList = Arrays.asList(files);

        return ResponseEntity.ok(uploadMultipleImagesUseCase.execute(user.getId(), category, fileList));
    }


    @PutMapping(value = "/profile/photo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfilePhoto(@RequestParam MultipartFile file) {
        User user = authenticatedUserService.getAuthenticatedUser();
        Image image = uploadUseCase.execute(user.getId(), ImageCategory.USER_PROFILE, file);
        user.setProfilePhotoId(image.getId());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("photoUrl", image.getUrl()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUrl(@PathVariable UUID id) {
        return ResponseEntity.ok(getUrlUseCase.execute(id));
    }
}
