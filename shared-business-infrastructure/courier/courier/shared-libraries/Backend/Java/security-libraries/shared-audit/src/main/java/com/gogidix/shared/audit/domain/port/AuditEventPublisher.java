package com.gogidix.shared.audit.domain.port;

import com.gogidix.shared.audit.domain.AuditEvent;
import com.gogidix.shared.audit.domain.ComplianceReport;

import java.util.concurrent.CompletableFuture;

/**
 * Domain port for publishing audit events to external systems.
 * This interface defines the contract for event publishing and notification.
 */
public interface AuditEventPublisher {
    
    /**
     * Publishes an audit event to the event streaming system
     */
    CompletableFuture<Void> publishAuditEvent(AuditEvent auditEvent);
    
    /**
     * Publishes a security escalation event for immediate attention
     */
    CompletableFuture<Void> publishSecurityEscalation(AuditEvent auditEvent);
    
    /**
     * Publishes a compliance report for regulatory purposes
     */
    CompletableFuture<Void> publishComplianceReport(ComplianceReport report);
    
    /**
     * Publishes suspicious pattern detection alerts
     */
    CompletableFuture<Void> publishSuspiciousPatternAlert(AuditEvent auditEvent);
    
    /**
     * Publishes high-value financial transaction alerts
     */
    CompletableFuture<Void> publishFinancialTransactionAlert(AuditEvent auditEvent);
    
    /**
     * Publishes real-time dashboard updates
     */
    CompletableFuture<Void> publishDashboardUpdate(AuditEvent auditEvent);
    
    /**
     * Sends notification to security team
     */
    CompletableFuture<Void> notifySecurityTeam(AuditEvent auditEvent, String alertMessage);
    
    /**
     * Sends notification to compliance team
     */
    CompletableFuture<Void> notifyComplianceTeam(ComplianceReport report);
    
    /**
     * Sends notification to system administrators
     */
    CompletableFuture<Void> notifySystemAdministrators(AuditEvent auditEvent, String alertMessage);
    
    /**
     * Archives audit event to long-term storage
     */
    CompletableFuture<Void> archiveAuditEvent(AuditEvent auditEvent);
    
    /**
     * Publishes audit statistics for monitoring
     */
    CompletableFuture<Void> publishAuditStatistics(Object auditStatistics);
}