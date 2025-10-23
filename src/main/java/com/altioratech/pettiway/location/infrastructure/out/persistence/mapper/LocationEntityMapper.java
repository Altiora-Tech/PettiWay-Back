package com.altioratech.pettiway.location.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.infrastructure.out.persistence.LocationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationEntityMapper {
    LocationEntity toEntity(Location domain);
    Location toDomain(LocationEntity entity);
    List<Location> toDomainList(List<LocationEntity> entities);
}