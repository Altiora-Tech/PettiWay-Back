package com.altioratech.pettiway.location.infrastructure.in.rest;

import com.altioratech.pettiway.location.application.usecase.SaveLocationUseCase;
import com.altioratech.pettiway.location.application.usecase.UpdateLocationUseCase;
import com.altioratech.pettiway.location.domain.model.Location;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/locations")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class LocationController {

    private final SaveLocationUseCase saveLocationUseCase;
    private final UpdateLocationUseCase updateLocationUseCase;

    @PostMapping
    public ResponseEntity<Location> create(@RequestBody Location location) {
        return ResponseEntity.ok(saveLocationUseCase.execute(location));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Location dto) {
        return ResponseEntity.ok(updateLocationUseCase.execute(id, dto));
    }
}