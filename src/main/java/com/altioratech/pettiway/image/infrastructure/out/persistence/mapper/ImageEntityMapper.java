package com.altioratech.pettiway.image.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.image.domain.Image;
import com.altioratech.pettiway.image.infrastructure.out.persistence.ImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ImageEntityMapper {

    ImageEntity toEntity(Image domain);

    Image toDomain(ImageEntity entity);
}
