package com.altioratech.pettiway.location.domain.service;

import com.altioratech.pettiway.location.application.dto.AutocompleteSuggestion;
import com.altioratech.pettiway.location.application.dto.LocationDTO;

import java.util.List;

public interface GeocodingServicePort {
    double[] getCoordinatesFromAddress(String address);
    List<AutocompleteSuggestion> getAutocompleteSuggestions(String input);
    LocationDTO getLocationFromPlaceId(String placeId);
    AutocompleteSuggestion getPlaceIdFromLocationDTO(LocationDTO dto);
}