package com.gogidix.shared.audit.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;

/**
 * Rich Domain Entity representing an audit event in the Gogidix ecosystem.
 * Contains comprehensive business logic for audit tracking, compliance, and reporting.
 * 
 * Business Rules:
 * - All user actions must be audited
 * - Financial transactions require enhanced audit detail
 * - Compliance events must include regulatory context
 * - System events must track performance metrics
 * - Security events require immediate escalation flags
 */
@Entity
@Table(name = "audit_events", indexes = {
    @Index(name = "idx_audit_user_id", columnList = "userId"),
    @Index(name = "idx_audit_timestamp", columnList = "timestamp"),
    @Index(name = "idx_audit_event_type", columnList = "eventType"),
    @Index(name = "idx_audit_domain", columnList = "domain"),
    @Index(name = "idx_audit_compliance", columnList = "complianceType")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, updatable = false)
    private String eventId;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String sessionId;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditEventType eventType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BusinessDomain domain;
    
    @Column(nullable = false)
    private String action;
    
    @Column(nullable = false)
    private String resource;
    
    @Column
    private String resourceId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditResult result;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column
    private String ipAddress;
    
    @Column
    private String userAgent;
    
    @Column
    private String correlationId;
    
    @Enumerated(EnumType.STRING)
    private ComplianceType complianceType;
    
    @Column
    private String riskScore;
    
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    // Business logic methods
    
    /**
     * Validates if this audit event meets compliance requirements
     */
    public boolean isCompliantEvent() {
        return complianceType != null && 
               !description.isEmpty() && 
               timestamp != null &&
               result != null;
    }
    
    /**
     * Determines if this event requires immediate security escalation
     */
    public boolean requiresSecurityEscalation() {
        return eventType == AuditEventType.SECURITY_EVENT ||
               (eventType == AuditEventType.ACCESS_DENIED && 
                getFailureCount() > 3) ||
               isHighRiskTransaction();
    }
    
    /**
     * Calculates the severity level of this audit event
     */
    public String calculateSeverity() {
        if (eventType == AuditEventType.SECURITY_EVENT) {
            return "CRITICAL";
        }
        
        if (eventType == AuditEventType.FINANCIAL_TRANSACTION && 
            getTransactionAmount() > 10000.0) {
            return "HIGH";
        }
        
        if (result == AuditResult.FAILURE) {
            return "MEDIUM";
        }
        
        return "LOW";
    }
    
    /**
     * Checks if this event is part of a suspicious pattern
     */
    public boolean isSuspiciousPattern() {
        return (eventType == AuditEventType.LOGIN_ATTEMPT && 
                result == AuditResult.FAILURE) ||
               (eventType == AuditEventType.FINANCIAL_TRANSACTION && 
                isOutsideNormalHours()) ||
               (getRequestFrequency() > 100); // More than 100 requests per minute
    }
    
    /**
     * Generates compliance report data for this event
     */
    public ComplianceReport generateComplianceReport() {
        return ComplianceReport.builder()
            .eventId(eventId)
            .timestamp(timestamp)
            .complianceType(complianceType)
            .auditTrail(description)
            .riskAssessment(riskScore)
            .regulatoryContext(getRegulatoryContext())
            .build();
    }
    
    /**
     * Creates an audit trail entry for this event
     */
    public AuditTrailEntry createTrailEntry() {
        return AuditTrailEntry.builder()
            .eventId(eventId)
            .timestamp(timestamp)
            .userId(userId)
            .action(action)
            .resource(resource)
            .result(result.toString())
            .details(description)
            .build();
    }
    
    // Private helper methods for business logic
    
    private int getFailureCount() {
        // In real implementation, this would query recent failure events
        return metadata != null && metadata.contains("failureCount") ? 
               extractFailureCountFromMetadata() : 0;
    }
    
    private boolean isHighRiskTransaction() {
        return eventType == AuditEventType.FINANCIAL_TRANSACTION &&
               (getTransactionAmount() > 5000.0 || 
                isInternationalTransaction() ||
                isFirstTimeVendor());
    }
    
    private double getTransactionAmount() {
        // Extract from metadata
        return metadata != null && metadata.contains("amount") ?
               extractAmountFromMetadata() : 0.0;
    }
    
    private boolean isOutsideNormalHours() {
        int hour = timestamp.getHour();
        return hour < 6 || hour > 22; // Outside 6 AM - 10 PM
    }
    
    private int getRequestFrequency() {
        // In real implementation, this would calculate recent request frequency
        return metadata != null && metadata.contains("frequency") ?
               extractFrequencyFromMetadata() : 0;
    }
    
    private String getRegulatoryContext() {
        switch (complianceType) {
            case PCI_DSS:
                return "Payment Card Industry Data Security Standard";
            case GDPR:
                return "General Data Protection Regulation";
            case SOX:
                return "Sarbanes-Oxley Act";
            case HIPAA:
                return "Health Insurance Portability and Accountability Act";
            default:
                return "General Business Compliance";
        }
    }
    
    private boolean isInternationalTransaction() {
        return metadata != null && metadata.contains("international=true");
    }
    
    private boolean isFirstTimeVendor() {
        return metadata != null && metadata.contains("firstTime=true");
    }
    
    private int extractFailureCountFromMetadata() {
        // Simplified extraction - in real implementation would use JSON parsing
        try {
            String count = metadata.substring(
                metadata.indexOf("failureCount:") + 13,
                metadata.indexOf(",", metadata.indexOf("failureCount:"))
            );
            return Integer.parseInt(count.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    private double extractAmountFromMetadata() {
        try {
            String amount = metadata.substring(
                metadata.indexOf("amount:") + 7,
                metadata.indexOf(",", metadata.indexOf("amount:"))
            );
            return Double.parseDouble(amount.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    private int extractFrequencyFromMetadata() {
        try {
            String freq = metadata.substring(
                metadata.indexOf("frequency:") + 10,
                metadata.indexOf(",", metadata.indexOf("frequency:"))
            );
            return Integer.parseInt(freq.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Determines if this audit event is related to financial operations
     */
    public boolean isFinancialEvent() {
        return eventType == AuditEventType.FINANCIAL_TRANSACTION ||
               eventType == AuditEventType.PAYMENT_PROCESSING ||
               eventType == AuditEventType.REFUND_PROCESSED ||
               eventType == AuditEventType.COMMISSION_CALCULATION ||
               action.toLowerCase().contains("payment") ||
               action.toLowerCase().contains("transaction") ||
               action.toLowerCase().contains("financial") ||
               resource.toLowerCase().contains("payment") ||
               resource.toLowerCase().contains("billing") ||
               (metadata != null && metadata.contains("amount:"));
    }
    
    
    // Helper methods for missing functionality
    private boolean isOffHoursActivity() {
        return isOutsideNormalHours();
    }
}