package com.gogidix.shared.audit.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Value object representing audit statistics for reporting and monitoring.
 */
@Data
@Builder
public class AuditStatistics {
    
    private final LocalDateTime periodStart;
    private final LocalDateTime periodEnd;
    
    private final long totalEvents;
    private final long successfulEvents;
    private final long failedEvents;
    private final long securityEvents;
    private final long complianceEvents;
    
    private final Map<AuditEventType, Long> eventTypeDistribution;
    private final Map<BusinessDomain, Long> domainDistribution;
    private final Map<AuditResult, Long> resultDistribution;
    private final Map<ComplianceType, Long> complianceDistribution;
    
    private final long uniqueUsers;
    private final long uniqueSessions;
    private final String mostActiveUser;
    private final long mostActiveUserEvents;
    
    private final double averageEventsPerUser;
    private final double averageEventsPerHour;
    private final double compliancePercentage;
    private final double securityEventPercentage;
    
    private final int criticalSeverityEvents;
    private final int highSeverityEvents;
    private final int mediumSeverityEvents;
    private final int lowSeverityEvents;
    
    private final long suspiciousPatterns;
    private final long escalationRequiredEvents;
    private final long financialTransactions;
    private final double totalFinancialAmount;
    
    /**
     * Calculates the success rate as a percentage
     */
    public double getSuccessRate() {
        if (totalEvents == 0) return 0.0;
        return (double) successfulEvents / totalEvents * 100.0;
    }
    
    /**
     * Calculates the failure rate as a percentage
     */
    public double getFailureRate() {
        if (totalEvents == 0) return 0.0;
        return (double) failedEvents / totalEvents * 100.0;
    }
    
    /**
     * Determines if the audit statistics indicate healthy system operation
     */
    public boolean isHealthyOperation() {
        double successRate = getSuccessRate();
        double securityEventRate = getSecurityEventPercentage();
        
        return successRate >= 95.0 && 
               securityEventRate <= 5.0 &&
               suspiciousPatterns <= (totalEvents * 0.01); // Less than 1%
    }
    
    /**
     * Gets the risk score based on the audit statistics (0-100)
     */
    public int getRiskScore() {
        int score = 0;
        
        // High failure rate increases risk
        if (getFailureRate() > 10.0) score += 30;
        else if (getFailureRate() > 5.0) score += 15;
        
        // High security event rate increases risk
        if (securityEventPercentage > 10.0) score += 25;
        else if (securityEventPercentage > 5.0) score += 10;
        
        // Suspicious patterns increase risk
        if (suspiciousPatterns > (totalEvents * 0.05)) score += 20; // More than 5%
        else if (suspiciousPatterns > (totalEvents * 0.01)) score += 10; // More than 1%
        
        // Critical severity events increase risk
        if (criticalSeverityEvents > 0) score += 15;
        
        // Escalation required events increase risk
        if (escalationRequiredEvents > (totalEvents * 0.02)) score += 10; // More than 2%
        
        return Math.min(score, 100);
    }
    
    /**
     * Gets the compliance health score (0-100)
     */
    public int getComplianceHealthScore() {
        int score = 100;
        
        // Deduct points for low compliance percentage
        if (compliancePercentage < 95.0) score -= 20;
        else if (compliancePercentage < 98.0) score -= 10;
        
        // Deduct points for missing compliance events
        if (complianceEvents == 0 && totalEvents > 0) score -= 30;
        
        // Deduct points for high security event rate
        if (securityEventPercentage > 5.0) score -= 15;
        
        return Math.max(score, 0);
    }
}