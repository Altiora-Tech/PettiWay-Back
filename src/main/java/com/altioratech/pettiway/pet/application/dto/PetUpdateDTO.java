package com.altioratech.pettiway.pet.application.dto;

import com.altioratech.pettiway.pet.domain.model.enums.HealthStatusEnum;
import com.altioratech.pettiway.pet.domain.model.enums.PetSizeEnum;
import com.altioratech.pettiway.pet.domain.model.enums.TemperamentEnum;
import com.altioratech.pettiway.pet.domain.model.enums.VaccinationEnum;

import java.time.LocalDate;

public record PetUpdateDTO(
        String name,
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
