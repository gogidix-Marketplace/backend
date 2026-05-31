package com.gogidix.ecommerce.inventory.interfaces.rest;

import com.gogidix.ecommerce.inventory.application.dto.*;
import com.gogidix.ecommerce.inventory.application.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inventorys")
@Tag(name = "Inventory Service", description = "APIs for managing inventorys")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active inventorys")
    public ResponseEntity<List<InventoryResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get inventory by ID")
    public ResponseEntity<InventoryResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create inventory")
    public ResponseEntity<InventoryResponse> create(@Valid @RequestBody CreateInventoryRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update inventory")
    public ResponseEntity<InventoryResponse> update(@PathVariable String id, @Valid @RequestBody UpdateInventoryRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete inventory")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
