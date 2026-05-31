package com.gogidix.courier.dynamicpricingservice.interfaces.rest;

import com.gogidix.courier.dynamicpricingservice.application.dto.*;
import com.gogidix.courier.dynamicpricingservice.application.service.DynamicPricingApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
@Tag(name = "Dynamic Pricing", description = "APIs for dynamic pricing and surge management")
public class DynamicPricingController {

    private final DynamicPricingApplicationService service;

    public DynamicPricingController(DynamicPricingApplicationService service) {
        this.service = service;
    }

    @GetMapping("/surge/{zoneId}")
    @Operation(summary = "Get surge status", description = "Get current surge multiplier for a zone")
    public ResponseEntity<SurgeStatusResponse> getSurgeStatus(
            @Parameter(description = "Zone ID", required = true)
            @PathVariable @NotBlank String zoneId) {
        return ResponseEntity.ok(service.getSurgeStatus(zoneId));
    }

    @PostMapping("/surge/calculate")
    @Operation(summary = "Calculate surge price", description = "Calculate price with surge applied")
    public ResponseEntity<SurgePriceResponse> calculateSurgePrice(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,
            @Valid @RequestBody CalculateSurgeRequest request) {
        return ResponseEntity.ok(service.calculateSurgePrice(tenantId, request));
    }

    @PostMapping("/demand/update")
    @Operation(summary = "Update demand level", description = "Update demand level for a zone")
    public ResponseEntity<Void> updateDemandLevel(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,
            @Valid @RequestBody UpdateDemandRequest request) {
        service.updateDemandLevel(tenantId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }
}
