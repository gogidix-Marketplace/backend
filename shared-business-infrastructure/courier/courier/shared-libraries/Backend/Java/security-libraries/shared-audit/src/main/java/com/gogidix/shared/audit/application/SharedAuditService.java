package com.gogidix.shared.audit.application;

import com.gogidix.shared.audit.domain.*;
import com.gogidix.shared.audit.domain.port.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Application service for shared audit operations.
 * Orchestrates audit event processing, compliance reporting, and security monitoring.
 * 
 * This service implements the use cases for the audit domain following hexagonal architecture.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SharedAuditService {
    
    private final AuditEventRepository auditEventRepository;
    private final AuditEventPublisher auditEventPublisher;
    private final ComplianceReporter complianceReporter;
    
    /**
     * Records an audit event with automatic compliance and security analysis
     */
    @Async
    public CompletableFuture<AuditEvent> recordAuditEvent(AuditEventCreationRequest request) {
        log.debug("Recording audit event for user: {}, action: {}", request.getUserId(), request.getAction());
        
        try {
            // Create audit event with business logic
            AuditEvent auditEvent = createAuditEventFromRequest(request);
            
            // Validate business rules
            validateAuditEvent(auditEvent);
            
            // Save to repository
            AuditEvent savedEvent = auditEventRepository.save(auditEvent);
            log.info("Audit event recorded: {}", savedEvent.getEventId());
            
            // Process asynchronously for performance
            processAuditEventAsync(savedEvent);
            
            return CompletableFuture.completedFuture(savedEvent);
            
        } catch (Exception e) {
            log.error("Error recording audit event: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Processes multiple audit events in batch for performance
     */
    @Async
    public CompletableFuture<List<AuditEvent>> recordBatchAuditEvents(List<AuditEventCreationRequest> requests) {
        log.debug("Recording batch of {} audit events", requests.size());
        
        try {
            List<AuditEvent> auditEvents = requests.stream()
                .map(this::createAuditEventFromRequest)
                .peek(this::validateAuditEvent)
                .map(auditEventRepository::save)
                .toList();
            
            log.info("Batch recorded {} audit events", auditEvents.size());
            
            // Process each event asynchronously
            auditEvents.forEach(this::processAuditEventAsync);
            
            return CompletableFuture.completedFuture(auditEvents);
            
        } catch (Exception e) {
            log.error("Error recording batch audit events: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Searches audit events based on complex criteria
     */
    public CompletableFuture<List<AuditEvent>> searchAuditEvents(AuditSearchCriteria criteria) {
        log.debug("Searching audit events with criteria: {}", criteria.getCacheKey());
        
        try {
            if (!criteria.isValid()) {
                throw new IllegalArgumentException("Invalid search criteria provided");
            }
            
            List<AuditEvent> results = auditEventRepository.findBySearchCriteria(criteria);
            log.info("Found {} audit events matching criteria", results.size());
            
            return CompletableFuture.completedFuture(results);
            
        } catch (Exception e) {
            log.error("Error searching audit events: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Generates comprehensive audit statistics for monitoring
     */
    public CompletableFuture<AuditStatistics> generateAuditStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Generating audit statistics for period: {} to {}", startTime, endTime);
        
        try {
            AuditStatistics statistics = auditEventRepository.getAuditStatistics(startTime, endTime);
            
            // Publish statistics for real-time monitoring
            auditEventPublisher.publishAuditStatistics(statistics);
            
            log.info("Generated audit statistics: {} total events, {} compliance events", 
                    statistics.getTotalEvents(), statistics.getComplianceEvents());
            
            return CompletableFuture.completedFuture(statistics);
            
        } catch (Exception e) {
            log.error("Error generating audit statistics: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Generates compliance report for regulatory requirements
     */
    public CompletableFuture<ComplianceReport> generateComplianceReport(
            ComplianceType complianceType, 
            LocalDateTime startTime, 
            LocalDateTime endTime) {
        
        log.debug("Generating compliance report for {} from {} to {}", complianceType, startTime, endTime);
        
        return complianceReporter.generateComplianceReport(complianceType, startTime, endTime)
            .thenApply(report -> {
                log.info("Generated compliance report: {} (Score: {})", 
                        report.getReportId(), report.getComplianceScore());
                
                // Publish for regulatory systems
                auditEventPublisher.publishComplianceReport(report);
                
                return report;
            })
            .exceptionally(throwable -> {
                log.error("Error generating compliance report: {}", throwable.getMessage(), throwable);
                throw new RuntimeException("Failed to generate compliance report", throwable);
            });
    }
    
    /**
     * Monitors and detects suspicious patterns in audit events
     */
    @Async
    public CompletableFuture<List<AuditEvent>> detectSuspiciousPatterns() {
        log.debug("Detecting suspicious patterns in audit events");
        
        try {
            List<AuditEvent> suspiciousEvents = auditEventRepository.findSuspiciousPatterns();
            
            // Process each suspicious event
            for (AuditEvent event : suspiciousEvents) {
                log.warn("Suspicious pattern detected: {} for user: {}", 
                        event.getEventType(), event.getUserId());
                
                // Publish alert
                auditEventPublisher.publishSuspiciousPatternAlert(event);
                
                // Escalate if necessary
                if ("CRITICAL".equals(event.calculateSeverity())) {
                    auditEventPublisher.publishSecurityEscalation(event);
                }
            }
            
            log.info("Detected {} suspicious patterns", suspiciousEvents.size());
            return CompletableFuture.completedFuture(suspiciousEvents);
            
        } catch (Exception e) {
            log.error("Error detecting suspicious patterns: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Validates audit trail completeness for compliance
     */
    public CompletableFuture<Boolean> validateAuditTrailCompleteness(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Validating audit trail completeness from {} to {}", startTime, endTime);
        
        try {
            boolean isComplete = auditEventRepository.isAuditTrailComplete(startTime, endTime);
            
            if (!isComplete) {
                log.warn("Audit trail completeness validation failed for period: {} to {}", startTime, endTime);
                // Could trigger compliance alerts here
            } else {
                log.info("Audit trail completeness validation passed for period: {} to {}", startTime, endTime);
            }
            
            return CompletableFuture.completedFuture(isComplete);
            
        } catch (Exception e) {
            log.error("Error validating audit trail completeness: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Performs audit data retention and cleanup
     */
    @Async
    public CompletableFuture<Long> performAuditDataRetention() {
        log.debug("Performing audit data retention cleanup");
        
        try {
            LocalDateTime cutoffTime = LocalDateTime.now().minusDays(90); // Default 90 days
            
            // Get events to be deleted for archival
            List<AuditEvent> eventsToDelete = auditEventRepository
                .findByTimestampBefore(cutoffTime);
            
            // Archive events before deletion
            for (AuditEvent event : eventsToDelete) {
                auditEventPublisher.archiveAuditEvent(event);
            }
            
            // Delete old events
            auditEventRepository.deleteByTimestampBefore(cutoffTime);
            
            long deletedCount = eventsToDelete.size();
            log.info("Completed audit data retention: archived and deleted {} events", deletedCount);
            
            return CompletableFuture.completedFuture(deletedCount);
            
        } catch (Exception e) {
            log.error("Error performing audit data retention: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    
    // Private helper methods
    
    private AuditEvent createAuditEventFromRequest(AuditEventCreationRequest request) {
        return AuditEvent.builder()
            .eventId(UUID.randomUUID().toString())
            .userId(request.getUserId())
            .sessionId(request.getSessionId())
            .timestamp(LocalDateTime.now())
            .eventType(request.getEventType())
            .domain(request.getDomain())
            .action(request.getAction())
            .resource(request.getResource())
            .resourceId(request.getResourceId())
            .result(request.getResult())
            .description(request.getDescription())
            .ipAddress(request.getIpAddress())
            .userAgent(request.getUserAgent())
            .correlationId(request.getCorrelationId())
            .complianceType(determineComplianceType(request))
            .riskScore(calculateRiskScore(request))
            .metadata(request.getMetadata())
            .build();
    }
    
    private void validateAuditEvent(AuditEvent auditEvent) {
        if (auditEvent.getUserId() == null || auditEvent.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID is required for audit events");
        }
        
        if (auditEvent.getAction() == null || auditEvent.getAction().isEmpty()) {
            throw new IllegalArgumentException("Action is required for audit events");
        }
        
        if (auditEvent.getResource() == null || auditEvent.getResource().isEmpty()) {
            throw new IllegalArgumentException("Resource is required for audit events");
        }
        
        if (auditEvent.getResult() == null) {
            throw new IllegalArgumentException("Result is required for audit events");
        }
    }
    
    @Async
    private void processAuditEventAsync(AuditEvent auditEvent) {
        try {
            // Publish to event stream
            auditEventPublisher.publishAuditEvent(auditEvent);
            
            // Check for security escalation
            if (auditEvent.requiresSecurityEscalation()) {
                auditEventPublisher.publishSecurityEscalation(auditEvent);
                auditEventPublisher.notifySecurityTeam(auditEvent, "Security escalation required");
            }
            
            // Check for suspicious patterns
            if (auditEvent.isSuspiciousPattern()) {
                auditEventPublisher.publishSuspiciousPatternAlert(auditEvent);
            }
            
            // Handle financial transactions
            if (auditEvent.getEventType().isFinancialEvent()) {
                auditEventPublisher.publishFinancialTransactionAlert(auditEvent);
            }
            
            // Update real-time dashboard
            auditEventPublisher.publishDashboardUpdate(auditEvent);
            
        } catch (Exception e) {
            log.error("Error processing audit event asynchronously: {}", e.getMessage(), e);
        }
    }
    
    private ComplianceType determineComplianceType(AuditEventCreationRequest request) {
        if (request.getEventType().isFinancialEvent()) {
            return ComplianceType.PCI_DSS;
        }
        
        if (request.getEventType().involvesSensitiveData()) {
            return ComplianceType.GDPR;
        }
        
        if (request.getDomain().isFinancialDomain()) {
            return ComplianceType.SOX;
        }
        
        return ComplianceType.INTERNAL_POLICY;
    }
    
    private String calculateRiskScore(AuditEventCreationRequest request) {
        int score = 0;
        
        if (request.getEventType().requiresSecurityMonitoring()) {
            score += 30;
        }
        
        if (request.getResult() == AuditResult.FAILURE) {
            score += 20;
        }
        
        if (request.getEventType().isFinancialEvent()) {
            score += 15;
        }
        
        return String.valueOf(Math.min(score, 100));
    }
}