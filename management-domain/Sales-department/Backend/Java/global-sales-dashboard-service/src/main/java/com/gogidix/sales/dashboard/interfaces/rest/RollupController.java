package com.gogidix.sales.dashboard.interfaces.rest;

import com.gogidix.sales.dashboard.application.service.RollupCommandService;
import com.gogidix.sales.dashboard.application.service.RollupQueryService;
import com.gogidix.sales.dashboard.application.dto.response.RollupResponseDto;
import com.gogidix.sales.dashboard.domain.model.MetricRollup;
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
 * REST Controller for Metric Rollup Operations
 * Handles pre-calculated rollup metrics for efficient dashboard queries
 */
@RestController
@RequestMapping("/rollups")
@Tag(name = "Metric Rollup", description = "Pre-calculated metric rollup operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class RollupController {

    private final RollupCommandService commandService;
    private final RollupQueryService queryService;

    @GetMapping
    @Operation(summary = "Get all rollups", description = "Returns all rollups for current tenant")
    public ResponseEntity<List<RollupResponseDto>> getAllRollups() {
        log.info("Getting all rollups for tenant: {}", RequestContextHolder.getTenantId());
        return ResponseEntity.ok(queryService.getAllForTenant());
    }

    @GetMapping("/{rollupId}")
    @Operation(summary = "Get rollup by ID", description = "Returns a single rollup")
    public ResponseEntity<RollupResponseDto> getRollup(
            @Parameter(description = "Rollup ID") @PathVariable String rollupId) {
        log.info("Getting rollup: {}", rollupId);
        return ResponseEntity.ok(queryService.getById(rollupId));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get rollups by type", description = "Returns rollups filtered by type")
    public ResponseEntity<List<RollupResponseDto>> getByType(
            @Parameter(description = "Rollup type") @PathVariable MetricRollup.RollupType type) {
        log.info("Getting rollups by type: {}", type);
        return ResponseEntity.ok(queryService.getByType(type));
    }

    @GetMapping("/key/{key}")
    @Operation(summary = "Get rollups by key", description = "Returns rollups for a specific key")
    public ResponseEntity<List<RollupResponseDto>> getByKey(
            @Parameter(description = "Rollup key") @PathVariable String key) {
        log.info("Getting rollups by key: {}", key);
        return ResponseEntity.ok(queryService.getByKey(key));
    }

    @GetMapping("/global")
    @Operation(summary = "Get global rollup", description = "Returns the global aggregated rollup")
    public ResponseEntity<RollupResponseDto> getGlobalRollup() {
        log.info("Getting global rollup for tenant: {}", RequestContextHolder.getTenantId());
        return ResponseEntity.ok(queryService.getGlobalRollup());
    }

    @GetMapping("/regional")
    @Operation(summary = "Get regional rollups", description = "Returns all regional rollups")
    public ResponseEntity<List<RollupResponseDto>> getRegionalRollups() {
        log.info("Getting regional rollups");
        return ResponseEntity.ok(queryService.getRegionalRollups());
    }

    @GetMapping("/region/{regionCode}")
    @Operation(summary = "Get rollup by region", description = "Returns rollup for a specific region")
    public ResponseEntity<RollupResponseDto> getRegionalRollup(
            @Parameter(description = "Region code") @PathVariable String regionCode) {
        log.info("Getting rollup for region: {}", regionCode);
        return ResponseEntity.ok(queryService.getByKey(regionCode).stream().findFirst().orElse(null));
    }

    @GetMapping("/hierarchy/{parentRollupId}")
    @Operation(summary = "Get child rollups", description = "Returns all child rollups of a parent")
    public ResponseEntity<List<RollupResponseDto>> getChildRollups(
            @Parameter(description = "Parent rollup ID") @PathVariable String parentRollupId) {
        log.info("Getting child rollups of parent: {}", parentRollupId);
        return ResponseEntity.ok(queryService.getChildRollups(parentRollupId));
    }

    @GetMapping("/performance")
    @Operation(summary = "Get performance summary", description = "Returns performance summary across all rollups")
    public ResponseEntity<Map<String, Object>> getPerformanceSummary() {
        log.info("Getting performance summary");
        return ResponseEntity.ok(queryService.getPerformanceSummary());
    }

    @GetMapping("/comparison")
    @Operation(summary = "Compare rollups", description = "Compares metrics across different rollups")
    public ResponseEntity<Map<String, Object>> compareRollups(
            @Parameter(description = "Rollup IDs to compare") @RequestParam List<String> rollupIds,
            @Parameter(description = "Metric to compare") @RequestParam(defaultValue = "revenue") String metric) {
        log.info("Comparing rollups: {}", rollupIds);
        return ResponseEntity.ok(queryService.compareRollups(rollupIds, metric));
    }

    @GetMapping("/leaderboard")
    @Operation(summary = "Get leaderboard", description = "Returns ranked list by specified metric")
    public ResponseEntity<List<RollupResponseDto>> getLeaderboard(
            @Parameter(description = "Metric for ranking") @RequestParam(defaultValue = "revenue") String metric,
            @Parameter(description = "Limit results") @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Rollup type filter") @RequestParam(required = false) MetricRollup.RollupType type) {
        log.info("Getting leaderboard for metric: {}", metric);
        return ResponseEntity.ok(queryService.getLeaderboard(metric, limit, type));
    }

    @PostMapping
    @Operation(summary = "Create rollup", description = "Creates a new metric rollup")
    public ResponseEntity<RollupResponseDto> createRollup(
            @Valid @RequestBody CreateRollupRequest request) {
        log.info("Creating rollup of type: {} for key: {}", request.type(), request.key());
        MetricRollup rollup = commandService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(queryService.toDto(rollup));
    }

    @PostMapping("/aggregate")
    @Operation(summary = "Aggregate from children", description = "Aggregates metrics from child rollups")
    public ResponseEntity<Void> aggregateFromChildren(
            @Parameter(description = "Parent rollup ID") @PathVariable String parentRollupId) {
        log.info("Aggregating from children for rollup: {}", parentRollupId);
        commandService.aggregateFromChildren(parentRollupId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/batch")
    @Operation(summary = "Create batch rollups", description = "Creates multiple rollups in batch")
    public ResponseEntity<List<RollupResponseDto>> createBatch(
            @Valid @RequestBody List<CreateRollupRequest> requests) {
        log.info("Creating batch of {} rollups", requests.size());
        List<MetricRollup> rollups = commandService.createBatch(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                rollups.stream().map(queryService::toDto).toList()
        );
    }

    @PutMapping("/{rollupId}/metrics")
    @Operation(summary = "Update rollup metrics", description = "Updates the metrics of a rollup")
    public ResponseEntity<Void> updateMetrics(
            @Parameter(description = "Rollup ID") @PathVariable String rollupId,
            @RequestBody UpdateMetricsRequest request) {
        log.info("Updating metrics for rollup: {}", rollupId);
        commandService.updateMetrics(rollupId, request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{rollupId}/target")
    @Operation(summary = "Update target", description = "Updates a target for the rollup")
    public ResponseEntity<Void> updateTarget(
            @Parameter(description = "Rollup ID") @PathVariable String rollupId,
            @RequestBody UpdateTargetRequest request) {
        log.info("Updating target {} for rollup: {}", request.targetType(), rollupId);
        commandService.updateTarget(rollupId, request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{rollupId}/refresh")
    @Operation(summary = "Refresh rollup", description = "Refreshes rollup data from source aggregations")
    public ResponseEntity<RollupResponseDto> refreshRollup(
            @Parameter(description = "Rollup ID") @PathVariable String rollupId) {
        log.info("Refreshing rollup: {}", rollupId);
        MetricRollup rollup = commandService.refresh(rollupId);
        return ResponseEntity.ok(queryService.toDto(rollup));
    }

    @PostMapping("/refresh-all")
    @Operation(summary = "Refresh all rollups", description = "Refreshes all rollups for the tenant")
    public ResponseEntity<Map<String, Object>> refreshAllRollups() {
        log.info("Refreshing all rollups for tenant: {}", RequestContextHolder.getTenantId());
        Map<String, Object> result = commandService.refreshAll();
        return ResponseEntity.accepted().body(result);
    }

    @PostMapping("/{rollupId}/recalculate-score")
    @Operation(summary = "Recalculate score", description = "Recalculates the performance score")
    public ResponseEntity<Void> recalculateScore(
            @Parameter(description = "Rollup ID") @PathVariable String rollupId) {
        log.info("Recalculating score for rollup: {}", rollupId);
        commandService.recalculateScore(rollupId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{rollupId}")
    @Operation(summary = "Delete rollup", description = "Deletes a rollup")
    public ResponseEntity<Void> deleteRollup(
            @Parameter(description = "Rollup ID") @PathVariable String rollupId) {
        log.info("Deleting rollup: {}", rollupId);
        commandService.delete(rollupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/old-data")
    @Operation(summary = "Delete old rollups", description = "Deletes rollups older than specified date")
    public ResponseEntity<Map<String, Object>> deleteOldData(
            @Parameter(description = "Delete data before this date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate) {
        log.info("Deleting rollups older than: {}", beforeDate);
        Map<String, Object> result = commandService.deleteOldRollups(beforeDate);
        return ResponseEntity.ok(result);
    }

    // Request DTOs

    public record CreateRollupRequest(
            MetricRollup.RollupType type,
            String key,
            String name,
            LocalDate startDate,
            LocalDate endDate,
            String periodType,
            Integer periodValue,
            Integer year,
            String quarter,
            String currency,
            String parentRollupId
    ) {}

    public record UpdateMetricsRequest(
            BigDecimal revenue,
            Integer deals,
            BigDecimal winRate,
            BigDecimal pipelineValue,
            Integer opportunities,
            BigDecimal averageDealSize,
            BigDecimal growthRate,
            Integer newCustomers,
            BigDecimal margin
    ) {}

    public record UpdateTargetRequest(
            String targetType,
            BigDecimal targetValue,
            BigDecimal currentValue,
            LocalDate targetDate,
            String frequency
    ) {}
}
