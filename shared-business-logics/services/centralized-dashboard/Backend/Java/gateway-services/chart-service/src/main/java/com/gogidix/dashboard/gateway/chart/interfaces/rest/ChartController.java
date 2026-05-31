package com.gogidix.dashboard.gateway.chart.interfaces.rest;

import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartConfigurationResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartDataResponseDto;
import com.gogidix.dashboard.gateway.chart.application.dto.response.ChartSummaryResponseDto;
import com.gogidix.dashboard.gateway.chart.application.service.ChartDataService;
import com.gogidix.dashboard.gateway.chart.domain.model.ChartConfiguration;
import com.gogidix.dashboard.gateway.chart.domain.model.TimeSeriesData;
import com.gogidix.dashboard.gateway.chart.infrastructure.security.TenantContext;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST controller for Chart operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/charts")
@RequiredArgsConstructor
@Tag(name = "Chart Service", description = "APIs for chart data and configurations")
public class ChartController {

    private final ChartDataService chartDataService;

    /**
     * Get chart data by chart ID
     */
    @GetMapping(value = "/{chartId}/data", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get chart data",
        description = "Retrieves time-series data for a specific chart"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Chart data retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Chart not found")
    })
    public ResponseEntity<ChartDataResponseDto> getChartData(
        @Parameter(description = "Chart ID") @PathVariable String chartId,
        @Parameter(description = "Start time") @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @Parameter(description = "End time") @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
        @Parameter(description = "Tenant ID") @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/charts/{}/data - tenantId: {}", chartId, tenantId);
        TenantContext.setTenantId(tenantId);

        ChartDataResponseDto data = chartDataService.getChartData(chartId, tenantId, startTime, endTime);
        return ResponseEntity.ok(data);
    }

    /**
     * Get all chart configurations
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get all chart configurations",
        description = "Retrieves all chart configurations for the current tenant"
    )
    public ResponseEntity<List<ChartConfigurationResponseDto>> getAllCharts(
        @Parameter(description = "Tenant ID") @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/charts - tenantId: {}", tenantId);
        TenantContext.setTenantId(tenantId);

        List<ChartConfigurationResponseDto> charts = chartDataService.getAllCharts(tenantId);
        return ResponseEntity.ok(charts);
    }

    /**
     * Get chart summary
     */
    @GetMapping(value = "/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get chart summary",
        description = "Returns a summary of all charts and data points"
    )
    public ResponseEntity<ChartSummaryResponseDto> getChartSummary(
        @Parameter(description = "Tenant ID") @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/charts/summary - tenantId: {}", tenantId);
        TenantContext.setTenantId(tenantId);

        ChartSummaryResponseDto summary = chartDataService.getChartSummary(tenantId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Create chart configuration
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Create chart configuration",
        description = "Creates a new chart configuration"
    )
    public ResponseEntity<ChartConfigurationResponseDto> createChart(
        @Parameter(description = "Chart configuration") @Valid @RequestBody ChartConfiguration chartConfiguration
    ) {
        log.info("POST /api/v1/charts - chartId: {}", chartConfiguration.getChartId());

        ChartConfigurationResponseDto created = chartDataService.createChart(chartConfiguration);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * Add time-series data point
     */
    @PostMapping(value = "/data", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Add time-series data point",
        description = "Adds a new data point to the time-series store"
    )
    public ResponseEntity<TimeSeriesData> addDataPoint(
        @Parameter(description = "Data point") @RequestBody TimeSeriesData dataPoint
    ) {
        log.info("POST /api/v1/charts/data - metric: {}", dataPoint.getMetricName());

        TimeSeriesData saved = chartDataService.addTimeSeriesData(dataPoint);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * Get aggregate metrics
     */
    @GetMapping(value = "/metrics", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Get aggregate metrics",
        description = "Returns aggregated metrics for dashboard display"
    )
    public ResponseEntity<Map<String, Object>> getAggregateMetrics(
        @Parameter(description = "Tenant ID") @RequestHeader(value = "X-Tenant-ID", defaultValue = "default") String tenantId
    ) {
        log.info("GET /api/v1/charts/metrics - tenantId: {}", tenantId);
        TenantContext.setTenantId(tenantId);

        Map<String, Object> metrics = chartDataService.getAggregateMetrics(tenantId);
        return ResponseEntity.ok(metrics);
    }
}
