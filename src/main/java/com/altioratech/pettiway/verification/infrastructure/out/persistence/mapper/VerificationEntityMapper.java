package com.altioratech.pettiway.verification.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.verification.domain.Verification;
import com.altioratech.pettiway.verification.infrastructure.out.persistence.VerificationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VerificationEntityMapper {

    VerificationEntity toEntity(Verification domain);

    Verification toDomain(VerificationEntity entity);
}
