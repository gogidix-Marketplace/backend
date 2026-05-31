package com.gogidix.analytics.metrics.interfaces.rest;

import com.gogidix.analytics.metrics.application.service.MetricsCommandService;
import com.gogidix.analytics.metrics.application.service.MetricsQueryService;
import com.gogidix.analytics.metrics.domain.model.MetricAggregation;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.port.in.CreateMetricAlertCommand;
import com.gogidix.analytics.metrics.domain.port.in.GetMetricsQuery;
import com.gogidix.analytics.metrics.domain.port.in.IngestMetricCommand;
import com.gogidix.analytics.metrics.application.service.MetricsQueryService.MetricStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for Metrics Aggregation API.
 */
@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Tag(name = "Metrics", description = "Metrics aggregation and retrieval API")
public class MetricsController {

    private final MetricsCommandService commandService;
    private final MetricsQueryService queryService;

    @PostMapping("/ingest")
    @Operation(summary = "Ingest a metric data point")
    public ResponseEntity<MetricDataPoint> ingestMetric(@Valid @RequestBody IngestMetricCommand command) {
        MetricDataPoint metric = commandService.ingestMetric(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @PostMapping("/ingest/batch")
    @Operation(summary = "Ingest multiple metric data points")
    public ResponseEntity<List<MetricDataPoint>> ingestMetricsBatch(
        @Valid @RequestBody List<IngestMetricCommand> commands) {
        List<MetricDataPoint> metrics = commandService.ingestMetricsBatch(commands);
        return ResponseEntity.status(HttpStatus.CREATED).body(metrics);
    }

    @GetMapping
    @Operation(summary = "Query metrics with filters")
    public ResponseEntity<Page<MetricDataPoint>> getMetrics(
        @RequestParam(required = false) String metricName,
        @RequestParam(required = false) String sourceService,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
        @RequestParam(required = false) String aggregationType,
        @RequestParam(required = false) Integer aggregationWindowSeconds,
        Pageable pageable) {

        GetMetricsQuery query = GetMetricsQuery.builder()
            .metricName(metricName)
            .sourceService(sourceService)
            .startTime(startTime)
            .endTime(endTime)
            .aggregationType(aggregationType)
            .aggregationWindowSeconds(aggregationWindowSeconds)
            .build();

        return ResponseEntity.ok(queryService.getMetrics(query, pageable));
    }

    @GetMapping("/current/{metricName}")
    @Operation(summary = "Get current value for a metric")
    public ResponseEntity<MetricDataPoint> getCurrentMetric(@PathVariable String metricName) {
        return ResponseEntity.ok(queryService.getCurrentMetricValue(metricName));
    }

    @GetMapping("/statistics/{metricName}")
    @Operation(summary = "Get metric statistics for a time range")
    public ResponseEntity<MetricStatistics> getMetricStatistics(
        @PathVariable String metricName,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return ResponseEntity.ok(queryService.getMetricStatistics(metricName, startTime, endTime));
    }

    @GetMapping("/aggregated")
    @Operation(summary = "Get aggregated metrics")
    public ResponseEntity<List<MetricAggregation>> getAggregatedMetrics(
        @RequestParam String metricName,
        @RequestParam(required = false) String aggregationType,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {

        return ResponseEntity.ok(queryService.getAggregatedMetrics(
            metricName, aggregationType, startTime, endTime));
    }

    // Alert endpoints

    @PostMapping("/alerts")
    @Operation(summary = "Create a metric alert")
    public ResponseEntity<MetricAlert> createAlert(@Valid @RequestBody CreateMetricAlertCommand command) {
        MetricAlert alert = commandService.createAlert(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get all alerts for tenant")
    public ResponseEntity<List<MetricAlert>> getAlerts() {
        return ResponseEntity.ok(queryService.getAlerts());
    }

    @GetMapping("/alerts/{alertId}")
    @Operation(summary = "Get alert by ID")
    public ResponseEntity<MetricAlert> getAlert(@PathVariable String alertId) {
        return ResponseEntity.ok(queryService.getAlert(alertId));
    }

    @PutMapping("/alerts/{alertId}")
    @Operation(summary = "Update a metric alert")
    public ResponseEntity<MetricAlert> updateAlert(
        @PathVariable String alertId,
        @Valid @RequestBody CreateMetricAlertCommand command) {

        return ResponseEntity.ok(commandService.updateAlert(alertId, command));
    }

    @DeleteMapping("/alerts/{alertId}")
    @Operation(summary = "Delete a metric alert")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlert(@PathVariable String alertId) {
        commandService.deleteAlert(alertId);
    }

    @PutMapping("/alerts/{alertId}/toggle")
    @Operation(summary = "Enable/disable a metric alert")
    public ResponseEntity<MetricAlert> toggleAlert(
        @PathVariable String alertId,
        @RequestParam boolean enabled) {

        return ResponseEntity.ok(commandService.toggleAlert(alertId, enabled));
    }
}
