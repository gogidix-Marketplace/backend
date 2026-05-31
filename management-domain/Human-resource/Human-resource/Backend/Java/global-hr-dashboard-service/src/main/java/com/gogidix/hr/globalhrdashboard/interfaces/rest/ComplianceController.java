package com.gogidix.hr.globalhrdashboard.interfaces.rest;

import com.gogidix.hr.globalhrdashboard.application.dto.response.ComplianceResponseDto;
import com.gogidix.hr.globalhrdashboard.application.service.ComplianceQueryService;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceMetric;
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
 * REST Controller for Compliance Metrics
 * Provides 10 endpoints for managing compliance data
 */
@RestController
@RequestMapping("/compliance-metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Compliance Metrics", description = "API for managing HR compliance metrics")
public class ComplianceController {

    private final ComplianceQueryService complianceQueryService;

    @GetMapping
    @Operation(summary = "Get all compliance metrics", description = "Returns all compliance metrics for the current tenant")
    public ResponseEntity<List<ComplianceResponseDto>> getAllComplianceMetrics() {
        List<ComplianceMetric> metrics = complianceQueryService.getAllComplianceMetrics();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get compliance by country", description = "Returns compliance metrics for a specific country")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceByCountry(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceByCountry(countryCode);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/country/{countryCode}/period/{period}")
    @Operation(summary = "Get compliance by country and period", description = "Returns compliance metrics for a country in a specific period")
    public ResponseEntity<ComplianceResponseDto> getComplianceByCountryAndPeriod(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Period (e.g., 2024-01)", required = true)
            @PathVariable String period) {
        ComplianceMetric metric = complianceQueryService.getComplianceByCountryAndPeriod(countryCode, period);
        return ResponseEntity.ok(toResponseDto(metric));
    }

