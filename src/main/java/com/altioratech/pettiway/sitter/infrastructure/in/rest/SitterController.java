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

@RestController
@RequestMapping("/api/sitters")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class SitterController {

    private final UpdateSitterProfileUseCase updateSitterProfileUseCase;
    private final GeocodingServiceAdapter geocodingService;

    // =============================================================
    // ✅ FRONTEND (usa placeId del Autocomplete)
    // =============================================================
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
