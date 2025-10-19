package com.altioratech.pettiway.location.application.mapper;

import com.altioratech.pettiway.location.application.dto.LocationDTO;
import com.altioratech.pettiway.location.domain.model.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toEntity(LocationDTO dto);

    LocationDTO toDTO(Location location);
}