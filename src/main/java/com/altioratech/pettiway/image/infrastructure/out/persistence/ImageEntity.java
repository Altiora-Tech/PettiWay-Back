package com.altioratech.pettiway.image.infrastructure.out.persistence;

import com.altioratech.pettiway.image.domain.ImageCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    // Entidad asociada: petId, productId, businessId, etc.
    private UUID referenceId;

    @Enumerated(EnumType.STRING)
    private ImageCategory category;

    private String fileName;
    private String url;
    private String contentType;
    private long size;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
