package com.gogidix.shared.audit.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain object representing a request to create an audit event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventCreationRequest {
    
    private String userId;
    private String sessionId;
    private LocalDateTime timestamp;
    private AuditEventType eventType;
    private BusinessDomain domain;
    private String action;
    private String resource;
    private String resourceId;
    private AuditResult result;
    private String description;
    private String ipAddress;
    private String userAgent;
    private String correlationId;
    private ComplianceType complianceType;
    private String riskScore;
    private String metadata;
    
    /**
     * Validates if this creation request meets business rules
     */
    public boolean isValidForBusinessRules() {
        return userId != null && !userId.trim().isEmpty() &&
               sessionId != null && !sessionId.trim().isEmpty() &&
               eventType != null &&
               domain != null &&
               action != null && !action.trim().isEmpty() &&
               resource != null && !resource.trim().isEmpty() &&
               result != null;
    }
}