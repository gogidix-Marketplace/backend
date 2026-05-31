package com.gogidix.shared.courier.pricing.interfaces.rest;

import com.gogidix.shared.courier.pricing.application.command.CalculatePriceCommand;
import com.gogidix.shared.courier.pricing.application.command.CreatePricingRuleCommand;
import com.gogidix.shared.courier.pricing.application.command.UpdatePricingRuleCommand;
import com.gogidix.shared.courier.pricing.application.dto.PriceCalculationDTO;
import com.gogidix.shared.courier.pricing.application.dto.PricingRuleDTO;
import com.gogidix.shared.courier.pricing.application.mapper.PricingDtoMapper;
import com.gogidix.shared.courier.pricing.application.service.PricingApplicationService;
import com.gogidix.shared.courier.pricing.application.query.PricingQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Pricing Engine Management
 * Provides endpoints for pricing rules and price calculations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/pricing")
@RequiredArgsConstructor
public class PricingController {

    private final PricingApplicationService pricingApplicationService;
    private final PricingDtoMapper dtoMapper;

    /**
     * Create a new pricing rule
     * POST /api/v1/pricing/rules
     */
    @PostMapping("/rules")
    public ResponseEntity<PricingRuleResponse> createPricingRule(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Valid @RequestBody CreatePricingRuleRequest request) {

        log.info("POST /api/v1/pricing/rules - Create pricing rule for tenant: {}", tenantId);

        CreatePricingRuleCommand command = CreatePricingRuleCommand.builder()
                .tenantId(tenantId)
                .ruleName(request.getRuleName())
                .description(request.getDescription())
                .ruleType(request.getRuleType())
                .priority(request.getPriority())
                .active(request.getActive())
                .vehicleType(request.getVehicleType())
                .serviceType(request.getServiceType())
                .parameters(request.getParameters())
                .build();

        PricingRuleDTO dto = pricingApplicationService.createPricingRule(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(PricingRuleResponse.fromDTO(dto));
    }

    /**
     * Get pricing rule by ID
     * GET /api/v1/pricing/rules/{ruleId}
     */
    @GetMapping("/rules/{ruleId}")
    public ResponseEntity<PricingRuleResponse> getPricingRule(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String ruleId) {

        log.info("GET /api/v1/pricing/rules/{} - Get pricing rule for tenant: {}", ruleId, tenantId);

        PricingRuleDTO dto = pricingApplicationService.getPricingRule(tenantId, ruleId);
        return ResponseEntity.ok(PricingRuleResponse.fromDTO(dto));
    }

    /**
     * Get all pricing rules with pagination
     * GET /api/v1/pricing/rules?page=0&size=20
     */
    @GetMapping("/rules")
    public ResponseEntity<List<PricingRuleResponse>> getPricingRules(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection) {

        log.info("GET /api/v1/pricing/rules - Get all pricing rules for tenant: {}", tenantId);

        PricingQuery query = PricingQuery.builder()
                .tenantId(tenantId)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Page<PricingRuleDTO> dtos = pricingApplicationService.queryPricingRules(query);

        List<PricingRuleResponse> response = dtos.getContent().stream()
                .map(PricingRuleResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(dtos.getTotalElements()))
                .header("X-Total-Pages", String.valueOf(dtos.getTotalPages()))
                .body(response);
    }

    /**
     * Get active rules for service type and vehicle type
     * GET /api/v1/pricing/rules/active?serviceType=STANDARD&vehicleType=CAR
     */
    @GetMapping("/rules/active")
    public ResponseEntity<List<PricingRuleResponse>> getActiveRules(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String serviceType,
            @RequestParam String vehicleType) {

        log.info("GET /api/v1/pricing/rules/active - Get active rules for tenant: {}", tenantId);

        List<PricingRuleDTO> dtos = pricingApplicationService.getActiveRules(
                tenantId, serviceType, vehicleType);

        List<PricingRuleResponse> response = dtos.stream()
                .map(PricingRuleResponse::fromDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Update pricing rule
     * PUT /api/v1/pricing/rules/{ruleId}
     */
    @PutMapping("/rules/{ruleId}")
    public ResponseEntity<PricingRuleResponse> updatePricingRule(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String ruleId,
            @Valid @RequestBody UpdatePricingRuleRequest request) {

        log.info("PUT /api/v1/pricing/rules/{} - Update pricing rule for tenant: {}", ruleId, tenantId);

        UpdatePricingRuleCommand command = dtoMapper.toCommand(request);
        PricingRuleDTO dto = pricingApplicationService.updatePricingRule(tenantId, ruleId, command);

        return ResponseEntity.ok(PricingRuleResponse.fromDTO(dto));
    }

    /**
     * Delete pricing rule
     * DELETE /api/v1/pricing/rules/{ruleId}
     */
    @DeleteMapping("/rules/{ruleId}")
    public ResponseEntity<Void> deletePricingRule(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String ruleId) {

        log.info("DELETE /api/v1/pricing/rules/{} - Delete pricing rule for tenant: {}", ruleId, tenantId);

        pricingApplicationService.deletePricingRule(tenantId, ruleId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calculate price for delivery
     * POST /api/v1/pricing/calculate
     */
    @PostMapping("/calculate")
    public ResponseEntity<PriceQuoteResponse> calculatePrice(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Valid @RequestBody CalculatePriceRequest request) {

        log.info("POST /api/v1/pricing/calculate - Calculate price for tenant: {}", tenantId);

        CalculatePriceCommand command = CalculatePriceCommand.builder()
                .tenantId(tenantId)
                .serviceType(request.getServiceType())
                .vehicleType(request.getVehicleType())
                .pickupLocation(CalculatePriceCommand.LocationCommand.builder()
                        .latitude(request.getPickupLocation() != null ? request.getPickupLocation().getLatitude() : null)
                        .longitude(request.getPickupLocation() != null ? request.getPickupLocation().getLongitude() : null)
                        .address(request.getPickupLocation() != null ? request.getPickupLocation().getAddress() : null)
                        .build())
                .deliveryLocation(CalculatePriceCommand.LocationCommand.builder()
                        .latitude(request.getDeliveryLocation() != null ? request.getDeliveryLocation().getLatitude() : null)
                        .longitude(request.getDeliveryLocation() != null ? request.getDeliveryLocation().getLongitude() : null)
                        .address(request.getDeliveryLocation() != null ? request.getDeliveryLocation().getAddress() : null)
                        .build())
                .distanceKm(request.getDistanceKm())
                .estimatedDurationMinutes(request.getEstimatedDurationMinutes())
                .packageWeightKg(request.getPackageWeightKg())
                .packageCategory(request.getPackageCategory())
                .urgentDelivery(request.getUrgentDelivery())
                .scheduledTime(request.getScheduledTime())
                .promoCode(request.getPromoCode())
                .metadata(request.getMetadata())
                .build();

        PriceCalculationDTO dto = pricingApplicationService.calculatePrice(command);
        return ResponseEntity.ok(PriceQuoteResponse.fromDTO(dto));
    }

    /**
     * Request DTOs
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CreatePricingRuleRequest {
        private String ruleName;
        private String description;
        private String ruleType;
        private Integer priority;
        private Boolean active;
        private String vehicleType;
        private String serviceType;
        private java.util.Map<String, Object> parameters;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UpdatePricingRuleRequest {
        private String ruleName;
        private String description;
        private String ruleType;
        private Integer priority;
        private Boolean active;
        private String vehicleType;
        private String serviceType;
        private java.util.Map<String, Object> parameters;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CalculatePriceRequest {
        private String serviceType;
        private String vehicleType;
        private LocationRequest pickupLocation;
        private LocationRequest deliveryLocation;
        private Double distanceKm;
        private Integer estimatedDurationMinutes;
        private Double packageWeightKg;
        private String packageCategory;
        private Boolean urgentDelivery;
        private String scheduledTime;
        private String promoCode;
        private java.util.Map<String, String> metadata;
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class LocationRequest {
        private Double latitude;
        private Double longitude;
        private String address;
    }

    /**
     * Response DTOs
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class PricingRuleResponse {
        private String id;
        private String tenantId;
        private String ruleId;
        private String ruleName;
        private String description;
        private String ruleType;
        private Integer priority;
        private Boolean active;
        private String vehicleType;
        private String serviceType;
        private java.util.Map<String, Object> parameters;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;

        public static PricingRuleResponse fromDTO(PricingRuleDTO dto) {
            return PricingRuleResponse.builder()
                    .id(dto.getId())
                    .tenantId(dto.getTenantId())
                    .ruleId(dto.getRuleId())
                    .ruleName(dto.getRuleName())
                    .description(dto.getDescription())
                    .ruleType(dto.getRuleType())
                    .priority(dto.getPriority())
                    .active(dto.getActive())
                    .vehicleType(dto.getVehicleType())
                    .serviceType(dto.getServiceType())
                    .parameters(dto.getParameters())
                    .createdAt(dto.getCreatedAt())
                    .updatedAt(dto.getUpdatedAt())
                    .build();
        }
    }

    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class PriceQuoteResponse {
        private String quoteId;
        private String tenantId;
        private String baseFare;
        private String distanceCharge;
        private String timeCharge;
        private String weightCharge;
        private String surcharge;
        private String discount;
        private String tax;
        private String subtotal;
        private String totalAmount;
        private String currency;
        private Integer validityMinutes;
        private java.time.LocalDateTime calculatedAt;
        private java.time.LocalDateTime expiresAt;
        private java.util.Map<String, Object> breakdown;

        public static PriceQuoteResponse fromDTO(PriceCalculationDTO dto) {
            return PriceQuoteResponse.builder()
                    .quoteId(dto.getQuoteId())
                    .tenantId(dto.getTenantId())
                    .baseFare(dto.getBaseFare().toString())
                    .distanceCharge(dto.getDistanceCharge().toString())
                    .timeCharge(dto.getTimeCharge().toString())
                    .weightCharge(dto.getWeightCharge().toString())
                    .surcharge(dto.getSurcharge().toString())
                    .discount(dto.getDiscount().toString())
                    .tax(dto.getTax().toString())
                    .subtotal(dto.getSubtotal().toString())
                    .totalAmount(dto.getTotalAmount().toString())
                    .currency(dto.getCurrency())
                    .validityMinutes(dto.getValidityMinutes())
                    .calculatedAt(dto.getCalculatedAt())
                    .expiresAt(dto.getExpiresAt())
                    .breakdown(dto.getBreakdown())
                    .build();
        }
    }
}
