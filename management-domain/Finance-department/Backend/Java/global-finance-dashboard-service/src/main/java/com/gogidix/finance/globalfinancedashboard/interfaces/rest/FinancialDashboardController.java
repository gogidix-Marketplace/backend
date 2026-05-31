package com.gogidix.finance.globalfinancedashboard.interfaces.rest;

import com.gogidix.finance.globalfinancedashboard.application.service.FinancialDashboardService;
import com.gogidix.finance.globalfinancedashboard.domain.model.FinancialMetric;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Financial Dashboard Operations
 */
@RestController
@RequestMapping("/api/v1/financial-dashboard")
@Tag(name = "Financial Dashboard", description = "Financial metrics and dashboard operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class FinancialDashboardController {

    private final FinancialDashboardService dashboardService;

    @GetMapping("/metrics")
    @Operation(summary = "Get all financial metrics", description = "Returns all financial metrics for current tenant")
    public ResponseEntity<List<FinancialMetric>> getAllMetrics() {
        return ResponseEntity.ok(dashboardService.getAllMetrics());
    }

    @GetMapping("/metrics/{id}")
    @Operation(summary = "Get metric by ID", description = "Returns a single financial metric")
    public ResponseEntity<FinancialMetric> getMetric(
            @Parameter(description = "Metric ID") @PathVariable String id) {
        return ResponseEntity.ok(dashboardService.getMetric(id));
    }

    @GetMapping("/metrics/type/{metricType}")
    @Operation(summary = "Get metrics by type", description = "Returns all metrics of a specific type")
    public ResponseEntity<List<FinancialMetric>> getMetricsByType(
            @Parameter(description = "Metric type") @PathVariable FinancialMetric.MetricType metricType) {
        return ResponseEntity.ok(dashboardService.getMetricsByType(metricType));
    }

    @GetMapping("/metrics/period/{period}")
    @Operation(summary = "Get metrics by period", description = "Returns all metrics for a specific period")
    public ResponseEntity<List<FinancialMetric>> getMetricsByPeriod(
            @Parameter(description = "Period (YYYY-MM)") @PathVariable String period) {
        return ResponseEntity.ok(dashboardService.getMetricsByPeriod(period));
    }

    @PostMapping("/metrics")
    @Operation(summary = "Create new metric", description = "Creates a new financial metric")
    public ResponseEntity<FinancialMetric> createMetric(@Valid @RequestBody CreateMetricRequest request) {
        FinancialMetric metric = dashboardService.createMetric(
                request.metricType(),
                request.amount(),
                request.currency(),
                request.period()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @PutMapping("/metrics/{id}/amount")
    @Operation(summary = "Update metric amount", description = "Updates the amount of an existing metric")
    public ResponseEntity<FinancialMetric> updateMetricAmount(
            @Parameter(description = "Metric ID") @PathVariable String id,
            @Valid @RequestBody UpdateAmountRequest request) {
        return ResponseEntity.ok(dashboardService.updateMetricAmount(id, request.amount()));
    }

    @DeleteMapping("/metrics/{id}")
    @Operation(summary = "Delete metric", description = "Deletes a financial metric")
    public ResponseEntity<Void> deleteMetric(
            @Parameter(description = "Metric ID") @PathVariable String id) {
        dashboardService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Returns summary statistics for the dashboard")
    public ResponseEntity<FinancialDashboardService.DashboardSummary> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }

    public record CreateMetricRequest(
        FinancialMetric.MetricType metricType,
        BigDecimal amount,
        String currency,
        String period
    ) {}

    public record UpdateAmountRequest(
        BigDecimal amount
    ) {}
}
