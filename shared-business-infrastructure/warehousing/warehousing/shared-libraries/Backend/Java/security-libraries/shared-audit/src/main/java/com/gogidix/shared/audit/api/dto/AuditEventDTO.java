package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for audit event responses.
 * Contains complete audit event information for API responses.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEventDTO {
    
    private String eventId;
    private String userId;
    private String sessionId;
    private LocalDateTime timestamp;
    private String action;
    private String resourceType;
    private String resourceId;
    private String ipAddress;
    private String userAgent;
    private Map<String, String> additionalData;
    
    // Computed fields for mapper
    private String severity;
    private Boolean requiresSecurityEscalation;
    private Boolean suspiciousPattern;
    private Boolean compliantEvent;
    private Boolean financialEvent;
    
}