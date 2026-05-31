package com.gogidix.ecommerce.storecredit.interfaces.rest;

import com.gogidix.ecommerce.storecredit.application.dto.*;
import com.gogidix.ecommerce.storecredit.application.service.StoreCreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store-credits")
@Tag(name = "StoreCredit Service", description = "APIs for managing store-credits")
public class StoreCreditController {

    private final StoreCreditService service;

    public StoreCreditController(StoreCreditService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active store-credits")
    public ResponseEntity<List<StoreCreditResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get store-credit by ID")
    public ResponseEntity<StoreCreditResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create store-credit")
    public ResponseEntity<StoreCreditResponse> create(@Valid @RequestBody CreateStoreCreditRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update store-credit")
    public ResponseEntity<StoreCreditResponse> update(@PathVariable String id, @Valid @RequestBody UpdateStoreCreditRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete store-credit")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
