package com.altioratech.pettiway.sitter.application.usecase;

import com.altioratech.pettiway.image.domain.ImageRepository;
import com.altioratech.pettiway.location.application.dto.LocationDTO;
import com.altioratech.pettiway.location.application.usecase.SaveLocationUseCase;
import com.altioratech.pettiway.location.application.usecase.UpdateLocationUseCase;
import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.domain.service.GeocodingServicePort;
import com.altioratech.pettiway.sitter.domain.model.ProfessionalRole;
import com.altioratech.pettiway.sitter.domain.model.Sitter;
import com.altioratech.pettiway.sitter.domain.model.SitterStatus;
import com.altioratech.pettiway.sitter.domain.repository.SitterRepository;
import com.altioratech.pettiway.user.application.service.ProfileCompletionService;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.verification.domain.VerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateSitterProfileUseCase {

    private final SitterRepository sitterRepository;
    private final UpdateLocationUseCase updateLocationUseCase;
    private final SaveLocationUseCase saveLocationUseCase;
    private final GeocodingServicePort geocodingServicePort;
    private final ProfileCompletionService profileCompletionService;

    /**
     * Actualiza o completa el perfil profesional del Sitter autenticado.
     */
    public Sitter execute(String bio, String experience, LocationDTO locationDTO, List<ProfessionalRole> professionalRoles) {

        // 🔹 Obtenemos el Sitter autenticado
        Sitter sitter = getAuthenticatedSitter();

        sitter.setBio(bio);
        sitter.setExperience(experience);
        sitter.setProfessionalRoles(professionalRoles);

        // 🧭 Manejo de ubicación
        if (locationDTO != null) {
            Location location = Location.fromDTO(locationDTO);

            // Si no hay placeId, lo generamos
            if (location.getPlaceId() == null || location.getPlaceId().isBlank()) {
                var autocomplete = geocodingServicePort.getPlaceIdFromLocationDTO(locationDTO);
                location.setPlaceId(autocomplete.placeId());
            }

            // Guardar o actualizar
            Location persisted = (sitter.getLocationId() == null)
                    ? saveLocationUseCase.execute(location)
                    : updateLocationUseCase.execute(sitter.getLocationId(), location);

            sitter.setLocationId(persisted.getId());
        }
        Sitter updated = sitterRepository.save(sitter);

        // Actualizamos estado de perfil del usuario base
        User user = sitter.getUser();
        profileCompletionService.updateProfileCompletion(user);

        return updated;
    }

    private Sitter getAuthenticatedSitter() {
        var auth = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var email = auth.getUsername();
        return sitterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Sitter no encontrado"));
    }
}
