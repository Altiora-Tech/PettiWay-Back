package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.application.dto.request.LoginRequest;
import com.altioratech.pettiway.user.application.dto.response.LoginResponse;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import com.altioratech.pettiway.user.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new LoginResponse(user.getId(), user.getEmail(), user.getName(), user.getRole(), token);
    }
}
