package com.gogidix.shared.audit.domain.port;

import com.gogidix.shared.audit.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain port for audit event persistence operations.
 * This interface defines the contract for storing and retrieving audit events.
 */
public interface AuditEventRepository {
    
    /**
     * Saves an audit event to persistent storage
     */
    AuditEvent save(AuditEvent auditEvent);
    
    /**
     * Finds an audit event by its unique identifier
     */
    Optional<AuditEvent> findById(Long id);
    
    /**
     * Finds an audit event by its event ID
     */
    Optional<AuditEvent> findByEventId(String eventId);
    
    /**
     * Finds all audit events for a specific user
     */
    List<AuditEvent> findByUserId(String userId);
    
    /**
     * Finds all audit events for a specific user within a time range
     */
    List<AuditEvent> findByUserIdAndTimestampBetween(String userId, 
                                                    LocalDateTime startTime, 
                                                    LocalDateTime endTime);
    
    /**
     * Finds all audit events of a specific type
     */
    List<AuditEvent> findByEventType(AuditEventType eventType);
    
    /**
     * Finds all audit events for a specific business domain
     */
    List<AuditEvent> findByDomain(BusinessDomain domain);
    
    /**
     * Finds all audit events with a specific result
     */
    List<AuditEvent> findByResult(AuditResult result);
    
    /**
     * Finds all audit events requiring compliance tracking
     */
    List<AuditEvent> findByComplianceType(ComplianceType complianceType);
    
    /**
     * Finds all audit events within a time range
     */
    List<AuditEvent> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Finds all audit events before a specific timestamp
     */
    List<AuditEvent> findByTimestampBefore(LocalDateTime cutoffTime);
    
    /**
     * Finds all audit events with a specific correlation ID
     */
    List<AuditEvent> findByCorrelationId(String correlationId);
    
    /**
     * Finds all audit events for a specific session
     */
    List<AuditEvent> findBySessionId(String sessionId);
    
    /**
     * Finds all audit events that require security escalation
     */
    List<AuditEvent> findSecurityEscalationEvents();
    
    /**
     * Finds all audit events that are suspicious patterns
     */
    List<AuditEvent> findSuspiciousPatterns();
    
    /**
     * Finds all financial transaction audit events above a certain amount
     */
    List<AuditEvent> findHighValueFinancialTransactions(double minimumAmount);
    
    /**
     * Counts audit events by user within a time period
     */
    long countByUserIdAndTimestampBetween(String userId, 
                                         LocalDateTime startTime, 
                                         LocalDateTime endTime);
    
    /**
     * Counts failed login attempts for a user within a time period
     */
    long countFailedLoginsByUserIdAndTimestampBetween(String userId,
                                                     LocalDateTime startTime,
                                                     LocalDateTime endTime);
    
    /**
     * Finds all audit events that match complex search criteria
     */
    List<AuditEvent> findBySearchCriteria(AuditSearchCriteria criteria);
    
    /**
     * Deletes audit events older than the specified retention period
     */
    void deleteByTimestampBefore(LocalDateTime cutoffTime);
    
    /**
     * Gets audit statistics for reporting
     */
    AuditStatistics getAuditStatistics(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Checks if audit trail is complete for a given time period
     */
    boolean isAuditTrailComplete(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Gets compliance report data for a specific compliance type
     */
    List<AuditEvent> getComplianceReportData(ComplianceType complianceType,
                                           LocalDateTime startTime,
                                           LocalDateTime endTime);
}