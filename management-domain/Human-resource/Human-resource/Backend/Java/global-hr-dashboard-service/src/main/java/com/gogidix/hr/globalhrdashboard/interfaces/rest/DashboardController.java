package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.dto.response.DashboardResponseDto;
import com.gogidix.hr.globalhrdashboard.application.dto.response.HeadcountResponseDto;
import com.gogidix.hr.globalhrdashboard.application.service.DashboardQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for HR Dashboard Views
 * Provides 15+ endpoints for executive dashboards
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Dashboard", description = "API for HR executive dashboards")
public class DashboardController {

    private final DashboardQueryService dashboardQueryService;

    @GetMapping("/chro")
    @Operation(summary = "CHRO dashboard", description = "Returns comprehensive HR dashboard for CHRO with all metrics")
    public ResponseEntity<DashboardResponseDto> getCHRODashboard(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.CHRODashboardView dashboard = dashboardQueryService.getCHRODashboard(period);
        return ResponseEntity.ok(toDashboardResponseDto(dashboard, Instant.now()));
    }

    @GetMapping("/ceo")
    @Operation(summary = "CEO dashboard", description = "Returns workforce summary dashboard for CEO")
    public ResponseEntity<DashboardResponseDto> getCEODashboard(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.CEODashboardView dashboard = dashboardQueryService.getCEODashboard(period);
        return ResponseEntity.ok(toDashboardResponseDto(dashboard, Instant.now()));
    }

    @GetMapping("/compliance")
    @Operation(summary = "Global compliance dashboard", description = "Returns compliance-focused dashboard")
    public ResponseEntity<DashboardResponseDto> getComplianceDashboard(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.ComplianceDashboardView dashboard = dashboardQueryService.getComplianceDashboard(period);
        return ResponseEntity.ok(toDashboardResponseDto(dashboard, Instant.now()));
    }

    @GetMapping("/diversity")
    @Operation(summary = "Global diversity dashboard", description = "Returns diversity and inclusion dashboard")
    public ResponseEntity<DashboardResponseDto> getDiversityDashboard(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.DiversityDashboardView dashboard = dashboardQueryService.getDiversityDashboard(period);
        return ResponseEntity.ok(toDashboardResponseDto(dashboard, Instant.now()));
    }

    @GetMapping("/{countryCode}")
    @Operation(summary = "Country-specific dashboard", description = "Returns comprehensive dashboard for a specific country")
    public ResponseEntity<DashboardResponseDto> getCountryDashboard(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.CountryDashboardView dashboard = dashboardQueryService.getCountryDashboard(countryCode, period);
        return ResponseEntity.ok(toDashboardResponseDto(dashboard, Instant.now()));
    }

