package com.altioratech.pettiway.user.infrastructure.out.persistence;

import com.altioratech.pettiway.user.domain.model.User;
import com.altioratech.pettiway.user.domain.repository.UserRepository;
import com.altioratech.pettiway.user.infrastructure.out.persistence.mapper.UserEntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository repo;
    private final UserEntityMapper mapper;

    public UserRepositoryAdapter(UserJpaRepository repo, UserEntityMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repo.findById(id).map(mapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        return mapper.toDomain(repo.save(entity));
    }

    @Override
    public List<User> findAll() {
        return repo.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}