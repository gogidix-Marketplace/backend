package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.service.RegionalMetricsQueryService;
import com.gogidix.hr.globalhrdashboard.domain.model.RegionalMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Regional Metrics
 * Provides 15 endpoints for managing regional HR metrics
 */
@RestController
@RequestMapping("/regional-metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Regional Metrics", description = "API for managing regional workforce metrics")
public class RegionalMetricsController {

    private final RegionalMetricsQueryService regionalMetricsQueryService;

    @GetMapping
    @Operation(summary = "Get all regional metrics", description = "Returns all regional metrics for the current tenant")
    public ResponseEntity<List<RegionalMetricDto>> getAllRegionalMetrics() {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getAllRegionalMetrics();
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/{regionCode}")
    @Operation(summary = "Get specific region", description = "Returns metrics for a specific region")
    public ResponseEntity<List<RegionalMetricDto>> getRegionalMetrics(
            @Parameter(description = "Region code (e.g., EUROPE, ASIA)", required = true)
            @PathVariable String regionCode) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsByRegion(regionCode);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/{regionCode}/countries")
    @Operation(summary = "Get countries in region", description = "Returns all country-level metrics within a region")
    public ResponseEntity<List<RegionalMetric.CountryMetric>> getCountriesInRegion(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Metric name", required = true)
            @RequestParam String metricName,
            @Parameter(description = "Period", required = true)
            @RequestParam String period) {
        List<RegionalMetric.CountryMetric> countries = regionalMetricsQueryService.getCountriesInRegion(
                regionCode, metricName, period);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{regionCode}/trend")
    @Operation(summary = "Get regional trend", description = "Returns trend data for a specific region and metric")
    public ResponseEntity<List<RegionalMetricDto>> getRegionalTrend(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Metric name", required = true)
            @RequestParam String metricName,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getRegionalTrend(
                regionCode, metricName, startPeriod, endPeriod);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get metrics by period", description = "Returns all regional metrics for a specific period")
    public ResponseEntity<List<RegionalMetricDto>> getMetricsByPeriod(
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsByPeriod(period);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/{regionCode}/period/{period}")
    @Operation(summary = "Get region by period", description = "Returns metrics for a region in a specific period")
    public ResponseEntity<List<RegionalMetricDto>> getMetricsByRegionAndPeriod(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsByRegionAndPeriod(regionCode, period);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/regions/list")
    @Operation(summary = "Get all regions", description = "Returns list of all available regions")
    public ResponseEntity<List<String>> getAllRegions() {
        List<String> regions = regionalMetricsQueryService.getAllRegions();
        return ResponseEntity.ok(regions);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get regional summary", description = "Returns summary statistics across all regions")
    public ResponseEntity<RegionalMetricsQueryService.RegionalSummary> getRegionalSummary(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        RegionalMetricsQueryService.RegionalSummary summary = regionalMetricsQueryService.getRegionalSummary(period);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get metric by ID", description = "Returns a specific regional metric by ID")
    public ResponseEntity<RegionalMetricDto> getMetricById(
            @Parameter(description = "Metric ID", required = true)
            @PathVariable String id) {
        RegionalMetric metric = regionalMetricsQueryService.getMetricById(id);
        return ResponseEntity.ok(toDto(metric));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active metrics", description = "Returns all active regional metrics")
    public ResponseEntity<List<RegionalMetricDto>> getActiveMetrics() {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getActiveMetrics();
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get metrics by category", description = "Returns metrics filtered by category")
    public ResponseEntity<List<RegionalMetricDto>> getMetricsByCategory(
            @Parameter(description = "Metric category", required = true)
            @PathVariable String category) {
        com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory metricCategory =
                com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory.valueOf(category);
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsByCategory(metricCategory);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/top")
    @Operation(summary = "Get top performing regions", description = "Returns regions with highest metric values")
    public ResponseEntity<List<RegionalMetricDto>> getTopRegions(
            @Parameter(description = "Metric name", required = true)
            @RequestParam String metricName,
            @Parameter(description = "Period", required = true)
            @RequestParam String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "5") int limit) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getTopRegionsByMetric(metricName, period, limit);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/{regionCode}/metric/{metricName}")
    @Operation(summary = "Get specific regional metric", description = "Returns a specific metric for a region")
    public ResponseEntity<RegionalMetricDto> getSpecificRegionalMetric(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Metric name", required = true)
            @PathVariable String metricName,
            @Parameter(description = "Period", required = true)
            @RequestParam String period) {
        RegionalMetric metric = regionalMetricsQueryService.getRegionalMetric(regionCode, metricName, period);
        return ResponseEntity.ok(toDto(metric));
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare regions", description = "Compares metrics across multiple regions")
    public ResponseEntity<RegionalComparison> compareRegions(
            @Parameter(description = "Metric name", required = true)
            @RequestParam String metricName,
            @Parameter(description = "Period", required = true)
            @RequestParam String period,
            @Parameter(description = "Region codes to compare", required = true)
            @RequestParam List<String> regionCodes) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsByPeriod(period);

        RegionalComparison comparison = new RegionalComparison(
                metricName,
                period,
                metrics.stream()
                        .filter(m -> regionCodes.contains(m.getRegionCode()))
                        .filter(m -> metricName.equals(m.getMetricName()))
                        .map(this::toDto)
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(comparison);
    }

    @GetMapping("/updated-since")
    @Operation(summary = "Get recently updated metrics", description = "Returns metrics updated since a specific time")
    public ResponseEntity<List<RegionalMetricDto>> getUpdatedSince(
            @Parameter(description = "Timestamp", required = true)
            @RequestParam long timestamp) {
        java.time.Instant since = java.time.Instant.ofEpochMilli(timestamp);
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsUpdatedSince(since);
        return ResponseEntity.ok(toDtoList(metrics));
    }

    @GetMapping("/{regionCode}/summary")
    @Operation(summary = "Get region summary", description = "Returns summary for a specific region")
    public ResponseEntity<RegionSummary> getRegionSummary(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Period", required = true)
            @RequestParam String period) {
        List<RegionalMetric> metrics = regionalMetricsQueryService.getMetricsByRegionAndPeriod(regionCode, period);

        double avgValue = metrics.stream()
                .filter(m -> m.getValue() != null)
                .mapToDouble(RegionalMetric::getValue)
                .average()
                .orElse(0.0);

        int totalCountries = metrics.stream()
                .mapToInt(m -> m.getCountries() != null ? m.getCountries().size() : 0)
                .sum();

        RegionSummary summary = new RegionSummary(
                regionCode,
                period,
                metrics.size(),
                totalCountries,
                avgValue
        );

        return ResponseEntity.ok(summary);
    }

    private List<RegionalMetricDto> toDtoList(List<RegionalMetric> metrics) {
        return metrics.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private RegionalMetricDto toDto(RegionalMetric metric) {
        return new RegionalMetricDto(
                metric.getId(),
                metric.getRegionCode(),
                metric.getRegionName(),
                metric.getMetricName(),
                metric.getValue(),
                metric.getPreviousValue(),
                metric.getPeriod(),
                metric.getTrend(),
                metric.getTotalCountries()
        );
    }

    private String getCurrentPeriod() {
        return java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public record RegionalMetricDto(
            String id,
            String regionCode,
            String regionName,
            String metricName,
            Double value,
            Double previousValue,
            String period,
            com.gogidix.hr.globalhrdashboard.domain.model.MetricTrend trend,
            Integer totalCountries
    ) {}

    public record RegionalComparison(
            String metricName,
            String period,
            List<RegionalMetricDto> regions
    ) {}

    public record RegionSummary(
            String regionCode,
            String period,
            int totalMetrics,
            int totalCountries,
            double averageValue
    ) {}
}
