package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.application.mapper.UserDtoMapper;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserResponse execute(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return userDtoMapper.toResponse(user);
    }
}
