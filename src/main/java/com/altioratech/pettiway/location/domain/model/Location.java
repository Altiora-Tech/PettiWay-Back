package com.altioratech.pettiway.location.domain.model;

import com.altioratech.pettiway.location.application.dto.LocationDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    private UUID id;
    private String street;
    private String number;
    private String city;
    private String province;
    private String country;
    private Double latitude;
    private Double longitude;
    private String placeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ======================
    // Métodos de dominio puro
    // ======================
    public boolean isValid() {
        return latitude != null && longitude != null && latitude != 0 && longitude != 0;
    }

    public static Location fromDTO(LocationDTO dto) {
        return Location.builder()
                .street(dto.street())
                .number(dto.number())
                .city(dto.city())
                .province(dto.province())
                .country(dto.country())
                .latitude(dto.lat())
                .longitude(dto.lng())
                .placeId(dto.placeId())
                .build();
    }

    public LocationDTO toDTO() {
        return new LocationDTO(street, number, city, province, country, latitude, longitude, placeId);
    }

    public String fullAddress() {
        return Stream.of(street + " " + number, city, province, country)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(", "));
    }
}

