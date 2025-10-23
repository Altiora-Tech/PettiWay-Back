package com.altioratech.pettiway.pet.application.dto;

import com.altioratech.pettiway.pet.domain.model.enums.*;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponseDTO(
        UUID id,
        String name,
        PetTypeEnum type,
        String breed,
        Integer age,
        Double weight,
        String color,
        PetSizeEnum petSize,
        LocalDate birthDate,
        TemperamentEnum temperament,
        VaccinationEnum vaccination,
        HealthStatusEnum health,
        Boolean active,
        UUID mainImageId
) {
}
