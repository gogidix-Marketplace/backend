package com.gogidix.ecommerce.pricing.interfaces.rest;

import com.gogidix.ecommerce.pricing.application.dto.*;
import com.gogidix.ecommerce.pricing.application.service.PricingRuleService;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pricing-rules")
@RequiredArgsConstructor
@Tag(name = "Pricing Rule Service", description = "APIs for managing pricing rules")
public class PricingRuleController {

    private final PricingRuleService pricingRuleService;

    @GetMapping
    @Operation(summary = "Get all active pricing rules", description = "Retrieve all active pricing rules for a tenant")
    public ResponseEntity<List<PricingRuleResponse>> getAllPricingRules() {
        return ResponseEntity.ok(pricingRuleService.getAllActivePricingRules());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pricing rule by ID", description = "Retrieve a specific pricing rule by its ID")
    public ResponseEntity<PricingRuleResponse> getPricingRule(@Parameter(description = "Pricing rule ID") @PathVariable String id) {
        return ResponseEntity.ok(pricingRuleService.getPricingRuleById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get pricing rule by code", description = "Retrieve a specific pricing rule by its code")
    public ResponseEntity<PricingRuleResponse> getPricingRuleByCode(@Parameter(description = "Rule code") @PathVariable String code) {
        return ResponseEntity.ok(pricingRuleService.getPricingRuleByCode(code));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get pricing rules by product", description = "Retrieve pricing rules applicable to a product")
    public ResponseEntity<List<PricingRuleResponse>> getPricingRulesByProduct(
            @Parameter(description = "Product ID") @PathVariable String productId) {
        return ResponseEntity.ok(pricingRuleService.getPricingRulesByProduct(productId));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get pricing rules by category", description = "Retrieve pricing rules applicable to a category")
    public ResponseEntity<List<PricingRuleResponse>> getPricingRulesByCategory(
            @Parameter(description = "Category ID") @PathVariable String categoryId) {
        return ResponseEntity.ok(pricingRuleService.getPricingRulesByCategory(categoryId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get pricing rules by type", description = "Retrieve pricing rules of a specific type")
    public ResponseEntity<List<PricingRuleResponse>> getPricingRulesByType(
            @Parameter(description = "Pricing type") @PathVariable PricingRule.PricingType type) {
        return ResponseEntity.ok(pricingRuleService.getPricingRulesByType(type));
    }

    @PostMapping
    @Operation(summary = "Create pricing rule", description = "Create a new pricing rule")
    public ResponseEntity<PricingRuleResponse> createPricingRule(@Valid @RequestBody CreatePricingRuleRequest request) {
        return ResponseEntity.ok(pricingRuleService.createPricingRule(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pricing rule", description = "Update an existing pricing rule")
    public ResponseEntity<PricingRuleResponse> updatePricingRule(
            @Parameter(description = "Pricing rule ID") @PathVariable String id,
            @Valid @RequestBody UpdatePricingRuleRequest request) {
        return ResponseEntity.ok(pricingRuleService.updatePricingRule(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pricing rule", description = "Delete a pricing rule")
    public ResponseEntity<Void> deletePricingRule(@Parameter(description = "Pricing rule ID") @PathVariable String id) {
        pricingRuleService.deletePricingRule(id);
        return ResponseEntity.ok().build();
    }
}
