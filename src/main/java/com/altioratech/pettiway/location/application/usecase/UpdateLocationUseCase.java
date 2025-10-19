package com.altioratech.pettiway.location.application.usecase;

import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.domain.repository.LocationRepository;
import com.altioratech.pettiway.location.domain.service.GeocodingServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateLocationUseCase {

    private final LocationRepository locationRepository;
    private final GeocodingServicePort geocodingServicePort;

    public Location execute(UUID id, Location updatedData) {
        Location existing = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

        boolean addressChanged = !existing.fullAddress().equals(updatedData.fullAddress());
        existing.setStreet(updatedData.getStreet());
        existing.setNumber(updatedData.getNumber());
        existing.setCity(updatedData.getCity());
        existing.setProvince(updatedData.getProvince());
        existing.setCountry(updatedData.getCountry());
        existing.setPlaceId(updatedData.getPlaceId());

        if (addressChanged) {
            double[] coords = geocodingServicePort.getCoordinatesFromAddress(existing.fullAddress());
            if (coords == null || coords.length != 2) {
                throw new RuntimeException("No se pudieron obtener coordenadas para: " + existing.fullAddress());
            }
            existing.setLatitude(coords[0]);
            existing.setLongitude(coords[1]);
        }

        return locationRepository.save(existing);
    }
}

