package com.gogidix.aiservices.aigatewayservice.interfaces.rest;

import com.gogidix.aiservices.aigatewayservice.application.dto.CreateRouteRequestDto;
import com.gogidix.aiservices.aigatewayservice.application.dto.GatewayRouteResponseDto;
import com.gogidix.aiservices.aigatewayservice.application.dto.UpdateRouteRequestDto;
import com.gogidix.aiservices.aigatewayservice.application.service.GatewayRouteApplicationService;
import com.gogidix.aiservices.aigatewayservice.domain.model.LoadBalancingStrategy;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteFilter;
import com.gogidix.aiservices.aigatewayservice.domain.model.RouteStatus;
import com.gogidix.aiservices.aigatewayservice.shared.requestcontext.RequestContext;
import com.gogidix.aiservices.aigatewayservice.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller for gateway route operations.
 */
@RestController
@RequestMapping("/api/v1/gateway")
@Tag(name = "Gateway Routes", description = "APIs for managing API gateway routes")
public class GatewayRouteController {

    private final GatewayRouteApplicationService service;

    public GatewayRouteController(GatewayRouteApplicationService service) {
        this.service = service;
    }

    @PostMapping("/routes")
    @Operation(summary = "Create a new route", description = "Creates a new API gateway route")
    @ApiResponse(responseCode = "201", description = "Route created successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GatewayRouteResponseDto> createRoute(
            @Valid @RequestBody CreateRouteRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        GatewayRouteResponseDto response = service.createRoute(
                context.tenantId(),
                request.path(),
                request.targetService(),
                request.targetUrls(),
                request.filters(),
                request.rateLimit(),
                request.loadBalancingStrategy(),
                request.requestTimeout()
        );

        return ResponseEntity
                .created(URI.create("/api/v1/gateway/routes/" + response.routeId()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/routes/{routeId}")
    @Operation(summary = "Get a route by ID", description = "Retrieves a gateway route by its ID")
    @ApiResponse(responseCode = "200", description = "Route retrieved successfully")
    public ResponseEntity<GatewayRouteResponseDto> getRoute(
            @PathVariable @NotBlank String routeId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        GatewayRouteResponseDto response = service.getRouteById(routeId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/routes")
    @Operation(summary = "List routes", description = "Retrieves a list of routes for the current tenant")
    @ApiResponse(responseCode = "200", description = "Routes retrieved successfully")
    public ResponseEntity<List<GatewayRouteResponseDto>> listRoutes(
            @RequestParam(required = false) RouteStatus status
    ) {
        RequestContext context = RequestContextHolder.getContext();

        List<GatewayRouteResponseDto> response = status != null
                ? service.getRoutesByTenantAndStatus(context.tenantId(), status)
                : service.getRoutesByTenant(context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/routes/{routeId}")
    @Operation(summary = "Update a route", description = "Updates an existing gateway route")
    @ApiResponse(responseCode = "200", description = "Route updated successfully")
    public ResponseEntity<GatewayRouteResponseDto> updateRoute(
            @PathVariable @NotBlank String routeId,
            @Valid @RequestBody UpdateRouteRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        GatewayRouteResponseDto response = service.updateRoute(
                routeId,
                context.tenantId(),
                request.path(),
                request.targetService(),
                request.targetUrls(),
                request.filters(),
                request.rateLimit(),
                request.loadBalancingStrategy(),
                request.requestTimeout()
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/routes/{routeId}")
    @Operation(summary = "Delete a route", description = "Deletes a gateway route by its ID")
    @ApiResponse(responseCode = "204", description = "Route deleted successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRoute(
            @PathVariable @NotBlank String routeId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.deleteRoute(routeId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/routes/{routeId}/activate")
    @Operation(summary = "Activate a route", description = "Activates a gateway route")
    @ApiResponse(responseCode = "204", description = "Route activated successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> activateRoute(
            @PathVariable @NotBlank String routeId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.activateRoute(routeId, context.tenantId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/routes/{routeId}/deactivate")
    @Operation(summary = "Deactivate a route", description = "Deactivates a gateway route")
    @ApiResponse(responseCode = "204", description = "Route deactivated successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deactivateRoute(
            @PathVariable @NotBlank String routeId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.deactivateRoute(routeId, context.tenantId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/routes/{routeId}/reset-circuit-breaker")
    @Operation(summary = "Reset circuit breaker", description = "Resets the circuit breaker for a route")
    @ApiResponse(responseCode = "204", description = "Circuit breaker reset successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> resetCircuitBreaker(
            @PathVariable @NotBlank String routeId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        service.resetCircuitBreaker(routeId, context.tenantId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Gateway service health check")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "AI Gateway Service is running"));
    }

    public record HealthResponse(
            String status,
            String message
    ) {}
}
