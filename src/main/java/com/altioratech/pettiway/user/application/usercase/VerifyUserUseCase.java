package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.application.dto.request.VerifyUserRequest;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyUserUseCase {

    private final UserRepository userRepository;

    public void execute(VerifyUserRequest request) {

        User authenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (authenticated.getRole() != Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("Sólo los administradores pueden verificar usuarios");
        }
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setVerified(true);
        userRepository.save(user);
    }
}
