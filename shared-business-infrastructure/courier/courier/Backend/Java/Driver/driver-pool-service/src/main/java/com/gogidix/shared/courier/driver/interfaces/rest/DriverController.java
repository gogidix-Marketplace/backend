package com.gogidix.shared.courier.driver.interfaces.rest;

import com.gogidix.shared.courier.driver.application.service.DriverPoolService;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.CreateDriverRequest;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.DriverResponse;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.UpdateDriverLocationRequest;
import com.gogidix.shared.courier.driver.interfaces.rest.dto.UpdateDriverStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver Pool", description = "Driver pool management API for managing driver profiles, locations, and availability")
@SecurityRequirement(name = "tenant-header")
public class DriverController {

    private static final Logger log = LoggerFactory.getLogger(DriverController.class);
    private final DriverPoolService driverPoolService;

    @PostMapping
    @Operation(
        summary = "Create a new driver",
        description = "Creates a new driver profile for the specified tenant. The driver ID must be unique within the tenant."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Driver created successfully",
            headers = @Header(name = "X-Tenant-ID", description = "Tenant identifier", required = true),
            content = @Content(schema = @Schema(implementation = DriverResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Driver with given ID already exists for this tenant"
        )
    })
    public ResponseEntity<DriverResponse> createDriver(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Valid @RequestBody CreateDriverRequest request) {
        log.info("POST /api/v1/drivers - Create driver for tenant: {}", tenantId);
        DriverResponse response = driverPoolService.createDriver(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{driverId}")
    @Operation(
        summary = "Get driver by ID",
        description = "Retrieves a specific driver's profile by their driver ID within the tenant context."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Driver found",
            content = @Content(schema = @Schema(implementation = DriverResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Driver not found"
        )
    })
    public ResponseEntity<DriverResponse> getDriver(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Driver identifier", required = true, example = "driver-001")
            @PathVariable String driverId) {
        log.info("GET /api/v1/drivers/{} - Get driver for tenant: {}", driverId, tenantId);
        DriverResponse response = driverPoolService.getDriver(tenantId, driverId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{driverId}/location")
    @Operation(
        summary = "Update driver location",
        description = "Updates the current GPS location of a driver. This location is used for nearby driver searches."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Location updated successfully",
            content = @Content(schema = @Schema(implementation = DriverResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid coordinates"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Driver not found"
        )
    })
    public ResponseEntity<DriverResponse> updateLocation(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Driver identifier", required = true, example = "driver-001")
            @PathVariable String driverId,
            @Valid @RequestBody UpdateDriverLocationRequest request) {
        log.info("PUT /api/v1/drivers/{}/location - Update location for tenant: {}", driverId, tenantId);
        DriverResponse response = driverPoolService.updateDriverLocation(tenantId, driverId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{driverId}/status")
    @Operation(
        summary = "Update driver status",
        description = "Updates the availability status of a driver. Valid statuses: ONLINE, OFFLINE, BUSY, ON_BREAK"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status updated successfully",
            content = @Content(schema = @Schema(implementation = DriverResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid status value"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Driver not found"
        )
    })
    public ResponseEntity<DriverResponse> updateStatus(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Driver identifier", required = true, example = "driver-001")
            @PathVariable String driverId,
            @Valid @RequestBody UpdateDriverStatusRequest request) {
        log.info("PUT /api/v1/drivers/{}/status - Update status for tenant: {}", driverId, tenantId);
        DriverResponse response = driverPoolService.updateDriverStatus(tenantId, driverId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    @Operation(
        summary = "Find nearby drivers",
        description = "Finds all available drivers within a specified radius from a given location using geospatial indexing."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of nearby drivers",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DriverResponse.class)))
        )
    })
    public ResponseEntity<List<DriverResponse>> findNearby(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Latitude coordinate", required = true, example = "40.7484")
            @RequestParam double latitude,
            @Parameter(description = "Longitude coordinate", required = true, example = "-73.9857")
            @RequestParam double longitude,
            @Parameter(description = "Search radius in kilometers", example = "5.0")
            @RequestParam(defaultValue = "5.0") double radiusKm) {
        log.info("GET /api/v1/drivers/nearby - Find nearby drivers for tenant: {}", tenantId);
        List<DriverResponse> response = driverPoolService.findNearbyDrivers(
                tenantId, latitude, longitude, radiusKm);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(
        summary = "Find drivers by status",
        description = "Retrieves all drivers with a specific status within the tenant context."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of drivers with the specified status",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DriverResponse.class)))
        )
    })
    public ResponseEntity<List<DriverResponse>> findByStatus(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Driver status to filter by", required = true, example = "ONLINE",
                       schema = @Schema(allowableValues = {"ONLINE", "OFFLINE", "BUSY", "ON_BREAK"}))
            @PathVariable String status) {
        log.info("GET /api/v1/drivers/status/{} - Find by status for tenant: {}", status, tenantId);
        List<DriverResponse> response = driverPoolService.findDriversByStatus(tenantId, status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{driverId}/rating")
    @Operation(
        summary = "Update driver rating",
        description = "Updates a driver's aggregate rating after a delivery completion."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Rating updated successfully"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid rating value (must be between 0 and 5)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Driver not found"
        )
    })
    public ResponseEntity<Void> updateRating(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Driver identifier", required = true, example = "driver-001")
            @PathVariable String driverId,
            @Parameter(description = "Rating value (0-5)", required = true, example = "4.5")
            @RequestParam Double rating) {
        log.info("POST /api/v1/drivers/{}/rating - Update rating for tenant: {}", driverId, tenantId);
        driverPoolService.updateDriverRating(tenantId, driverId, rating);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{driverId}")
    @Operation(
        summary = "Delete a driver",
        description = "Soft deletes or permanently removes a driver from the system."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Driver deleted successfully"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Driver not found"
        )
    })
    public ResponseEntity<Void> deleteDriver(
            @Parameter(description = "Tenant identifier", required = true, example = "tenant-001")
            @RequestHeader("X-Tenant-ID") String tenantId,
            @Parameter(description = "Driver identifier", required = true, example = "driver-001")
            @PathVariable String driverId) {
        log.info("DELETE /api/v1/drivers/{} - Delete driver for tenant: {}", driverId, tenantId);
        driverPoolService.deleteDriver(tenantId, driverId);
        return ResponseEntity.noContent().build();
    }
}
