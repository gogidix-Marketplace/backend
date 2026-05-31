package com.gogidix.dashboard.reporting.adapter.in.web;

import com.gogidix.dashboard.reporting.adapter.in.web.dto.GenerateReportRequestDTO;
import com.gogidix.dashboard.reporting.adapter.in.web.dto.ScheduleReportRequestDTO;
import com.gogidix.dashboard.reporting.domain.model.*;
import com.gogidix.dashboard.reporting.domain.port.in.ReportManagementUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Report REST Controller
 * 
 * Inbound adapter for HTTP/REST requests
 * Provides REST API for report generation and management
 */
@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    
    private final ReportManagementUseCase reportService;
    
    public ReportController(ReportManagementUseCase reportService) {
        this.reportService = reportService;
    }
    
    /**
     * Generate a new report
     */
    @PostMapping
    public ResponseEntity<Report> generateReport(@Valid @RequestBody GenerateReportRequestDTO request) {
        ReportManagementUseCase.GenerateReportCommand command = 
            new ReportManagementUseCase.GenerateReportCommand(
                request.getReportName(),
                request.getType(),
                request.getDomain(),
                request.getFromDate(),
                request.getToDate(),
                request.getConfiguration()
            );
        
        Report report = reportService.generateReport(command);
        return ResponseEntity.ok(report);
    }
    
    /**
     * Get report by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable String id) {
        return reportService.getReport(ReportId.fromString(id))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get reports by type
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Report>> getReportsByType(@PathVariable ReportType type) {
        List<Report> reports = reportService.getReportsByType(type);
        return ResponseEntity.ok(reports);
    }
    
    /**
     * Get reports by domain
     */
    @GetMapping("/domain/{domain}")
    public ResponseEntity<List<Report>> getReportsByDomain(@PathVariable String domain) {
        List<Report> reports = reportService.getReportsByDomain(domain);
        return ResponseEntity.ok(reports);
    }
    
    /**
     * Export report
     */
    @PostMapping("/{id}/export")
    public ResponseEntity<ReportExport> exportReport(
            @PathVariable String id,
            @RequestParam ExportFormat format) {
        
        ReportExport export = reportService.exportReport(ReportId.fromString(id), format);
        return ResponseEntity.ok(export);
    }
    
    /**
     * Schedule report
     */
    @PostMapping("/schedule")
    public ResponseEntity<ScheduledReport> scheduleReport(@Valid @RequestBody ScheduleReportRequestDTO request) {
        ReportManagementUseCase.ScheduleReportCommand command = 
            new ReportManagementUseCase.ScheduleReportCommand(
                request.getReportName(),
                request.getType(),
                request.getDomain(),
                request.getCronExpression(),
                request.getConfiguration()
            );
        
        ScheduledReport scheduledReport = reportService.scheduleReport(command);
        return ResponseEntity.ok(scheduledReport);
    }
    
    /**
     * Get scheduled reports
     */
    @GetMapping("/scheduled")
    public ResponseEntity<List<ScheduledReport>> getScheduledReports() {
        List<ScheduledReport> reports = reportService.getScheduledReports();
        return ResponseEntity.ok(reports);
    }
    
    /**
     * Delete report
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable String id) {
        reportService.deleteReport(ReportId.fromString(id));
        return ResponseEntity.ok().build();
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Reporting Service is running");
    }
}