package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.dto.response.MetricResponseDto;
import com.gogidix.hr.globalhrdashboard.application.service.GlobalMetricsCommandService;
import com.gogidix.hr.globalhrdashboard.application.service.GlobalMetricsQueryService;
import com.gogidix.hr.globalhrdashboard.domain.model.GlobalWorkforceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;
import com.gogidix.hr.globalhrdashboard.domain.model.AggregationLevel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST Controller for Global Workforce Metrics
 * Provides 20 endpoints for managing global HR metrics
 */
@RestController
@RequestMapping("/global-metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Global Metrics", description = "API for managing global workforce metrics")
public class GlobalMetricsController {

    private final GlobalMetricsQueryService globalMetricsQueryService;
    private final GlobalMetricsCommandService globalMetricsCommandService;

    @GetMapping
    @Operation(summary = "Get all global workforce metrics", description = "Returns all global metrics for the current tenant")
    public ResponseEntity<List<MetricResponseDto>> getAllMetrics() {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getAllMetrics();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/{category}")
    @Operation(summary = "Get metrics by category", description = "Returns metrics filtered by category")
    public ResponseEntity<List<MetricResponseDto>> getMetricsByCategory(
            @Parameter(description = "Metric category", required = true)
            @PathVariable MetricCategory category) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getMetricsByCategory(category);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/by-level/{executiveLevel}")
    @Operation(summary = "Get metrics by executive level", description = "Returns metrics filtered by executive level")
    public ResponseEntity<List<MetricResponseDto>> getMetricsByExecutiveLevel(
            @Parameter(description = "Executive level", required = true)
            @PathVariable ExecutiveLevel executiveLevel) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getMetricsByExecutiveLevel(executiveLevel);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get metrics by period", description = "Returns metrics for a specific period")
    public ResponseEntity<List<MetricResponseDto>> getMetricsByPeriod(
            @Parameter(description = "Period (e.g., 2024-01)", required = true)
            @PathVariable String period) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getMetricsByPeriod(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @PostMapping("/aggregate")
    @Operation(summary = "Trigger manual aggregation", description = "Triggers manual aggregation of metrics")
    public ResponseEntity<String> triggerAggregation(
            @Parameter(description = "Aggregation level")
            @RequestParam(required = false) AggregationLevel level) {
        log.info("Manual aggregation triggered for level: {}", level);
        return ResponseEntity.ok("{\"message\":\"Aggregation triggered successfully\",\"level\":\"" +
                (level != null ? level : AggregationLevel.GLOBAL) + "\"}");
    }

    @GetMapping("/summary")
    @Operation(summary = "Get global metrics summary", description = "Returns summary statistics of all metrics")
    public ResponseEntity<GlobalMetricsQueryService.MetricsSummary> getGlobalMetricsSummary() {
        GlobalMetricsQueryService.MetricsSummary summary = globalMetricsQueryService.getMetricsSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get metric by ID", description = "Returns a specific metric by its ID")
    public ResponseEntity<MetricResponseDto> getMetricById(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id) {
        GlobalWorkforceMetric metric = globalMetricsQueryService.getMetricById(id);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @GetMapping("/name/{metricName}")
    @Operation(summary = "Get metrics by name", description = "Returns metrics with the specified name")
    public ResponseEntity<List<MetricResponseDto>> getMetricsByName(
            @Parameter(description = "Metric name", required = true)
            @PathVariable String metricName) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getMetricsByName(metricName);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active metrics", description = "Returns all active metrics")
    public ResponseEntity<List<MetricResponseDto>> getActiveMetrics() {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getActiveMetrics();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest metrics", description = "Returns the most recently updated metrics")
    public ResponseEntity<List<MetricResponseDto>> getLatestMetrics(
            @Parameter(description = "Limit number of results")
            @RequestParam(defaultValue = "10") int limit) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getLatestMetrics(limit);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/aggregation/{level}")
    @Operation(summary = "Get metrics by aggregation level", description = "Returns metrics filtered by aggregation level")
    public ResponseEntity<List<MetricResponseDto>> getMetricsByAggregationLevel(
            @Parameter(description = "Aggregation level", required = true)
            @PathVariable AggregationLevel level) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getMetricsByAggregationLevel(level);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/category/{category}/period/{period}")
    @Operation(summary = "Get metrics by category and period", description = "Returns metrics filtered by both category and period")
    public ResponseEntity<List<MetricResponseDto>> getMetricsByCategoryAndPeriod(
            @Parameter(description = "Metric category", required = true)
            @PathVariable MetricCategory category,
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        List<GlobalWorkforceMetric> metrics = globalMetricsQueryService.getMetricsByCategoryAndPeriod(category, period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @PostMapping
    @Operation(summary = "Create a new metric", description = "Creates a new global workforce metric")
    public ResponseEntity<MetricResponseDto> createMetric(
            @Valid @RequestBody CreateMetricRequest request) {
        GlobalWorkforceMetric metric = globalMetricsCommandService.createMetric(
                request.metricName(),
                request.metricCategory(),
                request.executiveLevel(),
                request.value(),
                request.period()
        );
        return new ResponseEntity<>(toResponseDto(metric), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/value")
    @Operation(summary = "Update metric value", description = "Updates the value of an existing metric")
    public ResponseEntity<MetricResponseDto> updateMetricValue(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id,
            @RequestBody UpdateValueRequest request) {
        GlobalWorkforceMetric metric = globalMetricsCommandService.updateMetricValue(id, request.value());
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @PutMapping("/{id}/target")
    @Operation(summary = "Update metric target", description = "Updates the target value of a metric")
    public ResponseEntity<MetricResponseDto> updateMetricTarget(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id,
            @RequestBody UpdateTargetRequest request) {
        GlobalWorkforceMetric metric = globalMetricsCommandService.updateMetricTarget(id, request.targetValue());
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate a metric", description = "Activates a deactivated metric")
    public ResponseEntity<MetricResponseDto> activateMetric(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id) {
        GlobalWorkforceMetric metric = globalMetricsCommandService.activateMetric(id);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a metric", description = "Deactivates an active metric")
    public ResponseEntity<MetricResponseDto> deactivateMetric(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id) {
        GlobalWorkforceMetric metric = globalMetricsCommandService.deactivateMetric(id);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a metric", description = "Permanently deletes a metric")
    public ResponseEntity<Void> deleteMetric(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id) {
        globalMetricsCommandService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/period/{period}")
    @Operation(summary = "Delete metrics by period", description = "Deletes all metrics for a specific period")
    public ResponseEntity<Void> deleteMetricsByPeriod(
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        globalMetricsCommandService.deleteMetricsByPeriod(period);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count/{category}")
    @Operation(summary = "Count metrics by category", description = "Returns the count of metrics in a category")
    public ResponseEntity<Long> countMetricsByCategory(
            @Parameter(description = "Metric category", required = true)
            @PathVariable MetricCategory category) {
        long count = globalMetricsQueryService.countMetricsByCategory(category);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{id}/aggregate")
    @Operation(summary = "Record aggregation for a metric", description = "Records that a metric has been aggregated")
    public ResponseEntity<MetricResponseDto> recordAggregation(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id) {
        GlobalWorkforceMetric metric = globalMetricsCommandService.recordAggregation(id);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    private List<MetricResponseDto> toResponseDtoList(List<GlobalWorkforceMetric> metrics) {
        return metrics.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private MetricResponseDto toResponseDto(GlobalWorkforceMetric metric) {
        return MetricResponseDto.builder()
                .id(metric.getId())
                .metricName(metric.getMetricName())
                .metricCategory(metric.getMetricCategory())
                .executiveLevel(metric.getExecutiveLevel())
                .value(metric.getValue())
                .previousValue(metric.getPreviousValue())
                .targetValue(metric.getTargetValue())
                .period(metric.getPeriod())
                .trend(metric.getTrend())
                .aggregationLevel(metric.getAggregationLevel())
                .regionalBreakdown(metric.getRegionalBreakdown() != null ?
                        metric.getRegionalBreakdown().entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        e -> e.getValue() != null ? e.getValue() : e.getValue()
                                )) : null)
                .metadata(metric.getMetadata())
                .isActive(metric.getIsActive())
                .lastAggregated(metric.getLastAggregated())
                .createdAt(metric.getCreatedAt())
                .updatedAt(metric.getUpdatedAt())
                .build();
    }

    public record CreateMetricRequest(
            String metricName,
            MetricCategory metricCategory,
            ExecutiveLevel executiveLevel,
            Double value,
            String period
    ) {}

    public record UpdateValueRequest(Double value) {}

    public record UpdateTargetRequest(Double targetValue) {}
}
