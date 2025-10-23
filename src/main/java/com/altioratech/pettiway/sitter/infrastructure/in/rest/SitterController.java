package com.altioratech.pettiway.sitter.infrastructure.in.rest;

import com.altioratech.pettiway.location.application.dto.LocationDTO;
import com.altioratech.pettiway.location.infrastructure.out.geocoding.GeocodingServiceAdapter;
import com.altioratech.pettiway.sitter.application.usecase.UpdateSitterProfileUseCase;
import com.altioratech.pettiway.sitter.domain.model.Sitter;
import com.altioratech.pettiway.sitter.infrastructure.in.rest.dto.UpdateSitterProfileBackendDTO;
import com.altioratech.pettiway.sitter.infrastructure.in.rest.dto.UpdateSitterProfileFrontendDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sitters")
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Cuidadores (Sitters)",
        description = "Gestión del perfil profesional de cuidadores: actualización de datos, experiencia, roles y ubicación geográfica."
)
@RequiredArgsConstructor
public class SitterController {

    private final UpdateSitterProfileUseCase updateSitterProfileUseCase;
    private final GeocodingServiceAdapter geocodingService;

    // =============================================================
    // ✅ FRONTEND (usa placeId del Autocomplete)
    // =============================================================
    @Operation(
            summary = "Actualizar perfil de Sitter (Frontend)",
            description = """
            Permite al frontend enviar un `placeId` obtenido desde Google Places Autocomplete,
            el cual se convierte automáticamente en coordenadas y dirección detallada.
            Incluye biografía, experiencia y roles profesionales.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateSitterProfileFrontendDTO.class),
                            examples = @ExampleObject(value = """
                            {
                              "bio": "Cuidadora responsable y amante de los animales con 5 años de experiencia.",
                              "experience": "Experiencia en paseos, adiestramiento y cuidado de perros mayores.",
                              "placeId": "ChIJN1t_tDeuEmsRUsoyG83frY4",
                              "professionalRoles": ["DOG_WALKER", "PET_SITTER"]
                            }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Perfil actualizado correctamente (frontend)",
                            content = @Content(schema = @Schema(implementation = Sitter.class))),
                    @ApiResponse(responseCode = "400", description = "Error en los datos o placeId inválido")
            }
    )
    @PutMapping(value = "/update-profile-frontend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileFrontend(
            @RequestBody UpdateSitterProfileFrontendDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            LocationDTO locationDTO = null;

            // Si se envía un placeId, lo resolvemos con el servicio de geocodificación
            if (dto.placeId() != null && !dto.placeId().isBlank()) {
                locationDTO = geocodingService.getLocationFromPlaceId(dto.placeId());
            }

            Sitter updated = updateSitterProfileUseCase.execute(
                    dto.bio(),
                    dto.experience(),
                    locationDTO,
                    dto.professionalRoles()
            );

            URI uri = uriBuilder.path("/api/sitters/{id}")
                    .buildAndExpand(updated.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Perfil de Sitter actualizado correctamente (frontend)",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // =============================================================
    // 🧪 BACKEND (envía Location completa)
    // =============================================================
    @Operation(
            summary = "Actualizar perfil de Sitter (Backend)",
            description = """
            Permite a servicios internos (backend) actualizar el perfil del cuidador enviando la información completa de ubicación.
            Este método se usa en paneles administrativos o integraciones directas.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateSitterProfileBackendDTO.class),
                            examples = @ExampleObject(value = """
                            {
                              "bio": "Amante de los animales, con formación en adiestramiento canino y primeros auxilios veterinarios.",
                              "experience": "He trabajado con refugios y hogares transitorios durante más de 3 años.",
                              "location": {
                                "street": "Av. Siempre Viva",
                                "number": "742",
                                "city": "Springfield",
                                "province": "Buenos Aires",
                                "country": "Argentina",
                                "lat": -34.6037,
                                "lng": -58.3816
                              },
                              "professionalRoles": ["PET_SITTER"]
                            }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Perfil actualizado correctamente (backend)",
                            content = @Content(schema = @Schema(implementation = Sitter.class))),
                    @ApiResponse(responseCode = "400", description = "Error en la estructura o datos inválidos")
            }
    )
    @PutMapping(value = "/update-profile-backend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProfileBackend(
            @RequestBody UpdateSitterProfileBackendDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            Sitter updated = updateSitterProfileUseCase.execute(
                    dto.bio(),
                    dto.experience(),
                    dto.location(),
                    dto.professionalRoles()
            );

            URI uri = uriBuilder.path("/api/sitters/{id}")
                    .buildAndExpand(updated.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Perfil de Sitter actualizado correctamente (backend)",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
