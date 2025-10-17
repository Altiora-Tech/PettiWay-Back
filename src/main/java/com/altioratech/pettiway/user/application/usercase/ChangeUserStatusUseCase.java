package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.model.UserStatus;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChangeUserStatusUseCase {

    private final UserRepository userRepository;

    public void execute(UUID id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setStatus(active ? UserStatus.ACTIVE : UserStatus.INACTIVE);
        userRepository.save(user);
    }
}