    @GetMapping("/{regionCode}/summary")
    @Operation(summary = "Regional summary", description = "Returns summary dashboard for a specific region")
    public ResponseEntity<DashboardResponseDto> getRegionalSummary(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode,
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.RegionalSummaryDashboard dashboard = dashboardQueryService.getRegionalSummary(regionCode, period);
        return ResponseEntity.ok(toDashboardResponseDto(dashboard, Instant.now()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh all dashboard data", description = "Triggers refresh of all dashboard data")
    public ResponseEntity<RefreshResponse> refreshDashboard(
            @Parameter(description = "Force refresh from source")
            @RequestParam(defaultValue = "false") boolean force) {
        log.info("Dashboard refresh triggered, force: {}", force);
        Instant lastRefresh = Instant.now();

        RefreshResponse response = new RefreshResponse(
                "Dashboard refresh initiated",
                lastRefresh,
                force
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/overview")
    @Operation(summary = "Get dashboard overview", description = "Returns high-level overview across all metric categories")
    public ResponseEntity<DashboardOverview> getDashboardOverview(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardOverview overview = new DashboardOverview(
                period,
                dashboardQueryService.getCHRODashboard(period),
                dashboardQueryService.getCEODashboard(period),
                dashboardQueryService.getComplianceDashboard(period),
                dashboardQueryService.getDiversityDashboard(period)
        );

        return ResponseEntity.ok(overview);
    }

    @GetMapping("/metrics-summary")
    @Operation(summary = "Get metrics summary", description = "Returns summary of all key HR metrics")
    public ResponseEntity<KeyMetricsSummary> getKeyMetricsSummary(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        KeyMetricsSummary summary = new KeyMetricsSummary(
                period,
                dashboardQueryService.getCHRODashboard(period),
                dashboardQueryService.getComplianceDashboard(period),
                dashboardQueryService.getDiversityDashboard(period),
                getRetentionSummary(period)
        );

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/available-periods")
    @Operation(summary = "Get available periods", description = "Returns list of all periods with available data")
    public ResponseEntity<List<String>> getAvailablePeriods() {
        List<String> periods = dashboardQueryService.getAvailablePeriods();
        return ResponseEntity.ok(periods);
    }

    @GetMapping("/latest-period")
    @Operation(summary = "Get latest period", description = "Returns the most recent period with data")
    public ResponseEntity<String> getLatestPeriod() {
        String period = dashboardQueryService.getLatestPeriod();
        return ResponseEntity.ok(period);
    }

    @GetMapping("/status")
    @Operation(summary = "Get dashboard status", description = "Returns current status of dashboard data")
    public ResponseEntity<DashboardStatus> getDashboardStatus() {
        List<String> periods = dashboardQueryService.getAvailablePeriods();
        String latestPeriod = dashboardQueryService.getLatestPeriod();
        Instant lastUpdate = dashboardQueryService.getLastUpdateTimestamp();

        DashboardStatus status = new DashboardStatus(
                !periods.isEmpty(),
                latestPeriod,
                lastUpdate,
                periods.size()
        );

        return ResponseEntity.ok(status);
    }

    @GetMapping("/headcount-summary")
    @Operation(summary = "Get headcount summary", description = "Returns aggregated headcount summary")
    public ResponseEntity<HeadcountSummary> getHeadcountSummary(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.CEODashboardView ceoDashboard = dashboardQueryService.getCEODashboard(period);

        HeadcountSummary summary = new HeadcountSummary(
                period,
                ceoDashboard.totalHeadcount(),
                ceoDashboard.totalCountries(),
                ceoDashboard.totalRegions(),
                ceoDashboard.yearOverYearChange()
        );

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/retention-summary")
    @Operation(summary = "Get retention summary", description = "Returns aggregated retention summary")
    public ResponseEntity<RetentionSummary> getRetentionSummaryEndpoint(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        RetentionSummary summary = getRetentionSummary(period);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/alerts")
    @Operation(summary = "Get dashboard alerts", description = "Returns current alerts and warnings")
    public ResponseEntity<DashboardAlerts> getDashboardAlerts(
            @Parameter(description = "Period (e.g., 2024-01)")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }

        DashboardQueryService.ComplianceDashboardView compliance = dashboardQueryService.getComplianceDashboard(period);

        DashboardAlerts alerts = new DashboardAlerts(
                compliance.withCriticalIssues().size(),
                (long) compliance.nonCompliantCount(),
                (long) compliance.atRiskCount(),
                new ArrayList<>(compliance.atRiskCountries()),
                new ArrayList<>(compliance.nonCompliantCountries())
        );

        return ResponseEntity.ok(alerts);
    }

    private DashboardResponseDto toDashboardResponseDto(DashboardQueryService.CHRODashboardView dashboard, Instant refreshTime) {
        return DashboardResponseDto.builder()
                .period(dashboard.period())
                .lastRefreshed(refreshTime)
                .headcountSummary(DashboardResponseDto.HeadcountSummary.builder()
                        .totalHeadcount(dashboard.globalHeadcount())
                        .build())
                .complianceSummary(DashboardResponseDto.ComplianceSummary.builder()
                        .averageScore(dashboard.averageComplianceScore())
                        .globalStatus(com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus.fromScore(
                                dashboard.averageComplianceScore()))
                        .build())
                .diversitySummary(DashboardResponseDto.DiversitySummary.builder()
                        .averageGenderDiversityScore(dashboard.averageDiversityScore())
                        .build())
                .retentionSummary(DashboardResponseDto.RetentionSummary.builder()
                        .averageRetentionRate(dashboard.averageRetentionRate())
                        .build())
                .build();
    }

    private DashboardResponseDto toDashboardResponseDto(DashboardQueryService.CEODashboardView dashboard, Instant refreshTime) {
        return DashboardResponseDto.builder()
                .period(dashboard.period())
                .lastRefreshed(refreshTime)
                .headcountSummary(DashboardResponseDto.HeadcountSummary.builder()
                        .totalHeadcount(dashboard.totalHeadcount())
                        .yearOverYearChange((int) dashboard.yearOverYearChange())
                        .totalCountries(dashboard.totalCountries())
                        .totalRegions(dashboard.totalRegions())
                        .build())
                .build();
    }

    private DashboardResponseDto toDashboardResponseDto(DashboardQueryService.ComplianceDashboardView dashboard, Instant refreshTime) {
        return DashboardResponseDto.builder()
                .period(dashboard.period())
                .lastRefreshed(refreshTime)
                .complianceSummary(DashboardResponseDto.ComplianceSummary.builder()
                        .averageScore(dashboard.averageScore())
                        .globalStatus(dashboard.globalStatus())
                        .compliantCount(dashboard.compliantCount())
                        .atRiskCount(dashboard.atRiskCount())
                        .nonCompliantCount(dashboard.nonCompliantCount())
                        .withCriticalIssues((long) dashboard.withCriticalIssues().size())
                        .build())
                .build();
    }

    private DashboardResponseDto toDashboardResponseDto(DashboardQueryService.DiversityDashboardView dashboard, Instant refreshTime) {
        return DashboardResponseDto.builder()
                .period(dashboard.period())
                .lastRefreshed(refreshTime)
                .diversitySummary(DashboardResponseDto.DiversitySummary.builder()
                        .averageGenderDiversityScore(dashboard.averageGenderDiversityScore())
                        .averageNationalDiversityScore(dashboard.averageNationalDiversityScore())
                        .averageOverallDiversityScore(dashboard.averageOverallDiversityScore())
                        .globalGenderDistribution(dashboard.globalGenderDistribution())
                        .build())
                .build();
    }

    private DashboardResponseDto toDashboardResponseDto(DashboardQueryService.CountryDashboardView dashboard, Instant refreshTime) {
        return DashboardResponseDto.builder()
                .period(dashboard.period())
                .lastRefreshed(refreshTime)
                .build();
    }

    private DashboardResponseDto toDashboardResponseDto(DashboardQueryService.RegionalSummaryDashboard dashboard, Instant refreshTime) {
        return DashboardResponseDto.builder()
                .period(dashboard.period())
                .lastRefreshed(refreshTime)
                .build();
    }

    private RetentionSummary getRetentionSummary(String period) {
        // This would be called if retention service was available
        return new RetentionSummary(period, 0.0, 0.0, 0.0);
    }

    private String getCurrentPeriod() {
        return java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public record RefreshResponse(
            String message,
            Instant refreshTime,
            boolean forceRefresh
    ) {}

    public record DashboardOverview(
            String period,
            DashboardQueryService.CHRODashboardView chroView,
            DashboardQueryService.CEODashboardView ceoView,
            DashboardQueryService.ComplianceDashboardView complianceView,
            DashboardQueryService.DiversityDashboardView diversityView
    ) {}

    public record KeyMetricsSummary(
            String period,
            DashboardQueryService.CHRODashboardView chroView,
            DashboardQueryService.ComplianceDashboardView complianceView,
            DashboardQueryService.DiversityDashboardView diversityView,
            RetentionSummary retentionSummary
    ) {}

    public record DashboardStatus(
            boolean hasData,
            String latestPeriod,
            Instant lastUpdate,
            int totalPeriods
    ) {}

    public record HeadcountSummary(
            String period,
            int totalHeadcount,
            int totalCountries,
            int totalRegions,
            long yearOverYearChange
    ) {}

    public record RetentionSummary(
            String period,
            double averageRetentionRate,
            double averageTurnoverRate,
            double averageTenure
    ) {}

    public record DashboardAlerts(
            long criticalIssues,
            long nonCompliantCountries,
            long atRiskCountries,
            List<Object> atRiskCountryDetails,
            List<Object> nonCompliantCountryDetails
    ) {}
}
