package com.gogidix.globalbusinessmanagement.dashboard.interfaces.rest;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.GlobalBusinessMetricsDto;
import com.gogidix.globalbusinessmanagement.dashboard.application.service.GlobalBusinessMetricsService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for GlobalBusinessMetrics operations.
 * Provides endpoints for managing global business metrics.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/global-metrics")
@RequiredArgsConstructor
@Tag(name = "Global Business Metrics", description = "APIs for managing global business metrics")
public class GlobalBusinessMetricsController {

    private final GlobalBusinessMetricsService metricsService;

    @PostMapping
    @Operation(summary = "Create global business metrics", description = "Create a new global business metrics entry")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Metrics created successfully",
            content = @Content(schema = @Schema(implementation = GlobalBusinessMetricsDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<GlobalBusinessMetricsDto> createMetrics(
        @Valid @RequestBody GlobalBusinessMetricsDto dto) {
        log.info("REST request to create global business metrics for period: {}", dto.getPeriodId());
        GlobalBusinessMetricsDto result = metricsService.createMetrics(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update global business metrics", description = "Update an existing global business metrics entry")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics updated successfully",
            content = @Content(schema = @Schema(implementation = GlobalBusinessMetricsDto.class))),
        @ApiResponse(responseCode = "404", description = "Metrics not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<GlobalBusinessMetricsDto> updateMetrics(
        @Parameter(description = "Metrics ID") @PathVariable String id,
        @Valid @RequestBody GlobalBusinessMetricsDto dto) {
        log.info("REST request to update global business metrics with ID: {}", id);
        GlobalBusinessMetricsDto result = metricsService.updateMetrics(id, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get global business metrics by ID", description = "Retrieve global business metrics by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully",
            content = @Content(schema = @Schema(implementation = GlobalBusinessMetricsDto.class))),
        @ApiResponse(responseCode = "404", description = "Metrics not found")
    })
    public ResponseEntity<GlobalBusinessMetricsDto> getMetricsById(
        @Parameter(description = "Metrics ID") @PathVariable String id) {
        log.debug("REST request to get global business metrics by ID: {}", id);
        GlobalBusinessMetricsDto result = metricsService.getMetricsById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/period/{periodId}")
    @Operation(summary = "Get global business metrics by period",
        description = "Retrieve global business metrics for a specific period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully",
            content = @Content(schema = @Schema(implementation = GlobalBusinessMetricsDto.class))),
        @ApiResponse(responseCode = "404", description = "Metrics not found for the given period")
    })
    public ResponseEntity<GlobalBusinessMetricsDto> getMetricsByPeriod(
        @Parameter(description = "Period ID") @PathVariable String periodId) {
        log.debug("REST request to get global business metrics by period: {}", periodId);
        GlobalBusinessMetricsDto result = metricsService.getMetricsByPeriod(periodId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest published global business metrics",
        description = "Retrieve the most recent published global business metrics")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Latest metrics retrieved successfully",
            content = @Content(schema = @Schema(implementation = GlobalBusinessMetricsDto.class))),
        @ApiResponse(responseCode = "404", description = "No published metrics found")
    })
    public ResponseEntity<GlobalBusinessMetricsDto> getLatestMetrics() {
        log.debug("REST request to get latest global business metrics");
        GlobalBusinessMetricsDto result = metricsService.getLatestMetrics();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(summary = "Get all global business metrics",
        description = "Retrieve all global business metrics with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully")
    })
    public ResponseEntity<org.springframework.data.domain.Page<GlobalBusinessMetricsDto>> getAllMetrics(
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Sort field") @RequestParam(defaultValue = "endDate") String sort,
        @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String direction) {

        log.debug("REST request to get all global business metrics with pagination");

        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.by(
            org.springframework.data.domain.Sort.Direction.fromString(direction), sort);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sortObj);

        org.springframework.data.domain.Page<GlobalBusinessMetricsDto> result = metricsService.getAllMetrics(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get metrics by date range", description = "Retrieve metrics within a specific date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully")
    })
    public ResponseEntity<List<GlobalBusinessMetricsDto>> getMetricsByDateRange(
        @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.debug("REST request to get metrics by date range: {} to {}", startDate, endDate);
        List<GlobalBusinessMetricsDto> result = metricsService.getMetricsByDateRange(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get metrics by status", description = "Retrieve metrics with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics retrieved successfully")
    })
    public ResponseEntity<List<GlobalBusinessMetricsDto>> getMetricsByStatus(
        @Parameter(description = "Metrics status") @PathVariable String status) {
        log.debug("REST request to get metrics by status: {}", status);
        List<GlobalBusinessMetricsDto> result = metricsService.getMetricsByStatus(status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/period/{periodId}/top-regions")
    @Operation(summary = "Get top performing regions", description = "Retrieve top performing regions for a period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Top regions retrieved successfully")
    })
    public ResponseEntity<List<GlobalBusinessMetricsDto.RegionalContributionDto>> getTopPerformingRegions(
        @Parameter(description = "Period ID") @PathVariable String periodId,
        @Parameter(description = "Maximum number of regions to return") @RequestParam(defaultValue = "5") int limit) {
        log.debug("REST request to get top {} performing regions for period: {}", limit, periodId);
        List<GlobalBusinessMetricsDto.RegionalContributionDto> result =
            metricsService.getTopPerformingRegions(periodId, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/period/{periodId}/financial-summary")
    @Operation(summary = "Get financial summary", description = "Retrieve financial summary for a period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Financial summary retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getFinancialSummary(
        @Parameter(description = "Period ID") @PathVariable String periodId) {
        log.debug("REST request to get financial summary for period: {}", periodId);
        Map<String, Object> result = metricsService.getFinancialSummary(periodId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/publish")
    @Operation(summary = "Publish metrics", description = "Publish metrics (change status to PUBLISHED)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Metrics published successfully"),
        @ApiResponse(responseCode = "404", description = "Metrics not found")
    })
    public ResponseEntity<GlobalBusinessMetricsDto> publishMetrics(
        @Parameter(description = "Metrics ID") @PathVariable String id) {
        log.info("REST request to publish metrics with ID: {}", id);
        GlobalBusinessMetricsDto result = metricsService.publishMetrics(id);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete global business metrics", description = "Delete global business metrics by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Metrics deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Metrics not found")
    })
    public ResponseEntity<Void> deleteMetrics(
        @Parameter(description = "Metrics ID") @PathVariable String id) {
        log.info("REST request to delete global business metrics with ID: {}", id);
        metricsService.deleteMetrics(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/period/{periodId}")
    @Operation(summary = "Check if metrics exist for period", description = "Check if metrics exist for a specific period")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<Boolean> existsByPeriod(
        @Parameter(description = "Period ID") @PathVariable String periodId) {
        log.debug("REST request to check if metrics exist for period: {}", periodId);
        boolean exists = metricsService.existsByPeriod(periodId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count/status/{status}")
    @Operation(summary = "Count metrics by status", description = "Count metrics with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<Long> countByStatus(
        @Parameter(description = "Metrics status") @PathVariable String status) {
        log.debug("REST request to count metrics by status: {}", status);
        long count = metricsService.countByStatus(status);
        return ResponseEntity.ok(count);
    }
}
