package com.gogidix.ecommerce.warehouse.interfaces.rest;

import com.gogidix.ecommerce.warehouse.application.dto.*;
import com.gogidix.ecommerce.warehouse.application.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
@Tag(name = "Warehouse Service", description = "APIs for managing warehouses")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active warehouses")
    public ResponseEntity<List<WarehouseResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<WarehouseResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create warehouse")
    public ResponseEntity<WarehouseResponse> create(@Valid @RequestBody CreateWarehouseRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse")
    public ResponseEntity<WarehouseResponse> update(@PathVariable String id, @Valid @RequestBody UpdateWarehouseRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete warehouse")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
