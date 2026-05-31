package com.gogidix.digitalmarketing.integration.interfaces.rest;

import com.gogidix.digitalmarketing.integration.application.dto.IntegrationRequestDto;
import com.gogidix.digitalmarketing.integration.application.dto.IntegrationResponseDto;
import com.gogidix.digitalmarketing.integration.application.service.IntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/integrations")
@RequiredArgsConstructor
@Tag(name = "Integration Management", description = "Integration Management")
public class IntegrationController {

    private final IntegrationService service;

    @PostMapping
    @Operation(summary = "Create a new Integration")
    public ResponseEntity<IntegrationResponseDto> create(@RequestBody IntegrationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Integration by ID")
    public ResponseEntity<IntegrationResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all Integrations")
    public ResponseEntity<List<IntegrationResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Integration")
    public ResponseEntity<IntegrationResponseDto> update(@PathVariable String id, @RequestBody IntegrationRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Integration")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}