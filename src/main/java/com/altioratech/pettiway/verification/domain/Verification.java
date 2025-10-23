package com.altioratech.pettiway.verification.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Verification {
    private UUID id;
    private UUID userId;              // referencia al usuario
    private DocumentType documentType;
    private List<String> documentUrls;   // URL de la imagen en el módulo image
    private VerificationStatus status;
    private String adminComment;      // comentario del superadmin en caso de rechazo
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isApproved() {
        return status == VerificationStatus.APPROVED;
    }

    public boolean isPending() {
        return status == VerificationStatus.PENDING;
    }

    public boolean isRejected() {
        return status == VerificationStatus.REJECTED;
    }
}