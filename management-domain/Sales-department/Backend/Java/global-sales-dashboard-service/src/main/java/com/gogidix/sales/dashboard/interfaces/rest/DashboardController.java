package com.gogidix.sales.dashboard.interfaces.rest;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
import com.gogidix.sales.dashboard.application.service.DashboardCommandService;
import com.gogidix.sales.dashboard.application.service.DashboardQueryService;
import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
import com.gogidix.sales.dashboard.domain.model.KPIWidget;
import com.gogidix.sales.dashboard.domain.port.in.DashboardCommand;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Global Sales Dashboard Operations
 * Provides endpoints for dashboard management, queries, and metrics
 */
@RestController
@RequestMapping("/dashboards")
@Tag(name = "Sales Dashboard", description = "Global sales dashboard and metrics operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class DashboardController {

    private final DashboardCommandService commandService;
    private final DashboardQueryService queryService;

    @GetMapping
    @Operation(summary = "Get all dashboards", description = "Returns paginated list of dashboards for current tenant")
    public ResponseEntity<Page<DashboardResponseDto>> getAllDashboards(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        log.info("Getting all dashboards for page: {}, size: {}", page, size);
        return ResponseEntity.ok(queryService.getAllDashboardsDto(page, size));
    }

    @GetMapping("/{dashboardId}")
    @Operation(summary = "Get dashboard by ID", description = "Returns a single dashboard with all details")
    public ResponseEntity<DashboardResponseDto> getDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getDashboardDto(dashboardId));
    }

    @GetMapping("/{dashboardId}/summary")
    @Operation(summary = "Get dashboard summary", description = "Returns a summary view of the dashboard")
    public ResponseEntity<GlobalSalesDashboard> getDashboardSummary(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting dashboard summary: {}", dashboardId);
        return ResponseEntity.ok(queryService.getDashboardSummary(dashboardId));
    }

    @GetMapping("/{dashboardId}/metrics")
    @Operation(summary = "Get global metrics", description = "Returns global metrics for a dashboard")
    public ResponseEntity<GlobalSalesDashboard.GlobalMetrics> getGlobalMetrics(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Target currency") @RequestParam(required = false) String currency) {
        log.info("Getting global metrics for dashboard: {} with currency: {}", dashboardId, currency);
        return ResponseEntity.ok(queryService.getGlobalMetrics(dashboardId, currency));
    }

    @GetMapping("/{dashboardId}/regions")
    @Operation(summary = "Get regional metrics", description = "Returns regional breakdown metrics")
    public ResponseEntity<List<GlobalSalesDashboard.RegionalMetric>> getRegionalMetrics(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Region code filter") @RequestParam(required = false) String regionCode,
            @Parameter(description = "Base currency") @RequestParam(required = false) String baseCurrency) {
        log.info("Getting regional metrics for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getRegionalMetrics(dashboardId, regionCode, baseCurrency));
    }

    @GetMapping("/{dashboardId}/regions/top")
    @Operation(summary = "Get top performing regions", description = "Returns top regions by specified metric")
    public ResponseEntity<List<GlobalSalesDashboard.RegionalMetric>> getTopPerformingRegions(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Limit results") @RequestParam(defaultValue = "5") Integer limit,
            @Parameter(description = "Sort by (REVENUE, GROWTH_RATE, ACHIEVEMENT)") @RequestParam(defaultValue = "REVENUE") String sortBy) {
        log.info("Getting top performing regions for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getTopPerformingRegions(dashboardId, limit, sortBy));
    }

    @GetMapping("/{dashboardId}/trends")
    @Operation(summary = "Get trend data", description = "Returns historical trend data")
    public ResponseEntity<List<GlobalSalesDashboard.TimeSeriesData>> getTrendData(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Start date") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam(required = false) LocalDate endDate,
            @Parameter(description = "Region filter") @RequestParam(required = false) String region) {
        log.info("Getting trend data for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getTrendData(dashboardId, startDate, endDate, region));
    }

    @GetMapping("/{dashboardId}/summary/executive")
    @Operation(summary = "Get executive summary", description = "Returns executive-level summary")
    public ResponseEntity<GlobalSalesDashboard.ExecutiveSummary> getExecutiveSummary(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting executive summary for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getExecutiveSummary(dashboardId));
    }

    @GetMapping("/{dashboardId}/widgets")
    @Operation(summary = "Get dashboard widgets", description = "Returns all widgets for a dashboard")
    public ResponseEntity<List<GlobalSalesDashboard.KPIWidget>> getWidgets(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting widgets for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getWidgets(dashboardId));
    }

    @PostMapping
    @Operation(summary = "Create dashboard", description = "Creates a new dashboard")
    public ResponseEntity<GlobalSalesDashboard> createDashboard(
            @Valid @RequestBody CreateDashboardRequest request) {
        log.info("Creating dashboard: {} for tenant: {}", request.name(), RequestContextHolder.getTenantId());
        GlobalSalesDashboard dashboard = commandService.create(toCreateCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(dashboard);
    }

    @PutMapping("/{dashboardId}")
    @Operation(summary = "Update dashboard", description = "Updates dashboard details")
    public ResponseEntity<GlobalSalesDashboard> updateDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody UpdateDashboardRequest request) {
        log.info("Updating dashboard: {}", dashboardId);
        GlobalSalesDashboard dashboard = commandService.update(toUpdateCommand(dashboardId, request));
        return ResponseEntity.ok(dashboard);
    }

    @PutMapping("/{dashboardId}/metrics")
    @Operation(summary = "Update global metrics", description = "Updates global metrics for a dashboard")
    public ResponseEntity<Void> updateGlobalMetrics(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody UpdateGlobalMetricsRequest request) {
        log.info("Updating global metrics for dashboard: {}", dashboardId);
        commandService.updateGlobalMetrics(toUpdateMetricsCommand(dashboardId, request));
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{dashboardId}/regions/{regionCode}")
    @Operation(summary = "Update regional metric", description = "Updates a specific region's metrics")
    public ResponseEntity<Void> updateRegionalMetric(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Region code") @PathVariable String regionCode,
            @Valid @RequestBody UpdateRegionalMetricRequest request) {
        log.info("Updating regional metric for region: {} in dashboard: {}", regionCode, dashboardId);
        commandService.updateRegionalMetric(toUpdateRegionalCommand(dashboardId, regionCode, request));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/widgets")
    @Operation(summary = "Add widget", description = "Adds a new widget to the dashboard")
    public ResponseEntity<Void> addWidget(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody AddWidgetRequest request) {
        log.info("Adding widget to dashboard: {}", dashboardId);
        commandService.addWidget(toAddWidgetCommand(dashboardId, request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{dashboardId}/refresh")
    @Operation(summary = "Refresh dashboard", description = "Triggers a dashboard refresh")
    public ResponseEntity<Void> refreshDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @RequestBody(required = false) RefreshDashboardRequest request) {
        String userId = request != null ? request.userId() : RequestContextHolder.getTenantId();
        log.info("Refreshing dashboard: {} by user: {}", dashboardId, userId);
        commandService.refreshDashboard(toRefreshCommand(dashboardId, userId, request));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/publish")
    @Operation(summary = "Publish dashboard", description = "Publishes a draft dashboard")
    public ResponseEntity<Void> publishDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Publishing dashboard: {}", dashboardId);
        commandService.publishDashboard(toPublishCommand(dashboardId));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/archive")
    @Operation(summary = "Archive dashboard", description = "Archives a dashboard")
    public ResponseEntity<Void> archiveDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Archiving dashboard: {}", dashboardId);
        commandService.archiveDashboard(toArchiveCommand(dashboardId));
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/trends")
    @Operation(summary = "Add trend data", description = "Adds historical trend data point")
    public ResponseEntity<Void> addTrendData(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody AddTrendDataRequest request) {
        log.info("Adding trend data to dashboard: {}", dashboardId);
        commandService.addTrendData(toAddTrendDataCommand(dashboardId, request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{dashboardId}/exchange-rates")
    @Operation(summary = "Update exchange rates", description = "Updates currency exchange rates")
    public ResponseEntity<Void> updateExchangeRates(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @RequestBody Map<String, BigDecimal> exchangeRates) {
        log.info("Updating exchange rates for dashboard: {}", dashboardId);
        commandService.updateExchangeRates(toUpdateExchangeRatesCommand(dashboardId, exchangeRates));
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{dashboardId}")
    @Operation(summary = "Delete dashboard", description = "Deletes a dashboard")
    public ResponseEntity<Void> deleteDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Deleting dashboard: {}", dashboardId);
        commandService.delete(toDeleteCommand(dashboardId));
        return ResponseEntity.noContent().build();
    }

    // Request DTOs

    public record CreateDashboardRequest(
            String name,
            String description,
            GlobalSalesDashboard.DashboardType type,
            String baseCurrency,
            List<String> enabledRegions,
            Integer refreshIntervalMinutes
    ) {}

    public record UpdateDashboardRequest(
            String name,
            String description,
            GlobalSalesDashboard.DashboardStatus status
    ) {}

    public record UpdateGlobalMetricsRequest(
            BigDecimal totalRevenue,
            BigDecimal targetRevenue,
            Integer totalDeals,
            Integer wonDeals,
            BigDecimal averageDealSize,
            BigDecimal weightedPipeline,
            Integer opportunitiesInPipeline,
            BigDecimal yearOverYearGrowth,
            BigDecimal monthOverMonthGrowth
    ) {}

    public record UpdateRegionalMetricRequest(
            String regionName,
            BigDecimal revenue,
            BigDecimal target,
            Integer deals,
            BigDecimal growthRate,
            String currency
    ) {}

    public record AddWidgetRequest(
            String title,
            String description,
            KPIWidget.WidgetType widgetType,
            KPIWidget.WidgetCategory category,
            Object initialValue,
            Integer row,
            Integer column
    ) {}

    public record RefreshDashboardRequest(
            String userId,
            Boolean forceRefresh
    ) {}

    public record AddTrendDataRequest(
            String period,
            LocalDate date,
            BigDecimal revenue,
            Integer deals,
            BigDecimal conversionRate,
            String region
    ) {}

    // Helper methods to convert to command objects

    private DashboardCommand.CreateDashboardCommand toCreateCommand(CreateDashboardRequest request) {
        return new DashboardCommand.CreateDashboardCommand(
                RequestContextHolder.getTenantId(),
                request.name(),
                request.description(),
                request.type(),
                request.baseCurrency(),
                request.enabledRegions(),
                request.refreshIntervalMinutes(),
                RequestContextHolder.getTenantId()
        );
    }

    private DashboardCommand.UpdateDashboardCommand toUpdateCommand(String dashboardId, UpdateDashboardRequest request) {
        return new DashboardCommand.UpdateDashboardCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                request.name(),
                request.description(),
                request.status()
        );
    }

    private DashboardCommand.UpdateGlobalMetricsCommand toUpdateMetricsCommand(String dashboardId, UpdateGlobalMetricsRequest request) {
        return new DashboardCommand.UpdateGlobalMetricsCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                request.totalRevenue(),
                request.targetRevenue(),
                request.totalDeals(),
                request.wonDeals(),
                request.averageDealSize(),
                request.weightedPipeline(),
                request.opportunitiesInPipeline(),
                request.yearOverYearGrowth(),
                request.monthOverMonthGrowth()
        );
    }

    private DashboardCommand.UpdateRegionalMetricCommand toUpdateRegionalCommand(String dashboardId, String regionCode, UpdateRegionalMetricRequest request) {
        return new DashboardCommand.UpdateRegionalMetricCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                regionCode,
                request.regionName(),
                request.revenue(),
                request.currency() != null ? request.currency() : "USD",
                request.target(),
                request.deals(),
                request.growthRate()
        );
    }

    private DashboardCommand.AddWidgetCommand toAddWidgetCommand(String dashboardId, AddWidgetRequest request) {
        return new DashboardCommand.AddWidgetCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                request.title(),
                request.widgetType(),
                request.category(),
                request.description(),
                request.initialValue(),
                request.row(),
                request.column()
        );
    }

    private DashboardCommand.RefreshDashboardCommand toRefreshCommand(String dashboardId, String userId, RefreshDashboardRequest request) {
        return new DashboardCommand.RefreshDashboardCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                userId,
                request != null ? request.forceRefresh() : false
        );
    }

    private DashboardCommand.PublishDashboardCommand toPublishCommand(String dashboardId) {
        return new DashboardCommand.PublishDashboardCommand(
                RequestContextHolder.getTenantId(),
                dashboardId
        );
    }

    private DashboardCommand.ArchiveDashboardCommand toArchiveCommand(String dashboardId) {
        return new DashboardCommand.ArchiveDashboardCommand(
                RequestContextHolder.getTenantId(),
                dashboardId
        );
    }

    private DashboardCommand.AddTrendDataCommand toAddTrendDataCommand(String dashboardId, AddTrendDataRequest request) {
        return new DashboardCommand.AddTrendDataCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                request.period(),
                request.date(),
                request.revenue(),
                request.deals(),
                request.conversionRate(),
                request.region()
        );
    }

    private DashboardCommand.UpdateExchangeRatesCommand toUpdateExchangeRatesCommand(String dashboardId, Map<String, BigDecimal> rates) {
        return new DashboardCommand.UpdateExchangeRatesCommand(
                RequestContextHolder.getTenantId(),
                dashboardId,
                rates
        );
    }

    private DashboardCommand.DeleteDashboardCommand toDeleteCommand(String dashboardId) {
        return new DashboardCommand.DeleteDashboardCommand(
                RequestContextHolder.getTenantId(),
                dashboardId
        );
    }
}
