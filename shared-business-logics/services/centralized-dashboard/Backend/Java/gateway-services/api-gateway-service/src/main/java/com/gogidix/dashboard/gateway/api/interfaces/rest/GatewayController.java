package com.gogidix.dashboard.gateway.api.interfaces.rest;

import com.gogidix.dashboard.gateway.api.application.dto.response.AggregatedDashboardDataDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.AggregateResponseDto;
import com.gogidix.dashboard.gateway.api.application.dto.response.ServiceHealthDto;
import com.gogidix.dashboard.gateway.api.application.service.GatewayService;
import com.gogidix.dashboard.gateway.api.domain.model.ServiceRegistry;
import com.gogidix.dashboard.gateway.api.infrastructure.security.TenantContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Gateway operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/gateway")
@RequiredArgsConstructor
@Tag(name = "API Gateway", description = "BFF gateway APIs for centralized dashboard")
public class GatewayController {

    private final GatewayService gatewayService;

    /**
     * Get aggregated dashboard data
     */
    @GetMapping(value = "/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get aggregated dashboard data",
        description = "Aggregates data from all downstream services for the dashboard"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard data retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AggregatedDashboardDataDto> getDashboardData(
        @Parameter(description = "Tenant ID") @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/gateway/dashboard - tenantId: {}", tenantId);
        TenantContext.setTenantId(tenantId);

        AggregatedDashboardDataDto data = gatewayService.getAggregatedDashboardData(tenantId);
        return ResponseEntity.ok(data);
    }

    /**
     * Proxy request to downstream service
     */
    @PostMapping(value = "/proxy/{service}/**", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Proxy request to downstream service",
        description = "Proxies requests to registered downstream services"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Request proxied successfully"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    public ResponseEntity<AggregateResponseDto> proxyRequest(
        @Parameter(description = "Service name") @PathVariable String service,
        @Parameter(description = "Request body") @RequestBody(required = false) Object body,
        @Parameter(description = "HTTP method") @RequestParam(defaultValue = "GET") String method
    ) {
        log.info("POST /api/v1/gateway/proxy/{} - method: {}", service, method);

        String path = "";
        AggregateResponseDto response = gatewayService.proxyRequest(service, path, method, body);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Get service health status
     */
    @GetMapping(value = "/health/services", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get health status of all services",
        description = "Returns the health status of all registered downstream services"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Health status retrieved successfully")
    })
    public ResponseEntity<Map<String, ServiceHealthDto>> getServicesHealth() {
        log.info("GET /api/v1/gateway/health/services");

        Map<String, ServiceHealthDto> health = gatewayService.checkAllServicesHealth();
        return ResponseEntity.ok(health);
    }

    /**
     * Register a new downstream service
     */
    @PostMapping(value = "/services/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Register a new downstream service",
        description = "Registers a new service in the gateway's service registry"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Service registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<ServiceRegistry> registerService(
        @Parameter(description = "Service registration details") @Valid @RequestBody ServiceRegistry serviceRegistry
    ) {
        log.info("POST /api/v1/gateway/services/register - serviceName: {}", serviceRegistry.getServiceName());

        ServiceRegistry registered = gatewayService.registerService(serviceRegistry);
        return ResponseEntity.status(201).body(registered);
    }

    /**
     * Get all registered services
     */
    @GetMapping(value = "/services", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get all registered services",
        description = "Returns a list of all registered services for the current tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Services retrieved successfully")
    })
    public ResponseEntity<List<ServiceRegistry>> getAllServices() {
        log.info("GET /api/v1/gateway/services");

        List<ServiceRegistry> services = gatewayService.getAllServices();
        return ResponseEntity.ok(services);
    }

    /**
     * Get saga statistics (proxy to saga coordinator)
     */
    @GetMapping(value = "/sagas/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get saga statistics",
        description = "Returns aggregate statistics about saga instances"
    )
    public ResponseEntity<Map<String, Object>> getSagaStatistics() {
        log.info("GET /api/v1/gateway/sagas/statistics");

        Map<String, Object> stats = gatewayService.fetchSagaStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get chart data (proxy to chart service)
     */
    @GetMapping(value = "/charts/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get chart data summary",
        description = "Returns aggregated chart data for visualization"
    )
    public ResponseEntity<Map<String, Object>> getChartData() {
        log.info("GET /api/v1/gateway/charts/summary");

        Map<String, Object> data = gatewayService.fetchChartData();
        return ResponseEntity.ok(data);
    }
}
