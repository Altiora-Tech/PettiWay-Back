package com.altioratech.pettiway.pet.application.usecase;

import com.altioratech.pettiway.pet.application.dto.PetResponseDTO;
import com.altioratech.pettiway.pet.application.mapper.PetMapper;
import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.domain.repository.PetRepository;
import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.shared.exception.MyException;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListClientPetsUseCase {

    private final AuthenticatedUserService authenticatedUserService;
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public Page<PetResponseDTO> execute(Pageable pageable) throws MyException {

        // ✅ Obtener usuario autenticado
        User user = authenticatedUserService.getAuthenticatedUser();

        // 🔒 Validar rol
        if (user.getRole() != Role.CLIENT && user.getRole() != Role.SUPER_ADMIN) {
            throw new MyException("Solo los clientes pueden listar sus mascotas");
        }

        // 🔎 Obtener mascotas del cliente autenticado
        Page<Pet> petsPage = petRepository.findAllByClientId(user.getId(), pageable);

        // 🚀 Mapear dominio → DTO de respuesta
        return petsPage.map(petMapper::toResponse);
    }
}