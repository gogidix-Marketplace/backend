package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.application.dto.response.ComplianceScoreDto;
import com.gogidix.hr.globalcompliance.application.dto.response.CountryDashboardDto;
import com.gogidix.hr.globalcompliance.application.dto.response.CriticalIssueDto;
import com.gogidix.hr.globalcompliance.application.dto.response.DashboardSummaryDto;
import com.gogidix.hr.globalcompliance.application.dto.response.UpcomingCheckDto;
import com.gogidix.hr.globalcompliance.application.service.DashboardService;
import com.gogidix.hr.globalcompliance.application.service.ReportCommandService;
import com.gogidix.hr.globalcompliance.application.service.ReportQueryService;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceReport;
import com.gogidix.hr.globalcompliance.domain.port.in.ReportCommand;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Compliance Report REST Controller
 * Handles HTTP requests for compliance report operations
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Compliance Reports", description = "Compliance report management endpoints")
public class ReportController {

    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;

    @PostMapping
    @Operation(summary = "Generate a new compliance report")
    public ResponseEntity<ComplianceReport> generateReport(
            @Valid @RequestBody ReportCommand.CreateReportCommand command) {
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setPreparedBy(RequestContextHolder.getUserId());
        ComplianceReport report = reportCommandService.create(command);
        return ResponseEntity.status(201).body(report);
    }

    @GetMapping
    @Operation(summary = "Get all compliance reports")
    public ResponseEntity<Page<ComplianceReport>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceReport> reports = reportQueryService.getAllForTenant();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "Get report by ID")
    public ResponseEntity<ComplianceReport> getReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {
        ComplianceReport report = reportQueryService.getById(reportId);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/{reportId}/submit")
    @Operation(summary = "Submit report")
    public ResponseEntity<Void> submitReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {
        ReportCommand.SubmitReportCommand command = new ReportCommand.SubmitReportCommand(
                RequestContextHolder.getTenantId(), reportId);
        reportCommandService.submit(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reportId}/approve")
    @Operation(summary = "Approve report")
    public ResponseEntity<Void> approveReport(
            @Parameter(description = "Report ID") @PathVariable String reportId,
            @RequestBody ApproveReportRequestDto request) {
        ReportCommand.ApproveReportCommand command = new ReportCommand.ApproveReportCommand(
                RequestContextHolder.getTenantId(), reportId,
                RequestContextHolder.getUserId(), request.getApprovedByName());
        reportCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-country/{countryCode}")
    @Operation(summary = "Get reports by country")
    public ResponseEntity<Page<ComplianceReport>> getByCountry(
            @Parameter(description = "Country Code") @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<ComplianceReport> reports = reportQueryService.getByCountryCode(countryCode, page, size);
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{reportId}")
    @Operation(summary = "Update report")
    public ResponseEntity<Void> updateReport(
            @Parameter(description = "Report ID") @PathVariable String reportId,
            @RequestBody UpdateReportRequestDto request) {
        if (request.getMetrics() != null) {
            ReportCommand.SetMetricsCommand metricsCommand = new ReportCommand.SetMetricsCommand(
                    RequestContextHolder.getTenantId(), reportId,
                    request.getMetrics().getTotalRequirements(),
                    request.getMetrics().getPassedChecks(),
                    request.getMetrics().getFailedChecks(),
                    request.getMetrics().getPendingChecks());
            reportCommandService.setMetrics(metricsCommand);
        }
        if (request.getSummary() != null) {
            ReportCommand.SetSummaryCommand summaryCommand = new ReportCommand.SetSummaryCommand(
                    RequestContextHolder.getTenantId(), reportId, request.getSummary());
            reportCommandService.setSummary(summaryCommand);
        }
        if (request.getCriticalIssue() != null) {
            ReportCommand.AddCriticalIssueCommand issueCommand = new ReportCommand.AddCriticalIssueCommand(
                    RequestContextHolder.getTenantId(), reportId, request.getCriticalIssue());
            reportCommandService.addCriticalIssue(issueCommand);
        }
        if (request.getRecommendation() != null) {
            ReportCommand.AddRecommendationCommand recommendationCommand = new ReportCommand.AddRecommendationCommand(
                    RequestContextHolder.getTenantId(), reportId, request.getRecommendation());
            reportCommandService.addRecommendation(recommendationCommand);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reportId}/reject")
    @Operation(summary = "Reject report")
    public ResponseEntity<Void> rejectReport(
            @Parameter(description = "Report ID") @PathVariable String reportId,
            @RequestBody RejectReportRequestDto request) {
        ReportCommand.RejectReportCommand command = new ReportCommand.RejectReportCommand(
                RequestContextHolder.getTenantId(), reportId, request.getRejectionReason());
        reportCommandService.reject(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reportId}/publish")
    @Operation(summary = "Publish report")
    public ResponseEntity<Void> publishReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {
        ReportCommand.PublishReportCommand command = new ReportCommand.PublishReportCommand(
                RequestContextHolder.getTenantId(), reportId);
        reportCommandService.publish(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reportId}")
    @Operation(summary = "Delete report")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {
        ReportCommand.DeleteReportCommand command = new ReportCommand.DeleteReportCommand(
                RequestContextHolder.getTenantId(), reportId);
        reportCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @lombok.Data
    public static class UpdateReportRequestDto {
        private MetricsDto metrics;
        private String summary;
        private String criticalIssue;
        private String recommendation;
    }

    @lombok.Data
    public static class MetricsDto {
        private Integer totalRequirements;
        private Integer passedChecks;
        private Integer failedChecks;
        private Integer pendingChecks;
    }

    @lombok.Data
    public static class ApproveReportRequestDto {
        private String approvedByName;
    }

    @lombok.Data
    public static class RejectReportRequestDto {
        private String rejectionReason;
    }
}
