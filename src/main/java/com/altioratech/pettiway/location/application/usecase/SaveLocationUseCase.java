package com.altioratech.pettiway.location.application.usecase;

import com.altioratech.pettiway.location.domain.model.Location;
import com.altioratech.pettiway.location.domain.repository.LocationRepository;
import com.altioratech.pettiway.location.domain.service.GeocodingServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveLocationUseCase {

    private final LocationRepository locationRepository;
    private final GeocodingServicePort geocodingServicePort;

    public Location execute(Location location) {
        String address = location.fullAddress();
        double[] coords = geocodingServicePort.getCoordinatesFromAddress(address);

        if (coords == null || coords.length != 2) {
            throw new RuntimeException("No se pudieron obtener coordenadas para: " + address);
        }

        location.setLatitude(coords[0]);
        location.setLongitude(coords[1]);
        return locationRepository.save(location);
    }
}