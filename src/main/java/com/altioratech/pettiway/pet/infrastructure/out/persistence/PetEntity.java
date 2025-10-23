package com.altioratech.pettiway.pet.infrastructure.out.persistence;

import com.altioratech.pettiway.pet.domain.model.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetTypeEnum type;

    private String breed;
    private Integer age;
    private Double weight;
    private String color;

    @Enumerated(EnumType.STRING)
    private PetSizeEnum petSize;

    private LocalDate birthDate;
    private String microchip;

    @Enumerated(EnumType.STRING)
    private TemperamentEnum temperament;

    @Enumerated(EnumType.STRING)
    private VaccinationEnum vaccination;

    @Enumerated(EnumType.STRING)
    private HealthStatusEnum health;

    private String allergies;
    private String medications;
    private String specialNeeds;
    private String emergencyContact;

    // ✅ Imagen principal (foto de perfil)
    private UUID imageId;

    // ✅ Galería (solo IDs de imágenes adicionales)
    private List<UUID> galleryIds;

    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
