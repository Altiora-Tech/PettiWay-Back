package com.altioratech.pettiway.location.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.infrastructure.out.persistence.LocationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationEntityMapper {
    LocationEntity toEntity(Location domain);
    Location toDomain(LocationEntity entity);
}