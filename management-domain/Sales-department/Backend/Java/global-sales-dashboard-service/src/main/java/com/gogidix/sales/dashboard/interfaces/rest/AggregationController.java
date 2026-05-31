package com.gogidix.sales.dashboard.interfaces.rest;

import com.gogidix.sales.dashboard.application.service.AggregationCommandService;
import com.gogidix.sales.dashboard.application.service.AggregationQueryService;
import com.gogidix.sales.dashboard.application.dto.response.AggregationResponseDto;
import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Sales Aggregation Operations
 * Handles aggregation of sales data across different dimensions
 */
@RestController
@RequestMapping("/aggregations")
@Tag(name = "Sales Aggregation", description = "Sales data aggregation and metrics operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class AggregationController {

    private final AggregationCommandService commandService;
    private final AggregationQueryService queryService;

    @GetMapping
    @Operation(summary = "Get all aggregations", description = "Returns all aggregations for current tenant")
    public ResponseEntity<List<AggregationResponseDto>> getAllAggregations() {
        log.info("Getting all aggregations for tenant: {}", RequestContextHolder.getTenantId());
        return ResponseEntity.ok(queryService.getAllForTenant());
    }

    @GetMapping("/{aggregationId}")
    @Operation(summary = "Get aggregation by ID", description = "Returns a single aggregation")
    public ResponseEntity<AggregationResponseDto> getAggregation(
            @Parameter(description = "Aggregation ID") @PathVariable String aggregationId) {
        log.info("Getting aggregation: {}", aggregationId);
        return ResponseEntity.ok(queryService.getById(aggregationId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get aggregations by type", description = "Returns aggregations filtered by type")
    public ResponseEntity<List<AggregationResponseDto>> getByType(
            @Parameter(description = "Aggregation type") @PathVariable SalesAggregation.AggregationType type) {
        log.info("Getting aggregations by type: {}", type);
        return ResponseEntity.ok(queryService.getByType(type));
    }

    @GetMapping("/dimension/{dimension}")
    @Operation(summary = "Get aggregations by dimension", description = "Returns aggregations filtered by dimension")
    public ResponseEntity<List<AggregationResponseDto>> getByDimension(
            @Parameter(description = "Aggregation dimension") @PathVariable SalesAggregation.AggregationDimension dimension) {
        log.info("Getting aggregations by dimension: {}", dimension);
        return ResponseEntity.ok(queryService.getByDimension(dimension));
    }

    @GetMapping("/dimension/{dimension}/value/{value}")
    @Operation(summary = "Get aggregations by dimension and value", description = "Returns aggregations for a specific dimension value")
    public ResponseEntity<List<AggregationResponseDto>> getByDimensionAndValue(
            @Parameter(description = "Aggregation dimension") @PathVariable SalesAggregation.AggregationDimension dimension,
            @Parameter(description = "Dimension value") @PathVariable String value) {
        log.info("Getting aggregations by dimension: {} and value: {}", dimension, value);
        return ResponseEntity.ok(queryService.getByDimensionAndValue(dimension, value));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get aggregations by date range", description = "Returns aggregations within a date range")
    public ResponseEntity<List<AggregationResponseDto>> getByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("Getting aggregations from {} to {}", startDate, endDate);
        return ResponseEntity.ok(queryService.getByDateRange(startDate, endDate));
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest aggregations", description = "Returns the most recent aggregations")
    public ResponseEntity<List<AggregationResponseDto>> getLatest(
            @Parameter(description = "Aggregation type") @RequestParam SalesAggregation.AggregationType type,
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") int limit) {
        log.info("Getting latest aggregations of type: {}", type);
        return ResponseEntity.ok(queryService.getLatestByType(type, limit));
    }

    @GetMapping("/compare")
    @Operation(summary = "Compare aggregations", description = "Compares aggregations across different periods")
    public ResponseEntity<Map<String, Object>> compareAggregations(
            @Parameter(description = "Aggregation type") @RequestParam SalesAggregation.AggregationType type,
            @Parameter(description = "Dimension") @RequestParam SalesAggregation.AggregationDimension dimension,
            @Parameter(description = "Period 1 start") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period1Start,
            @Parameter(description = "Period 1 end") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period1End,
            @Parameter(description = "Period 2 start") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period2Start,
            @Parameter(description = "Period 2 end") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate period2End) {
        log.info("Comparing aggregations for periods {} to {} vs {} to {}", period1Start, period1End, period2Start, period2End);
        return ResponseEntity.ok(queryService.comparePeriods(type, dimension, period1Start, period1End, period2Start, period2End));
    }

    @PostMapping
    @Operation(summary = "Create aggregation", description = "Creates a new sales aggregation")
    public ResponseEntity<AggregationResponseDto> createAggregation(
            @Valid @RequestBody CreateAggregationRequest request) {
        log.info("Creating aggregation of type: {} for tenant: {}", request.type(), RequestContextHolder.getTenantId());
        SalesAggregation aggregation = commandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(queryService.toDto(aggregation));
    }

    @PostMapping("/{aggregationId}/complete")
    @Operation(summary = "Mark aggregation as complete", description = "Marks an aggregation as complete")
    public ResponseEntity<Void> markAsComplete(
            @Parameter(description = "Aggregation ID") @PathVariable String aggregationId) {
        log.info("Marking aggregation as complete: {}", aggregationId);
        commandService.markAsComplete(aggregationId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/recalculate")
    @Operation(summary = "Recalculate aggregations", description = "Triggers recalculation of aggregations")
    public ResponseEntity<Map<String, Object>> recalculate(
            @RequestBody RecalculateRequest request) {
        log.info("Recalculating aggregations from {} to {}", request.startDate(), request.endDate());
        Map<String, Object> result = commandService.recalculate(request);
        return ResponseEntity.accepted().body(result);
    }

    @PostMapping("/aggregate")
    @Operation(summary = "Aggregate data", description = "Aggregates raw sales data into metrics")
    public ResponseEntity<AggregationResponseDto> aggregate(
            @Valid @RequestBody AggregateDataRequest request) {
        log.info("Aggregating data for dimension: {}", request.dimension());
        SalesAggregation aggregation = commandService.aggregate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(queryService.toDto(aggregation));
    }

    @PutMapping("/{aggregationId}/metrics")
    @Operation(summary = "Update aggregation metrics", description = "Updates the metrics of an aggregation")
    public ResponseEntity<Void> updateMetrics(
            @Parameter(description = "Aggregation ID") @PathVariable String aggregationId,
            @RequestBody UpdateMetricsRequest request) {
        log.info("Updating metrics for aggregation: {}", aggregationId);
        commandService.updateMetrics(aggregationId, request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{aggregationId}/comparison")
    @Operation(summary = "Update comparison data", description = "Updates comparison data for trend analysis")
    public ResponseEntity<Void> updateComparison(
            @Parameter(description = "Aggregation ID") @PathVariable String aggregationId,
            @RequestBody UpdateComparisonRequest request) {
        log.info("Updating comparison data for aggregation: {}", aggregationId);
        commandService.updateComparison(aggregationId, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{aggregationId}/breakdown")
    @Operation(summary = "Add breakdown item", description = "Adds a breakdown item to the aggregation")
    public ResponseEntity<Void> addBreakdownItem(
            @Parameter(description = "Aggregation ID") @PathVariable String aggregationId,
            @RequestBody BreakdownItemRequest request) {
        log.info("Adding breakdown item to aggregation: {}", aggregationId);
        commandService.addBreakdownItem(aggregationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{aggregationId}")
    @Operation(summary = "Delete aggregation", description = "Deletes an aggregation")
    public ResponseEntity<Void> deleteAggregation(
            @Parameter(description = "Aggregation ID") @PathVariable String aggregationId) {
        log.info("Deleting aggregation: {}", aggregationId);
        commandService.delete(aggregationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/old-data")
    @Operation(summary = "Delete old aggregations", description = "Deletes aggregations older than specified date")
    public ResponseEntity<Map<String, Object>> deleteOldData(
            @Parameter(description = "Delete data before this date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        log.info("Deleting aggregations older than: {}", beforeDate);
        Map<String, Object> result = commandService.deleteOldAggregations(beforeDate);
        return ResponseEntity.ok(result);
    }

    // Request DTOs

    public record CreateAggregationRequest(
            SalesAggregation.AggregationType type,
            SalesAggregation.AggregationDimension dimension,
            String dimensionValue,
            LocalDate startDate,
            LocalDate endDate,
            String periodType,
            Integer periodValue,
            Integer year,
            String dataSource,
            String currency
    ) {}

    public record RecalculateRequest(
            SalesAggregation.AggregationType type,
            LocalDate startDate,
            LocalDate endDate,
            String dimension,
            List<String> dimensionValues
    ) {}

    public record AggregateDataRequest(
            SalesAggregation.AggregationType type,
            SalesAggregation.AggregationDimension dimension,
            String dimensionValue,
            LocalDate startDate,
            LocalDate endDate,
            String currency,
            Map<String, Object> rawData
    ) {}

    public record UpdateMetricsRequest(
            BigDecimal totalRevenue,
            BigDecimal targetRevenue,
            Integer totalDeals,
            Integer wonDeals,
            BigDecimal averageDealSize,
            BigDecimal averageDiscount,
            Integer newOpportunities,
            BigDecimal pipelineValue,
            BigDecimal weightedPipeline,
            Integer activeCustomers
    ) {}

    public record UpdateComparisonRequest(
            BigDecimal previousPeriodRevenue,
            BigDecimal currentRevenue,
            String currency
    ) {}

    public record BreakdownItemRequest(
            String key,
            String label,
            BigDecimal value,
            Integer count,
            String color
    ) {}
}
