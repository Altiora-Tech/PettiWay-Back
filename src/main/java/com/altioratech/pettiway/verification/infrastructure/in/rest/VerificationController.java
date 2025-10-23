package com.altioratech.pettiway.verification.infrastructure.in.rest;

import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.verification.application.GetVerificationStatusUseCase;
import com.altioratech.pettiway.verification.application.ReviewVerificationUseCase;
import com.altioratech.pettiway.verification.application.SubmitVerificationRequestUseCase;
import com.altioratech.pettiway.verification.domain.DocumentType;
import com.altioratech.pettiway.verification.domain.Verification;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/verifications")
@SecurityRequirement(name = "bearer-key")
@Tag(
        name = "Verificaciones de Usuario",
        description = "Gestión del proceso de verificación de identidad de usuarios, revisión por administradores y consulta de estado."
)
@RequiredArgsConstructor
public class VerificationController {

    private final SubmitVerificationRequestUseCase submitUseCase;
    private final ReviewVerificationUseCase reviewUseCase;
    private final GetVerificationStatusUseCase statusUseCase;
    private final AuthenticatedUserService authenticatedUserService;

    // 📤 Enviar solicitud de verificación
    @Operation(
            summary = "Enviar solicitud de verificación",
            description = "Permite a un usuario autenticado enviar URLs de documentos para su revisión (DNI, pasaporte, certificado, etc.).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(
                                    type = "object",
                                    example = """
                                    {
                                      "documentType": "DNI",
                                      "documentUrls": [
                                        "https://storage.pettiway.com/uploads/documents/dni_frente.jpg",
                                        "https://storage.pettiway.com/uploads/documents/dni_dorso.jpg"
                                      ]
                                    }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Solicitud enviada correctamente",
                            content = @Content(schema = @Schema(implementation = Verification.class))),
                    @ApiResponse(responseCode = "401", description = "Usuario no autenticado o token inválido")
            }
    )
    @PostMapping
    public ResponseEntity<Verification> submit(
            @RequestParam DocumentType documentType,
            @RequestBody List<String> documentUrls
    ) {
        var user = authenticatedUserService.getAuthenticatedUser();
        Verification verification = submitUseCase.execute(user.getId(), documentType, documentUrls);
        return ResponseEntity.ok(verification);
    }

    // 🧾 Revisar una verificación (solo admins)
    @Operation(
            summary = "Revisar solicitud de verificación",
            description = "Permite a un administrador aprobar o rechazar una verificación, agregando un comentario opcional.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Verificación actualizada correctamente",
                            content = @Content(schema = @Schema(implementation = Verification.class))),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (no es administrador)")
            }
    )
    @PostMapping("/{id}/review")
    public ResponseEntity<Verification> review(
            @PathVariable UUID id,
            @RequestParam boolean approved,
            @RequestParam(required = false) String comment
    ) {
        return ResponseEntity.ok(reviewUseCase.execute(id, approved, comment));
    }

    // 🔎 Consultar estado de verificación
    @Operation(
            summary = "Consultar estado de verificación",
            description = "Permite a un usuario o administrador consultar el estado actual de la verificación (PENDING, APPROVED, REJECTED).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado obtenido correctamente",
                            content = @Content(schema = @Schema(implementation = Verification.class))),
                    @ApiResponse(responseCode = "404", description = "Verificación no encontrada")
            }
    )
    @GetMapping("/status/{userId}")
    public ResponseEntity<Verification> getStatus(@PathVariable UUID userId) {
        return ResponseEntity.ok(statusUseCase.execute(userId));
    }
}
