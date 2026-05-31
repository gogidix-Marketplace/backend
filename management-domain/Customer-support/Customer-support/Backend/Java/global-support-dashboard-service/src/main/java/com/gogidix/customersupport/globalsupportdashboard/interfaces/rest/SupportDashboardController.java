package com.gogidix.customersupport.globalsupportdashboard.interfaces.rest;

import com.gogidix.customersupport.globalsupportdashboard.application.dto.AgentPerformanceDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.DashboardSummaryDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.RegionalMetricsDto;
import com.gogidix.customersupport.globalsupportdashboard.application.dto.SupportMetricsDto;
import com.gogidix.customersupport.globalsupportdashboard.application.service.SupportDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Global Support Dashboard
 *
 * Provides endpoints for accessing global support metrics,
 * regional breakdowns, and agent performance data.
 */
@RestController
@RequestMapping("/api/v1/support-dashboard")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Support Dashboard", description = "Global Support Dashboard API")
public class SupportDashboardController {

    private final SupportDashboardService dashboardService;

    /**
     * Get global support metrics
     *
     * GET /api/v1/support-dashboard/metrics
     */
    @GetMapping("/metrics")
    @Operation(summary = "Get global support metrics",
               description = "Retrieve aggregated global support metrics including ticket volumes, SLA compliance, and CSAT scores")
    public ResponseEntity<SupportMetricsDto> getGlobalMetrics(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/support-dashboard/metrics - tenant: {}", tenantId);
        SupportMetricsDto metrics = dashboardService.getGlobalMetrics(tenantId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get complete dashboard summary
     *
     * GET /api/v1/support-dashboard/summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary",
               description = "Retrieve complete dashboard summary with global overview, regional metrics, and top agents")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/support-dashboard/summary - tenant: {}", tenantId);
        DashboardSummaryDto summary = dashboardService.getDashboardSummary(tenantId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Get regional metrics
     *
     * GET /api/v1/support-dashboard/regions/{regionCode}
     */
    @GetMapping("/regions/{regionCode}")
    @Operation(summary = "Get regional metrics",
               description = "Retrieve support metrics for a specific region")
    public ResponseEntity<RegionalMetricsDto> getRegionalMetrics(
            @Parameter(description = "Region Code (NA, EU, APAC, LATAM, MEA)")
            @PathVariable @NotBlank String regionCode,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/support-dashboard/regions/{} - tenant: {}", regionCode, tenantId);
        RegionalMetricsDto metrics = dashboardService.getRegionalMetrics(tenantId, regionCode);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get all regional metrics
     *
     * GET /api/v1/support-dashboard/regions
     */
    @GetMapping("/regions")
    @Operation(summary = "Get all regional metrics",
               description = "Retrieve support metrics for all regions")
    public ResponseEntity<List<RegionalMetricsDto>> getAllRegionalMetrics(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/support-dashboard/regions - tenant: {}", tenantId);
        List<RegionalMetricsDto> metrics = dashboardService.getAllRegionalMetrics(tenantId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get agent performance
     *
     * GET /api/v1/support-dashboard/agents/{agentId}
     */
    @GetMapping("/agents/{agentId}")
    @Operation(summary = "Get agent performance",
               description = "Retrieve performance metrics for a specific agent")
    public ResponseEntity<AgentPerformanceDto> getAgentPerformance(
            @Parameter(description = "Agent ID")
            @PathVariable @NotBlank String agentId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/support-dashboard/agents/{} - tenant: {}", agentId, tenantId);
        AgentPerformanceDto performance = dashboardService.getAgentPerformance(tenantId, agentId);
        return ResponseEntity.ok(performance);
    }

    /**
     * Get top performing agents
     *
     * GET /api/v1/support-dashboard/agents/top
     */
    @GetMapping("/agents/top")
    @Operation(summary = "Get top agents",
               description = "Retrieve top performing agents ranked by tickets resolved")
    public ResponseEntity<List<AgentPerformanceDto>> getTopAgents(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Maximum number of agents to return")
            @RequestParam(defaultValue = "10") int limit) {

        log.info("GET /api/v1/support-dashboard/agents/top - tenant: {}, limit: {}", tenantId, limit);
        List<AgentPerformanceDto> agents = dashboardService.getTopAgents(tenantId, limit);
        return ResponseEntity.ok(agents);
    }

    /**
     * Get active agents
     *
     * GET /api/v1/support-dashboard/agents/active
     */
    @GetMapping("/agents/active")
    @Operation(summary = "Get active agents",
               description = "Retrieve all currently active agents")
    public ResponseEntity<List<AgentPerformanceDto>> getActiveAgents(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/support-dashboard/agents/active - tenant: {}", tenantId);
        List<AgentPerformanceDto> agents = dashboardService.getActiveAgents(tenantId);
        return ResponseEntity.ok(agents);
    }

    /**
     * Refresh metrics
     *
     * POST /api/v1/support-dashboard/refresh
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh metrics",
               description = "Trigger a refresh of all support metrics")
    public ResponseEntity<SupportMetricsDto> refreshMetrics(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("POST /api/v1/support-dashboard/refresh - tenant: {}", tenantId);
        SupportMetricsDto metrics = dashboardService.refreshMetrics(tenantId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Health check endpoint
     *
     * GET /api/v1/support-dashboard/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check",
               description = "Check if the service is healthy")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(
            new HealthResponse("UP", "Global Support Dashboard Service is running")
        );
    }

    /**
     * Exception handler for IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST", ex.getMessage()));
    }

    /**
     * Exception handler for general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }

    /**
     * Health response record
     */
    private record HealthResponse(String status, String message) {}

    /**
     * Error response record
     */
    private record ErrorResponse(String code, String message) {}
}
