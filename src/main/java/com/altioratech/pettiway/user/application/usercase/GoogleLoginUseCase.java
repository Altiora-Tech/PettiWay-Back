package com.altioratech.pettiway.user.application.usercase;

import com.altioratech.pettiway.user.application.dto.request.GoogleLoginRequest;
import com.altioratech.pettiway.user.application.dto.response.GoogleUserInfo;
import com.altioratech.pettiway.user.application.dto.response.LoginResponse;
import com.altioratech.pettiway.user.domain.model.AuthProvider;
import com.altioratech.pettiway.user.domain.model.Role;
import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.model.UserStatus;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import com.altioratech.pettiway.user.infrastructure.security.GoogleOAuth2Service;
import com.altioratech.pettiway.user.infrastructure.security.JwtService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleLoginUseCase {
    private final GoogleOAuth2Service googleOAuth2Service;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public GoogleLoginUseCase(GoogleOAuth2Service googleOAuth2Service,
                              JwtService jwtService,
                              UserRepository userRepository) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public LoginResponse execute(GoogleLoginRequest request) {
        GoogleUserInfo googleUser = googleOAuth2Service.authenticateWithGoogle(request.idToken());
        String name = googleUser.name();
        String email = googleUser.email();

        Optional<User> existingUser = userRepository.findByEmail(email);
        User user;

        Role chosenRole = request.role() != null ? request.role() : Role.CLIENT;

        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setRole(request.role() != null ? request.role() : Role.CLIENT);
            user.setProvider(AuthProvider.GOOGLE);
            user.setVerified(chosenRole.isAutoVerified());
            user.setStatus(UserStatus.ACTIVE);
            user.setCreatedAt(LocalDateTime.now());

            user = userRepository.save(user);
        }

        String token = jwtService.generateToken(user.getId(), email, user.getRole().name());

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                token
        );
    }
}