    @GetMapping("/by-region/{regionCode}")
    @Operation(summary = "Get compliance by region", description = "Returns compliance metrics for a specific region")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceByRegion(
            @Parameter(description = "Region code", required = true)
            @PathVariable String regionCode) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceByRegion(regionCode);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/at-risk")
    @Operation(summary = "Get at-risk countries", description = "Returns countries with at-risk compliance status")
    public ResponseEntity<List<ComplianceResponseDto>> getAtRiskCountries() {
        List<ComplianceMetric> metrics = complianceQueryService.getAtRiskCountries();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get compliance by period", description = "Returns all compliance metrics for a specific period")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceByPeriod(
            @Parameter(description = "Period", required = true)
            @PathVariable String period) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceByPeriod(period);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get compliance summary", description = "Returns summary statistics for compliance metrics")
    public ResponseEntity<ComplianceQueryService.ComplianceSummary> getComplianceSummary(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        ComplianceQueryService.ComplianceSummary summary = complianceQueryService.getComplianceSummary(period);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/average-score")
    @Operation(summary = "Get average compliance score", description = "Returns average compliance score across all countries")
    public ResponseEntity<Double> getAverageComplianceScore(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        Double score = complianceQueryService.getAverageComplianceScore(period);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/global-status")
    @Operation(summary = "Get global compliance status", description = "Returns overall compliance status")
    public ResponseEntity<com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus> getGlobalComplianceStatus(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus status =
                complianceQueryService.getGlobalComplianceStatus(period);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/bottom-countries")
    @Operation(summary = "Get countries with lowest compliance", description = "Returns countries with lowest compliance scores")
    public ResponseEntity<List<ComplianceResponseDto>> getBottomCountries(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "5") int limit) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<ComplianceMetric> metrics = complianceQueryService.getBottomCountriesByCompliance(period, limit);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/top-countries")
    @Operation(summary = "Get countries with highest compliance", description = "Returns countries with highest compliance scores")
    public ResponseEntity<List<ComplianceResponseDto>> getTopCountries(
            @Parameter(description = "Period")
            @RequestParam(required = false) String period,
            @Parameter(description = "Limit results")
            @RequestParam(defaultValue = "5") int limit) {
        if (period == null) {
            period = getCurrentPeriod();
        }
        List<ComplianceMetric> metrics = complianceQueryService.getTopCountriesByCompliance(period, limit);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get compliance by status", description = "Returns metrics filtered by compliance status")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceByStatus(
            @Parameter(description = "Compliance status", required = true)
            @PathVariable com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus status) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceByStatus(status);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/below-threshold/{threshold}")
    @Operation(summary = "Get compliance below threshold", description = "Returns metrics with compliance score below threshold")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceBelowThreshold(
            @Parameter(description = "Score threshold", required = true)
            @PathVariable Double threshold) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceBelowThreshold(threshold);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/above-threshold/{threshold}")
    @Operation(summary = "Get compliance above threshold", description = "Returns metrics with compliance score above threshold")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceAboveThreshold(
            @Parameter(description = "Score threshold", required = true)
            @PathVariable Double threshold) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceAboveThreshold(threshold);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/{countryCode}/trend")
    @Operation(summary = "Get compliance trend for country", description = "Returns compliance trend over time for a country")
    public ResponseEntity<List<ComplianceResponseDto>> getComplianceTrend(
            @Parameter(description = "ISO country code", required = true)
            @PathVariable String countryCode,
            @Parameter(description = "Start period", required = true)
            @RequestParam String startPeriod,
            @Parameter(description = "End period", required = true)
            @RequestParam String endPeriod) {
        List<ComplianceMetric> metrics = complianceQueryService.getComplianceTrendByCountry(
                countryCode, startPeriod, endPeriod);
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    @GetMapping("/with-critical-issues")
    @Operation(summary = "Get metrics with critical issues", description = "Returns compliance metrics with critical issues")
    public ResponseEntity<List<ComplianceResponseDto>> getWithCriticalIssues() {
        List<ComplianceMetric> metrics = complianceQueryService.getWithCriticalIssues();
        return ResponseEntity.ok(toResponseDtoList(metrics));
    }

    private List<ComplianceResponseDto> toResponseDtoList(List<ComplianceMetric> metrics) {
        return metrics.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private ComplianceResponseDto toResponseDto(ComplianceMetric metric) {
        return ComplianceResponseDto.builder()
                .id(metric.getId())
                .countryCode(metric.getCountryCode())
                .countryName(metric.getCountryName())
                .regionCode(metric.getRegionCode())
                .complianceScore(metric.getComplianceScore())
                .totalRequirements(metric.getTotalRequirements())
                .passedChecks(metric.getPassedChecks())
                .failedChecks(metric.getFailedChecks())
                .criticalIssues(metric.getCriticalIssues())
                .period(metric.getPeriod())
                .status(metric.getStatus())
                .issues(metric.getIssues() != null ?
                        metric.getIssues().stream()
                                .map(issue -> ComplianceResponseDto.ComplianceIssueDto.builder()
                                        .issueId(issue.getIssueId())
                                        .title(issue.getTitle())
                                        .description(issue.getDescription())
                                        .severity(issue.getSeverity() != null ?
                                                ComplianceResponseDto.IssueSeverity.valueOf(issue.getSeverity().name()) : null)
                                        .category(issue.getCategory())
                                        .identifiedDate(issue.getIdentifiedDate())
                                        .targetResolutionDate(issue.getTargetResolutionDate())
                                        .assignedTo(issue.getAssignedTo())
                                        .status(issue.getStatus() != null ?
                                                ComplianceResponseDto.IssueStatus.valueOf(issue.getStatus().name()) : null)
                                        .build())
                                .collect(Collectors.toList()) : null)
                .pendingActions(metric.getPendingActions())
                .lastAssessed(metric.getLastAssessed())
                .assessedBy(metric.getAssessedBy())
                .notes(metric.getNotes())
                .isActive(metric.getIsActive())
                .createdAt(metric.getCreatedAt())
                .updatedAt(metric.getUpdatedAt())
                .build();
    }

    private String getCurrentPeriod() {
        return java.time.YearMonth.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
    }
}
