package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeUserPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void execute(String email, String currentPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar la contraseña actual
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Validar coincidencia de la nueva
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas nuevas no coinciden");
        }

        // Guardar la nueva contraseña
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}

