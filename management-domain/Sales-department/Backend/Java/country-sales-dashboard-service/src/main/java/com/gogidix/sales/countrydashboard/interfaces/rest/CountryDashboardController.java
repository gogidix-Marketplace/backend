package com.gogidix.sales.countrydashboard.interfaces.rest;

import com.gogidix.sales.countrydashboard.application.dto.response.*;
import com.gogidix.sales.countrydashboard.application.mapper.CountryDashboardMapper;
import com.gogidix.sales.countrydashboard.application.service.CountryDashboardCommandService;
import com.gogidix.sales.countrydashboard.application.service.CountryDashboardQueryService;
import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.port.in.CountryDashboardCommand;
import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContextHolder;
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
 * REST Controller for Country Sales Dashboard Operations
 * Provides endpoints for country-specific dashboard management
 */
@RestController
@RequestMapping("/country-dashboards")
@Tag(name = "Country Sales Dashboard", description = "Country-specific sales dashboard and metrics operations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class CountryDashboardController {

    private final CountryDashboardCommandService commandService;
    private final CountryDashboardQueryService queryService;
    private final CountryDashboardMapper mapper;

    @GetMapping
    @Operation(summary = "Get all country dashboards", description = "Returns paginated list of country dashboards for current tenant")
    public ResponseEntity<Page<CountryDashboardResponseDto>> getAllDashboards(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        log.info("Getting all country dashboards for page: {}, size: {}", page, size);
        return ResponseEntity.ok(queryService.getAllDashboardsDto(RequestContextHolder.getTenantId(),
                org.springframework.data.domain.PageRequest.of(page, size)));
    }

    @GetMapping("/{dashboardId}")
    @Operation(summary = "Get dashboard by ID", description = "Returns a single country dashboard with all details")
    public ResponseEntity<CountryDashboardResponseDto> getDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting country dashboard: {}", dashboardId);
        CountrySalesDashboard dashboard = queryService.getDashboard(dashboardId, RequestContextHolder.getTenantId());
        return ResponseEntity.ok(mapper.toResponseDto(dashboard));
    }

    @GetMapping("/country/{countryCode}")
    @Operation(summary = "Get dashboard by country code", description = "Returns the dashboard for a specific country")
    public ResponseEntity<CountryDashboardResponseDto> getDashboardByCountry(
            @Parameter(description = "Country code (ISO 3166-1 alpha-2)") @PathVariable String countryCode) {
        log.info("Getting country dashboard for country: {}", countryCode);
        CountrySalesDashboard dashboard = queryService.getDashboardByCountry(
                countryCode.toUpperCase(), RequestContextHolder.getTenantId());
        return ResponseEntity.ok(mapper.toResponseDto(dashboard));
    }

    @GetMapping("/{dashboardId}/metrics")
    @Operation(summary = "Get country metrics", description = "Returns country-level metrics for a dashboard")
    public ResponseEntity<CountrySalesDashboard.CountryMetrics> getMetrics(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting metrics for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getMetrics(dashboardId));
    }

    @GetMapping("/{dashboardId}/territories")
    @Operation(summary = "Get territories", description = "Returns territory breakdown for a country")
    public ResponseEntity<List<TerritoryBreakdownResponseDto>> getTerritories(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting territories for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getTerritories(dashboardId));
    }

    @GetMapping("/{dashboardId}/territories/top")
    @Operation(summary = "Get top performing territories", description = "Returns top territories by performance")
    public ResponseEntity<List<TerritoryBreakdownResponseDto>> getTopTerritories(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Limit results") @RequestParam(defaultValue = "5") Integer limit) {
        log.info("Getting top {} territories for country dashboard: {}", limit, dashboardId);
        return ResponseEntity.ok(queryService.getTopTerritories(dashboardId, limit));
    }

    @GetMapping("/{dashboardId}/territories/{territoryId}")
    @Operation(summary = "Get territory details", description = "Returns details for a specific territory")
    public ResponseEntity<TerritoryBreakdownResponseDto> getTerritory(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Territory ID") @PathVariable String territoryId) {
        log.info("Getting territory: {} for country dashboard: {}", territoryId, dashboardId);
        return ResponseEntity.ok(queryService.getTerritory(dashboardId, territoryId));
    }

    @GetMapping("/{dashboardId}/comparison")
    @Operation(summary = "Get comparison data", description = "Returns YoY, MoM, QoQ comparison data")
    public ResponseEntity<ComparisonDataResponseDto> getComparisonData(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting comparison data for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getComparisonData(dashboardId));
    }

    @GetMapping("/{dashboardId}/quota")
    @Operation(summary = "Get quota information", description = "Returns quota and achievement information")
    public ResponseEntity<CountrySalesDashboard.QuotaInfo> getQuotaInfo(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting quota info for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getQuotaInfo(dashboardId));
    }

    @GetMapping("/{dashboardId}/trends")
    @Operation(summary = "Get trend data", description = "Returns historical trend data")
    public ResponseEntity<List<CountrySalesDashboard.TrendDataPoint>> getTrendData(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Start date") @RequestParam(required = false) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam(required = false) LocalDate endDate) {
        log.info("Getting trend data for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getTrendData(dashboardId, startDate, endDate));
    }

    @GetMapping("/{dashboardId}/kpis")
    @Operation(summary = "Get KPIs", description = "Returns all KPIs for a country")
    public ResponseEntity<List<KpiResponseDto>> getKPIs(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting KPIs for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getKPIs(dashboardId));
    }

    @GetMapping("/{dashboardId}/kpis/type/{metricType}")
    @Operation(summary = "Get KPIs by type", description = "Returns KPIs filtered by metric type")
    public ResponseEntity<List<KpiResponseDto>> getKPIsByType(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Metric type") @PathVariable String metricType) {
        log.info("Getting KPIs of type {} for country dashboard: {}", metricType, dashboardId);
        return ResponseEntity.ok(queryService.getKPIsByType(dashboardId, metricType));
    }

    @GetMapping("/{dashboardId}/summary")
    @Operation(summary = "Get executive summary", description = "Returns executive-level summary")
    public ResponseEntity<CountryDashboardResponseDto.ExecutiveSummaryDto> getExecutiveSummary(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting executive summary for country dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getExecutiveSummary(dashboardId));
    }

    @GetMapping("/{dashboardId}/snapshot")
    @Operation(summary = "Get dashboard snapshot", description = "Returns a quick snapshot of key metrics")
    public ResponseEntity<Map<String, Object>> getDashboardSnapshot(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting dashboard snapshot for: {}", dashboardId);
        return ResponseEntity.ok(queryService.getDashboardSnapshot(dashboardId));
    }

    @GetMapping("/{dashboardId}/underperforming")
    @Operation(summary = "Get underperforming territories", description = "Returns list of underperforming territories")
    public ResponseEntity<List<String>> getUnderperformingTerritories(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting underperforming territories for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getUnderperformingTerritories(dashboardId));
    }

    @GetMapping("/{dashboardId}/overperforming")
    @Operation(summary = "Get overperforming territories", description = "Returns list of overperforming territories")
    public ResponseEntity<List<String>> getOverperformingTerritories(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Getting overperforming territories for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.getOverperformingTerritories(dashboardId));
    }

    @GetMapping("/{dashboardId}/on-track")
    @Operation(summary = "Check if quota is on track", description = "Returns whether quota achievement is on track")
    public ResponseEntity<Boolean> isQuotaOnTrack(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Checking if quota is on track for dashboard: {}", dashboardId);
        return ResponseEntity.ok(queryService.isQuotaOnTrack(dashboardId));
    }

    @PostMapping
    @Operation(summary = "Create country dashboard", description = "Creates a new country-specific sales dashboard")
    public ResponseEntity<CountrySalesDashboard> createDashboard(
            @Valid @RequestBody CreateDashboardRequest request) {
        log.info("Creating country dashboard for country: {}", request.getCountryCode());

        CountryDashboardCommand.CreateDashboardCommand command =
                new CountryDashboardCommand.CreateDashboardCommand(
                        RequestContextHolder.getTenantId(),
                        request.getCountryCode(),
                        request.getCountryName(),
                        request.getType(),
                        request.getLocalCurrency(),
                        request.getEnabledTerritories(),
                        request.getRefreshIntervalMinutes(),
                        RequestContextHolder.getUserId()
                );

        CountrySalesDashboard dashboard = commandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dashboard);
    }

    @PutMapping("/{dashboardId}")
    @Operation(summary = "Update dashboard", description = "Updates dashboard details")
    public ResponseEntity<CountrySalesDashboard> updateDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody UpdateDashboardRequest request) {
        log.info("Updating country dashboard: {}", dashboardId);

        CountryDashboardCommand.UpdateDashboardCommand command =
                new CountryDashboardCommand.UpdateDashboardCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        request.getCountryName(),
                        null,
                        request.getStatus()
                );

        CountrySalesDashboard dashboard = commandService.update(command);
        return ResponseEntity.ok(dashboard);
    }

    @PutMapping("/{dashboardId}/metrics")
    @Operation(summary = "Update country metrics", description = "Updates country-level metrics")
    public ResponseEntity<Void> updateMetrics(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody UpdateMetricsRequest request) {
        log.info("Updating metrics for country dashboard: {}", dashboardId);

        CountryDashboardCommand.UpdateCountryMetricsCommand command =
                new CountryDashboardCommand.UpdateCountryMetricsCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        request.getTotalRevenue(),
                        request.getTargetRevenue(),
                        request.getTotalDeals(),
                        request.getWonDeals(),
                        request.getLostDeals(),
                        request.getAverageDealSize(),
                        request.getWeightedPipeline(),
                        request.getOpportunitiesInPipeline(),
                        request.getNewCustomers(),
                        request.getChurnedCustomers(),
                        request.getRetentionRate(),
                        request.getNpsScore(),
                        request.getActiveSalesReps()
                );

        commandService.updateMetrics(command);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{dashboardId}/territories/{territoryId}")
    @Operation(summary = "Update territory metric", description = "Updates a specific territory's metrics")
    public ResponseEntity<Void> updateTerritoryMetric(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "Territory ID") @PathVariable String territoryId,
            @Valid @RequestBody UpdateTerritoryRequest request) {
        log.info("Updating territory metric for territory: {} in dashboard: {}", territoryId, dashboardId);

        CountryDashboardCommand.UpdateTerritoryMetricCommand command =
                new CountryDashboardCommand.UpdateTerritoryMetricCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        territoryId,
                        request.getTerritoryName(),
                        request.getTerritoryCode(),
                        request.getRevenue(),
                        request.getCurrency(),
                        request.getQuota(),
                        request.getDeals(),
                        request.getWonDeals(),
                        request.getGrowthRate(),
                        request.getAttributes()
                );

        commandService.updateTerritoryMetric(command);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{dashboardId}/quota")
    @Operation(summary = "Update quota", description = "Updates the annual quota for the country")
    public ResponseEntity<Void> updateQuota(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody UpdateQuotaRequest request) {
        log.info("Updating quota for country dashboard: {}", dashboardId);

        CountryDashboardCommand.UpdateQuotaCommand command =
                new CountryDashboardCommand.UpdateQuotaCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        request.getAnnualQuota(),
                        request.getCurrency(),
                        request.getReason()
                );

        commandService.updateQuota(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/kpis")
    @Operation(summary = "Add KPI", description = "Adds a new KPI to the dashboard")
    public ResponseEntity<Void> addKPI(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody AddKpiRequest request) {
        log.info("Adding KPI to country dashboard: {}", dashboardId);

        CountryDashboardCommand.AddKPICommand command =
                new CountryDashboardCommand.AddKPICommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        request.getName(),
                        request.getType(),
                        request.getValue(),
                        request.getTarget(),
                        request.getWeight(),
                        request.getIsCritical()
                );

        commandService.addKPI(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{dashboardId}/kpis/{kpiId}")
    @Operation(summary = "Update KPI", description = "Updates a specific KPI value")
    public ResponseEntity<Void> updateKPI(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Parameter(description = "KPI ID") @PathVariable String kpiId,
            @RequestBody UpdateKpiRequest request) {
        log.info("Updating KPI: {} for country dashboard: {}", kpiId, dashboardId);

        CountryDashboardCommand.UpdateKPICommand command =
                new CountryDashboardCommand.UpdateKPICommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        kpiId,
                        request.getValue()
                );

        commandService.updateKPI(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/trends")
    @Operation(summary = "Add trend data", description = "Adds historical trend data point")
    public ResponseEntity<Void> addTrendData(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @Valid @RequestBody AddTrendDataRequest request) {
        log.info("Adding trend data to country dashboard: {}", dashboardId);

        CountryDashboardCommand.AddTrendDataCommand command =
                new CountryDashboardCommand.AddTrendDataCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        request.getPeriod(),
                        request.getDate(),
                        request.getRevenue(),
                        request.getCurrency(),
                        request.getDeals(),
                        request.getWinRate(),
                        request.getAverageDealSize(),
                        request.getNewCustomers(),
                        request.getGrowthRate(),
                        request.getTerritory()
                );

        commandService.addTrendData(command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{dashboardId}/refresh")
    @Operation(summary = "Refresh dashboard", description = "Triggers a dashboard refresh with recalculations")
    public ResponseEntity<Void> refreshDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @RequestBody(required = false) RefreshDashboardRequest request) {
        log.info("Refreshing country dashboard: {}", dashboardId);

        CountryDashboardCommand.RefreshDashboardCommand command =
                new CountryDashboardCommand.RefreshDashboardCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        RequestContextHolder.getUserId(),
                        request != null ? request.getForceRefresh() : false,
                        request != null ? request.getCalculateYoY() : null,
                        request != null ? request.getCalculateMoM() : null,
                        request != null ? request.getCalculateQoQ() : null
                );

        commandService.refreshDashboard(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/publish")
    @Operation(summary = "Publish dashboard", description = "Publishes a draft dashboard")
    public ResponseEntity<Void> publishDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Publishing country dashboard: {}", dashboardId);

        CountryDashboardCommand.PublishDashboardCommand command =
                new CountryDashboardCommand.PublishDashboardCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId
                );

        commandService.publishDashboard(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dashboardId}/archive")
    @Operation(summary = "Archive dashboard", description = "Archives a dashboard")
    public ResponseEntity<Void> archiveDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Archiving country dashboard: {}", dashboardId);

        CountryDashboardCommand.ArchiveDashboardCommand command =
                new CountryDashboardCommand.ArchiveDashboardCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId
                );

        commandService.archiveDashboard(command);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{dashboardId}/exchange-rates")
    @Operation(summary = "Update exchange rates", description = "Updates currency exchange rates for the dashboard")
    public ResponseEntity<Void> updateExchangeRates(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId,
            @RequestBody Map<String, BigDecimal> exchangeRates) {
        log.info("Updating exchange rates for country dashboard: {}", dashboardId);

        CountryDashboardCommand.UpdateExchangeRatesCommand command =
                new CountryDashboardCommand.UpdateExchangeRatesCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId,
                        exchangeRates
                );

        commandService.updateExchangeRates(command);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{dashboardId}")
    @Operation(summary = "Delete dashboard", description = "Deletes a country dashboard")
    public ResponseEntity<Void> deleteDashboard(
            @Parameter(description = "Dashboard ID") @PathVariable String dashboardId) {
        log.info("Deleting country dashboard: {}", dashboardId);

        CountryDashboardCommand.DeleteDashboardCommand command =
                new CountryDashboardCommand.DeleteDashboardCommand(
                        RequestContextHolder.getTenantId(),
                        dashboardId
                );

        commandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    // Request DTOs

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class CreateDashboardRequest {
            private String countryCode;
            private String countryName;
            private CountrySalesDashboard.DashboardType type;
            private String localCurrency;
            private List<String> enabledTerritories;
            private Integer refreshIntervalMinutes;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class UpdateDashboardRequest {
            private String countryName;
            private CountrySalesDashboard.DashboardStatus status;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class UpdateMetricsRequest {
            private BigDecimal totalRevenue;
            private BigDecimal targetRevenue;
            private Integer totalDeals;
            private Integer wonDeals;
            private Integer lostDeals;
            private BigDecimal averageDealSize;
            private BigDecimal weightedPipeline;
            private Integer opportunitiesInPipeline;
            private Integer newCustomers;
            private Integer churnedCustomers;
            private BigDecimal retentionRate;
            private BigDecimal npsScore;
            private Integer activeSalesReps;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class UpdateTerritoryRequest {
            private String territoryName;
            private String territoryCode;
            private BigDecimal revenue;
            private String currency;
            private BigDecimal quota;
            private Integer deals;
            private Integer wonDeals;
            private BigDecimal growthRate;
            private Map<String, Object> attributes;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class UpdateQuotaRequest {
            private BigDecimal annualQuota;
            private String currency;
            private String reason;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class AddKpiRequest {
            private String name;
            private com.gogidix.sales.countrydashboard.domain.valueobject.MetricType type;
            private Object value;
            private String target;
            private Integer weight;
            private Boolean isCritical;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class UpdateKpiRequest {
            private Object value;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class AddTrendDataRequest {
            private String period;
            private LocalDate date;
            private BigDecimal revenue;
            private String currency;
            private Integer deals;
            private BigDecimal winRate;
            private BigDecimal averageDealSize;
            private Integer newCustomers;
            private BigDecimal growthRate;
            private String territory;
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class RefreshDashboardRequest {
            private Boolean forceRefresh;
            private Boolean calculateYoY;
            private Boolean calculateMoM;
            private Boolean calculateQoQ;
    }
}
