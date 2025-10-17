package com.altioratech.pettiway.user.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.infrastructure.out.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring", // para inyectar con @Autowired
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserEntityMapper {

    @Mapping(target = "id", source = "id")
    UserEntity toEntity(User user);

    @Mapping(target = "id", source = "id")
    User toDomain(UserEntity entity);
}