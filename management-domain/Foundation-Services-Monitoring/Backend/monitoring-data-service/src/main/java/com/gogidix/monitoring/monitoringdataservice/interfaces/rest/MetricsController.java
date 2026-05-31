package com.gogidix.monitoring.monitoringdataservice.interfaces.rest;

import com.gogidix.monitoring.monitoringdataservice.application.dto.CollectBatchMetricsRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.CollectMetricRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.MetricDataPointResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.MetricQueryResponseDto;
import com.gogidix.monitoring.monitoringdataservice.application.dto.QueryMetricsRequestDto;
import com.gogidix.monitoring.monitoringdataservice.application.service.MetricCollectionService;
import com.gogidix.monitoring.monitoringdataservice.domain.model.MetricAggregation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * REST controller for metrics collection and querying.
 */
@RestController
@RequestMapping("/metrics")
@Tag(name = "Metrics", description = "APIs for collecting and querying metrics")
public class MetricsController {

    private final MetricCollectionService metricCollectionService;

    public MetricsController(MetricCollectionService metricCollectionService) {
        this.metricCollectionService = metricCollectionService;
    }

    @PostMapping
    @Operation(summary = "Collect a single metric", description = "Collects a single metric data point")
    @ApiResponse(responseCode = "201", description = "Metric collected successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public MetricDataPointResponseDto collectMetric(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Valid @RequestBody CollectMetricRequestDto request
    ) {
        return metricCollectionService.collectMetric(tenantId, request);
    }

    @PostMapping("/batch")
    @Operation(summary = "Collect multiple metrics", description = "Collects multiple metric data points in batch")
    @ApiResponse(responseCode = "201", description = "Metrics collected successfully")
    @ResponseStatus(HttpStatus.CREATED)
    public List<MetricDataPointResponseDto> collectBatchMetrics(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Valid @RequestBody CollectBatchMetricsRequestDto request
    ) {
        return metricCollectionService.collectBatchMetrics(tenantId, request);
    }

    @PostMapping("/query")
    @Operation(summary = "Query metrics", description = "Query metrics with optional aggregation")
    @ApiResponse(responseCode = "200", description = "Query executed successfully")
    public MetricQueryResponseDto queryMetrics(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Valid @RequestBody QueryMetricsRequestDto request
    ) {
        return metricCollectionService.queryMetrics(tenantId, request);
    }

    @GetMapping("/services/{serviceName}")
    @Operation(summary = "Get metrics for a service", description = "Retrieves metrics for a specific service")
    @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully")
    public MetricQueryResponseDto getMetricsByService(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Service name", required = true)
            @PathVariable @NotBlank String serviceName,

            @Parameter(description = "Metric name (optional)")
            @RequestParam(required = false) String metricName,

            @Parameter(description = "Start time (epoch millis)", required = true)
            @RequestParam long startTime,

            @Parameter(description = "End time (epoch millis)", required = true)
            @RequestParam long endTime
    ) {
        QueryMetricsRequestDto request = QueryMetricsRequestDto.builder()
                .serviceName(serviceName)
                .metricName(metricName)
                .startTime(Instant.ofEpochMilli(startTime))
                .endTime(Instant.ofEpochMilli(endTime))
                .build();

        return metricCollectionService.queryMetrics(tenantId, request);
    }

    @GetMapping("/services/{serviceName}/aggregated")
    @Operation(summary = "Get aggregated metrics", description = "Retrieves aggregated metrics for a service")
    @ApiResponse(responseCode = "200", description = "Aggregated metrics retrieved successfully")
    public MetricQueryResponseDto getAggregatedMetrics(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId,

            @Parameter(description = "Service name", required = true)
            @PathVariable @NotBlank String serviceName,

            @Parameter(description = "Metric name", required = true)
            @RequestParam @NotBlank String metricName,

            @Parameter(description = "Aggregation window", required = true)
            @RequestParam MetricAggregation.AggregationWindow window,

            @Parameter(description = "Start time (epoch millis)", required = true)
            @RequestParam long startTime,

            @Parameter(description = "End time (epoch millis)", required = true)
            @RequestParam long endTime
    ) {
        return metricCollectionService.getAggregatedMetrics(
                tenantId,
                serviceName,
                metricName,
                window,
                Instant.ofEpochMilli(startTime),
                Instant.ofEpochMilli(endTime)
        );
    }

    @GetMapping("/count")
    @Operation(summary = "Get metric count", description = "Returns the total count of metrics for a tenant")
    @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    public long getMetricCount(
            @Parameter(description = "Tenant ID from context", hidden = true)
            @RequestParam(defaultValue = "default") String tenantId
    ) {
        return metricCollectionService.getMetricCount(tenantId);
    }
}
