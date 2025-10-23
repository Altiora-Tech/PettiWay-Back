package com.altioratech.pettiway.image.domain;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    private UUID id;
    private UUID userId;
    // Entidad asociada: petId, productId, businessId, etc.
    private UUID referenceId;
    private ImageCategory category;
    private String fileName;
    private String url;
    private String contentType;
    private long size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}