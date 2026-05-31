package com.gogidix.globalbusinessmanagement.datavalidation.interfaces.rest;

import com.gogidix.globalbusinessmanagement.datavalidation.application.service.DataQualityService;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.DataQualityReportDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.DataQualityReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for data quality reporting
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/data-quality")
@RequiredArgsConstructor
public class DataQualityController {

    private final DataQualityService dataQualityService;

    /**
     * Generate a quality report for an entity
     */
    @PostMapping("/report/entity")
    public ResponseEntity<DataQualityReportDTO> generateEntityReport(
            @RequestParam String entityType,
            @RequestParam String entityId,
            @RequestParam(required = false) String tenantId,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("POST /api/v1/data-quality/report/entity - {} ({})", entityType, entityId);

        DataQualityReportDTO report = dataQualityService.generateEntityReport(
                entityType, entityId, tenantId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /**
     * Generate a quality report for a dataset
     */
    @PostMapping("/report/dataset")
    public ResponseEntity<DataQualityReportDTO> generateDatasetReport(
            @RequestParam String dataSource,
            @RequestParam String dataSourceType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodEnd,
            @RequestParam(required = false) String tenantId,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("POST /api/v1/data-quality/report/dataset - {}", dataSource);

        DataQualityReportDTO report = dataQualityService.generateDatasetReport(
                dataSource, dataSourceType, periodStart, periodEnd, tenantId, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /**
     * Generate a tenant-wide quality report
     */
    @PostMapping("/report/tenant")
    public ResponseEntity<DataQualityReportDTO> generateTenantReport(
            @RequestParam String tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodStart,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime periodEnd,
            @RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {
        log.info("POST /api/v1/data-quality/report/tenant - {}", tenantId);

        DataQualityReportDTO report = dataQualityService.generateTenantReport(
                tenantId, periodStart, periodEnd, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(report);
    }

    /**
     * Get a report by ID
     */
    @GetMapping("/report/{id}")
    public ResponseEntity<DataQualityReportDTO> getReport(@PathVariable String id) {
        log.info("GET /api/v1/data-quality/report/{}", id);
        DataQualityReportDTO report = dataQualityService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    /**
     * Get a report by report ID
     */
    @GetMapping("/report/id/{reportId}")
    public ResponseEntity<DataQualityReportDTO> getReportByReportId(@PathVariable String reportId) {
        log.info("GET /api/v1/data-quality/report/id/{}", reportId);
        DataQualityReportDTO report = dataQualityService.getReportByReportId(reportId);
        return ResponseEntity.ok(report);
    }

    /**
     * Get reports for an entity
     */
    @GetMapping("/report/entity/{entityType}/{entityId}")
    public ResponseEntity<List<DataQualityReportDTO>> getReportsForEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        log.info("GET /api/v1/data-quality/report/entity/{}/{}", entityType, entityId);
        List<DataQualityReportDTO> reports = dataQualityService.getReportsForEntity(entityType, entityId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get latest report for an entity
     */
    @GetMapping("/report/entity/{entityType}/{entityId}/latest")
    public ResponseEntity<DataQualityReportDTO> getLatestReportForEntity(
            @PathVariable String entityType,
            @PathVariable String entityId) {
        log.info("GET /api/v1/data-quality/report/entity/{}/{} /latest", entityType, entityId);
        DataQualityReportDTO report = dataQualityService.getLatestReportForEntity(entityType, entityId);
        return ResponseEntity.ok(report);
    }

    /**
     * Get reports for tenant with pagination
     */
    @GetMapping("/report/enant/{tenantId}")
    public ResponseEntity<Page<DataQualityReportDTO>> getReportsForTenant(
            @PathVariable String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /api/v1/data-quality/report/tenant/{}", tenantId);

        Pageable pageable = PageRequest.of(page, size);
        Page<DataQualityReportDTO> reports = dataQualityService.getReportsForTenant(tenantId, pageable);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get reports by quality level
     */
    @GetMapping("/report/level/{qualityLevel}")
    public ResponseEntity<List<DataQualityReportDTO>> getReportsByQualityLevel(
            @PathVariable DataQualityReport.QualityLevel qualityLevel) {
        log.info("GET /api/v1/data-quality/report/level/{}", qualityLevel);
        List<DataQualityReportDTO> reports = dataQualityService.getReportsByQualityLevel(qualityLevel);
        return ResponseEntity.ok(reports);
    }

    /**
     * Archive a report
     */
    @PostMapping("/report/{id}/archive")
    public ResponseEntity<Void> archiveReport(@PathVariable String id) {
        log.info("POST /api/v1/data-quality/report/{}/archive", id);
        dataQualityService.archiveReport(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get quality summary for dashboard
     */
    @GetMapping("/summary/{tenantId}")
    public ResponseEntity<DataQualityService.QualitySummary> getQualitySummary(@PathVariable String tenantId) {
        log.info("GET /api/v1/data-quality/summary/{}", tenantId);
        DataQualityService.QualitySummary summary = dataQualityService.getQualitySummary(tenantId);
        return ResponseEntity.ok(summary);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Data quality service is healthy");
    }
}
