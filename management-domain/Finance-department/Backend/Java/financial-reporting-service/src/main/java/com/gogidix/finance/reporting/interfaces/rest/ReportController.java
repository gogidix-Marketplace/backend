package com.gogidix.finance.reporting.interfaces.rest;

import com.gogidix.finance.reporting.application.service.ReportService;
import com.gogidix.finance.reporting.domain.model.Report;
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
 * Report REST Controller
 */
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Financial report management endpoints")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @Operation(summary = "Create a new report")
    public ResponseEntity<Report> createReport(
            @RequestBody CreateReportRequestDto request) {

        Report report = reportService.createReport(
            request.getName(),
            request.getReportType(),
            request.getFormat(),
            request.getReportDate(),
            request.getParameters()
        );

        return ResponseEntity.accepted().body(report);
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "Get report by ID")
    public ResponseEntity<Report> getReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {

        Report report = reportService.getReport(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    @Operation(summary = "Get all reports for tenant")
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reports by status")
    public ResponseEntity<List<Report>> getReportsByStatus(
            @Parameter(description = "Report status") @PathVariable Report.ReportStatus status) {

        return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get reports by type")
    public ResponseEntity<List<Report>> getReportsByType(
            @Parameter(description = "Report type") @PathVariable Report.ReportType type) {

        return ResponseEntity.ok(reportService.getReportsByType(type));
    }

    @PostMapping("/{reportId}/cancel")
    @Operation(summary = "Cancel report generation")
    public ResponseEntity<Void> cancelReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {

        reportService.cancelReport(reportId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reportId}")
    @Operation(summary = "Delete report")
    public ResponseEntity<Void> deleteReport(
            @Parameter(description = "Report ID") @PathVariable String reportId) {

        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }

    public static class CreateReportRequestDto {
        public String name;
        public Report.ReportType reportType;
        public Report.ReportFormat format;
        public LocalDate reportDate;
        public Map<String, Object> parameters;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Report.ReportType getReportType() {
            return reportType;
        }

        public void setReportType(Report.ReportType reportType) {
            this.reportType = reportType;
        }

        public Report.ReportFormat getFormat() {
            return format;
        }

        public void setFormat(Report.ReportFormat format) {
            this.format = format;
        }

        public LocalDate getReportDate() {
            return reportDate;
        }

        public void setReportDate(LocalDate reportDate) {
            this.reportDate = reportDate;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }
    }
}
