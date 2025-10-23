package com.altioratech.pettiway.pet.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PetSizeEnum {
    MINI("Mini (menos de 5 kg)"),
    PEQUENO("Pequeño (5 - 10 kg)"),
    MEDIANO("Mediano (10 - 25 kg)"),
    GRANDE("Grande (25 - 45 kg)"),
    GIGANTE("Gigante (más de 45 kg)");

    private final String label;

    PetSizeEnum(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PetSizeEnum from(String value) {
        for (PetSizeEnum type : values()) {
            if (type.name().equalsIgnoreCase(value) || type.label.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tamaño de mascota inválido: " + value);
    }
}
