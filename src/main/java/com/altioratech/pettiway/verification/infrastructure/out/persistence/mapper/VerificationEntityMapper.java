package com.altioratech.pettiway.verification.infrastructure.out.persistence.mapper;

import com.altioratech.pettiway.verification.domain.Verification;
import com.altioratech.pettiway.verification.infrastructure.out.persistence.VerificationJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VerificationEntityMapper {

    VerificationJpaEntity toEntity(Verification domain);

    Verification toDomain(VerificationJpaEntity entity);
}
