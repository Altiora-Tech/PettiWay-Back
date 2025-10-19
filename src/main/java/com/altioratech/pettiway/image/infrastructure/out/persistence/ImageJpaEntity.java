package com.altioratech.pettiway.image.infrastructure.out.persistence;

import com.altioratech.pettiway.image.domain.ImageCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageJpaEntity {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private ImageCategory category;

    private String fileName;
    private String url;
    private String contentType;
    private long size;
}
