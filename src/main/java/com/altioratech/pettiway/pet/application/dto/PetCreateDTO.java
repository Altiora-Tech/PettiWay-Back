package com.altioratech.pettiway.pet.application.dto;

import com.altioratech.pettiway.pet.domain.model.enums.*;

import java.time.LocalDate;

public record PetCreateDTO(
        String name,
        PetTypeEnum type,
        String breed,
        Double weight,
        PetSizeEnum petSize,
        LocalDate birthDate,
        String color,
        TemperamentEnum temperament,
        VaccinationEnum vaccination,
        HealthStatusEnum health,
        String allergies,
        String medications,
        String specialNeeds,
        String emergencyContact
) {
}
