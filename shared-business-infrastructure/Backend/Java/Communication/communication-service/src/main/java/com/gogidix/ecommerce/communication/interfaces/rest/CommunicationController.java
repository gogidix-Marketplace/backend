package com.gogidix.ecommerce.communication.interfaces.rest;

import com.gogidix.ecommerce.communication.application.dto.*;
import com.gogidix.ecommerce.communication.application.service.CommunicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/communications")
@Tag(name = "Communication Service", description = "APIs for managing communications")
public class CommunicationController {

    private final CommunicationService service;

    public CommunicationController(CommunicationService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active communications")
    public ResponseEntity<List<CommunicationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get communication by ID")
    public ResponseEntity<CommunicationResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create communication")
    public ResponseEntity<CommunicationResponse> create(@Valid @RequestBody CreateCommunicationRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update communication")
    public ResponseEntity<CommunicationResponse> update(@PathVariable String id, @Valid @RequestBody UpdateCommunicationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete communication")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
