package com.altioratech.pettiway.location.infrastructure.out.persistence;

import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.domain.repository.LocationRepository;
import com.altioratech.pettiway.location.infrastructure.out.persistence.mapper.LocationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Component
@RequiredArgsConstructor
public class LocationRepositoryAdapter implements LocationRepository {

    private final JpaLocationRepository jpaRepository;
    private final LocationEntityMapper mapper;

    @Override
    public Location save(Location location) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(location)));
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Location> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Location> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Location> findByCountry(String country, Pageable pageable) {
        return jpaRepository.findByCountry(country, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Location> findByProvince(String province, Pageable pageable) {
        return jpaRepository.findByProvince(province, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<Location> findByCountryAndProvince(String country, String province, Pageable pageable) {
        return jpaRepository.findByCountryAndProvince(country, province, pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
