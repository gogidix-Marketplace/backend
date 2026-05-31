package com.gogidix.ecommerce.discount.interfaces.rest;

import com.gogidix.ecommerce.discount.application.dto.*;
import com.gogidix.ecommerce.discount.application.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
@Tag(name = "Discount Service", description = "APIs for managing discounts")
public class DiscountController {

    private final DiscountService service;

    public DiscountController(DiscountService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active discounts")
    public ResponseEntity<List<DiscountResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get discount by ID")
    public ResponseEntity<DiscountResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create discount")
    public ResponseEntity<DiscountResponse> create(@Valid @RequestBody CreateDiscountRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update discount")
    public ResponseEntity<DiscountResponse> update(@PathVariable String id, @Valid @RequestBody UpdateDiscountRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete discount")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
