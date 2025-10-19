package com.altioratech.pettiway.sitter.infrastructure.in.rest.dto;

import com.altioratech.pettiway.sitter.domain.model.ProfessionalRole;

import java.util.List;

public record UpdateSitterProfileFrontendDTO(
        String bio,
        String experience,
        String placeId,
        List<ProfessionalRole> professionalRoles
) {}