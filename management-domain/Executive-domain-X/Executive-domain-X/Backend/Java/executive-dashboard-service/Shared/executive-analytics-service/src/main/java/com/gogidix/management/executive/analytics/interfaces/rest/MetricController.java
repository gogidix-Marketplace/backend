package com.gogidix.management.executive.analytics.interfaces.rest;

import com.gogidix.management.executive.analytics.application.command.MetricCommandService;
import com.gogidix.management.executive.analytics.application.dto.CreateMetricRequest;
import com.gogidix.management.executive.analytics.application.query.MetricQueryService;
import com.gogidix.management.executive.analytics.domain.model.Metric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Metrics", description = "Granular measurement management APIs")
public class MetricController {
    private final MetricCommandService metricCommandService;
    private final MetricQueryService metricQueryService;

    @PostMapping
    @Operation(summary = "Create a new metric", description = "Creates a new metric for the current tenant")
    public ResponseEntity<Metric> createMetric(@Valid @RequestBody CreateMetricRequest request) {
        log.debug("REST: Creating metric: {}", request.getMetricName());
        Metric created = metricCommandService.createMetric(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create metrics", description = "Creates multiple metrics in a single request")
    public ResponseEntity<List<Metric>> bulkCreateMetrics(@Valid @RequestBody List<CreateMetricRequest> requests) {
        log.info("REST: Bulk creating {} metrics", requests.size());
        List<Metric> created = metricCommandService.bulkCreateMetrics(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get metric by ID", description = "Retrieves a metric by its ID")
    public ResponseEntity<Metric> getMetric(
        @Parameter(description = "Metric ID") @PathVariable String id
    ) {
        log.debug("REST: Getting metric: {}", id);
        Metric metric = metricQueryService.getMetricById(id);
        return ResponseEntity.ok(metric);
    }

    @GetMapping("/by-name/{metricName}")
    @Operation(summary = "Get metrics by name", description = "Retrieves all metrics with the given name")
    public ResponseEntity<List<Metric>> getMetricsByName(
        @Parameter(description = "Metric name") @PathVariable String metricName
    ) {
        log.debug("REST: Getting metrics by name: {}", metricName);
        List<Metric> metrics = metricQueryService.getMetricsByName(metricName);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/{metricName}/latest")
    @Operation(summary = "Get latest metrics by name", description = "Retrieves most recent metrics, paginated")
    public ResponseEntity<Page<Metric>> getLatestMetricsByName(
        @Parameter(description = "Metric name") @PathVariable String metricName,
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("REST: Getting latest metrics: {} page={}", metricName, page);
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        Page<Metric> metrics = metricQueryService.getLatestMetricsByName(metricName, pageable);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/by-source/{sourceDomain}")
    @Operation(summary = "Get metrics by source domain", description = "Retrieves metrics from a specific domain")
    public ResponseEntity<List<Metric>> getMetricsBySourceDomain(
        @Parameter(description = "Source domain") @PathVariable String sourceDomain
    ) {
        log.debug("REST: Getting metrics from source: {}", sourceDomain);
        List<Metric> metrics = metricQueryService.getMetricsBySourceDomain(sourceDomain);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/by-name-and-source")
    @Operation(summary = "Get metrics by name and source", description = "Retrieves metrics matching name and source")
    public ResponseEntity<List<Metric>> getMetricsByNameAndSource(
        @Parameter(description = "Metric name") @RequestParam String metricName,
        @Parameter(description = "Source domain") @RequestParam String sourceDomain
    ) {
        log.debug("REST: Getting metrics: {} from {}", metricName, sourceDomain);
        List<Metric> metrics = metricQueryService.getMetricsByNameAndSource(metricName, sourceDomain);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/by-time-range")
    @Operation(summary = "Get metrics by time range", description = "Retrieves metrics within a time range")
    public ResponseEntity<List<Metric>> getMetricsByTimeRange(
        @Parameter(description = "Start time (ISO-8601)") @RequestParam String startTime,
        @Parameter(description = "End time (ISO-8601)") @RequestParam String endTime
    ) {
        Instant start = Instant.parse(startTime);
        Instant end = Instant.parse(endTime);
        log.debug("REST: Getting metrics between {} and {}", start, end);
        List<Metric> metrics = metricQueryService.getMetricsByTimeRange(start, end);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent metrics", description = "Retrieves metrics from last N hours")
    public ResponseEntity<List<Metric>> getRecentMetrics(
        @Parameter(description = "Number of hours") @RequestParam(defaultValue = "24") int hours
    ) {
        log.debug("REST: Getting metrics from last {} hours", hours);
        List<Metric> metrics = metricQueryService.getRecentMetrics(hours);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/high-quality")
    @Operation(summary = "Get high quality metrics", description = "Retrieves metrics with quality score >= 0.9")
    public ResponseEntity<List<Metric>> getHighQualityMetrics() {
        log.debug("REST: Getting high quality metrics");
        List<Metric> metrics = metricQueryService.getHighQualityMetrics();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/aggregates")
    @Operation(summary = "Get aggregate metrics", description = "Retrieves aggregated metrics")
    public ResponseEntity<List<Metric>> getAggregateMetrics() {
        log.debug("REST: Getting aggregate metrics");
        List<Metric> metrics = metricQueryService.getAggregateMetrics();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/raw")
    @Operation(summary = "Get raw metrics", description = "Retrieves non-aggregated metrics")
    public ResponseEntity<List<Metric>> getRawMetrics() {
        log.debug("REST: Getting raw metrics");
        List<Metric> metrics = metricQueryService.getRawMetrics();
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/by-dimension")
    @Operation(summary = "Get metrics by dimension", description = "Retrieves metrics filtered by dimension")
    public ResponseEntity<List<Metric>> getMetricsByDimension(
        @Parameter(description = "Dimension key") @RequestParam String dimensionKey,
        @Parameter(description = "Dimension value") @RequestParam String dimensionValue
    ) {
        log.debug("REST: Getting metrics by dimension: {} = {}", dimensionKey, dimensionValue);
        List<Metric> metrics = metricQueryService.getMetricsByDimension(dimensionKey, dimensionValue);
        return ResponseEntity.ok(metrics);
    }

    @PostMapping("/by-sources")
    @Operation(summary = "Get metrics by sources", description = "Retrieves metrics from specified source domains")
    public ResponseEntity<List<Metric>> getMetricsBySources(
        @Parameter(description = "List of source domains") @RequestBody List<String> sourceDomains
    ) {
        log.debug("REST: Getting metrics from sources: {}", sourceDomains);
        List<Metric> metrics = metricQueryService.getMetricsBySourceDomains(sourceDomains);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/search")
    @Operation(summary = "Search metrics", description = "Searches metrics by name or source domain")
    public ResponseEntity<List<Metric>> searchMetrics(
        @Parameter(description = "Search term") @RequestParam String term
    ) {
        log.debug("REST: Searching metrics with term: {}", term);
        List<Metric> metrics = metricQueryService.searchMetrics(term);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/metadata/metric-names")
    @Operation(summary = "Get unique metric names", description = "Retrieves list of unique metric names")
    public ResponseEntity<List<String>> getUniqueMetricNames() {
        log.debug("REST: Getting unique metric names");
        List<String> names = metricQueryService.getUniqueMetricNames();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/metadata/source-domains")
    @Operation(summary = "Get unique source domains", description = "Retrieves list of unique source domains")
    public ResponseEntity<List<String>> getUniqueSourceDomains() {
        log.debug("REST: Getting unique source domains");
        List<String> domains = metricQueryService.getUniqueSourceDomains();
        return ResponseEntity.ok(domains);
    }

    @PutMapping("/{id}/quality-score")
    @Operation(summary = "Update quality score", description = "Updates the quality score of a metric")
    public ResponseEntity<Metric> updateQualityScore(
        @Parameter(description = "Metric ID") @PathVariable String id,
        @Parameter(description = "Quality score (0-1)") @RequestParam Double qualityScore
    ) {
        log.info("REST: Updating quality score for metric: {} = {}", id, qualityScore);
        Metric metric = metricCommandService.updateQualityScore(id, qualityScore);
        return ResponseEntity.ok(metric);
    }

    @PostMapping("/{id}/mark-aggregate")
    @Operation(summary = "Mark as aggregate", description = "Marks a metric as an aggregate")
    public ResponseEntity<Void> markAsAggregate(@Parameter(description = "Metric ID") @PathVariable String id) {
        log.info("REST: Marking metric as aggregate: {}", id);
        metricCommandService.markAsAggregate(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/old")
    @Operation(summary = "Delete old metrics", description = "Deletes metrics older than specified timestamp")
    public ResponseEntity<Long> deleteOldMetrics(
        @Parameter(description = "Timestamp threshold (ISO-8601)") @RequestParam String threshold
    ) {
        Instant instant = Instant.parse(threshold);
        log.info("REST: Deleting metrics older than {}", instant);
        long deleted = metricCommandService.deleteOlderThan(instant);
        return ResponseEntity.ok(deleted);
    }
}
