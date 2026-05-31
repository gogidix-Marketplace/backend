package com.gogidix.shared.warehousing.pricing.interfaces.rest;

import com.gogidix.shared.warehousing.pricing.application.command.CalculatePriceCommand;
import com.gogidix.shared.warehousing.pricing.application.command.CreatePriceTierCommand;
import com.gogidix.shared.warehousing.pricing.application.command.CreatePricingRuleCommand;
import com.gogidix.shared.warehousing.pricing.application.command.UpdatePricingRuleCommand;
import com.gogidix.shared.warehousing.pricing.application.dto.*;
import com.gogidix.shared.warehousing.pricing.application.service.PricingService;
import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pricing REST Controller
 *
 * Provides multi-tenant pricing management and calculation APIs
 * All endpoints require X-Tenant-ID header for tenant isolation
 */
@RestController
@RequestMapping("/pricing")
@RequiredArgsConstructor
@Tag(name = "Pricing", description = "Storage pricing and price calculation APIs")
public class PricingController {

    private final PricingService pricingService;

    /**
     * Create pricing rule (tenant-scoped)
     * POST /api/v1/pricing/rules
     */
    @PostMapping("/rules")
    @Operation(summary = "Create a new pricing rule", description = "Creates a new pricing rule for the current tenant")
    public ResponseEntity<PricingRuleDTO> createPricingRule(
            @Valid @RequestBody CreatePricingRuleCommand command) {
        PricingRuleDTO pricingRule = pricingService.createPricingRule(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(pricingRule);
    }

    /**
     * Get pricing rule by ID (tenant-scoped)
     * GET /api/v1/pricing/rules/{id}
     */
    @GetMapping("/rules/{id}")
    @Operation(summary = "Get pricing rule by ID", description = "Retrieves a specific pricing rule by ID for the current tenant")
    public ResponseEntity<PricingRuleDTO> getPricingRule(
            @Parameter(description = "Pricing rule ID") @PathVariable String id) {
        PricingRuleDTO pricingRule = pricingService.getPricingRule(id);
        return ResponseEntity.ok(pricingRule);
    }

    /**
     * Get all pricing rules for current tenant
     * GET /api/v1/pricing/rules
     */
    @GetMapping("/rules")
    @Operation(summary = "Get all pricing rules", description = "Retrieves all active pricing rules for the current tenant")
    public ResponseEntity<List<PricingRuleDTO>> getAllPricingRules() {
        List<PricingRuleDTO> rules = pricingService.getAllPricingRules();
        return ResponseEntity.ok(rules);
    }

    /**
     * Get pricing rules by service type (tenant-scoped)
     * GET /api/v1/pricing/rules/service/{serviceType}
     */
    @GetMapping("/rules/service/{serviceType}")
    @Operation(summary = "Get pricing rules by service type", description = "Retrieves pricing rules filtered by service type")
    public ResponseEntity<List<PricingRuleDTO>> getPricingRulesByServiceType(
            @Parameter(description = "Service type") @PathVariable PricingRule.ServiceType serviceType) {
        List<PricingRuleDTO> rules = pricingService.getPricingRulesByServiceType(serviceType);
        return ResponseEntity.ok(rules);
    }

    /**
     * Get pricing rules by warehouse (tenant-scoped)
     * GET /api/v1/pricing/rules/warehouse/{warehouseId}
     */
    @GetMapping("/rules/warehouse/{warehouseId}")
    @Operation(summary = "Get pricing rules by warehouse", description = "Retrieves pricing rules for a specific warehouse")
    public ResponseEntity<List<PricingRuleDTO>> getPricingRulesByWarehouse(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<PricingRuleDTO> rules = pricingService.getPricingRulesByWarehouse(warehouseId);
        return ResponseEntity.ok(rules);
    }

    /**
     * Update pricing rule (tenant-scoped)
     * PUT /api/v1/pricing/rules/{id}
     */
    @PutMapping("/rules/{id}")
    @Operation(summary = "Update pricing rule", description = "Updates an existing pricing rule")
    public ResponseEntity<PricingRuleDTO> updatePricingRule(
            @Parameter(description = "Pricing rule ID") @PathVariable String id,
            @Valid @RequestBody UpdatePricingRuleCommand command) {
        PricingRuleDTO pricingRule = pricingService.updatePricingRule(id, command);
        return ResponseEntity.ok(pricingRule);
    }

    /**
     * Delete pricing rule (tenant-scoped)
     * DELETE /api/v1/pricing/rules/{id}
     */
    @DeleteMapping("/rules/{id}")
    @Operation(summary = "Delete pricing rule", description = "Deletes a pricing rule")
    public ResponseEntity<Void> deletePricingRule(
            @Parameter(description = "Pricing rule ID") @PathVariable String id) {
        pricingService.deletePricingRule(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calculate price (tenant-scoped)
     * POST /api/v1/pricing/calculate
     */
    @PostMapping("/calculate")
    @Operation(summary = "Calculate price", description = "Calculates price for storage service based on provided parameters")
    public ResponseEntity<PriceCalculationResultDTO> calculatePrice(
            @Valid @RequestBody CalculatePriceCommand command) {
        PriceCalculationResultDTO result = pricingService.calculatePrice(command);
        return ResponseEntity.ok(result);
    }

    /**
     * Get price quote by number (tenant-scoped)
     * GET /api/v1/pricing/quotes/{quoteNumber}
     */
    @GetMapping("/quotes/{quoteNumber}")
    @Operation(summary = "Get price quote", description = "Retrieves a price quote by quote number")
    public ResponseEntity<PriceQuoteDTO> getQuoteByNumber(
            @Parameter(description = "Quote number") @PathVariable String quoteNumber) {
        PriceQuoteDTO quote = pricingService.getQuoteByNumber(quoteNumber);
        return ResponseEntity.ok(quote);
    }

    /**
     * Get all quotes for current tenant
     * GET /api/v1/pricing/quotes
     */
    @GetMapping("/quotes")
    @Operation(summary = "Get all quotes", description = "Retrieves all price quotes for the current tenant")
    public ResponseEntity<List<PriceQuoteDTO>> getAllQuotes() {
        List<PriceQuoteDTO> quotes = pricingService.getAllQuotes();
        return ResponseEntity.ok(quotes);
    }

    /**
     * Create price tier (tenant-scoped)
     * POST /api/v1/pricing/tiers
     */
    @PostMapping("/tiers")
    @Operation(summary = "Create price tier", description = "Creates a new volume-based pricing tier")
    public ResponseEntity<PriceTierDTO> createPriceTier(
            @Valid @RequestBody CreatePriceTierCommand command) {
        PriceTierDTO tier = pricingService.createPriceTier(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(tier);
    }

    /**
     * Get price tiers for a pricing rule (tenant-scoped)
     * GET /api/v1/pricing/rules/{ruleId}/tiers
     */
    @GetMapping("/rules/{ruleId}/tiers")
    @Operation(summary = "Get price tiers", description = "Retrieves all price tiers for a specific pricing rule")
    public ResponseEntity<List<PriceTierDTO>> getPriceTiersForRule(
            @Parameter(description = "Pricing rule ID") @PathVariable String ruleId) {
        List<PriceTierDTO> tiers = pricingService.getPriceTiersForRule(ruleId);
        return ResponseEntity.ok(tiers);
    }
}
