package com.altioratech.pettiway.user.infrastructure.in.rest;

import com.altioratech.pettiway.user.application.dto.request.ChangePasswordRequest;
import com.altioratech.pettiway.user.application.dto.request.RegisterUserRequest;
import com.altioratech.pettiway.user.application.dto.request.UpdateUserRequest;
import com.altioratech.pettiway.user.application.dto.request.VerifyUserRequest;
import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.application.usercase.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = "http://localhost", allowedHeaders = "*", allowCredentials = "true")
@Tag(name = "Usuarios", description = "Endpoints relacionados con la gestión y autenticación de usuarios en PettiWay")
@RequiredArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final ChangeUserStatusUseCase changeUserStatusUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final VerifyUserUseCase verifyUserUseCase;
    private final ChangeUserPasswordUseCase changeUserPasswordUseCase;

    // 🧩 Registrar nuevo usuario
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Permite registrar un usuario en la plataforma especificando nombre, email, contraseña y rol.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterUserRequest request) {
        try {
            UserResponse user = registerUserUseCase.execute(request);
            return ResponseEntity.ok(Map.of("status", "success", "data", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // 👤 Obtener perfil del usuario autenticado
    @Operation(
            summary = "Obtener perfil del usuario autenticado",
            description = "Devuelve la información del perfil del usuario actual autenticado mediante el token JWT.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autorizado / Token inválido")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getProfile(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        try {
            UserResponse profile = getUserProfileUseCase.execute(userDetails.getUsername());
            return ResponseEntity.ok(Map.of("status", "success", "data", profile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // ✏️ Actualizar perfil
    @Operation(
            summary = "Actualizar perfil de usuario",
            description = "Permite modificar datos del usuario autenticado como nombre, teléfono o contraseña.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil actualizado correctamente",
                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Error al actualizar perfil")
            }
    )
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> update(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody UpdateUserRequest request) {
        try {
            UserResponse updated = updateUserProfileUseCase.execute(userDetails.getUsername(), request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Perfil actualizado correctamente",
                    "data", updated
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // 📋 Listar todos los usuarios
    @Operation(
            summary = "Listar todos los usuarios (solo administradores)",
            description = "Devuelve un listado completo de todos los usuarios registrados en la plataforma.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuarios listados correctamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<UserResponse> users = listUsersUseCase.execute();
            return ResponseEntity.ok(Map.of("status", "success", "data", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // 🔒 Cambiar estado de usuario
    @Operation(
            summary = "Activar o desactivar un usuario",
            description = "Permite a un administrador cambiar el estado activo de un usuario.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> changeStatus(
            @PathVariable UUID id,
            @RequestParam boolean active) {
        try {
            changeUserStatusUseCase.execute(id, active);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Estado del usuario actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // 🔄 Cambiar rol
    @Operation(
            summary = "Cambiar el rol de un usuario",
            description = "Permite asignar un nuevo rol a un usuario existente (solo para administradores).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol actualizado correctamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> changeRole(
            @PathVariable UUID id,
            @RequestParam String role) {
        try {
            changeUserRoleUseCase.execute(id, role);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Rol del usuario actualizado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // ✅ Verificar usuario
    @Operation(
            summary = "Verificar usuario",
            description = "Permite a un administrador marcar un usuario como verificado, validando su identidad o documentos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario verificado correctamente"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado")
            }
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/verify")
    public ResponseEntity<Map<String, Object>> verifyUser(@RequestBody VerifyUserRequest request) {
        try {
            verifyUserUseCase.execute(request);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Usuario verificado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @PutMapping("/change-password")
    @Operation(
            summary = "Cambiar contraseña del usuario autenticado",
            description = "Permite al usuario cambiar su contraseña validando la actual. Es obligatorio confirmar la nueva contraseña."
    )
    public ResponseEntity<Map<String, Object>> changePassword(
            @AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails,
            @RequestBody ChangePasswordRequest request
    ) {
        try {
            changeUserPasswordUseCase.execute(
                    userDetails.getUsername(),
                    request.currentPassword(),
                    request.newPassword(),
                    request.confirmPassword()
            );

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Contraseña actualizada correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}
