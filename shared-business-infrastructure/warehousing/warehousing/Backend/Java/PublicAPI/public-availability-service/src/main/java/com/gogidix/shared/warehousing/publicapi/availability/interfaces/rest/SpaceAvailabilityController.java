package com.gogidix.shared.warehousing.publicapi.availability.interfaces.rest;

import com.gogidix.shared.warehousing.publicapi.availability.application.service.SpaceAvailabilityService;
import com.gogidix.shared.warehousing.publicapi.availability.domain.entity.SpaceAvailability;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST Controller for Public Space Availability
 */
@RestController
@RequestMapping("/api/v1/public/availability")
@RequiredArgsConstructor
@Tag(name = "Space Availability", description = "Public APIs for checking warehouse space availability")
public class SpaceAvailabilityController {

    private final SpaceAvailabilityService availabilityService;

    @PostMapping
    @Operation(summary = "Create space availability", description = "Create a new space availability entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Space availability created successfully",
                    content = @Content(schema = @Schema(implementation = SpaceAvailability.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SpaceAvailability> createAvailability(
            @Parameter(description = "Space availability details", required = true)
            @Valid @RequestBody SpaceAvailability availability) {
        SpaceAvailability created = availabilityService.createAvailability(availability);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get available spaces", description = "Retrieve all available spaces for a tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available spaces retrieved successfully")
    })
    public ResponseEntity<List<SpaceAvailability>> getAvailableSpaces(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "Date to check availability for", example = "2024-01-15T10:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        List<SpaceAvailability> availabilities = availabilityService.getAvailableSpaces(tenantId, date);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/climate-controlled")
    @Operation(summary = "Get climate controlled spaces", description = "Retrieve all climate controlled available spaces")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Climate controlled spaces retrieved successfully")
    })
    public ResponseEntity<List<SpaceAvailability>> getClimateControlledSpaces(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        List<SpaceAvailability> availabilities = availabilityService.getClimateControlledSpaces(tenantId);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/by-price")
    @Operation(summary = "Get spaces by max price", description = "Retrieve available spaces within a price range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaces retrieved successfully")
    })
    public ResponseEntity<List<SpaceAvailability>> getSpacesByMaxPrice(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "Maximum price", required = true, example = "1000.00")
            @RequestParam Double maxPrice) {
        List<SpaceAvailability> availabilities = availabilityService.getSpacesByMaxPrice(tenantId, maxPrice);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/by-city")
    @Operation(summary = "Get spaces by city", description = "Retrieve available spaces in a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaces retrieved successfully")
    })
    public ResponseEntity<List<SpaceAvailability>> getSpacesByCity(
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId,
            @Parameter(description = "City name", required = true, example = "New York")
            @RequestParam String city) {
        List<SpaceAvailability> availabilities = availabilityService.getSpacesByCity(tenantId, city);
        return ResponseEntity.ok(availabilities);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Get spaces by warehouse", description = "Retrieve available spaces in a specific warehouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaces retrieved successfully")
    })
    public ResponseEntity<List<SpaceAvailability>> getSpacesByWarehouse(
            @Parameter(description = "Warehouse ID", required = true, example = "warehouse-456")
            @PathVariable String warehouseId,
            @Parameter(description = "Tenant ID", required = true, example = "tenant-123")
            @RequestParam String tenantId) {
        List<SpaceAvailability> availabilities = availabilityService.getAvailableByWarehouse(tenantId, warehouseId);
        return ResponseEntity.ok(availabilities);
    }

    @PatchMapping("/{availabilityId}/status")
    @Operation(summary = "Update availability status", description = "Update the status of a space availability entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Availability entry not found")
    })
    public ResponseEntity<SpaceAvailability> updateStatus(
            @Parameter(description = "Availability ID", required = true, example = "507f1f77bcf86cd799439011")
            @PathVariable String availabilityId,
            @Parameter(description = "New status", required = true, example = "AVAILABLE")
            @RequestParam String status) {
        SpaceAvailability availability = availabilityService.updateAvailability(
                availabilityId, SpaceAvailability.AvailabilityStatus.valueOf(status));
        return ResponseEntity.ok(availability);
    }
}
