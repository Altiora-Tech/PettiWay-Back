package com.altioratech.pettiway.image.infrastructure.in.rest;

import com.altioratech.pettiway.image.application.DeleteImageUseCase;
import com.altioratech.pettiway.image.application.GetImageUrlUseCase;
import com.altioratech.pettiway.image.application.UploadFileUseCase;
import com.altioratech.pettiway.image.application.UploadMultipleImagesUseCase;
import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.domain.ImageCategory;
import com.altioratech.pettiway.image.domain.ImageRepository;
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
import org.springframework.http.HttpStatus;
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
    private final ImageRepository imageRepository;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Subir una imagen por categoría o referencia",
            description = """
                Permite subir **una sola imagen** asociada al usuario autenticado o a una entidad específica.
                La categoría determina el propósito del archivo:
                - `USER_PROFILE` → Foto de perfil de usuario.
                - `VERIFICATION_DOCUMENT` / `VERIFICATION_PDF` → Documentación para verificación.
                - `PET_PROFILE` / `PET_GALLERY` → Imagen o galería de una mascota.
                - `BUSINESS_LOGO` / `BUSINESS_GALLERY` → Logo o galería del comercio.
                - `PRODUCT_MAIN` / `PRODUCT_GALLERY` → Imagen principal o galería de producto.
                - `SERVICE_GALLERY` / `SERVICE_ICON` → Imagen o ícono de un servicio.
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
            @RequestParam MultipartFile file,

            @Parameter(
                    description = "ID de referencia opcional (por ejemplo, el ID de una mascota, producto o negocio).",
                    example = "b3f4c3e2-9d81-4c27-bac3-13e9b5a0f6a9"
            )
            @RequestParam(required = false) UUID referenceId
    ) {
        var user = authenticatedUserService.getAuthenticatedUser();
        return ResponseEntity.ok(uploadUseCase.execute(user.getId(), referenceId, category, file));
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
            summary = "Actualizar foto de perfil del usuario autenticado o de una entidad asociada",
            description = """
                Permite subir o reemplazar la **foto de perfil** del usuario autenticado, 
                o de una entidad referenciada (por ejemplo, mascota, producto o negocio).
                El archivo se almacena en la categoría `USER_PROFILE` por defecto, 
                o en la categoría correspondiente si se provee un `referenceId`.
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
            @RequestParam MultipartFile file,

            @Parameter(
                    description = "ID de referencia opcional (por ejemplo, una mascota, producto o negocio).",
                    example = "b3f4c3e2-9d81-4c27-bac3-13e9b5a0f6a9"
            )
            @RequestParam(required = false) UUID referenceId
    ) {
        User user = authenticatedUserService.getAuthenticatedUser();

        // ✅ Si tiene referenceId, la foto no es de usuario, sino de otra entidad (ej: mascota)
        Image image = uploadUseCase.execute(
                user.getId(),
                referenceId,
                ImageCategory.USER_PROFILE,
                file
        );

        // ✅ Solo actualizar el campo profilePhotoId si no hay referenceId (es del usuario)
        if (referenceId == null) {
            user.setProfilePhotoId(image.getId());
            userRepository.save(user);
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Foto actualizada correctamente",
                "photoUrl", image.getUrl()
        ));
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

    @GetMapping("/by-reference/{referenceId}")
    @Operation(
            summary = "Obtener todas las imágenes asociadas a una entidad",
            description = """
                Devuelve todas las imágenes vinculadas a una entidad específica (por ejemplo, una mascota, un negocio o un producto).  
                Se basa en el campo `referenceId` del registro de imagen.  
                Ejemplo de uso: `/api/images/by-reference/{petId}` devuelve la galería de una mascota.
                """
    )
    public ResponseEntity<Map<String, Object>> getImagesByReference(
            @Parameter(
                    description = "UUID de la entidad referenciada (por ejemplo, una mascota, producto o negocio).",
                    required = true,
                    example = "f3a0e6d9-ffb8-42e9-a0b1-8a531a5d96b2"
            )
            @PathVariable UUID referenceId
    ) {
        try {
            List<Image> images = imageRepository.findByReferenceId(referenceId);

            if (images.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "No hay imágenes asociadas a esta entidad",
                        "data", List.of()
                ));
            }

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "count", images.size(),
                    "data", images
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}