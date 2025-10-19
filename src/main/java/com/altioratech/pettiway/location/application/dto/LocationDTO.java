package com.altioratech.pettiway.location.application.dto;

public record LocationDTO(
        String street,
        String number,
        String city,
        String province,
        String country,
        Double lat,
        Double lng,
        String placeId
) {
}
