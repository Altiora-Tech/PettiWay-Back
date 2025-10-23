package com.altioratech.pettiway.pet.application.usecase;

import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.shared.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.altioratech.pettiway.pet.application.dto.PetResponseDTO;
import com.altioratech.pettiway.pet.application.dto.PetUpdateDTO;
import com.altioratech.pettiway.pet.application.mapper.PetMapper;
import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.domain.repository.PetRepository;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdatePetUseCase {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public PetResponseDTO execute(UUID petId, PetUpdateDTO dto) throws MyException {

        // ✅ Obtener usuario autenticado
        User user = authenticatedUserService.getAuthenticatedUser();

        // 🔒 Validar roles
        if (user.getRole() != Role.CLIENT && user.getRole() != Role.SUPER_ADMIN) {
            throw new MyException("Tu rol no permite editar mascotas");
        }

        // 🔎 Buscar la mascota existente
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new MyException("Mascota no encontrada"));

        // 🚫 Validar que la mascota pertenece al usuario (si no es admin)
        if (user.getRole() == Role.CLIENT && !existingPet.getClientId().equals(user.getId())) {
            throw new MyException("No tienes permisos para editar esta mascota");
        }

        // 🧠 Actualizar datos (MapStruct ignora nulls)
        petMapper.updateDomain(existingPet, dto);

        // 🧮 Recalcular edad si cambió la fecha de nacimiento
        if (dto.birthDate() != null) {
            int years = Period.between(dto.birthDate(), LocalDate.now()).getYears();
            existingPet.setAge(years);
        }

        // 💾 Guardar cambios
        Pet updatedPet = petRepository.save(existingPet);

        // 🔁 Retornar respuesta
        return petMapper.toResponse(updatedPet);
    }
}
