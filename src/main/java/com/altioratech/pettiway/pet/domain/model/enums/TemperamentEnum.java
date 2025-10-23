package com.altioratech.pettiway.pet.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TemperamentEnum {
    TRANQUILO("Tranquilo"),
    JUGUETON("Juguetón"),
    PROTECTOR("Protector"),
    NERVIOSO("Nervioso"),
    SOCIABLE("Sociable"),
    INDEPENDIENTE("Independiente"),
    CARIÑOSO("Cariñoso"),
    TEMEROSO("Temeroso"),
    AGRESIVO("Agresivo");

    private final String label;

    TemperamentEnum(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static TemperamentEnum from(String value) {
        for (TemperamentEnum t : values()) {
            if (t.name().equalsIgnoreCase(value) || t.label.equalsIgnoreCase(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Temperamento inválido: " + value);
    }
}