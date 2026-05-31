package com.gogidix.ecommerce.analytics.interfaces.rest;

import com.gogidix.ecommerce.analytics.application.dto.*;
import com.gogidix.ecommerce.analytics.application.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/analyticss")
@Tag(name = "Analytics Service", description = "APIs for managing analyticss")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active analyticss")
    public ResponseEntity<List<AnalyticsResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get analytics by ID")
    public ResponseEntity<AnalyticsResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create analytics")
    public ResponseEntity<AnalyticsResponse> create(@Valid @RequestBody CreateAnalyticsRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update analytics")
    public ResponseEntity<AnalyticsResponse> update(@PathVariable String id, @Valid @RequestBody UpdateAnalyticsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete analytics")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
