package com.altioratech.pettiway.pet.domain.model;

import com.altioratech.pettiway.pet.domain.model.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    private UUID id;
    private UUID clientId;
    private String name;
    private PetTypeEnum type;
    private String breed;
    private Integer age;
    private Double weight;
    private String color;
    private PetSizeEnum petSize;
    private LocalDate birthDate;
    private String microchip;
    private TemperamentEnum temperament;
    private VaccinationEnum vaccination;
    private HealthStatusEnum health;
    private String allergies;
    private String medications;
    private String specialNeeds;
    private String emergencyContact;
    // ✅ Imagen principal (foto de perfil)
    private UUID imageId;

    // ✅ Galería (solo IDs de imágenes adicionales)
    private List<UUID> galleryIds;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
