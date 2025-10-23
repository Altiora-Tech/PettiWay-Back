package com.altioratech.pettiway.pet.application.usecase;

import com.altioratech.pettiway.pet.application.dto.PetCreateDTO;
import com.altioratech.pettiway.pet.application.dto.PetResponseDTO;
import com.altioratech.pettiway.pet.application.mapper.PetMapper;
import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.domain.repository.PetRepository;
import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.shared.exception.MyException;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class RegisterPetUseCase {

    private final AuthenticatedUserService authenticatedUserService;
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Transactional
    public PetResponseDTO execute(PetCreateDTO dto) throws MyException {

        // ✅ Usuario autenticado
        User user = authenticatedUserService.getAuthenticatedUser();

        // 🛑 Validación de rol
        if (user.getRole() != Role.CLIENT) {
            throw new MyException("Solo los clientes pueden registrar mascotas");
        }

        // 🧩 Mapear DTO → dominio
        Pet pet = petMapper.toDomain(dto);
        pet.setClientId(user.getId());
        pet.setActive(true);

        // 🧠 Cálculo automático de edad
        if (pet.getBirthDate() != null) {
            int years = Period.between(pet.getBirthDate(), LocalDate.now()).getYears();
            pet.setAge(years);
        }

        // 💾 Guardar
        Pet savedPet = petRepository.save(pet);

        // 🔁 Mapear a respuesta
        return petMapper.toResponse(savedPet);
    }
}