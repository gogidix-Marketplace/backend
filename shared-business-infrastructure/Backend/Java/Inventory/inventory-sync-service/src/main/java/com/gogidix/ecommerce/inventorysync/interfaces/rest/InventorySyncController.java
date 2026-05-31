package com.gogidix.ecommerce.inventorysync.interfaces.rest;

import com.gogidix.ecommerce.inventorysync.application.dto.*;
import com.gogidix.ecommerce.inventorysync.application.service.InventorySyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory-syncs")
@Tag(name = "InventorySync Service", description = "APIs for managing inventory-syncs")
public class InventorySyncController {

    private final InventorySyncService service;

    public InventorySyncController(InventorySyncService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active inventory-syncs")
    public ResponseEntity<List<InventorySyncResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory-sync by ID")
    public ResponseEntity<InventorySyncResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create inventory-sync")
    public ResponseEntity<InventorySyncResponse> create(@Valid @RequestBody CreateInventorySyncRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory-sync")
    public ResponseEntity<InventorySyncResponse> update(@PathVariable String id, @Valid @RequestBody UpdateInventorySyncRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory-sync")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
