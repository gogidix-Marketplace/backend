package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.domain.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * REST controller for analytics and reporting.
 */
@Tag(name = "Analytics", description = "Analytics and reporting APIs")
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Get dashboard data", description = "Retrieve comprehensive analytics dashboard data")
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardData(
            @Parameter(description = "Start date") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        AnalyticsService.AnalyticsDashboard dashboard = analyticsService.getDashboardData(startDate, endDate);

        Map<String, Object> response = Map.of(
                "contentStats", dashboard.getContentStats(),
                "mediaStats", dashboard.getMediaStats(),
                "userStats", dashboard.getUserStats(),
                "productStats", dashboard.getProductStats(),
                "jobStats", dashboard.getJobStats(),
                "leadStats", dashboard.getLeadStats(),
                "workflowStats", dashboard.getWorkflowStats(),
                "recentActivity", dashboard.getRecentActivity(),
                "period", Map.of("startDate", startDate, "endDate", endDate)
        );

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Get content statistics", description = "Retrieve content-related statistics")
    @GetMapping("/content")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.ContentStats>> getContentStats() {
        AnalyticsService.ContentStats stats = analyticsService.getContentStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get media statistics", description = "Retrieve media-related statistics")
    @GetMapping("/media")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.MediaStats>> getMediaStats() {
        AnalyticsService.MediaStats stats = analyticsService.getMediaStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get user statistics", description = "Retrieve user-related statistics")
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.UserStats>> getUserStats() {
        AnalyticsService.UserStats stats = analyticsService.getUserStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get product statistics", description = "Retrieve product-related statistics")
    @GetMapping("/products")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.ProductStats>> getProductStats() {
        AnalyticsService.ProductStats stats = analyticsService.getProductStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get job statistics", description = "Retrieve job-related statistics")
    @GetMapping("/jobs")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.JobStats>> getJobStats() {
        AnalyticsService.JobStats stats = analyticsService.getJobStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get lead statistics", description = "Retrieve lead-related statistics")
    @GetMapping("/leads")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.LeadStats>> getLeadStats(
            @Parameter(description = "Start date") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        AnalyticsService.LeadStats stats = analyticsService.getLeadStats(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @Operation(summary = "Get workflow statistics", description = "Retrieve workflow-related statistics")
    @GetMapping("/workflows")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    public ResponseEntity<ApiResponse<AnalyticsService.WorkflowStats>> getWorkflowStats() {
        AnalyticsService.WorkflowStats stats = analyticsService.getWorkflowStats();
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
}
