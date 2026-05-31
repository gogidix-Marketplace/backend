package com.gogidix.dashboard.performance.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

/**
 * Performance Metric Domain Entity
 * 
 * Core business entity for system performance metrics
 * Implements sophisticated performance analysis and alerting logic
 */
public class PerformanceMetric {
    
    private final MetricId id;
    private final String metricName;
    private final MetricType metricType;
    private final PerformanceValue value;
    private final PerformanceThresholds thresholds;
    private final LocalDateTime timestamp;
    private final String sourceDomain;
    private final String sourceService;
    private final Map<String, String> tags;
    private final List<PerformanceAnomaly> anomalies;
    private final PerformanceStatus status;
    
    private PerformanceMetric(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "Metric ID is required");
        this.metricName = Objects.requireNonNull(builder.metricName, "Metric name is required");
        this.metricType = Objects.requireNonNull(builder.metricType, "Metric type is required");
        this.value = Objects.requireNonNull(builder.value, "Performance value is required");
        this.thresholds = Objects.requireNonNull(builder.thresholds, "Thresholds are required");
        this.timestamp = Objects.requireNonNull(builder.timestamp, "Timestamp is required");
        this.sourceDomain = Objects.requireNonNull(builder.sourceDomain, "Source domain is required");
        this.sourceService = Objects.requireNonNull(builder.sourceService, "Source service is required");
        this.tags = builder.tags;
        this.anomalies = new ArrayList<>(builder.anomalies);
        this.status = calculateStatus();
    }
    
    /**
     * Calculate performance status based on value and thresholds
     */
    private PerformanceStatus calculateStatus() {
        double currentValue = value.getValue();
        
        if (thresholds.isCriticalThresholdBreached(currentValue)) {
            return PerformanceStatus.CRITICAL;
        } else if (thresholds.isWarningThresholdBreached(currentValue)) {
            return PerformanceStatus.WARNING;
        } else if (thresholds.isOptimal(currentValue)) {
            return PerformanceStatus.OPTIMAL;
        } else {
            return PerformanceStatus.NORMAL;
        }
    }
    
    /**
     * Check if metric requires immediate attention
     */
    public boolean requiresImmediateAttention() {
        return status == PerformanceStatus.CRITICAL || 
               hasP0Anomalies() ||
               isServiceDegraded();
    }
    
    /**
     * Check for P0 (highest priority) anomalies
     */
    public boolean hasP0Anomalies() {
        return anomalies.stream()
            .anyMatch(anomaly -> anomaly.getSeverity() == AnomalySeverity.P0);
    }
    
    /**
     * Determine if service is degraded
     */
    public boolean isServiceDegraded() {
        if (metricType == MetricType.ERROR_RATE) {
            return value.getValue() > 5.0; // More than 5% error rate
        } else if (metricType == MetricType.RESPONSE_TIME) {
            return value.getValue() > 3000; // More than 3 seconds
        } else if (metricType == MetricType.AVAILABILITY) {
            return value.getValue() < 99.0; // Less than 99% availability
        }
        return false;
    }
    
    /**
     * Calculate SLA compliance
     */
    public double calculateSLACompliance() {
        if (metricType == MetricType.AVAILABILITY) {
            return Math.min(100.0, (value.getValue() / 99.9) * 100);
        } else if (metricType == MetricType.ERROR_RATE) {
            double targetErrorRate = 0.1; // 0.1% target
            return Math.max(0, 100 - ((value.getValue() - targetErrorRate) * 100));
        }
        return 100.0;
    }
    
    /**
     * Get performance trend analysis
     */
    public PerformanceTrend analyzeTrend(List<PerformanceMetric> historicalMetrics) {
        if (historicalMetrics.isEmpty()) {
            return PerformanceTrend.STABLE;
        }
        
        double average = historicalMetrics.stream()
            .mapToDouble(m -> m.getValue().getValue())
            .average()
            .orElse(value.getValue());
        
        double currentValue = value.getValue();
        double percentageChange = ((currentValue - average) / average) * 100;
        
        if (metricType.isHigherBetter()) {
            if (percentageChange > 10) return PerformanceTrend.IMPROVING;
            if (percentageChange < -10) return PerformanceTrend.DEGRADING;
        } else {
            if (percentageChange < -10) return PerformanceTrend.IMPROVING;
            if (percentageChange > 10) return PerformanceTrend.DEGRADING;
        }
        
        return PerformanceTrend.STABLE;
    }
    
    /**
     * Calculate resource utilization percentage
     */
    public double calculateUtilizationPercentage() {
        if (metricType == MetricType.CPU_USAGE || 
            metricType == MetricType.MEMORY_USAGE || 
            metricType == MetricType.DISK_USAGE) {
            return value.getValue();
        }
        return 0.0;
    }
    
    /**
     * Determine if auto-scaling should be triggered
     */
    public boolean shouldTriggerAutoScaling() {
        if (metricType == MetricType.CPU_USAGE && value.getValue() > 80) {
            return true;
        }
        if (metricType == MetricType.MEMORY_USAGE && value.getValue() > 85) {
            return true;
        }
        if (metricType == MetricType.REQUEST_RATE && value.getValue() > thresholds.getMaxThreshold() * 0.9) {
            return true;
        }
        return false;
    }
    
    /**
     * Get alert priority based on metric status and type
     */
    public AlertPriority getAlertPriority() {
        if (status == PerformanceStatus.CRITICAL) {
            return AlertPriority.P1;
        } else if (status == PerformanceStatus.WARNING && metricType.isCriticalMetric()) {
            return AlertPriority.P2;
        } else if (status == PerformanceStatus.WARNING) {
            return AlertPriority.P3;
        }
        return AlertPriority.P4;
    }
    
    /**
     * Calculate time until threshold breach
     */
    public Duration estimateTimeToThresholdBreach(double rateOfChange) {
        if (rateOfChange == 0) {
            return Duration.ofDays(365); // No change means no breach
        }
        
        double currentValue = value.getValue();
        double threshold = metricType.isHigherBetter() 
            ? thresholds.getMinThreshold() 
            : thresholds.getMaxThreshold();
        
        double timeToBreach = Math.abs((threshold - currentValue) / rateOfChange);
        return Duration.ofMinutes((long) timeToBreach);
    }
    
    // Getters
    public MetricId getId() { return id; }
    public String getMetricName() { return metricName; }
    public MetricType getMetricType() { return metricType; }
    public PerformanceValue getValue() { return value; }
    public PerformanceThresholds getThresholds() { return thresholds; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSourceDomain() { return sourceDomain; }
    public String getSourceService() { return sourceService; }
    public Map<String, String> getTags() { return tags; }
    public List<PerformanceAnomaly> getAnomalies() { return new ArrayList<>(anomalies); }
    public PerformanceStatus getStatus() { return status; }
    
    // Builder
    public static class Builder {
        private MetricId id;
        private String metricName;
        private MetricType metricType;
        private PerformanceValue value;
        private PerformanceThresholds thresholds;
        private LocalDateTime timestamp = LocalDateTime.now();
        private String sourceDomain;
        private String sourceService;
        private Map<String, String> tags = Map.of();
        private List<PerformanceAnomaly> anomalies = new ArrayList<>();
        
        public Builder withId(MetricId id) {
            this.id = id;
            return this;
        }
        
        public Builder withMetricName(String metricName) {
            this.metricName = metricName;
            return this;
        }
        
        public Builder withMetricType(MetricType metricType) {
            this.metricType = metricType;
            return this;
        }
        
        public Builder withValue(PerformanceValue value) {
            this.value = value;
            return this;
        }
        
        public Builder withThresholds(PerformanceThresholds thresholds) {
            this.thresholds = thresholds;
            return this;
        }
        
        public Builder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder withSource(String domain, String service) {
            this.sourceDomain = domain;
            this.sourceService = service;
            return this;
        }
        
        public Builder withTags(Map<String, String> tags) {
            this.tags = tags;
            return this;
        }
        
        public Builder withAnomalies(List<PerformanceAnomaly> anomalies) {
            this.anomalies = anomalies;
            return this;
        }
        
        public PerformanceMetric build() {
            return new PerformanceMetric(this);
        }
    }
}