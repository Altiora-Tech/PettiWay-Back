package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.application.dto.request.UpdateUserRequest;
import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.application.mapper.UserDtoMapper;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserResponse execute(String email, UpdateUserRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if(request.name() != null) user.setName(request.name());
        if(request.password() != null) user.setPassword(request.password());

        User updatedUser = userRepository.save(user);
        return userDtoMapper.toResponse(updatedUser);
    }
}
