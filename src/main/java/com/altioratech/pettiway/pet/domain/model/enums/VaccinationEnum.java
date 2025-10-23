package com.altioratech.pettiway.pet.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum VaccinationEnum {
    COMPLETA("Vacunación completa"),
    PARCIAL("Vacunación parcial"),
    PENDIENTE("Pendiente"),
    DESCONOCIDA("Desconocida");

    private final String label;

    VaccinationEnum(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static VaccinationEnum from(String value) {
        for (VaccinationEnum v : values()) {
            if (v.name().equalsIgnoreCase(value) || v.label.equalsIgnoreCase(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Estado de vacunación inválido: " + value);
    }
}
