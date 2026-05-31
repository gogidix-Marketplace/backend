package com.gogidix.courier.availabilityservice.interfaces.rest;

import com.gogidix.courier.availabilityservice.application.dto.*;
import com.gogidix.courier.availabilityservice.application.query.FindAvailableDriversQuery;
import com.gogidix.courier.availabilityservice.application.service.AvailabilityApplicationService;
import com.gogidix.courier.availabilityservice.shared.context.RequestContext;
import com.gogidix.courier.availabilityservice.shared.context.RequestContextHolder;
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
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for driver availability operations.
 */
@RestController
@RequestMapping("/availability")
@Tag(name = "Driver Availability", description = "APIs for managing driver availability")
public class AvailabilityController {

    private final AvailabilityApplicationService service;

    public AvailabilityController(AvailabilityApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create driver availability", description = "Creates availability for a driver on a specific date")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Availability created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = DriverAvailabilityResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Availability already exists")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DriverAvailabilityResponse> createAvailability(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,

            @Valid @RequestBody DriverAvailabilityRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        DriverAvailabilityResponse response = service.createAvailability(tenantId, request);

        return ResponseEntity
                .created(URI.create("/availability/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get availability by ID", description = "Retrieves driver availability by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    public ResponseEntity<DriverAvailabilityResponse> getAvailability(
            @Parameter(description = "Availability ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();
        DriverAvailabilityResponse response = service.getAvailability(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/driver/{driverId}/date/{date}")
    @Operation(summary = "Get availability by driver and date", description = "Retrieves driver availability for a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    public ResponseEntity<DriverAvailabilityResponse> getAvailabilityByDriverAndDate(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId,

            @Parameter(description = "Date (YYYY-MM-DD)", required = true)
            @PathVariable LocalDate date
    ) {
        RequestContext context = RequestContextHolder.getContext();
        DriverAvailabilityResponse response = service.getAvailabilityByDriverAndDate(driverId, date);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/driver/{driverId}/range")
    @Operation(summary = "Get availability for date range", description = "Retrieves driver availability within a date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Availability retrieved successfully")
    })
    public ResponseEntity<List<DriverAvailabilityResponse>> getAvailabilityInRange(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId,

            @Parameter(description = "Start date (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate startDate,

            @Parameter(description = "End date (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate endDate
    ) {
        RequestContext context = RequestContextHolder.getContext();
        List<DriverAvailabilityResponse> response = service.getAvailabilityInRange(driverId, startDate, endDate);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/available")
    @Operation(summary = "Find available drivers", description = "Finds drivers available on a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available drivers retrieved successfully")
    })
    public ResponseEntity<List<AvailableDriverResponse>> findAvailableDrivers(
            @Parameter(description = "Tenant ID", required = true)
            @RequestParam @NotBlank String tenantId,

            @Parameter(description = "Date (YYYY-MM-DD)", required = true)
            @RequestParam LocalDate date,

            @Parameter(description = "Filter by zone ID")
            @RequestParam(required = false) String zoneId,

            @Parameter(description = "Include location information")
            @RequestParam(required = false, defaultValue = "true") Boolean includeLocation
    ) {
        RequestContext context = RequestContextHolder.getContext();

        FindAvailableDriversQuery query = new FindAvailableDriversQuery(
                tenantId, date, zoneId, null, null, includeLocation
        );
        List<AvailableDriverResponse> response = service.findAvailableDrivers(query);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete availability", description = "Deletes driver availability by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Availability deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Availability not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAvailability(
            @Parameter(description = "Availability ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.deleteAvailability(id);

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/slots")
    @Operation(summary = "Add availability slot", description = "Adds a new availability slot for a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Slot created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body or overlapping slots")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<SlotResponse> addSlot(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,

            @Valid @RequestBody SlotRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();
        SlotResponse response = service.addSlot(tenantId, request);

        return ResponseEntity
                .created(URI.create("/availability/slots/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/slots/{slotId}/book")
    @Operation(summary = "Book a slot", description = "Books an available slot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Slot booked successfully"),
            @ApiResponse(responseCode = "400", description = "Slot is not available")
    })
    public ResponseEntity<Void> bookSlot(
            @Parameter(description = "Slot ID", required = true)
            @PathVariable @NotBlank String slotId,

            @Parameter(description = "Booking ID", required = true)
            @RequestParam @NotBlank String bookingId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.bookSlot(slotId, bookingId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/slots/{slotId}/release")
    @Operation(summary = "Release a slot", description = "Releases a booked slot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Slot released successfully"),
            @ApiResponse(responseCode = "404", description = "Slot not found")
    })
    public ResponseEntity<Void> releaseSlot(
            @Parameter(description = "Slot ID", required = true)
            @PathVariable @NotBlank String slotId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.releaseSlot(slotId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/unavailable-periods")
    @Operation(summary = "Add unavailable period", description = "Adds an unavailable period for a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unavailable period created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> addUnavailablePeriod(
            @Parameter(description = "Tenant ID", required = true)
            @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId,

            @Valid @RequestBody UnavailablePeriodRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.addUnavailablePeriod(tenantId, request);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/drivers/{driverId}/location")
    @Operation(summary = "Update driver location", description = "Updates the current location of a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully")
    })
    public ResponseEntity<Void> updateLocation(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId,

            @Parameter(description = "Latitude", required = true)
            @RequestParam Double latitude,

            @Parameter(description = "Longitude", required = true)
            @RequestParam Double longitude
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.updateLocation(driverId, latitude, longitude);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }
}
