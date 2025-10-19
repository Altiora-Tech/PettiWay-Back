package com.altioratech.pettiway.sitter.application.usecase;

import com.altioratech.pettiway.sitter.domain.model.Sitter;
import com.altioratech.pettiway.sitter.domain.model.SitterStatus;
import com.altioratech.pettiway.sitter.domain.repository.SitterRepository;
import com.altioratech.pettiway.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateSitterProfileUseCase {

    private final SitterRepository sitterRepository;

    public Sitter execute(User user, String bio, String experience) {
        Sitter sitter = Sitter.builder()
                .user(user)
                .bio(bio)
                .experience(experience)
                .status(SitterStatus.PENDING)
                .build();

        return sitterRepository.save(sitter);
    }
}
