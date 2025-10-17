package com.altioratech.pettiway.user.application.mapper;

import com.altioratech.pettiway.user.application.dto.request.RegisterUserRequest;
import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoMapper {

    // de request → domain
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.altioratech.pettiway.user.domain.model.UserStatus.ACTIVE)")
    @Mapping(target = "provider", expression = "java(com.altioratech.pettiway.user.domain.model.AuthProvider.LOCAL)")
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    User toDomain(RegisterUserRequest dto);

    // de domain → response
    UserResponse toResponse(User user);
}