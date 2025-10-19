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

@RestController
@RequestMapping("/api/verifications")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class VerificationController {

    private final SubmitVerificationRequestUseCase submitUseCase;
    private final ReviewVerificationUseCase reviewUseCase;
    private final GetVerificationStatusUseCase statusUseCase;
    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping
    public ResponseEntity<Verification> submit(
            @RequestParam DocumentType documentType,
            @RequestBody List<String> documentUrls) {

        var user = authenticatedUserService.getAuthenticatedUser();
        Verification verification = submitUseCase.execute(user.getId(), documentType, documentUrls);
        return ResponseEntity.ok(verification);
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Verification> review(
            @PathVariable UUID id,
            @RequestParam boolean approved,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(reviewUseCase.execute(id, approved, comment));
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<Verification> getStatus(@PathVariable UUID userId) {
        return ResponseEntity.ok(statusUseCase.execute(userId));
    }
}
