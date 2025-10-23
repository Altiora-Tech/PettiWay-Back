package com.altioratech.pettiway.pet.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum HealthStatusEnum {
    EXCELENTE("Excelente"),
    BUENA("Buena"),
    REGULAR("Regular"),
    DELICADA("Delicada"),
    EN_TRATAMIENTO("En tratamiento"),
    DESCONOCIDA("Desconocida");

    private final String label;

    HealthStatusEnum(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static HealthStatusEnum from(String value) {
        for (HealthStatusEnum h : values()) {
            if (h.name().equalsIgnoreCase(value) || h.label.equalsIgnoreCase(value)) {
                return h;
            }
        }
        throw new IllegalArgumentException("Estado de salud inválido: " + value);
    }
}