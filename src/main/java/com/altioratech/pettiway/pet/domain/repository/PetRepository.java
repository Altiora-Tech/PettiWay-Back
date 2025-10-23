package com.altioratech.pettiway.pet.domain.repository;

import com.altioratech.pettiway.pet.domain.model.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PetRepository {

    Pet save(Pet pet);
    Optional<Pet> findById(UUID id);
    Page<Pet> findAllByClientId(UUID clientId, Pageable pageable);
}
