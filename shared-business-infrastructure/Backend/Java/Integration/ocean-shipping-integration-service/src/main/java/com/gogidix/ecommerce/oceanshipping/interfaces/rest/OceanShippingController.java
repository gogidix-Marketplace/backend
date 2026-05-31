package com.gogidix.ecommerce.oceanshipping.interfaces.rest;

import com.gogidix.ecommerce.oceanshipping.application.dto.*;
import com.gogidix.ecommerce.oceanshipping.application.service.OceanShippingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ocean-shippings")
@Tag(name = "OceanShipping Service", description = "APIs for managing ocean-shippings")
public class OceanShippingController {

    private final OceanShippingService service;

    public OceanShippingController(OceanShippingService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active ocean-shippings")
    public ResponseEntity<List<OceanShippingResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ocean-shipping by ID")
    public ResponseEntity<OceanShippingResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create ocean-shipping")
    public ResponseEntity<OceanShippingResponse> create(@Valid @RequestBody CreateOceanShippingRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ocean-shipping")
    public ResponseEntity<OceanShippingResponse> update(@PathVariable String id, @Valid @RequestBody UpdateOceanShippingRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ocean-shipping")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
