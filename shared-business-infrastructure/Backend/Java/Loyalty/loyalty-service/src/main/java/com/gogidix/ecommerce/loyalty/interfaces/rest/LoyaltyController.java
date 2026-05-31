package com.gogidix.ecommerce.loyalty.interfaces.rest;

import com.gogidix.ecommerce.loyalty.application.dto.*;
import com.gogidix.ecommerce.loyalty.application.service.LoyaltyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loyaltys")
@Tag(name = "Loyalty Service", description = "APIs for managing loyaltys")
public class LoyaltyController {

    private final LoyaltyService service;

    public LoyaltyController(LoyaltyService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active loyaltys")
    public ResponseEntity<List<LoyaltyResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loyalty by ID")
    public ResponseEntity<LoyaltyResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create loyalty")
    public ResponseEntity<LoyaltyResponse> create(@Valid @RequestBody CreateLoyaltyRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update loyalty")
    public ResponseEntity<LoyaltyResponse> update(@PathVariable String id, @Valid @RequestBody UpdateLoyaltyRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete loyalty")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
