package com.gogidix.ecommerce.vendor.onboarding.interfaces.rest;

import com.gogidix.ecommerce.vendor.onboarding.domain.model.Onboarding;
import com.gogidix.ecommerce.vendor.onboarding.domain.service.OnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
@Tag(name = "Vendor onboarding session Management", description = "APIs for managing vendor onboarding sessions")
public class OnboardingController {

    private final OnboardingService onboardingService;

    @GetMapping
    @Operation(summary = "List all")
    public ResponseEntity<List<Onboarding>> listAll() { return ResponseEntity.ok(onboardingService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID")
    public ResponseEntity<Onboarding> getById(@PathVariable String id) {
        Onboarding result = onboardingService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create")
    public ResponseEntity<Onboarding> create(@RequestBody Onboarding entity) { return ResponseEntity.ok(onboardingService.create(entity)); }

    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public ResponseEntity<Onboarding> update(@PathVariable String id, @RequestBody Onboarding entity) {
        Onboarding result = onboardingService.update(id, entity);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete")
    public ResponseEntity<Void> delete(@PathVariable String id) { onboardingService.delete(id); return ResponseEntity.noContent().build(); }
}