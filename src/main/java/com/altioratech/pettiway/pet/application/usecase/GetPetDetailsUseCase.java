package com.altioratech.pettiway.pet.application.usecase;

import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.shared.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.altioratech.pettiway.pet.application.dto.PetResponseDTO;
import com.altioratech.pettiway.pet.application.mapper.PetMapper;
import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.domain.repository.PetRepository;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPetDetailsUseCase {

    private final AuthenticatedUserService authenticatedUserService;
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    /**
     * Obtiene los detalles de una mascota por su ID, validando que
     * pertenezca al cliente autenticado (o que sea un SUPER_ADMIN).
     */
    @Transactional(readOnly = true)
    public PetResponseDTO execute(UUID petId) throws MyException {

        // ✅ 1. Obtener usuario autenticado
        User user = authenticatedUserService.getAuthenticatedUser();

        // 🔎 2. Buscar la mascota
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new MyException("Mascota no encontrada"));

        // 🔒 3. Validar permisos (solo el dueño o admin pueden ver los detalles)
        if (user.getRole() == Role.CLIENT && !pet.getClientId().equals(user.getId())) {
            throw new MyException("No tienes permisos para acceder a esta mascota");
        }

        // 🚀 4. Devolver DTO con todos los datos
        return petMapper.toResponse(pet);
    }
}
