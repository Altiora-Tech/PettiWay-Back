package com.altioratech.pettiway.verification.application;

import com.altioratech.pettiway.verification.domain.DocumentType;
import com.altioratech.pettiway.verification.domain.Verification;
import com.altioratech.pettiway.verification.domain.VerificationRepository;
import com.altioratech.pettiway.verification.domain.VerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmitVerificationRequestUseCase {

    private final VerificationRepository verificationRepository;

    public Verification execute(UUID userId, DocumentType type, List<String> documentUrl) {

        // 🚫 Validación simple: no permitir más de una solicitud pendiente
        verificationRepository.findLatestByUserId(userId).ifPresent(existing -> {
            if (existing.isPending()) {
                throw new IllegalStateException("Ya existe una verificación pendiente para este usuario.");
            }
        });

        Verification verification = Verification.builder()
                .userId(userId)
                .documentType(type)
                .documentUrls(documentUrl)
                .status(VerificationStatus.PENDING)
                .build();

        return verificationRepository.save(verification);
    }
}
