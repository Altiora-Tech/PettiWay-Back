package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.sitter.application.usecase.CreateSitterProfileUseCase;
import com.altioratech.pettiway.user.application.dto.request.RegisterUserRequest;
import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.application.mapper.UserDtoMapper;
import com.altioratech.pettiway.user.domain.model.AuthProvider;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.model.UserStatus;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final CreateSitterProfileUseCase createSitterProfileUseCase;

    public UserResponse execute(RegisterUserRequest request) {

        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new IllegalArgumentException("El email ya está registrado");
        });

        Role chosenRole = request.role() != null ? request.role() : Role.CLIENT;

        User user = userDtoMapper.toDomain(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setVerified(chosenRole.isAutoVerified());
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setProvider(AuthProvider.LOCAL);
        user.setProfileComplete(false);

        User saved = userRepository.save(user);

        switch (chosenRole) {
            case SITTER -> createSitterProfileUseCase.execute(saved, "", "");
            default -> {}
        }
        return userDtoMapper.toResponse(saved);
    }
}

