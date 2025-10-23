package com.altioratech.pettiway.pet.infrastructure.in.rest;

import com.altioratech.pettiway.pet.application.dto.*;
import com.altioratech.pettiway.pet.application.usecase.*;
import com.altioratech.pettiway.shared.exception.MyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Gestión de Mascotas", description = "Endpoints para registrar, actualizar, listar, eliminar y consultar mascotas del cliente autenticado.")
public class PetController {

    private final RegisterPetUseCase registerPetUseCase;
    private final UpdatePetUseCase updatePetUseCase;
    private final ListClientPetsUseCase listClientPetsUseCase;
    private final GetPetDetailsUseCase getPetDetailsUseCase;
    private final DeletePetUseCase deletePetUseCase;

    // =============================================================
    // 🐾 REGISTRAR MASCOTA
    // =============================================================
    @Operation(
            summary = "Registrar nueva mascota",
            description = "Permite registrar una nueva mascota asociada al usuario autenticado.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Mascota registrada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerPet(
            @Valid @RequestBody PetCreateDTO petCreateDTO,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            PetResponseDTO pet = registerPetUseCase.execute(petCreateDTO);
            URI uri = uriBuilder.path("/api/pets/{id}").buildAndExpand(pet.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Mascota registrada con éxito",
                    "data", pet
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }

    // =============================================================
    // ✏️ ACTUALIZAR MASCOTA
    // =============================================================
    @Operation(
            summary = "Actualizar mascota existente",
            description = "Permite modificar los datos de una mascota. Solo el propietario o un administrador pueden hacerlo."
    )
    @PutMapping("/update/{petId}")
    public ResponseEntity<?> updatePet(
            @PathVariable UUID petId,
            @Valid @RequestBody PetUpdateDTO petUpdateDTO
    ) {
        try {
            PetResponseDTO updatedPet = updatePetUseCase.execute(petId, petUpdateDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Mascota actualizada con éxito",
                    "data", updatedPet
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }

    // =============================================================
    // ❌ ELIMINAR MASCOTA (LÓGICAMENTE)
    // =============================================================
    @Operation(
            summary = "Eliminar mascota (baja lógica)",
            description = "Marca la mascota como inactiva. Solo el propietario o un administrador pueden realizar esta acción."
    )
    @DeleteMapping("/delete/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable UUID petId) {
        try {
            deletePetUseCase.execute(petId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Mascota dada de baja con éxito"
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }

    // =============================================================
    // 📋 LISTAR MASCOTAS DEL CLIENTE
    // =============================================================
    @Operation(
            summary = "Listar mis mascotas",
            description = "Devuelve todas las mascotas registradas por el cliente autenticado, con soporte de paginación."
    )
    @GetMapping("/list")
    public ResponseEntity<Page<PetResponseDTO>> listMyPets(
            @PageableDefault(size = 5, sort = "name") Pageable pageable
    ) throws MyException {
        return ResponseEntity.ok(listClientPetsUseCase.execute(pageable));
    }

    // =============================================================
    // 🔍 OBTENER DETALLES DE UNA MASCOTA
    // =============================================================
    @Operation(
            summary = "Obtener detalles de una mascota",
            description = "Devuelve toda la información de una mascota específica. Solo el cliente propietario o un administrador pueden acceder."
    )
    @GetMapping("/{petId}")
    public ResponseEntity<?> getPetDetails(@PathVariable UUID petId) {
        try {
            PetResponseDTO pet = getPetDetailsUseCase.execute(petId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", pet
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }
}
