package com.altioratech.pettiway.pet.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum PetTypeEnum {
    // 🐶 Mamíferos comunes
    PERRO("Perro"),
    GATO("Gato"),
    CONEJO("Conejo"),
    HAMSTER("Hámster"),
    COBAYO("Cobayo / Cuy"),
    HURON("Hurón"),

    // 🐦 Aves
    LORO("Loro"),
    CANARIO("Canario"),
    PERIQUITO("Periquito"),
    COTORRA("Cotorra"),
    AGAPORNIS("Agapornis"),
    PALOMA("Paloma"),

    // 🐢 Reptiles
    TORTUGA("Tortuga"),
    IGUANA("Iguana"),
    GECKO("Gecko"),
    SERPIENTE("Serpiente"),

    // 🐠 Acuáticos
    PEZ("Pez"),
    GOLDFISH("Goldfish"),
    BETTA("Betta"),
    TORTUGA_ACUATICA("Tortuga acuática"),

    // 🐸 Anfibios
    RANA("Rana"),
    SAPO("Sapo"),
    AXOLOTE("Ajolote"),

    // 🐴 Animales de granja o campo
    CABALLO("Caballo"),
    VACA("Vaca"),
    OVEJA("Oveja"),
    CERDO("Cerdo"),
    GALLINA("Gallina"),
    GALLO("Gallo"),

    // 🐾 Otros / Exóticos
    ERIZO("Erizo"),
    CHINCHILLA("Chinchilla"),
    MONO("Mono"),
    MAPACHE("Mapache"),
    OTRO("Otro");

    private final String label;

    PetTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PetTypeEnum from(String value) {
        for (PetTypeEnum type : values()) {
            if (type.name().equalsIgnoreCase(value) || type.getLabel().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de mascota inválido: " + value);
    }
}
