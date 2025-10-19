package com.altioratech.pettiway.sitter.infrastructure.in.rest.dto;

import com.altioratech.pettiway.location.application.dto.LocationDTO;
import com.altioratech.pettiway.sitter.domain.model.ProfessionalRole;

import java.util.List;

public record UpdateSitterProfileBackendDTO(
        String bio,
        String experience,
        LocationDTO location,
        List<ProfessionalRole> professionalRoles
) {}