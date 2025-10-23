package com.altioratech.pettiway.location.infrastructure.in.rest;

import com.altioratech.pettiway.location.application.usecase.SaveLocationUseCase;
import com.altioratech.pettiway.location.application.usecase.UpdateLocationUseCase;
import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.domain.repository.LocationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
@Tag(name = "Gestión de Ubicaciones", description = "Endpoints para crear, consultar, actualizar, listar y eliminar ubicaciones.")
public class LocationController {

    private final SaveLocationUseCase saveLocationUseCase;
    private final UpdateLocationUseCase updateLocationUseCase;
    private final LocationRepository locationRepository;

    // =============================================================
    // 📍 CREAR UBICACIÓN
    // =============================================================
    @PostMapping("/create")
    @Operation(
            summary = "Crear una nueva ubicación",
            description = """
            Crea una nueva ubicación con datos completos (calle, número, ciudad, provincia, país, etc.).
            Si se dispone de dirección completa, el servicio obtiene automáticamente las coordenadas
            geográficas (latitud y longitud) usando el `GeocodingServicePort`.
            """
    )
    public ResponseEntity<Location> create(@RequestBody Location location) {
        return ResponseEntity.ok(saveLocationUseCase.execute(location));
    }

    // =============================================================
    // 🧭 ACTUALIZAR UBICACIÓN
    // =============================================================
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Actualizar una ubicación existente",
            description = """
            Actualiza los datos de una ubicación existente según su identificador (`UUID`).
            Si cambian los campos de dirección, se recalculan las coordenadas geográficas automáticamente.
            """
    )
    public ResponseEntity<?> update(
            @Parameter(description = "ID de la ubicación a actualizar", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Datos nuevos de la ubicación", required = true)
            @RequestBody Location dto
    ) {
        return ResponseEntity.ok(updateLocationUseCase.execute(id, dto));
    }

    // =============================================================
    // 🔎 OBTENER UBICACIÓN POR ID
    // =============================================================
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una ubicación por su ID",
            description = "Devuelve todos los datos de una ubicación almacenada según su identificador (`UUID`)."
    )
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Location> locationOpt = locationRepository.findById(id);

        if (locationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "Ubicación no encontrada"));
        }

        return ResponseEntity.ok(locationOpt.get());
    }

    // =============================================================
    // 📋 LISTAR TODAS LAS UBICACIONES (ADMIN)
    // =============================================================
    @GetMapping
    @Operation(
            summary = "Listar todas las ubicaciones registradas",
            description = """
            Devuelve una lista paginada de todas las ubicaciones almacenadas en el sistema.
            Se pueden aplicar filtros opcionales por país y provincia.
            Solo accesible para roles administrativos o de gestión.
            """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "204", description = "No hay ubicaciones registradas")
    })
    public ResponseEntity<?> getAll(
            @Parameter(description = "Filtrar por país (opcional)") @RequestParam(required = false) String country,
            @Parameter(description = "Filtrar por provincia (opcional)") @RequestParam(required = false) String province,
            @Parameter(description = "Número de página (por defecto 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página (por defecto 20)") @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Location> locations;

        if (country != null && province != null) {
            locations = locationRepository.findByCountryAndProvince(country, province, pageable);
        } else if (country != null) {
            locations = locationRepository.findByCountry(country, pageable);
        } else if (province != null) {
            locations = locationRepository.findByProvince(province, pageable);
        } else {
            locations = locationRepository.findAll(pageable);
        }

        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(Map.of(
                "total", locations.getTotalElements(),
                "pages", locations.getTotalPages(),
                "data", locations.getContent()
        ));
    }

    // =============================================================
    // 🗑️ ELIMINAR UBICACIÓN
    // =============================================================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar una ubicación por ID",
            description = """
            Elimina una ubicación específica según su identificador (`UUID`).
            Se recomienda validar que la ubicación no esté en uso por un usuario o negocio antes de eliminarla.
            """
    )
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        locationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
