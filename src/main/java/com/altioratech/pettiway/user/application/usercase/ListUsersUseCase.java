package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.application.dto.response.UserResponse;
import com.altioratech.pettiway.user.application.mapper.UserDtoMapper;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public List<UserResponse> execute() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userDtoMapper::toResponse)
                .toList();
    }
}
