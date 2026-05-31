package com.gogidix.ecommerce.airfreight.interfaces.rest;

import com.gogidix.ecommerce.airfreight.application.dto.*;
import com.gogidix.ecommerce.airfreight.application.service.AirFreightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/air-freights")
@Tag(name = "AirFreight Service", description = "APIs for managing air-freights")
public class AirFreightController {

    private final AirFreightService service;

    public AirFreightController(AirFreightService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active air-freights")
    public ResponseEntity<List<AirFreightResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get air-freight by ID")
    public ResponseEntity<AirFreightResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create air-freight")
    public ResponseEntity<AirFreightResponse> create(@Valid @RequestBody CreateAirFreightRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update air-freight")
    public ResponseEntity<AirFreightResponse> update(@PathVariable String id, @Valid @RequestBody UpdateAirFreightRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete air-freight")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
