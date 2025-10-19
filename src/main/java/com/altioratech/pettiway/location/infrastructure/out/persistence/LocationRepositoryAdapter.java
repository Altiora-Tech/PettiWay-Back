package com.altioratech.pettiway.location.infrastructure.out.persistence;

import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.domain.repository.LocationRepository;
import com.altioratech.pettiway.location.infrastructure.out.persistence.mapper.LocationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocationRepositoryAdapter implements LocationRepository {

    private final JpaLocationRepository jpaRepo;
    private final LocationEntityMapper mapper;

    @Override
    public Location save(Location location) {
        return mapper.toDomain(jpaRepo.save(mapper.toEntity(location)));
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Location> findAll() {
        return jpaRepo.findAll().stream().map(mapper::toDomain).toList();
    }
}