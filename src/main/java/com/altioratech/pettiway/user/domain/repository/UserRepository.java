package com.altioratech.pettiway.user.domain.repository;

import com.altioratech.pettiway.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    User save(User user);
    List<User> findAll();
}
