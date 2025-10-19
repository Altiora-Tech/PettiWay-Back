package com.altioratech.pettiway.location.domain.repository;

import com.altioratech.pettiway.location.domain.model.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {
    Location save(Location location);
    Optional<Location> findById(UUID id);
    List<Location> findAll();
}