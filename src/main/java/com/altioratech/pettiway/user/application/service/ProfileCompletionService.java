package com.altioratech.pettiway.user.application.service;

import com.altioratech.pettiway.image.domain.ImageCategory;
import com.altioratech.pettiway.image.domain.ImageRepository;
import com.altioratech.pettiway.sitter.domain.repository.SitterRepository;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileCompletionService {

    private final ImageRepository imageRepository;
    private final SitterRepository sitterRepository;
    private final UserRepository userRepository;

    /**
     * Actualiza el estado del perfil completo para un usuario dado.
     * Se usa en cualquier momento en que el usuario actualiza datos relevantes:
     * - Información personal (nombre, teléfono)
     * - Foto de perfil
     * - Documentación
     * - Perfil profesional (Sitter)
     */
    public boolean updateProfileCompletion(User user) {
        boolean completo = isUserProfileComplete(user);
        user.setProfileComplete(completo);
        userRepository.save(user);
        return completo;
    }

    /**
     * Valida los datos base del usuario (nombre, teléfono, foto).
     * Si el usuario tiene roles extendidos (SITTER, PROVIDER), verifica sus subperfiles.
     */
    private boolean isUserProfileComplete(User user) {
        boolean tieneNombre = user.getName() != null && !user.getName().isBlank();
        boolean tieneTelefono = user.getPhone() != null && !user.getPhone().isBlank();
        boolean tieneFotoPerfil = imageRepository.countByUserIdAndCategory(user.getId(), ImageCategory.USER_PROFILE) > 0;

        boolean baseCompleto = tieneNombre && tieneTelefono && tieneFotoPerfil;

        // Si no tiene roles extendidos, solo validamos lo básico
        if (user.getRole() == null || user.getRole() == Role.CLIENT) {
            return baseCompleto;
        }

        // Si es Sitter o Provider, se verifica el perfil profesional
        if (user.getRole() == Role.SITTER) {
            return baseCompleto && isSitterProfileComplete(user.getId());
        }

        if (user.getRole() == Role.PROVIDER) {
            // Futuro: agregar verificación para negocios
            return baseCompleto; // temporal
        }

        return baseCompleto;
    }

    /**
     * Valida que el perfil profesional del Sitter esté completo:
     * - Bio y experiencia
     * - Roles profesionales
     * - Ubicación registrada
     * - Documentación de verificación
     */
    private boolean isSitterProfileComplete(UUID userId) {
        return sitterRepository.findByUserId(userId)
                .map(sitter -> {
                    boolean tieneBio = sitter.getBio() != null && !sitter.getBio().isBlank();
                    boolean tieneExp = sitter.getExperience() != null && !sitter.getExperience().isBlank();
                    boolean tieneRoles = sitter.getProfessionalRoles() != null && !sitter.getProfessionalRoles().isEmpty();
                    boolean tieneUbicacion = sitter.getLocationId() != null;

                    boolean tieneDocs = imageRepository.countByUserIdAndCategory(userId, ImageCategory.VERIFICATION_DOCUMENT) > 0
                            || imageRepository.countByUserIdAndCategory(userId, ImageCategory.VERIFICATION_PDF) > 0;

                    return tieneBio && tieneExp && tieneRoles && tieneUbicacion && tieneDocs;
                })
                .orElse(false);
    }
}