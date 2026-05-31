package com.gogidix.aiservices.aireporting.interfaces;

import com.gogidix.aiservices.aireporting.application.*;
import com.gogidix.aiservices.aireporting.application.port.in.GenerateReportCommand;
import com.gogidix.aiservices.aireporting.application.port.in.GenerateReportUseCase;
import com.gogidix.aiservices.aireporting.domain.Report;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for report generation operations.
 */
@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private static final Logger log = LoggerFactory.getLogger(ReportController.class);

    private final GenerateReportUseCase reportUseCase;

    public ReportController(GenerateReportUseCase reportUseCase) {
        this.reportUseCase = reportUseCase;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateReport(
            @Valid @RequestBody GenerateReportRequest request) {
        log.info("Received report generation request: type={}", request.type());

        GenerateReportCommand command = new GenerateReportCommand(
            request.type(),
            request.format(),
            request.dateRange().start(),
            request.dateRange().end(),
            request.includeMetrics(),
            request.options()
        );

        Report report = reportUseCase.generateReport(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseJson(report));
    }

    @GetMapping("/{reportId}/status")
    public ResponseEntity<Map<String, Object>> getReportStatus(@PathVariable String reportId) {
        log.info("Fetching report status for: {}", reportId);

        return reportUseCase.getReportStatus(reportId)
            .map(report -> ResponseEntity.ok(Map.<String, Object>of(
                "reportId", report.getReportId(),
                "status", report.getStatus().toString(),
                "downloadUrl", report.getDownloadUrl(),
                "expiresAt", report.getExpiresAt() != null ? report.getExpiresAt().toString() : null
            )))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Map<String, Object>> getReport(@PathVariable String reportId) {
        log.info("Fetching report: {}", reportId);

        return reportUseCase.getReportStatus(reportId)
            .map(report -> ResponseEntity.ok(toResponseJson(report)))
            .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", ex.getMessage()));
    }

    private Map<String, Object> toResponseJson(Report report) {
        return Map.of(
            "reportId", report.getReportId(),
            "status", report.getStatus().toString(),
            "downloadUrl", report.getDownloadUrl(),
            "expiresAt", report.getExpiresAt() != null ? report.getExpiresAt().toString() : null,
            "type", report.getType().toString(),
            "format", report.getFormat().toString()
        );
    }

    public record GenerateReportRequest(
        ReportType type,
        ExportFormat format,
        DateRange dateRange,
        java.util.List<String> includeMetrics,
        Map<String, Object> options
    ) {
        public record DateRange(LocalDateTime start, LocalDateTime end) {}
    }
}
