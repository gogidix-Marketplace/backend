package com.gogidix.shared.warehousing.warehouse.analytics.interfaces.rest;

import com.gogidix.shared.warehousing.warehouse.analytics.application.command.CreateMetricsCommand;
import com.gogidix.shared.warehousing.warehouse.analytics.application.command.GenerateReportCommand;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.PerformanceDataDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.UtilizationReportDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.WarehouseMetricsDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.service.WarehouseAnalyticsService;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.PerformanceData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Warehouse Analytics REST Controller
 *
 * Provides multi-tenant warehouse analytics APIs
 * All endpoints require X-Tenant-ID header for tenant isolation
 */
@Tag(name = "Warehouse Analytics", description = "Warehouse metrics, utilization, and performance analytics APIs")
@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseAnalyticsController {

    private final WarehouseAnalyticsService analyticsService;

    /**
     * Create warehouse metrics
     * POST /api/v1/analytics/warehouse/metrics
     */
    @Operation(summary = "Create warehouse metrics", description = "Create new warehouse metrics entry")
    @PostMapping("/metrics")
    public ResponseEntity<WarehouseMetricsDTO> createMetrics(
            @Valid @RequestBody CreateMetricsCommand command) {
        WarehouseMetricsDTO metrics = analyticsService.createMetrics(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(metrics);
    }

    /**
     * Get metrics by ID
     * GET /api/v1/analytics/warehouse/metrics/{id}
     */
    @Operation(summary = "Get metrics by ID", description = "Retrieve warehouse metrics by ID")
    @GetMapping("/metrics/{id}")
    public ResponseEntity<WarehouseMetricsDTO> getMetrics(
            @Parameter(description = "Metrics ID") @PathVariable String id) {
        WarehouseMetricsDTO metrics = analyticsService.getMetrics(id);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get metrics by warehouse
     * GET /api/v1/analytics/warehouse/{warehouseId}/metrics
     */
    @Operation(summary = "Get warehouse metrics", description = "Retrieve all metrics for a warehouse")
    @GetMapping("/{warehouseId}/metrics")
    public ResponseEntity<List<WarehouseMetricsDTO>> getMetricsByWarehouse(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<WarehouseMetricsDTO> metrics = analyticsService.getMetricsByWarehouse(warehouseId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get latest metrics for warehouse
     * GET /api/v1/analytics/warehouse/{warehouseId}/metrics/latest
     */
    @Operation(summary = "Get latest metrics", description = "Retrieve latest metrics for a warehouse")
    @GetMapping("/{warehouseId}/metrics/latest")
    public ResponseEntity<WarehouseMetricsDTO> getLatestMetrics(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        WarehouseMetricsDTO metrics = analyticsService.getLatestMetrics(warehouseId);
        return ResponseEntity.ok(metrics);
    }

    /**
     * Get warehouse utilization
     * GET /api/v1/analytics/warehouse/{warehouseId}/utilization
     */
    @Operation(summary = "Get warehouse utilization", description = "Retrieve current utilization percentage")
    @GetMapping("/{warehouseId}/utilization")
    public ResponseEntity<Double> getWarehouseUtilization(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        Double utilization = analyticsService.getWarehouseUtilization(warehouseId);
        return ResponseEntity.ok(utilization);
    }

    /**
     * Generate utilization report
     * POST /api/v1/analytics/warehouse/reports
     */
    @Operation(summary = "Generate report", description = "Generate utilization report for a warehouse")
    @PostMapping("/reports")
    public ResponseEntity<UtilizationReportDTO> generateReport(
            @Valid @RequestBody GenerateReportCommand command) {
        UtilizationReportDTO report = analyticsService.generateReport(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /**
     * Get report by ID
     * GET /api/v1/analytics/warehouse/reports/{id}
     */
    @Operation(summary = "Get report by ID", description = "Retrieve utilization report by ID")
    @GetMapping("/reports/{id}")
    public ResponseEntity<UtilizationReportDTO> getReport(
            @Parameter(description = "Report ID") @PathVariable String id) {
        UtilizationReportDTO report = analyticsService.getReport(id);
        return ResponseEntity.ok(report);
    }

    /**
     * Get reports by warehouse
     * GET /api/v1/analytics/warehouse/{warehouseId}/reports
     */
    @Operation(summary = "Get warehouse reports", description = "Retrieve all reports for a warehouse")
    @GetMapping("/{warehouseId}/reports")
    public ResponseEntity<List<UtilizationReportDTO>> getReportsByWarehouse(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<UtilizationReportDTO> reports = analyticsService.getReportsByWarehouse(warehouseId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get performance data
     * GET /api/v1/analytics/warehouse/{warehouseId}/performance
     */
    @Operation(summary = "Get performance data", description = "Retrieve performance data for a warehouse")
    @GetMapping("/{warehouseId}/performance")
    public ResponseEntity<List<PerformanceDataDTO>> getPerformanceData(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<PerformanceDataDTO> performance = analyticsService.getPerformanceData(warehouseId);
        return ResponseEntity.ok(performance);
    }

    /**
     * Get latest performance data
     * GET /api/v1/analytics/warehouse/{warehouseId}/performance/latest
     */
    @Operation(summary = "Get latest performance", description = "Retrieve latest performance data")
    @GetMapping("/{warehouseId}/performance/latest")
    public ResponseEntity<PerformanceDataDTO> getLatestPerformanceData(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        PerformanceDataDTO performance = analyticsService.getLatestPerformanceData(warehouseId);
        return ResponseEntity.ok(performance);
    }

    /**
     * Create performance data
     * POST /api/v1/analytics/warehouse/performance
     */
    @Operation(summary = "Create performance data", description = "Create new performance data entry")
    @PostMapping("/performance")
    public ResponseEntity<PerformanceDataDTO> createPerformanceData(
            @RequestBody PerformanceData performanceData) {
        PerformanceDataDTO performance = analyticsService.createPerformanceData(performanceData);
        return ResponseEntity.status(HttpStatus.CREATED).body(performance);
    }
}
