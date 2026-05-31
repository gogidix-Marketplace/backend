package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.application.dto.response.CategoryComplianceDto;
import com.gogidix.hr.globalcompliance.application.dto.response.ComplianceScoreDto;
import com.gogidix.hr.globalcompliance.application.dto.response.CountryDashboardDto;
import com.gogidix.hr.globalcompliance.application.dto.response.CriticalIssueDto;
import com.gogidix.hr.globalcompliance.application.dto.response.DashboardSummaryDto;
import com.gogidix.hr.globalcompliance.application.dto.response.UpcomingCheckDto;
import com.gogidix.hr.globalcompliance.application.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Dashboard REST Controller
 * Handles HTTP requests for dashboard and analytics operations
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Dashboard and analytics endpoints")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get compliance dashboard summary")
    public ResponseEntity<DashboardSummaryDto> getSummary() {
        DashboardSummaryDto summary = dashboardService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get country compliance dashboard")
    public ResponseEntity<CountryDashboardDto> getCountryDashboard(
            @Parameter(description = "Country Code") @PathVariable String countryCode) {
        CountryDashboardDto dashboard = dashboardService.getCountryDashboard(countryCode);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/critical-issues")
    @Operation(summary = "Get critical issues overview")
    public ResponseEntity<List<CriticalIssueDto>> getCriticalIssues() {
        List<CriticalIssueDto> issues = dashboardService.getCriticalIssues();
        return ResponseEntity.ok(issues);
    }

    @GetMapping("/upcoming-checks")
    @Operation(summary = "Get upcoming compliance checks")
    public ResponseEntity<List<UpcomingCheckDto>> getUpcomingChecks() {
        List<UpcomingCheckDto> checks = dashboardService.getUpcomingChecks();
        return ResponseEntity.ok(checks);
    }

    @GetMapping("/compliance-score")
    @Operation(summary = "Get overall compliance score")
    public ResponseEntity<ComplianceScoreDto> getComplianceScore() {
        ComplianceScoreDto score = dashboardService.getComplianceScore();
        return ResponseEntity.ok(score);
    }

    @GetMapping("/by-category")
    @Operation(summary = "Get compliance by category breakdown")
    public ResponseEntity<Map<String, CategoryComplianceDto>> getComplianceByCategory() {
        Map<String, CategoryComplianceDto> categoryCompliance = dashboardService.getComplianceByCategory();
        return ResponseEntity.ok(categoryCompliance);
    }

    @GetMapping("/trends")
    @Operation(summary = "Get compliance trends")
    public ResponseEntity<Map<String, Object>> getTrends(
            @Parameter(description = "Start Date") @RequestParam LocalDate startDate,
            @Parameter(description = "End Date") @RequestParam LocalDate endDate) {
        Map<String, Object> trends = dashboardService.getTrends(startDate, endDate);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/requirements/by-category/{category}/count")
    @Operation(summary = "Get requirements count by category")
    public ResponseEntity<Map<String, Long>> getRequirementsCountByCategory(
            @Parameter(description = "Category") @PathVariable String category) {
        Map<String, CategoryComplianceDto> categoryData = dashboardService.getComplianceByCategory();
        CategoryComplianceDto data = categoryData.get(category);
        return ResponseEntity.ok(Map.of(
                "total", data != null ? data.getTotalRequirements() : 0L,
                "active", data != null ? data.getActiveRequirements() : 0L
        ));
    }

    @GetMapping("/checks/statistics")
    @Operation(summary = "Get checks statistics")
    public ResponseEntity<Map<String, Long>> getChecksStatistics() {
        ComplianceScoreDto score = dashboardService.getComplianceScore();
        return ResponseEntity.ok(Map.of(
                "total", score.getTotalChecks(),
                "passed", score.getPassedChecks(),
                "failed", score.getFailedChecks(),
                "partial", score.getPartialChecks(),
                "pending", score.getTotalChecks() - score.getPassedChecks() - score.getFailedChecks() - score.getPartialChecks()
        ));
    }

    @GetMapping("/issues/statistics")
    @Operation(summary = "Get issues statistics")
    public ResponseEntity<Map<String, Long>> getIssuesStatistics() {
        DashboardSummaryDto summary = dashboardService.getSummary();
        return ResponseEntity.ok(Map.of(
                "total", summary.getTotalIssues(),
                "open", summary.getOpenIssues(),
                "critical", summary.getCriticalIssues(),
                "overdue", summary.getOverdueIssues()
        ));
    }
}
