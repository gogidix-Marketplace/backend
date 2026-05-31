package com.gogidix.shared.audit.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Value object representing search criteria for complex audit event queries.
 */
@Data
@Builder
public class AuditSearchCriteria {
    
    private String userId;
    private String sessionId;
    private String correlationId;
    private String ipAddress;
    
    private AuditEventType eventType;
    private BusinessDomain domain;
    private AuditResult result;
    private ComplianceType complianceType;
    private AuditSeverity minSeverity;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    private String resourceContains;
    private String descriptionContains;
    private String metadataContains;
    
    private List<String> userIds;
    private List<AuditEventType> eventTypes;
    private List<BusinessDomain> domains;
    private List<AuditResult> results;
    
    private Boolean requiresSecurityEscalation;
    private Boolean isSuspiciousPattern;
    private Boolean isCompliantEvent;
    
    private Double minTransactionAmount;
    private Double maxTransactionAmount;
    
    private Integer maxResults;
    private String sortBy;
    private String sortDirection;
    
    /**
     * Validates that the search criteria is valid
     */
    public boolean isValid() {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            return false;
        }
        
        if (minTransactionAmount != null && maxTransactionAmount != null && 
            minTransactionAmount > maxTransactionAmount) {
            return false;
        }
        
        if (maxResults != null && maxResults <= 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Determines if this is a complex search requiring advanced querying
     */
    public boolean isComplexSearch() {
        int criteriaCount = 0;
        
        if (userId != null) criteriaCount++;
        if (eventType != null) criteriaCount++;
        if (domain != null) criteriaCount++;
        if (result != null) criteriaCount++;
        if (complianceType != null) criteriaCount++;
        if (startTime != null && endTime != null) criteriaCount++;
        if (resourceContains != null) criteriaCount++;
        if (descriptionContains != null) criteriaCount++;
        if (requiresSecurityEscalation != null) criteriaCount++;
        if (isSuspiciousPattern != null) criteriaCount++;
        
        return criteriaCount > 3;
    }
    
    /**
     * Gets a cache key for this search criteria
     */
    public String getCacheKey() {
        StringBuilder key = new StringBuilder();
        
        if (userId != null) key.append("u:").append(userId).append("|");
        if (eventType != null) key.append("et:").append(eventType).append("|");
        if (domain != null) key.append("d:").append(domain).append("|");
        if (startTime != null) key.append("st:").append(startTime).append("|");
        if (endTime != null) key.append("et:").append(endTime).append("|");
        
        return key.toString();
    }
}