package com.altioratech.pettiway.pet.application.mapper;

import com.altioratech.pettiway.pet.application.dto.PetCreateDTO;
import com.altioratech.pettiway.pet.application.dto.PetResponseDTO;
import com.altioratech.pettiway.pet.application.dto.PetUpdateDTO;
import com.altioratech.pettiway.pet.domain.model.Pet;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    Pet toDomain(PetCreateDTO dto);

    PetResponseDTO toResponse(Pet pet);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDomain(@MappingTarget Pet pet, PetUpdateDTO dto);
}