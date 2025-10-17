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

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501/", allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final GoogleLoginUseCase googleLoginUseCase;

    public AuthController(LoginUserUseCase loginUserUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          GoogleLoginUseCase googleLoginUseCase) {
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.googleLoginUseCase = googleLoginUseCase;
    }

    // 🔐 Login tradicional (email + password)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = loginUserUseCase.execute(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔁 Refresh token
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestBody RefreshTokenRequest request) {
        try {
            LoginResponse response = refreshTokenUseCase.execute(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            LoginResponse response = googleLoginUseCase.execute(request);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}