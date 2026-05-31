package com.gogidix.courier.discountservice.interfaces.rest;

import com.gogidix.courier.discountservice.application.dto.*;
import com.gogidix.courier.discountservice.application.service.DiscountApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/discounts")
@Tag(name = "Discount Management", description = "APIs for managing discount codes")
public class DiscountController {

    private final DiscountApplicationService service;

    public DiscountController(DiscountApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create discount code", description = "Creates a new discount code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount created successfully",
                    content = @Content(schema = @Schema(implementation = DiscountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DiscountResponse> createDiscount(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,
            @Valid @RequestBody CreateDiscountRequest request) {
        String correlationId = UUID.randomUUID().toString();
        DiscountResponse response = service.createDiscount(tenantId, request);
        return ResponseEntity
                .created(URI.create("/discounts/" + response.id()))
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get discount by ID", description = "Retrieves a discount code by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    public ResponseEntity<DiscountResponse> getDiscount(
            @Parameter(description = "Discount ID", required = true)
            @PathVariable @NotBlank String id) {
        String correlationId = UUID.randomUUID().toString();
        DiscountResponse response = service.getDiscount(id);
        return ResponseEntity.ok()
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List all discounts", description = "Retrieves all discount codes for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discounts retrieved successfully")
    })
    public ResponseEntity<List<DiscountResponse>> listDiscounts(
            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId,
            @Parameter(description = "Filter by status")
            @RequestParam(required = false) String status) {
        String correlationId = UUID.randomUUID().toString();
        List<DiscountResponse> response = "active".equalsIgnoreCase(status)
                ? service.listActiveDiscounts(tenantId)
                : service.listDiscounts(tenantId);
        return ResponseEntity.ok()
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate discount code", description = "Validates a discount code without applying it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Validation completed"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<ValidationResponse> validateCode(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,
            @Valid @RequestBody ValidateCodeRequest request) {
        String correlationId = UUID.randomUUID().toString();
        ValidationResponse response = service.validateCode(tenantId, request);
        return ResponseEntity.ok()
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @PostMapping("/apply")
    @Operation(summary = "Apply discount code", description = "Applies a discount code to an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount applied successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid discount or request")
    })
    public ResponseEntity<ApplyDiscountResponse> applyDiscount(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,
            @Valid @RequestBody ApplyDiscountRequest request) {
        String correlationId = UUID.randomUUID().toString();
        ApplyDiscountResponse response = service.applyDiscount(tenantId, request);
        return ResponseEntity.ok()
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete discount", description = "Deletes a discount code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Discount deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDiscount(
            @Parameter(description = "Discount ID", required = true)
            @PathVariable @NotBlank String id) {
        String correlationId = UUID.randomUUID().toString();
        service.deleteDiscount(id);
        return ResponseEntity.noContent()
                .header("X-Correlation-Id", correlationId)
                .build();
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate discount", description = "Activates a discount code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount activated successfully"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    public ResponseEntity<DiscountResponse> activateDiscount(
            @Parameter(description = "Discount ID", required = true)
            @PathVariable @NotBlank String id) {
        String correlationId = UUID.randomUUID().toString();
        DiscountResponse response = service.activateDiscount(id);
        return ResponseEntity.ok()
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate discount", description = "Deactivates a discount code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    public ResponseEntity<DiscountResponse> deactivateDiscount(
            @Parameter(description = "Discount ID", required = true)
            @PathVariable @NotBlank String id) {
        String correlationId = UUID.randomUUID().toString();
        DiscountResponse response = service.deactivateDiscount(id);
        return ResponseEntity.ok()
                .header("X-Correlation-Id", correlationId)
                .body(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check service health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UP");
    }
}
