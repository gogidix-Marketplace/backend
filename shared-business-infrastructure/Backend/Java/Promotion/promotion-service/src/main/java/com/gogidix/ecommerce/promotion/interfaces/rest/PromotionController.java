package com.gogidix.ecommerce.promotion.interfaces.rest;

import com.gogidix.ecommerce.promotion.application.dto.*;
import com.gogidix.ecommerce.promotion.application.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
@Tag(name = "Promotion Service", description = "APIs for managing promotions")
public class PromotionController {

    private final PromotionService service;

    public PromotionController(PromotionService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active promotions")
    public ResponseEntity<List<PromotionResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get promotion by ID")
    public ResponseEntity<PromotionResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create promotion")
    public ResponseEntity<PromotionResponse> create(@Valid @RequestBody CreatePromotionRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update promotion")
    public ResponseEntity<PromotionResponse> update(@PathVariable String id, @Valid @RequestBody UpdatePromotionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete promotion")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
