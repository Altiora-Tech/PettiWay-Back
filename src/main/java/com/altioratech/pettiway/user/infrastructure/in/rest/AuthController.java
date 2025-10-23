package com.altioratech.pettiway.user.infrastructure.in.rest;

import com.altioratech.pettiway.user.application.dto.request.GoogleLoginRequest;
import com.altioratech.pettiway.user.application.dto.request.LoginRequest;
import com.altioratech.pettiway.user.application.dto.request.RefreshTokenRequest;
import com.altioratech.pettiway.user.application.dto.response.LoginResponse;
import com.altioratech.pettiway.user.application.usercase.GoogleLoginUseCase;
import com.altioratech.pettiway.user.application.usercase.LoginUserUseCase;
import com.altioratech.pettiway.user.application.usercase.RefreshTokenUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501/", allowedHeaders = "*", allowCredentials = "true")
@Tag(name = "Autenticación", description = "Endpoints de autenticación de usuarios: login, refresh token y login con Google")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final GoogleLoginUseCase googleLoginUseCase;

    // 🔐 Login tradicional (email + password)
    @Operation(
            summary = "Login tradicional",
            description = "Permite iniciar sesión con email y contraseña, devolviendo tokens de acceso y refresh.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "email": "usuario@ejemplo.com",
                              "password": "123456"
                            }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = loginUserUseCase.execute(request);
            return ResponseEntity.ok(Map.of("status", "success", "data", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // 🔁 Refresh token
    @Operation(
            summary = "Refrescar token de acceso",
            description = "Genera un nuevo token JWT válido a partir de un refresh token previamente emitido.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5..."
                            }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token renovado correctamente",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Refresh token inválido o expirado")
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            LoginResponse response = refreshTokenUseCase.execute(request);
            return ResponseEntity.ok(Map.of("status", "success", "data", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // 🔑 Login con Google OAuth2
    @Operation(
            summary = "Login con Google",
            description = "Permite iniciar sesión mediante el token de Google OAuth2 (ID Token).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GoogleLoginRequest.class),
                            examples = @ExampleObject(value = """
                            {
                              "idToken": "eyJhbGciOiJSUzI1NiIsImtpZCI6..."
                            }
                            """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión con Google exitoso",
                            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Token de Google inválido o expirado")
            }
    )
    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            LoginResponse response = googleLoginUseCase.execute(request);
            return ResponseEntity.ok(Map.of("status", "success", "data", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
