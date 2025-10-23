package com.altioratech.pettiway.pet.application.usecase;

import com.altioratech.pettiway.user.application.service.AuthenticatedUserService;
import com.altioratech.pettiway.shared.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.domain.repository.PetRepository;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeletePetUseCase {

    private final AuthenticatedUserService authenticatedUserService;
    private final PetRepository petRepository;

    @Transactional
    public void execute(UUID petId) throws MyException {

        // ✅ 1. Obtener usuario autenticado
        User user = authenticatedUserService.getAuthenticatedUser();

        // 🔒 2. Validar roles
        if (user.getRole() != Role.CLIENT && user.getRole() != Role.SUPER_ADMIN) {
            throw new MyException("Tu rol no permite eliminar mascotas");
        }

        // 🔎 3. Buscar la mascota
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new MyException("Mascota no encontrada"));

        // 🚫 4. Verificar que la mascota pertenece al usuario (si no es admin)
        if (user.getRole() == Role.CLIENT && !existingPet.getClientId().equals(user.getId())) {
            throw new MyException("No tienes permisos para eliminar esta mascota");
        }

        // ⚠️ 5. Validar si ya estaba inactiva
        if (Boolean.FALSE.equals(existingPet.getActive())) {
            throw new MyException("La mascota ya está inactiva");
        }

        // 🚀 6. Baja lógica
        existingPet.setActive(false);

        // 💾 7. Guardar cambios
        petRepository.save(existingPet);

        // 📨 8. (Opcional futuro) Notificar profesionales o cancelar reservas
        // TODO: notificar si hay reservas activas o pendientes
    }
}