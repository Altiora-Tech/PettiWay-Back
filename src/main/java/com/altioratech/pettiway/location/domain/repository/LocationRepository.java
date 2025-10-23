package com.altioratech.pettiway.location.domain.repository;

import com.altioratech.pettiway.location.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {

    Location save(Location location);
    Optional<Location> findById(UUID id);
    List<Location> findAll();
    // 🔎 Nuevos métodos para paginación y filtrado
    Page<Location> findAll(Pageable pageable);
    Page<Location> findByCountry(String country, Pageable pageable);
    Page<Location> findByProvince(String province, Pageable pageable);
    Page<Location> findByCountryAndProvince(String country, String province, Pageable pageable);
    void deleteById(UUID id);

}