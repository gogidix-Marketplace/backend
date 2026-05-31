package com.gogidix.sales.analytics.interfaces.rest;

import com.gogidix.sales.analytics.application.service.ReportService;
import com.gogidix.sales.analytics.domain.model.AnalyticsReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Analytics Reports Operations
 */
@RestController
@RequestMapping("/reports")
@Tag(name = "Analytics Reports", description = "Analytics report generation and management")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}")
public class ReportsController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "Create report", description = "Creates a new analytics report")
    public ResponseEntity<AnalyticsReport> createReport(@Valid @RequestBody CreateReportRequest request) {
        AnalyticsReport report = reportService.createReport(
                request.name(),
                request.reportType(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.filters(),
                request.format()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "Get report by ID", description = "Returns a single analytics report")
    public ResponseEntity<AnalyticsReport> getReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reports by status", description = "Returns all reports with a specific status")
    public ResponseEntity<List<AnalyticsReport>> getReportsByStatus(
            @Parameter(description = "Report status") @PathVariable AnalyticsReport.ReportStatus status) {
        return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    @GetMapping("/type/{reportType}")
    @Operation(summary = "Get reports by type", description = "Returns all reports of a specific type")
    public ResponseEntity<List<AnalyticsReport>> getReportsByType(
            @Parameter(description = "Report type") @PathVariable AnalyticsReport.ReportType reportType) {
        return ResponseEntity.ok(reportService.getReportsByType(reportType));
    }

    @GetMapping("/my-reports")
    @Operation(summary = "Get my reports", description = "Returns all reports created by the current user")
    public ResponseEntity<List<AnalyticsReport>> getMyReports() {
        return ResponseEntity.ok(reportService.getReportsByUser());
    }

    @PostMapping("/{reportId}/share")
    @Operation(summary = "Share report", description = "Shares a report with another user")
    public ResponseEntity<AnalyticsReport> shareReport(
            @Parameter(description = "Report ID") @PathVariable String reportId,
            @Valid @RequestBody ShareReportRequest request) {
        return ResponseEntity.ok(reportService.shareReport(reportId, request.userId()));
    }

    @DeleteMapping("/{reportId}/share/{userId}")
    @Operation(summary = "Unshare report", description = "Removes sharing for a user")
    public ResponseEntity<AnalyticsReport> unshareReport(
            @Parameter(description = "Report ID") @PathVariable String reportId,
            @Parameter(description = "User ID") @PathVariable String userId) {
        return ResponseEntity.ok(reportService.unshareReport(reportId, userId));
    }

    @PostMapping("/{reportId}/tags")
    @Operation(summary = "Add tag to report", description = "Adds a tag to the report")
    public ResponseEntity<AnalyticsReport> addReportTag(
            @Parameter(description = "Report ID") @PathVariable String reportId,
            @Valid @RequestBody AddTagRequest request) {
        return ResponseEntity.ok(reportService.addReportTag(reportId, request.tag()));
    }

    @DeleteMapping("/{reportId}")
    @Operation(summary = "Delete report", description = "Deletes an analytics report")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/cleanup-expired")
    @Operation(summary = "Cleanup expired reports", description = "Deletes all expired reports")
    public ResponseEntity<Void> cleanupExpiredReports() {
        reportService.cleanupExpiredReports();
        return ResponseEntity.noContent().build();
    }

    // ========== Request/Response Records ==========

    public record CreateReportRequest(
            String name,
            AnalyticsReport.ReportType reportType,
            String description,
            Instant startDate,
            Instant endDate,
            Map<String, Object> filters,
            AnalyticsReport.ReportFormat format
    ) {}

    public record ShareReportRequest(
            String userId
    ) {}

    public record AddTagRequest(
            String tag
    ) {}
}
