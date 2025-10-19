package com.altioratech.pettiway.verification.application;

import com.altioratech.pettiway.verification.domain.Verification;
import com.altioratech.pettiway.verification.domain.VerificationRepository;
import com.altioratech.pettiway.verification.domain.VerificationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewVerificationUseCase {

    private final VerificationRepository verificationRepository;

    public Verification execute(UUID verificationId, boolean approved, String adminComment) {
        Verification verification = verificationRepository.findById(verificationId)
                .orElseThrow(() -> new RuntimeException("Verificación no encontrada"));

        verification.setStatus(approved ? VerificationStatus.APPROVED : VerificationStatus.REJECTED);
        verification.setAdminComment(adminComment);

        return verificationRepository.save(verification);
    }
}
