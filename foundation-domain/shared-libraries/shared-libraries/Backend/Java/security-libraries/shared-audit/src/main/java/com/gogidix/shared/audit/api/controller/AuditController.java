package com.gogidix.shared.audit.api.controller;

import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.api.mapper.AuditEventMapper;
import com.gogidix.shared.audit.application.SharedAuditService;
import com.gogidix.shared.audit.domain.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for audit operations.
 * Provides endpoints for recording audit events, searching, and generating reports.
 * 
 * Security: All endpoints require AUDIT_READ or AUDIT_WRITE permissions.
 * Rate limiting: Applied to prevent abuse of audit endpoints.
 */
@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuditController {
    
    private final SharedAuditService sharedAuditService;
    private final AuditEventMapper auditEventMapper;
    
    /**
     * Records a single audit event
     */
    @PostMapping("/events")
    @PreAuthorize("hasPermission('AUDIT_WRITE')")
    public CompletableFuture<ResponseEntity<AuditEventDTO>> recordAuditEvent(
            @Valid @RequestBody CreateAuditEventDTO createRequest) {
        
        log.debug("Recording audit event for user: {}", createRequest.getUserId());
        
        return sharedAuditService.recordAuditEvent(
                auditEventMapper.toCreationRequest(createRequest))
            .thenApply(auditEvent -> ResponseEntity.ok(
                auditEventMapper.toDTO(auditEvent)))
            .exceptionally(throwable -> {
                log.error("Error recording audit event: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Records multiple audit events in batch
     */
    @PostMapping("/events/batch")
    @PreAuthorize("hasPermission('AUDIT_WRITE')")
    public CompletableFuture<ResponseEntity<List<AuditEventDTO>>> recordBatchAuditEvents(
            @Valid @RequestBody List<CreateAuditEventDTO> createRequests) {
        
        log.debug("Recording batch of {} audit events", createRequests.size());
        
        return sharedAuditService.recordBatchAuditEvents(
                createRequests.stream()
                    .map(auditEventMapper::toCreationRequest)
                    .toList())
            .thenApply(auditEvents -> ResponseEntity.ok(
                auditEvents.stream()
                    .map(auditEventMapper::toDTO)
                    .toList()))
            .exceptionally(throwable -> {
                log.error("Error recording batch audit events: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Searches audit events with complex criteria
     */
    @PostMapping("/events/search")
    @PreAuthorize("hasPermission('AUDIT_READ')")
    public CompletableFuture<ResponseEntity<List<AuditEventDTO>>> searchAuditEvents(
            @Valid @RequestBody AuditSearchDTO searchRequest) {
        
        log.debug("Searching audit events with criteria");
        
        return sharedAuditService.searchAuditEvents(
                auditEventMapper.toSearchCriteria(searchRequest))
            .thenApply(auditEvents -> ResponseEntity.ok(
                auditEvents.stream()
                    .map(auditEventMapper::toDTO)
                    .toList()))
            .exceptionally(throwable -> {
                log.error("Error searching audit events: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Gets audit events for a specific user
     */
    @GetMapping("/events/user/{userId}")
    @PreAuthorize("hasPermission('AUDIT_READ')")
    public ResponseEntity<List<AuditEventDTO>> getUserAuditEvents(
            @PathVariable String userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        log.debug("Getting audit events for user: {}", userId);
        
        try {
            AuditSearchCriteria criteria = AuditSearchCriteria.builder()
                .userId(userId)
                .startTime(startTime != null ? startTime : LocalDateTime.now().minusDays(30))
                .endTime(endTime != null ? endTime : LocalDateTime.now())
                .maxResults(1000)
                .sortBy("timestamp")
                .sortDirection("DESC")
                .build();
            
            List<AuditEvent> auditEvents = sharedAuditService.searchAuditEvents(criteria).get();
            
            return ResponseEntity.ok(
                auditEvents.stream()
                    .map(auditEventMapper::toDTO)
                    .toList());
            
        } catch (Exception e) {
            log.error("Error getting user audit events: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Generates audit statistics for monitoring
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasPermission('AUDIT_READ')")
    public CompletableFuture<ResponseEntity<AuditStatisticsDTO>> getAuditStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        log.debug("Generating audit statistics from {} to {}", startTime, endTime);
        
        return sharedAuditService.generateAuditStatistics(startTime, endTime)
            .thenApply(statistics -> ResponseEntity.ok(
                auditEventMapper.toStatisticsDTO(statistics)))
            .exceptionally(throwable -> {
                log.error("Error generating audit statistics: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Generates compliance report
     */
    @GetMapping("/compliance/report")
    @PreAuthorize("hasPermission('COMPLIANCE_READ')")
    public CompletableFuture<ResponseEntity<ComplianceReportDTO>> generateComplianceReport(
            @RequestParam ComplianceType complianceType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        log.debug("Generating compliance report for {} from {} to {}", complianceType, startTime, endTime);
        
        return sharedAuditService.generateComplianceReport(complianceType, startTime, endTime)
            .thenApply(report -> ResponseEntity.ok(
                auditEventMapper.toComplianceReportDTO(report)))
            .exceptionally(throwable -> {
                log.error("Error generating compliance report: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Detects suspicious patterns in audit events
     */
    @PostMapping("/security/suspicious-patterns")
    @PreAuthorize("hasPermission('SECURITY_READ')")
    public CompletableFuture<ResponseEntity<List<AuditEventDTO>>> detectSuspiciousPatterns() {
        
        log.debug("Detecting suspicious patterns");
        
        return sharedAuditService.detectSuspiciousPatterns()
            .thenApply(suspiciousEvents -> ResponseEntity.ok(
                suspiciousEvents.stream()
                    .map(auditEventMapper::toDTO)
                    .toList()))
            .exceptionally(throwable -> {
                log.error("Error detecting suspicious patterns: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Validates audit trail completeness
     */
    @GetMapping("/compliance/trail-completeness")
    @PreAuthorize("hasPermission('COMPLIANCE_READ')")
    public CompletableFuture<ResponseEntity<AuditTrailCompletenessDTO>> validateAuditTrailCompleteness(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        log.debug("Validating audit trail completeness from {} to {}", startTime, endTime);
        
        return sharedAuditService.validateAuditTrailCompleteness(startTime, endTime)
            .thenApply(isComplete -> ResponseEntity.ok(
                AuditTrailCompletenessDTO.builder()
                    .periodStart(startTime)
                    .periodEnd(endTime)
                    .isComplete(isComplete)
                    .validatedAt(LocalDateTime.now())
                    .build()))
            .exceptionally(throwable -> {
                log.error("Error validating audit trail completeness: {}", throwable.getMessage());
                return ResponseEntity.badRequest().build();
            });
    }
    
    /**
     * Health check endpoint for audit service
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckDTO> healthCheck() {
        try {
            // Perform basic health checks
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneHourAgo = now.minusHours(1);
            
            // Check if we can generate statistics (tests repository connectivity)
            AuditStatistics stats = sharedAuditService.generateAuditStatistics(oneHourAgo, now).get();
            
            return ResponseEntity.ok(HealthCheckDTO.builder()
                .status("UP")
                .timestamp(now)
                .details("Audit service is operational")
                .eventsLastHour(stats.getTotalEvents())
                .build());
            
        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage());
            
            return ResponseEntity.status(503).body(HealthCheckDTO.builder()
                .status("DOWN")
                .timestamp(LocalDateTime.now())
                .details("Audit service is experiencing issues: " + e.getMessage())
                .build());
        }
    }
}