package com.altioratech.pettiway.pet.infrastructure.out.persistence;

import com.altioratech.pettiway.pet.domain.model.Pet;
import com.altioratech.pettiway.pet.domain.repository.PetRepository;
import com.altioratech.pettiway.pet.infrastructure.out.mapper.PetEntityMapper;
import com.altioratech.pettiway.shared.util.PageMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PetRepositoryAdapter implements PetRepository {

    private final PetJpaRepository jpaRepository;
    private final PetEntityMapper mapper;

    @Override
    public Pet save(Pet pet) {
        var entity = mapper.toEntity(pet);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Pet> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Pet> findAllByClientId(UUID clientId, Pageable pageable) {
        var page = jpaRepository.findAllByClientId(clientId, pageable);
        return PageMapperUtil.mapPage(page, mapper::toDomain);
    }
}