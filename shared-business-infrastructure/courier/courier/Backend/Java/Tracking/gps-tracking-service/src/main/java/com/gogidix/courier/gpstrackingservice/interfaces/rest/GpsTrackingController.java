package com.gogidix.courier.gpstrackingservice.interfaces.rest;

import com.gogidix.courier.gpstrackingservice.application.command.StartTrackingCommand;
import com.gogidix.courier.gpstrackingservice.application.command.StopTrackingCommand;
import com.gogidix.courier.gpstrackingservice.application.command.SubmitGpsCommand;
import com.gogidix.courier.gpstrackingservice.application.dto.*;
import com.gogidix.courier.gpstrackingservice.application.mapper.GpsTrackingMapper;
import com.gogidix.courier.gpstrackingservice.application.service.GpsTrackingApplicationService;
import com.gogidix.courier.gpstrackingservice.shared.context.RequestContext;
import com.gogidix.courier.gpstrackingservice.shared.context.RequestContextHolder;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * REST controller for GPS tracking operations.
 */
@RestController
@RequestMapping("/tracking")
@Tag(name = "GPS Tracking", description = "APIs for real-time GPS tracking and monitoring")
public class GpsTrackingController {

    private final GpsTrackingApplicationService service;
    private final GpsTrackingMapper mapper;

    public GpsTrackingController(
            GpsTrackingApplicationService service,
            GpsTrackingMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    // ==================== GPS Location Endpoints ====================

    @PostMapping("/gps/update")
    @Operation(summary = "Submit GPS location update", description = "Submit a new GPS location update for a driver")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "GPS location saved successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = GpsLocationResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GpsLocationResponse> submitGpsLocation(
            @Valid @RequestBody GpsLocationRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        SubmitGpsCommand command = new SubmitGpsCommand(
                context.tenantId(),
                request.driverId(),
                request.orderId(),
                request.latitude(),
                request.longitude(),
                request.altitude(),
                request.accuracy(),
                request.speed(),
                request.heading(),
                request.batteryLevel(),
                request.locationSource(),
                context.userId()
        );

        GpsLocationResponse response = service.submitGpsLocation(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/gps/{driverId}")
    @Operation(summary = "Get current driver location", description = "Get the most recent GPS location for a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No location found for driver")
    })
    public ResponseEntity<GpsLocationResponse> getCurrentLocation(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId,

            @Parameter(description = "Tenant ID (optional, uses header if not provided)")
            @RequestParam(required = false) String tenantId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        String effectiveTenantId = tenantId != null ? tenantId : context.tenantId();

        GpsLocationResponse response = service.getCurrentLocation(effectiveTenantId, driverId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/gps/{driverId}/history")
    @Operation(summary = "Get driver location history", description = "Get GPS location history for a driver within a time range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "History retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<GpsLocationResponse>> getLocationHistory(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId,

            @Parameter(description = "Start time")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,

            @Parameter(description = "End time")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        RequestContext context = RequestContextHolder.getContext();

        PagedResponseDto<GpsLocationResponse> response = service.getLocationHistory(
                context.tenantId(), driverId, startTime, endTime, page, size
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/orders/{orderId}/locations")
    @Operation(summary = "Get order location history", description = "Get GPS location history for an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order locations retrieved successfully")
    })
    public ResponseEntity<List<GpsLocationResponse>> getOrderLocations(
            @Parameter(description = "Order ID", required = true)
            @PathVariable @NotBlank String orderId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        List<GpsLocationResponse> response = service.getOrderLocationHistory(
                context.tenantId(), orderId
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/nearby")
    @Operation(summary = "Find nearby drivers", description = "Find drivers near a specified location")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nearby drivers retrieved successfully")
    })
    public ResponseEntity<List<NearbyDriverResponse>> findNearbyDrivers(
            @Valid @RequestBody NearbyDriversRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        List<NearbyDriverResponse> response = service.findNearbyDrivers(
                context.tenantId(),
                request.latitude(),
                request.longitude(),
                request.radiusMeters(),
                request.maxResults()
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    // ==================== Tracking Session Endpoints ====================

    @PostMapping("/sessions/start")
    @Operation(summary = "Start tracking session", description = "Start a new tracking session for a driver")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tracking session started successfully",
                    content = @Content(schema = @Schema(implementation = TrackingSessionDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Driver already has an active session")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TrackingSessionDTO> startTracking(
            @Valid @RequestBody StartTrackingRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        StartTrackingCommand command = mapper.toCommand(request, context);
        TrackingSessionDTO response = service.startTracking(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/sessions/stop")
    @Operation(summary = "Stop tracking session", description = "Stop an active tracking session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tracking session stopped successfully"),
            @ApiResponse(responseCode = "400", description = "Session is not active"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<TrackingSessionDTO> stopTracking(
            @Valid @RequestBody StopTrackingRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        StopTrackingCommand command = new StopTrackingCommand(
                request.sessionId(),
                request.endReason(),
                context.userId()
        );
        TrackingSessionDTO response = service.stopTracking(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/sessions/active")
    @Operation(summary = "Get active tracking sessions", description = "Get all active tracking sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active sessions retrieved successfully")
    })
    public ResponseEntity<List<TrackingSessionDTO>> getActiveSessions(
            @Parameter(description = "Tenant ID (optional, uses header if not provided)")
            @RequestParam(required = false) String tenantId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        String effectiveTenantId = tenantId != null ? tenantId : context.tenantId();

        List<TrackingSessionDTO> response = service.getActiveSessions(effectiveTenantId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/sessions/{sessionId}")
    @Operation(summary = "Get tracking session", description = "Get details of a specific tracking session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public ResponseEntity<TrackingSessionDTO> getSession(
            @Parameter(description = "Session ID", required = true)
            @PathVariable @NotBlank String sessionId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        TrackingSessionDTO response = service.getSession(sessionId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/drivers/{driverId}/sessions")
    @Operation(summary = "Get driver tracking sessions", description = "Get all tracking sessions for a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver sessions retrieved successfully")
    })
    public ResponseEntity<List<TrackingSessionDTO>> getDriverSessions(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        List<TrackingSessionDTO> response = service.getDriverSessions(
                context.tenantId(), driverId
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    // ==================== Statistics Endpoint ====================

    @GetMapping("/drivers/{driverId}/stats")
    @Operation(summary = "Get driver tracking statistics", description = "Get tracking statistics for a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getDriverStats(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        Map<String, Object> response = service.getDriverStats(
                context.tenantId(), driverId
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}
