package com.altioratech.pettiway.sitter.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.sitter.domain.model.Sitter;
import com.altioratech.pettiway.sitter.infrastructure.out.persistence.SitterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SitterEntityMapper {

    // De dominio → entidad
    @Mapping(target = "user", source = "user") // El mapper de User se encarga del detalle
    SitterEntity toEntity(Sitter sitter);

    // De entidad → dominio
    @Mapping(target = "user", source = "user")
    Sitter toDomain(SitterEntity entity);
}
