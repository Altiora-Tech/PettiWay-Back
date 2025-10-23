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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Gestión de Imágenes", description = "Endpoints para subir, eliminar y obtener imágenes de usuario, documentos de verificación y fotos de mascotas en PettiWay")
@RequiredArgsConstructor
public class ImageController {

    private final UploadFileUseCase uploadUseCase;
    private final DeleteImageUseCase deleteUseCase;
    private final GetImageUrlUseCase getUrlUseCase;
    private final UploadMultipleImagesUseCase uploadMultipleImagesUseCase;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Subir una imagen por categoría",
            description = """
                    Permite subir **una sola imagen** asociada al usuario autenticado.
                    La categoría determina el propósito del archivo:
                    - `USER_PROFILE` → Foto de perfil.
                    - `VERIFICATION_DOCUMENT` → Imagen de documento para verificación.
                    - `VERIFICATION_PDF` → Documento PDF de verificación.
                    - `PET_PHOTO` → Foto de mascota.
                    - `BUSINESS_LOGO` → Logo del comercio o profesional.
                    - `OTHER` → Cualquier otro tipo de imagen.
                    """
    )
    public ResponseEntity<Image> upload(
            @Parameter(
                    description = "Categoría de la imagen que se desea subir.",
                    example = "USER_PROFILE"
            )
            @RequestParam ImageCategory category,

            @Parameter(
                    description = "Archivo de imagen (JPG, PNG o PDF).",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam MultipartFile file
    ) {
        var user = authenticatedUserService.getAuthenticatedUser();
        return ResponseEntity.ok(uploadUseCase.execute(user.getId(), category, file));
    }


    @PostMapping(value = "/upload/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Subir múltiples imágenes por categoría",
            description = """
                    Permite subir hasta 5 imágenes asociadas al usuario autenticado.
                    Categorías posibles:
                    - USER_PROFILE → Foto de perfil
                    - VERIFICATION_DOCUMENT → Documentos de verificación
                    - PET_PHOTO → Fotos de mascotas
                    - BUSINESS_LOGO → Logo de un comercio o proveedor
                    - OTHER → Otras imágenes.
                    """
    )
    public ResponseEntity<List<Image>> uploadMultiple(
            @Parameter(description = "Categoría de las imágenes", example = "VERIFICATION_DOCUMENT")
            @RequestParam ImageCategory category,

            @Parameter(
                    description = "Hasta 5 imágenes (archivos JPG, PNG o PDF)",
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

    @PutMapping(value = "/profile/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Actualizar foto de perfil del usuario autenticado",
            description = """
                    Permite subir o reemplazar la **foto de perfil** del usuario autenticado.
                    El archivo se almacena en la categoría `USER_PROFILE`.
                    """
    )
    public ResponseEntity<?> updateProfilePhoto(
            @Parameter(
                    description = "Archivo de imagen (JPG o PNG) para la nueva foto de perfil.",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestParam MultipartFile file
    ) {
        User user = authenticatedUserService.getAuthenticatedUser();
        Image image = uploadUseCase.execute(user.getId(), ImageCategory.USER_PROFILE, file);
        user.setProfilePhotoId(image.getId());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("photoUrl", image.getUrl()));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar imagen por ID",
            description = """
                    Elimina una imagen específica según su identificador (`UUID`).
                    Requiere permisos del usuario propietario o un rol administrador.
                    """
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador de la imagen a eliminar.", required = true)
            @PathVariable UUID id
    ) {
        deleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener URL de imagen por ID",
            description = """
                    Devuelve la **URL pública** o firmada (según la configuración del `StorageServicePort`)
                    asociada a la imagen indicada por su identificador (`UUID`).
                    """
    )
    public ResponseEntity<String> getUrl(
            @Parameter(description = "Identificador de la imagen a consultar.", required = true)
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(getUrlUseCase.execute(id));
    }
}