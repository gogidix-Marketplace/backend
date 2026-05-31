package com.gogidix.shared.audit.api.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for health check responses.
 * Contains service health status and diagnostic information.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HealthCheckDTO {
    
    private String status; // UP, DOWN, OUT_OF_SERVICE, UNKNOWN
    private LocalDateTime timestamp;
    private String details;
    private Long eventsLastHour;
    private Long eventsLastDay;
    private String version;
    private String buildTime;
    private String commitHash;
    
    // Component health status
    private Map<String, ComponentHealthDTO> components;
    
    // Performance metrics
    private Double averageResponseTime;
    private Long totalRequests;
    private Double requestsPerSecond;
    private Long errorCount;
    private Double errorRate;
    
    // Resource utilization
    private Double memoryUsage;
    private Double cpuUsage;
    private Double diskUsage;
    private Long availableMemory;
    private Long totalMemory;
    
    // Database health
    private String databaseStatus;
    private Integer activeConnections;
    private Integer maxConnections;
    private Double connectionPoolUsage;
    
    // Cache health
    private String cacheStatus;
    private Long cacheHits;
    private Long cacheMisses;
    private Double cacheHitRatio;
    
    // Messaging health
    private String messagingStatus;
    private Long messagesSent;
    private Long messagesReceived;
    private Long messagingErrors;
    
    /**
     * Nested DTO for component health status
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ComponentHealthDTO {
        private String status; // UP, DOWN, OUT_OF_SERVICE, UNKNOWN
        private String description;
        private Map<String, Object> details;
        private LocalDateTime lastCheck;
        private Long responseTime;
        private String errorMessage;
    }
    
    /**
     * Determines if the overall service is healthy
     */
    public boolean isHealthy() {
        return "UP".equals(status) && 
               isComponentsHealthy() && 
               isPerformanceHealthy() && 
               isResourcesHealthy();
    }
    
    /**
     * Checks if all components are healthy
     */
    public boolean isComponentsHealthy() {
        if (components == null || components.isEmpty()) {
            return true; // No components to check
        }
        
        return components.values().stream()
            .allMatch(component -> "UP".equals(component.getStatus()));
    }
    
    /**
     * Checks if performance metrics are within healthy ranges
     */
    public boolean isPerformanceHealthy() {
        // Error rate should be less than 5%
        if (errorRate != null && errorRate > 5.0) {
            return false;
        }
        
        // Average response time should be less than 1000ms
        if (averageResponseTime != null && averageResponseTime > 1000.0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if resource utilization is within healthy ranges
     */
    public boolean isResourcesHealthy() {
        // Memory usage should be less than 85%
        if (memoryUsage != null && memoryUsage > 85.0) {
            return false;
        }
        
        // CPU usage should be less than 80%
        if (cpuUsage != null && cpuUsage > 80.0) {
            return false;
        }
        
        // Disk usage should be less than 90%
        if (diskUsage != null && diskUsage > 90.0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Gets the health status color for UI display
     */
    public String getStatusColor() {
        if (status == null) {
            return "#808080"; // Gray for unknown
        }
        
        switch (status) {
            case "UP":
                return "#28a745"; // Green
            case "DOWN":
                return "#dc3545"; // Red
            case "OUT_OF_SERVICE":
                return "#ffc107"; // Yellow
            default:
                return "#6c757d"; // Gray
        }
    }
    
    /**
     * Gets a summary of critical issues
     */
    public String getCriticalIssuesSummary() {
        StringBuilder issues = new StringBuilder();
        
        if (!"UP".equals(status)) {
            issues.append("Service is ").append(status).append(". ");
        }
        
        if (!isComponentsHealthy()) {
            long downComponents = components.values().stream()
                .filter(c -> !"UP".equals(c.getStatus()))
                .count();
            issues.append(downComponents).append(" component(s) down. ");
        }
        
        if (errorRate != null && errorRate > 5.0) {
            issues.append("High error rate: ").append(String.format("%.1f%%", errorRate)).append(". ");
        }
        
        if (memoryUsage != null && memoryUsage > 85.0) {
            issues.append("High memory usage: ").append(String.format("%.1f%%", memoryUsage)).append(". ");
        }
        
        return issues.length() > 0 ? issues.toString().trim() : "No critical issues detected.";
    }
}