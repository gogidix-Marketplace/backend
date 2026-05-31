package com.gogidix.sales.analytics.interfaces.rest;

import com.gogidix.sales.analytics.application.service.MetricsCommandService;
import com.gogidix.sales.analytics.application.service.MetricsQueryService;
import com.gogidix.sales.analytics.domain.model.*;
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
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Sales Metrics Operations
 */
@RestController
@RequestMapping("/metrics")
@Tag(name = "Sales Metrics", description = "Sales performance and analytics metrics operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class MetricsController {

    private final MetricsCommandService commandService;
    private final MetricsQueryService queryService;

    // ========== Basic Metric Operations ==========

    @PostMapping
    @Operation(summary = "Create metric", description = "Creates a new sales metric")
    public ResponseEntity<Metric> createMetric(@Valid @RequestBody CreateMetricRequest request) {
        Metric metric = commandService.createMetric(
                request.entityType(),
                request.entityId(),
                request.metricType(),
                request.value(),
                request.period(),
                request.periodStart(),
                request.periodEnd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @GetMapping("/{metricId}")
    @Operation(summary = "Get metric by ID", description = "Returns a single metric")
    public ResponseEntity<Metric> getMetric(
            @Parameter(description = "Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(queryService.getMetric(metricId));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Get metrics by entity", description = "Returns all metrics for a specific entity")
    public ResponseEntity<List<Metric>> getMetricsByEntity(
            @Parameter(description = "Entity type") @PathVariable String entityType,
            @Parameter(description = "Entity ID") @PathVariable String entityId) {
        return ResponseEntity.ok(queryService.getMetricsByEntity(entityType, entityId));
    }

    @GetMapping("/type/{metricType}")
    @Operation(summary = "Get metrics by type", description = "Returns all metrics of a specific type")
    public ResponseEntity<List<Metric>> getMetricsByType(
            @Parameter(description = "Metric type") @PathVariable Metric.MetricType metricType) {
        return ResponseEntity.ok(queryService.getMetricsByType(metricType));
    }

    @PutMapping("/{metricId}/value")
    @Operation(summary = "Update metric value", description = "Updates the value of an existing metric")
    public ResponseEntity<Metric> updateMetricValue(
            @Parameter(description = "Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdateValueRequest request) {
        return ResponseEntity.ok(commandService.updateMetricValue(metricId, request.value()));
    }

    @DeleteMapping("/{metricId}")
    @Operation(summary = "Delete metric", description = "Deletes a sales metric")
    public ResponseEntity<Void> deleteMetric(
            @Parameter(description = "Metric ID") @PathVariable String metricId) {
        commandService.deleteMetric(metricId);
        return ResponseEntity.noContent().build();
    }

    // ========== Performance Metric Operations ==========

    @PostMapping("/performance")
    @Operation(summary = "Create performance metric", description = "Creates a new performance metric")
    public ResponseEntity<PerformanceMetric> createPerformanceMetric(
            @Valid @RequestBody CreatePerformanceMetricRequest request) {
        PerformanceMetric metric = commandService.createPerformanceMetric(
                request.entityType(),
                request.entityId(),
                request.entityName(),
                request.period(),
                request.periodStart(),
                request.periodEnd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @GetMapping("/performance/{metricId}")
    @Operation(summary = "Get performance metric", description = "Returns a specific performance metric")
    public ResponseEntity<PerformanceMetric> getPerformanceMetric(
            @Parameter(description = "Performance Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(queryService.getPerformanceMetric(metricId));
    }

    @GetMapping("/performance/entity/{entityType}/{entityId}")
    @Operation(summary = "Get performance metrics by entity", description = "Returns performance metrics for an entity")
    public ResponseEntity<List<PerformanceMetric>> getPerformanceMetricsByEntity(
            @Parameter(description = "Entity type") @PathVariable String entityType,
            @Parameter(description = "Entity ID") @PathVariable String entityId) {
        return ResponseEntity.ok(queryService.getPerformanceMetricsByEntity(entityType, entityId));
    }

    @GetMapping("/performance/top/{entityType}")
    @Operation(summary = "Get top performers", description = "Returns top performing entities")
    public ResponseEntity<List<PerformanceMetric>> getTopPerformers(
            @Parameter(description = "Entity type") @PathVariable String entityType,
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(queryService.getTopPerformers(entityType, limit));
    }

    @GetMapping("/performance/rankings/{entityType}")
    @Operation(summary = "Get performance rankings", description = "Returns rankings for entity type")
    public ResponseEntity<MetricsQueryService.PerformanceRankings> getRankings(
            @Parameter(description = "Entity type") @PathVariable String entityType) {
        return ResponseEntity.ok(queryService.getRankings(entityType));
    }

    @PutMapping("/performance/{metricId}/revenue")
    @Operation(summary = "Update performance revenue", description = "Updates revenue metrics")
    public ResponseEntity<PerformanceMetric> updatePerformanceRevenue(
            @Parameter(description = "Performance Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdateRevenueRequest request) {
        return ResponseEntity.ok(commandService.updatePerformanceRevenue(
                metricId, request.totalRevenue(), request.targetRevenue()));
    }

    @PutMapping("/performance/{metricId}/deals")
    @Operation(summary = "Update performance deals", description = "Updates deal metrics")
    public ResponseEntity<PerformanceMetric> updatePerformanceDeals(
            @Parameter(description = "Performance Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdateDealsRequest request) {
        return ResponseEntity.ok(commandService.updatePerformanceDeals(
                metricId, request.dealsWon(), request.dealsLost()));
    }

    // ========== Sales Cycle Metric Operations ==========

    @PostMapping("/sales-cycle")
    @Operation(summary = "Create sales cycle metric", description = "Creates a new sales cycle metric")
    public ResponseEntity<SalesCycleMetric> createSalesCycleMetric(
            @Valid @RequestBody CreateSalesCycleMetricRequest request) {
        SalesCycleMetric metric = commandService.createSalesCycleMetric(
                request.entityType(),
                request.entityId(),
                request.entityName(),
                request.period(),
                request.periodStart(),
                request.periodEnd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @GetMapping("/sales-cycle/{metricId}")
    @Operation(summary = "Get sales cycle metric", description = "Returns a specific sales cycle metric")
    public ResponseEntity<SalesCycleMetric> getSalesCycleMetric(
            @Parameter(description = "Sales Cycle Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(queryService.getSalesCycleMetric(metricId));
    }

    @GetMapping("/sales-cycle/summary")
    @Operation(summary = "Get sales cycle summary", description = "Returns summary of sales cycle metrics")
    public ResponseEntity<MetricsQueryService.SalesCycleSummary> getSalesCycleSummary() {
        return ResponseEntity.ok(queryService.getSalesCycleSummary());
    }

    @PutMapping("/sales-cycle/{metricId}/duration")
    @Operation(summary = "Update sales cycle duration", description = "Updates cycle duration metrics")
    public ResponseEntity<SalesCycleMetric> updateSalesCycleDuration(
            @Parameter(description = "Sales Cycle Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdateCycleDurationRequest request) {
        return ResponseEntity.ok(commandService.updateSalesCycleDuration(
                metricId, request.average(), request.median(),
                request.shortest(), request.longest()));
    }

    // ========== Pipeline Metric Operations ==========

    @PostMapping("/pipeline")
    @Operation(summary = "Create pipeline metric", description = "Creates a new pipeline metric")
    public ResponseEntity<PipelineMetric> createPipelineMetric(
            @Valid @RequestBody CreatePipelineMetricRequest request) {
        PipelineMetric metric = commandService.createPipelineMetric(
                request.entityType(),
                request.entityId(),
                request.entityName(),
                request.period(),
                request.periodStart(),
                request.periodEnd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @GetMapping("/pipeline/{metricId}")
    @Operation(summary = "Get pipeline metric", description = "Returns a specific pipeline metric")
    public ResponseEntity<PipelineMetric> getPipelineMetric(
            @Parameter(description = "Pipeline Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(queryService.getPipelineMetric(metricId));
    }

    @GetMapping("/pipeline/at-risk")
    @Operation(summary = "Get at-risk pipelines", description = "Returns pipelines that need attention")
    public ResponseEntity<List<PipelineMetric>> getAtRiskPipelines() {
        return ResponseEntity.ok(queryService.getAtRiskPipelines());
    }

    @GetMapping("/pipeline/health-summary")
    @Operation(summary = "Get pipeline health summary", description = "Returns summary of pipeline health")
    public ResponseEntity<MetricsQueryService.PipelineHealthSummary> getPipelineHealthSummary() {
        return ResponseEntity.ok(queryService.getPipelineHealthSummary());
    }

    @PutMapping("/pipeline/{metricId}/value")
    @Operation(summary = "Update pipeline value", description = "Updates pipeline value metrics")
    public ResponseEntity<PipelineMetric> updatePipelineValue(
            @Parameter(description = "Pipeline Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdatePipelineValueRequest request) {
        return ResponseEntity.ok(commandService.updatePipelineValue(
                metricId, request.totalValue(), request.openValue(),
                request.wonValue(), request.lostValue(), request.stagnantValue()));
    }

    @PutMapping("/pipeline/{metricId}/health")
    @Operation(summary = "Update pipeline health", description = "Recalculates pipeline health score")
    public ResponseEntity<PipelineMetric> updatePipelineHealth(
            @Parameter(description = "Pipeline Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdateHealthRequest request) {
        return ResponseEntity.ok(commandService.updatePipelineHealth(metricId, request.targetCoverage()));
    }

    // ========== Win/Loss Metric Operations ==========

    @PostMapping("/win-loss")
    @Operation(summary = "Create win/loss metric", description = "Creates a new win/loss metric")
    public ResponseEntity<WinLossMetric> createWinLossMetric(
            @Valid @RequestBody CreateWinLossMetricRequest request) {
        WinLossMetric metric = commandService.createWinLossMetric(
                request.entityType(),
                request.entityId(),
                request.entityName(),
                request.period(),
                request.periodStart(),
                request.periodEnd()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(metric);
    }

    @GetMapping("/win-loss/{metricId}")
    @Operation(summary = "Get win/loss metric", description = "Returns a specific win/loss metric")
    public ResponseEntity<WinLossMetric> getWinLossMetric(
            @Parameter(description = "Win/Loss Metric ID") @PathVariable String metricId) {
        return ResponseEntity.ok(queryService.getWinLossMetric(metricId));
    }

    @GetMapping("/win-loss/lowest/{entityType}")
    @Operation(summary = "Get lowest performers", description = "Returns entities with lowest win rates")
    public ResponseEntity<List<WinLossMetric>> getLowestPerformers(
            @Parameter(description = "Entity type") @PathVariable String entityType,
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(queryService.getLowestPerformers(entityType, limit));
    }

    @GetMapping("/win-loss/highest/{entityType}")
    @Operation(summary = "Get highest performers", description = "Returns entities with highest win rates")
    public ResponseEntity<List<WinLossMetric>> getHighestPerformers(
            @Parameter(description = "Entity type") @PathVariable String entityType,
            @Parameter(description = "Limit") @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(queryService.getHighestPerformers(entityType, limit));
    }

    @GetMapping("/win-loss/summary")
    @Operation(summary = "Get win/loss analysis summary", description = "Returns summary of win/loss analysis")
    public ResponseEntity<MetricsQueryService.WinLossAnalysisSummary> getWinLossAnalysisSummary() {
        return ResponseEntity.ok(queryService.getWinLossAnalysisSummary());
    }

    @PutMapping("/win-loss/{metricId}/counts")
    @Operation(summary = "Update win/loss counts", description = "Updates deal counts")
    public ResponseEntity<WinLossMetric> updateWinLossCounts(
            @Parameter(description = "Win/Loss Metric ID") @PathVariable String metricId,
            @Valid @RequestBody UpdateWinLossCountsRequest request) {
        return ResponseEntity.ok(commandService.updateWinLossCounts(
                metricId, request.dealsWon(), request.dealsLost(), request.inProgress()));
    }

    @PostMapping("/win-loss/{metricId}/loss-reason")
    @Operation(summary = "Add loss reason", description = "Adds a loss reason to the metric")
    public ResponseEntity<WinLossMetric> addLossReason(
            @Parameter(description = "Win/Loss Metric ID") @PathVariable String metricId,
            @Valid @RequestBody AddLossReasonRequest request) {
        return ResponseEntity.ok(commandService.addLossReason(
                metricId, request.reason(), request.count(), request.value()));
    }

    // ========== Summary Operations ==========

    @GetMapping("/dashboard/summary")
    @Operation(summary = "Get dashboard summary", description = "Returns summary statistics for the dashboard")
    public ResponseEntity<MetricsQueryService.DashboardSummary> getDashboardSummary() {
        return ResponseEntity.ok(queryService.getDashboardSummary());
    }

    // ========== Request/Response Records ==========

    public record CreateMetricRequest(
            String entityType,
            String entityId,
            Metric.MetricType metricType,
            BigDecimal value,
            String period,
            Instant periodStart,
            Instant periodEnd
    ) {}

    public record UpdateValueRequest(
            BigDecimal value
    ) {}

    public record CreatePerformanceMetricRequest(
            String entityType,
            String entityId,
            String entityName,
            PerformanceMetric.PerformancePeriod period,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {}

    public record UpdateRevenueRequest(
            BigDecimal totalRevenue,
            BigDecimal targetRevenue
    ) {}

    public record UpdateDealsRequest(
            Integer dealsWon,
            Integer dealsLost
    ) {}

    public record CreateSalesCycleMetricRequest(
            String entityType,
            String entityId,
            String entityName,
            SalesCycleMetric.MetricPeriod period,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {}

    public record UpdateCycleDurationRequest(
            BigDecimal average,
            BigDecimal median,
            BigDecimal shortest,
            BigDecimal longest
    ) {}

    public record CreatePipelineMetricRequest(
            String entityType,
            String entityId,
            String entityName,
            PipelineMetric.MetricPeriod period,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {}

    public record UpdatePipelineValueRequest(
            BigDecimal totalValue,
            BigDecimal openValue,
            BigDecimal wonValue,
            BigDecimal lostValue,
            BigDecimal stagnantValue
    ) {}

    public record UpdateHealthRequest(
            Integer targetCoverage
    ) {}

    public record CreateWinLossMetricRequest(
            String entityType,
            String entityId,
            String entityName,
            WinLossMetric.MetricPeriod period,
            LocalDate periodStart,
            LocalDate periodEnd
    ) {}

    public record UpdateWinLossCountsRequest(
            Integer dealsWon,
            Integer dealsLost,
            Integer inProgress
    ) {}

    public record AddLossReasonRequest(
            String reason,
            Integer count,
            BigDecimal value
    ) {}
}
