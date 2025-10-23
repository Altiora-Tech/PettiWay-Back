package com.altioratech.pettiway.location.infrastructure.out.persistence;

import com.altioratech.pettiway.location.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaLocationRepository extends JpaRepository<LocationEntity, UUID> {
    Page<LocationEntity> findByCountry(String country, Pageable pageable);
    Page<LocationEntity> findByProvince(String province, Pageable pageable);
    Page<LocationEntity> findByCountryAndProvince(String country, String province, Pageable pageable);

}
