package com.altioratech.pettiway.verification.infrastructure.out.persistence;

import com.altioratech.pettiway.verification.domain.DocumentType;
import com.altioratech.pettiway.verification.domain.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "verifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "verification_documents", joinColumns = @JoinColumn(name = "verification_id"))
    @Column(name = "document_url")
    private List<String> documentUrls;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    private String adminComment;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
