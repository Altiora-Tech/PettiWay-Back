package com.altioratech.pettiway.pet.infrastructure.out.mapper;

import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.infrastructure.out.persistence.PetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetEntityMapper {
    PetEntity toEntity(Pet domain);
    Pet toDomain(PetEntity entity);
}