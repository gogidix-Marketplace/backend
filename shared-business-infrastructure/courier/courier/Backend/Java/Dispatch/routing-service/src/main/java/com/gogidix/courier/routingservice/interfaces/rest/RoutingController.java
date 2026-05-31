package com.gogidix.courier.routingservice.interfaces.rest;

import com.gogidix.courier.routingservice.application.dto.*;
import com.gogidix.courier.routingservice.application.service.RoutingApplicationService;
import com.gogidix.courier.routingservice.domain.entity.Route;
import com.gogidix.courier.routingservice.shared.context.RequestContext;
import com.gogidix.courier.routingservice.shared.context.RequestContextHolder;
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

import java.net.URI;
import java.time.Instant;

/**
 * REST controller for routing operations.
 * Provides endpoints for route management and optimization.
 */
@RestController
@RequestMapping("/routes")
@Tag(name = "Routing", description = "APIs for managing delivery routes and optimization")
public class RoutingController {

    private final RoutingApplicationService service;

    public RoutingController(RoutingApplicationService service) {
        this.service = service;
    }

    @PostMapping("/optimize")
    @Operation(summary = "Optimize a route", description = "Optimizes an existing route using specified algorithm")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Route optimized successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = OptimizationResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request or route cannot be optimized"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<OptimizationResponse> optimizeRoute(
            @Parameter(description = "Route ID to optimize", required = true)
            @PathVariable @NotBlank String routeId,

            @Valid @RequestBody OptimizationRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        OptimizationResponse response = service.optimizeRoute(
                routeId,
                request,
                context.tenantId(),
                context.userId()
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a route by ID", description = "Retrieves a route by its internal ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<RouteResponse> getRoute(
            @Parameter(description = "Route internal ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();
        RouteResponse response = service.getRouteById(id);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping
    @Operation(summary = "Create a new route", description = "Creates a new route with waypoints")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Route created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = RouteResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Route already exists")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RouteResponse> createRoute(
            @Valid @RequestBody RouteRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RouteResponse response = service.createRoute(request, context.tenantId(), context.userId());

        return ResponseEntity
                .created(URI.create("/routes/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a route", description = "Updates an existing route")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<RouteResponse> updateRoute(
            @Parameter(description = "Route ID", required = true)
            @PathVariable @NotBlank String id,

            @Valid @RequestBody RouteRequest request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RouteResponse response = service.updateRoute(id, request, context.userId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List routes", description = "Retrieves a paginated list of routes with filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routes retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<RouteResponse>> listRoutes(
            @Parameter(description = "Filter by driver ID")
            @RequestParam(required = false) String driverId,

            @Parameter(description = "Filter by status")
            @RequestParam(required = false) Route.RouteStatus status,

            @Parameter(description = "Filter by priority")
            @RequestParam(required = false) Route.RoutePriority priority,

            @Parameter(description = "Filter by vehicle type")
            @RequestParam(required = false) Route.VehicleType vehicleType,

            @Parameter(description = "Filter by start date from (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDateFrom,

            @Parameter(description = "Filter by start date to (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDateTo,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size,

            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC")
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection
    ) {
        RequestContext context = RequestContextHolder.getContext();

        PagedResponseDto<RouteResponse> response = service.listRoutes(
                context.tenantId(),
                driverId,
                status,
                priority,
                vehicleType,
                startDateFrom,
                startDateTo,
                page,
                size,
                sortBy,
                sortDirection
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/active/{driverId}")
    @Operation(summary = "Get active routes for driver", description = "Retrieves all active routes for a specific driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active routes retrieved successfully")
    })
    public ResponseEntity<java.util.List<RouteResponse>> getActiveRoutesForDriver(
            @Parameter(description = "Driver ID", required = true)
            @PathVariable @NotBlank String driverId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        java.util.List<RouteResponse> response = service.getActiveRoutesForDriver(
                context.tenantId(),
                driverId
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete a route", description = "Marks a route as completed with actual metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route completed successfully"),
            @ApiResponse(responseCode = "400", description = "Route cannot be completed"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<RouteResponse> completeRoute(
            @Parameter(description = "Route ID", required = true)
            @PathVariable @NotBlank String id,

            @RequestParam(required = false) Double actualDistanceMeters,

            @RequestParam(required = false) Integer actualDurationSeconds
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RouteResponse response = service.completeRoute(
                id,
                actualDistanceMeters,
                actualDurationSeconds,
                context.userId()
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel a route", description = "Cancels a route with a reason")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Route cannot be cancelled"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<RouteResponse> cancelRoute(
            @Parameter(description = "Route ID", required = true)
            @PathVariable @NotBlank String id,

            @Parameter(description = "Cancellation reason")
            @RequestParam(required = false) String reason
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RouteResponse response = service.cancelRoute(id, reason, context.userId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a route", description = "Deletes a route by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Route deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRoute(
            @Parameter(description = "Route ID", required = true)
            @PathVariable @NotBlank String id
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteRoute(id);

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Get route statistics", description = "Retrieves aggregated statistics for routes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<RouteStatsResponse> getRouteStatistics(
            @Parameter(description = "From date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,

            @Parameter(description = "To date (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RouteStatsResponse response = service.getRouteStatistics(
                context.tenantId(),
                fromDate,
                toDate
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/route-id/{routeId}")
    @Operation(summary = "Get a route by route ID", description = "Retrieves a route by its business route ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Route retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Route not found")
    })
    public ResponseEntity<RouteResponse> getRouteByRouteId(
            @Parameter(description = "Business route ID", required = true)
            @PathVariable @NotBlank String routeId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        RouteResponse response = service.getRouteByRouteId(context.tenantId(), routeId);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}
