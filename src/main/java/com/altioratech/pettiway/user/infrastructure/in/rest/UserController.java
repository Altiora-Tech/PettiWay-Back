package com.altioratech.pettiway.user.infrastructure.in.rest;

import com.altioratech.pettiway.user.application.dto.request.RegisterUserRequest;
import com.altioratech.pettiway.user.application.dto.request.UpdateUserRequest;
import com.altioratech.pettiway.user.application.dto.request.VerifyUserRequest;
import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.application.usercase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final ChangeUserStatusUseCase changeUserStatusUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;
    private final VerifyUserUseCase verifyUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase,
                          GetUserProfileUseCase getUserProfileUseCase,
                          UpdateUserProfileUseCase updateUserProfileUseCase,
                          ListUsersUseCase listUsersUseCase,
                          ChangeUserStatusUseCase changeUserStatusUseCase,
                          ChangeUserRoleUseCase changeUserRoleUseCase,
                          VerifyUserUseCase verifyUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.changeUserStatusUseCase = changeUserStatusUseCase;
        this.changeUserRoleUseCase = changeUserRoleUseCase;
        this.verifyUserUseCase = verifyUserUseCase;
    }

    // 🧩 Registrar nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterUserRequest request) {
        try {
            UserResponse user = registerUserUseCase.execute(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", user
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 👤 Obtener perfil del usuario autenticado
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getProfile(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetails) {
        try {
            UserResponse profile = getUserProfileUseCase.execute(userDetails.getUsername()); // username = email
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", profile
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }


    // ✏️ Actualizar perfil
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 📋 Listar todos los usuarios (solo admins)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> listAll() {
        try {
            List<UserResponse> users = listUsersUseCase.execute();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔒 Activar o desactivar usuario
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> changeStatus(@PathVariable UUID id, @RequestParam boolean active) {
        try {
            changeUserStatusUseCase.execute(id, active);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Estado del usuario actualizado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔄 Cambiar rol de usuario
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> changeRole(@PathVariable UUID id, @RequestParam String role) {
        try {
            changeUserRoleUseCase.execute(id, role);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Rol del usuario actualizado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ Verificar usuario
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{id}/verify")
    public ResponseEntity<Map<String, Object>> verifyUser(@RequestBody VerifyUserRequest request) {
        try {
            verifyUserUseCase.execute(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Usuario verificado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}