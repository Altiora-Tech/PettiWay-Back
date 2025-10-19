package com.altioratech.pettiway.location.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLocationRepository extends JpaRepository<LocationEntity, UUID> {
}
