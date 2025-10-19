package com.altioratech.pettiway.verification.application;

import com.altioratech.pettiway.verification.domain.Verification;
import com.altioratech.pettiway.verification.domain.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetVerificationStatusUseCase {

    private final VerificationRepository verificationRepository;

    public Verification execute(UUID userId) {
        return verificationRepository.findLatestByUserId(userId)
                .orElseThrow(() -> new RuntimeException("No se encontró verificación para el usuario"));
    }
}
