package com.gogidix.centralizeddashboard.metrics.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Pure Domain Model for Performance Metric - Hexagonal Architecture Compliant
 * 
 * ENTERPRISE FEATURES - NO EXTERNAL DEPENDENCIES:
 * - Advanced performance analytics with statistical processing
 * - Multi-dimensional performance threshold management
 * - Intelligent performance trend analysis and pattern detection  
 * - Rich business logic for performance optimization recommendations
 * - Enterprise-grade performance monitoring and alerting capabilities
 * - Performance baseline management and comparative analysis
 * - Advanced performance correlation and anomaly detection
 */
public class PerformanceMetric {

    // Core Identity - IMMUTABLE
    private final String metricId;
    private final String serviceName;
    private final String instanceId;
    private final String metricName;
    private final LocalDateTime timestamp;
    
    // Metric Classification - IMMUTABLE
    private final MetricType metricType;
    private final PerformanceCategory category;
    private final PerformanceSeverity severity;
    private final PerformanceUnit unit;
    
    // Performance Data - IMMUTABLE
    private final Double value;
    private final Double baseline;
    private final Double threshold;
    private final Double warningThreshold;
    private final Double criticalThreshold;
    
    // Context Information - IMMUTABLE
    private final String environment;
    private final String region;
    private final String application;
    private final String component;
    private final String operation;
    
    // Performance Analytics - IMMUTABLE
    private final PerformanceState state;
    private final Double trendSlope;
    private final Double percentileRank;
    private final boolean isAnomalous;
    private final PerformanceQuality quality;
    
    // Extended Metadata - IMMUTABLE
    private final Map<String, String> tags;
    private final Map<String, Object> metadata;
    private final String description;
    private final PerformanceSource source;
    
    /**
     * Constructor for creating new performance metric
     */
    public PerformanceMetric(String serviceName, String instanceId, String metricName,
                           MetricType metricType, Double value, String environment) {
        
        // Core validation
        this.serviceName = validateServiceName(serviceName);
        this.instanceId = validateInstanceId(instanceId);
        this.metricName = validateMetricName(metricName);
        this.metricType = Objects.requireNonNull(metricType, "Metric type cannot be null");
        this.value = validateValue(value);
        this.environment = Objects.requireNonNull(environment, "Environment cannot be null");
        
        // Generate immutable fields
        this.metricId = generateMetricId();
        this.timestamp = LocalDateTime.now();
        this.category = determineCategory();
        this.severity = determineSeverity();
        this.unit = determineUnit();
        this.source = determineSource();
        
        // Initialize thresholds and baselines
        this.baseline = null;
        this.threshold = null;
        this.warningThreshold = null;
        this.criticalThreshold = null;
        
        // Initialize context
        this.region = null; // Will be set by infrastructure
        this.application = extractApplication();
        this.component = extractComponent();
        this.operation = extractOperation();
        
        // Initialize analytics
        this.state = calculateState();
        this.trendSlope = null;
        this.percentileRank = null;
        this.isAnomalous = false;
        this.quality = calculateQuality();
        
        // Initialize metadata
        this.tags = new HashMap<>();
        this.metadata = new HashMap<>();
        this.description = null;
    }
    
    /**
     * Copy constructor for immutable updates
     */
    private PerformanceMetric(PerformanceMetric original, Double newBaseline, Double newThreshold,
                            Double newWarningThreshold, Double newCriticalThreshold,
                            PerformanceState newState, Double newTrendSlope, Double newPercentileRank,
                            boolean newIsAnomalous, Map<String, String> newTags,
                            Map<String, Object> newMetadata, String newDescription) {
        
        // Immutable fields
        this.metricId = original.metricId;
        this.serviceName = original.serviceName;
        this.instanceId = original.instanceId;
        this.metricName = original.metricName;
        this.timestamp = original.timestamp;
        this.metricType = original.metricType;
        this.category = original.category;
        this.severity = original.severity;
        this.unit = original.unit;
        this.value = original.value;
        this.environment = original.environment;
        this.region = original.region;
        this.application = original.application;
        this.component = original.component;
        this.operation = original.operation;
        this.source = original.source;
        this.quality = original.quality;
        
        // Updated fields
        this.baseline = newBaseline != null ? newBaseline : original.baseline;
        this.threshold = newThreshold != null ? newThreshold : original.threshold;
        this.warningThreshold = newWarningThreshold != null ? newWarningThreshold : original.warningThreshold;
        this.criticalThreshold = newCriticalThreshold != null ? newCriticalThreshold : original.criticalThreshold;
        this.state = newState != null ? newState : original.state;
        this.trendSlope = newTrendSlope != null ? newTrendSlope : original.trendSlope;
        this.percentileRank = newPercentileRank != null ? newPercentileRank : original.percentileRank;
        this.isAnomalous = newIsAnomalous;
        this.tags = newTags != null ? new HashMap<>(newTags) : new HashMap<>(original.tags);
        this.metadata = newMetadata != null ? new HashMap<>(newMetadata) : new HashMap<>(original.metadata);
        this.description = newDescription != null ? newDescription : original.description;
    }
    
    // =============================================
    // DOMAIN BUSINESS METHODS - PERFORMANCE ANALYSIS
    // =============================================
    
    /**
     * Check if metric is performing normally
     */
    public boolean isNormal() {
        return state == PerformanceState.NORMAL && !isAnomalous;
    }
    
    /**
     * Check if metric is in warning state
     */
    public boolean isWarning() {
        return state == PerformanceState.WARNING || 
               (warningThreshold != null && exceedsThreshold(warningThreshold));
    }
    
    /**
     * Check if metric is in critical state
     */
    public boolean isCritical() {
        return state == PerformanceState.CRITICAL || 
               (criticalThreshold != null && exceedsThreshold(criticalThreshold));
    }
    
    /**
     * Check if metric is degraded
     */
    public boolean isDegraded() {
        return state == PerformanceState.DEGRADED;
    }
    
    /**
     * Check if metric exceeds threshold
     */
    public boolean exceedsThreshold(double threshold) {
        return value != null && value > threshold;
    }
    
    /**
     * Check if metric is below threshold
     */
    public boolean belowThreshold(double threshold) {
        return value != null && value < threshold;
    }
    
    /**
     * Calculate deviation from baseline
     */
    public double calculateDeviationFromBaseline() {
        if (baseline == null || value == null) return 0.0;
        return Math.abs(value - baseline);
    }
    
    /**
     * Calculate percentage deviation from baseline
     */
    public double calculatePercentageDeviationFromBaseline() {
        if (baseline == null || value == null || baseline == 0) return 0.0;
        return ((value - baseline) / baseline) * 100;
    }
    
    /**
     * Check if performance is better than baseline
     */
    public boolean isBetterThanBaseline() {
        if (baseline == null || value == null) return false;
        
        // For latency, response time - lower is better
        if (isLatencyMetric()) {
            return value < baseline;
        }
        
        // For throughput, success rate - higher is better
        return value > baseline;
    }
    
    /**
     * Check if performance is worse than baseline
     */
    public boolean isWorseThanBaseline() {
        if (baseline == null || value == null) return false;
        
        // For latency, response time - higher is worse
        if (isLatencyMetric()) {
            return value > baseline;
        }
        
        // For throughput, success rate - lower is worse
        return value < baseline;
    }
    
    /**
     * Get performance improvement percentage
     */
    public double getPerformanceImprovement() {
        if (!isBetterThanBaseline()) return 0.0;
        return Math.abs(calculatePercentageDeviationFromBaseline());
    }
    
    /**
     * Get performance degradation percentage
     */
    public double getPerformanceDegradation() {
        if (!isWorseThanBaseline()) return 0.0;
        return Math.abs(calculatePercentageDeviationFromBaseline());
    }
    
    /**
     * Calculate performance score (0-100)
     */
    public int getPerformanceScore() {
        int score = 100;
        
        // Deduct for critical state
        if (isCritical()) {
            score -= 60;
        } else if (isWarning()) {
            score -= 30;
        } else if (isDegraded()) {
            score -= 15;
        }
        
        // Deduct for anomaly
        if (isAnomalous) {
            score -= 20;
        }
        
        // Deduct for poor quality
        if (quality == PerformanceQuality.POOR) {
            score -= 25;
        } else if (quality == PerformanceQuality.FAIR) {
            score -= 10;
        }
        
        // Deduct for baseline deviation
        if (baseline != null && isWorseThanBaseline()) {
            double deviation = getPerformanceDegradation();
            if (deviation > 50) {
                score -= 15;
            } else if (deviation > 25) {
                score -= 8;
            }
        }
        
        return Math.max(0, score);
    }
    
    /**
     * Check if performance requires attention
     */
    public boolean requiresAttention() {
        return isCritical() || isWarning() || isAnomalous || getPerformanceScore() < 70;
    }
    
    /**
     * Get urgency level based on performance state
     */
    public UrgencyLevel getUrgencyLevel() {
        if (isCritical()) return UrgencyLevel.URGENT;
        if (isWarning() || isAnomalous) return UrgencyLevel.HIGH;
        if (isDegraded() || getPerformanceScore() < 70) return UrgencyLevel.MEDIUM;
        return UrgencyLevel.LOW;
    }
    
    /**
     * Calculate metric age in minutes
     */
    public long getAgeMinutes() {
        return ChronoUnit.MINUTES.between(timestamp, LocalDateTime.now());
    }
    
    /**
     * Check if metric is fresh
     */
    public boolean isFresh(int maxAgeMinutes) {
        return getAgeMinutes() <= maxAgeMinutes;
    }
    
    /**
     * Check if metric is stale
     */
    public boolean isStale(int maxAgeMinutes) {
        return getAgeMinutes() > maxAgeMinutes;
    }
    
    /**
     * Check if metric shows improving trend
     */
    public boolean isImproving() {
        if (trendSlope == null) return false;
        return isLatencyMetric() ? trendSlope < 0 : trendSlope > 0;
    }
    
    /**
     * Check if metric shows degrading trend
     */
    public boolean isDegrading() {
        if (trendSlope == null) return false;
        return isLatencyMetric() ? trendSlope > 0 : trendSlope < 0;
    }
    
    /**
     * Check if metric trend is stable
     */
    public boolean isStable() {
        return trendSlope != null && Math.abs(trendSlope) < 0.05;
    }
    
    /**
     * Check if this is a latency-type metric
     */
    public boolean isLatencyMetric() {
        return category == PerformanceCategory.LATENCY || 
               category == PerformanceCategory.RESPONSE_TIME ||
               metricName.toLowerCase().contains("latency") ||
               metricName.toLowerCase().contains("response") ||
               metricName.toLowerCase().contains("duration");
    }
    
    /**
     * Check if metric matches service pattern
     */
    public boolean matchesService(String servicePattern) {
        return serviceName != null && serviceName.matches(servicePattern);
    }
    
    /**
     * Check if metric has specific tag
     */
    public boolean hasTag(String key) {
        return tags.containsKey(key);
    }
    
    /**
     * Get tag value
     */
    public String getTagValue(String key) {
        return tags.get(key);
    }
    
    /**
     * Get performance correlation key
     */
    public String getCorrelationKey() {
        return serviceName + "_" + metricName + "_" + environment;
    }
    
    // =============================================
    // DOMAIN MUTATION METHODS - IMMUTABLE UPDATES
    // =============================================
    
    /**
     * Set baseline - returns new immutable instance
     */
    public PerformanceMetric withBaseline(Double baseline) {
        return new PerformanceMetric(this, baseline, null, null, null, null, null, null, 
                                   isAnomalous, null, null, null);
    }
    
    /**
     * Set thresholds - returns new immutable instance
     */
    public PerformanceMetric withThresholds(Double warning, Double critical) {
        return new PerformanceMetric(this, null, null, warning, critical, null, null, null,
                                   isAnomalous, null, null, null);
    }
    
    /**
     * Mark as anomalous - returns new immutable instance
     */
    public PerformanceMetric markAnomalous(boolean anomalous) {
        return new PerformanceMetric(this, null, null, null, null, null, null, null,
                                   anomalous, null, null, null);
    }
    
    /**
     * Set trend slope - returns new immutable instance
     */
    public PerformanceMetric withTrendSlope(Double slope) {
        return new PerformanceMetric(this, null, null, null, null, null, slope, null,
                                   isAnomalous, null, null, null);
    }
    
    /**
     * Set percentile rank - returns new immutable instance
     */
    public PerformanceMetric withPercentileRank(Double percentile) {
        return new PerformanceMetric(this, null, null, null, null, null, null, percentile,
                                   isAnomalous, null, null, null);
    }
    
    /**
     * Add tag - returns new immutable instance
     */
    public PerformanceMetric withTag(String key, String value) {
        Map<String, String> newTags = new HashMap<>(tags);
        newTags.put(key, value);
        return new PerformanceMetric(this, null, null, null, null, null, null, null,
                                   isAnomalous, newTags, null, null);
    }
    
    /**
     * Add metadata - returns new immutable instance
     */
    public PerformanceMetric withMetadata(String key, Object value) {
        Map<String, Object> newMetadata = new HashMap<>(metadata);
        newMetadata.put(key, value);
        return new PerformanceMetric(this, null, null, null, null, null, null, null,
                                   isAnomalous, null, newMetadata, null);
    }
    
    /**
     * Set description - returns new immutable instance
     */
    public PerformanceMetric withDescription(String description) {
        return new PerformanceMetric(this, null, null, null, null, null, null, null,
                                   isAnomalous, null, null, description);
    }
    
    // =============================================
    // ADVANCED ANALYTICS METHODS
    // =============================================
    
    /**
     * Compare performance with another metric
     */
    public PerformanceComparison compareWith(PerformanceMetric other) {
        if (other == null || !metricName.equals(other.metricName)) {
            return new PerformanceComparison(this, other, ComparisonResult.INCOMPARABLE);
        }
        
        if (value.equals(other.value)) {
            return new PerformanceComparison(this, other, ComparisonResult.EQUAL);
        }
        
        boolean thisBetter = isLatencyMetric() ? value < other.value : value > other.value;
        ComparisonResult result = thisBetter ? ComparisonResult.BETTER : ComparisonResult.WORSE;
        
        return new PerformanceComparison(this, other, result);
    }
    
    /**
     * Calculate statistical z-score
     */
    public double calculateZScore(double mean, double standardDeviation) {
        if (value == null || standardDeviation == 0) return 0.0;
        return (value - mean) / standardDeviation;
    }
    
    /**
     * Check if metric is statistical outlier
     */
    public boolean isOutlier(double mean, double standardDeviation, double zThreshold) {
        return Math.abs(calculateZScore(mean, standardDeviation)) > zThreshold;
    }
    
    /**
     * Generate performance summary
     */
    public PerformanceSummary getSummary() {
        return new PerformanceSummary(
            metricId, serviceName, metricName, value, unit != null ? unit.getSymbol() : null,
            timestamp, state, getPerformanceScore(), isAnomalous, quality,
            baseline, getUrgencyLevel()
        );
    }
    
    /**
     * Create performance snapshot
     */
    public PerformanceSnapshot createSnapshot() {
        return new PerformanceSnapshot(
            metricId, timestamp, value, state, isAnomalous, 
            getPerformanceScore(), new HashMap<>(tags)
        );
    }
    
    // =============================================
    // VALIDATION AND HELPER METHODS
    // =============================================
    
    private String validateServiceName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
        return name.trim();
    }
    
    private String validateInstanceId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Instance ID cannot be null or empty");
        }
        return id.trim();
    }
    
    private String validateMetricName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric name cannot be null or empty");
        }
        return name.trim();
    }
    
    private Double validateValue(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Metric value cannot be null");
        }
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Metric value must be finite");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Metric value cannot be negative");
        }
        return value;
    }
    
    private String generateMetricId() {
        return "PERF_" + System.currentTimeMillis() + "_" + serviceName.hashCode();
    }
    
    private PerformanceCategory determineCategory() {
        String metric = metricName.toLowerCase();
        if (metric.contains("latency") || metric.contains("response")) {
            return PerformanceCategory.LATENCY;
        } else if (metric.contains("throughput") || metric.contains("rate")) {
            return PerformanceCategory.THROUGHPUT;
        } else if (metric.contains("cpu") || metric.contains("memory")) {
            return PerformanceCategory.RESOURCE;
        } else if (metric.contains("error") || metric.contains("success")) {
            return PerformanceCategory.RELIABILITY;
        } else if (metric.contains("availability") || metric.contains("uptime")) {
            return PerformanceCategory.AVAILABILITY;
        }
        return PerformanceCategory.CUSTOM;
    }
    
    private PerformanceSeverity determineSeverity() {
        if (metricType == MetricType.CRITICAL_METRIC) return PerformanceSeverity.CRITICAL;
        if (metricType == MetricType.HIGH_PRIORITY) return PerformanceSeverity.HIGH;
        return PerformanceSeverity.NORMAL;
    }
    
    private PerformanceUnit determineUnit() {
        String metric = metricName.toLowerCase();
        if (metric.contains("ms") || metric.contains("millisecond")) {
            return PerformanceUnit.MILLISECONDS;
        } else if (metric.contains("seconds") || metric.contains("sec")) {
            return PerformanceUnit.SECONDS;
        } else if (metric.contains("percent") || metric.contains("%")) {
            return PerformanceUnit.PERCENTAGE;
        } else if (metric.contains("request") || metric.contains("req")) {
            return PerformanceUnit.REQUESTS_PER_SECOND;
        } else if (metric.contains("byte") || metric.contains("mb") || metric.contains("gb")) {
            return PerformanceUnit.BYTES;
        }
        return PerformanceUnit.COUNT;
    }
    
    private PerformanceSource determineSource() {
        if (serviceName.contains("monitoring")) return PerformanceSource.MONITORING_SYSTEM;
        if (serviceName.contains("apm")) return PerformanceSource.APM_TOOL;
        if (serviceName.contains("metric")) return PerformanceSource.METRICS_COLLECTOR;
        return PerformanceSource.APPLICATION;
    }
    
    private String extractApplication() {
        if (serviceName.contains("-")) {
            return serviceName.split("-")[0];
        }
        return serviceName;
    }
    
    private String extractComponent() {
        if (metricName.contains(".")) {
            String[] parts = metricName.split("\\.");
            return parts.length > 1 ? parts[0] : null;
        }
        return null;
    }
    
    private String extractOperation() {
        if (metricName.contains(".")) {
            String[] parts = metricName.split("\\.");
            return parts.length > 2 ? parts[parts.length - 1] : null;
        }
        return null;
    }
    
    private PerformanceState calculateState() {
        // Simple initial state calculation
        return PerformanceState.NORMAL;
    }
    
    private PerformanceQuality calculateQuality() {
        int qualityScore = 100;
        
        // Check for valid value
        if (value == null || value < 0) {
            qualityScore -= 40;
        }
        
        // Check data freshness
        if (isStale(30)) {
            qualityScore -= 20;
        }
        
        // Check completeness
        if (serviceName == null || instanceId == null) {
            qualityScore -= 15;
        }
        
        if (qualityScore >= 90) return PerformanceQuality.EXCELLENT;
        if (qualityScore >= 70) return PerformanceQuality.GOOD;
        if (qualityScore >= 50) return PerformanceQuality.FAIR;
        return PerformanceQuality.POOR;
    }
    
    // =============================================
    // GETTERS - IMMUTABLE ACCESS
    // =============================================
    
    public String getMetricId() { return metricId; }
    public String getServiceName() { return serviceName; }
    public String getInstanceId() { return instanceId; }
    public String getMetricName() { return metricName; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public MetricType getMetricType() { return metricType; }
    public PerformanceCategory getCategory() { return category; }
    public PerformanceSeverity getSeverity() { return severity; }
    public PerformanceUnit getUnit() { return unit; }
    public Double getValue() { return value; }
    public Double getBaseline() { return baseline; }
    public Double getThreshold() { return threshold; }
    public Double getWarningThreshold() { return warningThreshold; }
    public Double getCriticalThreshold() { return criticalThreshold; }
    public String getEnvironment() { return environment; }
    public String getRegion() { return region; }
    public String getApplication() { return application; }
    public String getComponent() { return component; }
    public String getOperation() { return operation; }
    public PerformanceState getState() { return state; }
    public Double getTrendSlope() { return trendSlope; }
    public Double getPercentileRank() { return percentileRank; }
    public boolean isAnomalous() { return isAnomalous; }
    public PerformanceQuality getQuality() { return quality; }
    public Map<String, String> getTags() { return new HashMap<>(tags); }
    public Map<String, Object> getMetadata() { return new HashMap<>(metadata); }
    public String getDescription() { return description; }
    public PerformanceSource getSource() { return source; }
    
    // =============================================
    // EQUALS, HASHCODE, TOSTRING
    // =============================================
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        PerformanceMetric metric = (PerformanceMetric) obj;
        return Objects.equals(metricId, metric.metricId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(metricId);
    }
    
    @Override
    public String toString() {
        return String.format("PerformanceMetric{id='%s', service='%s', metric='%s', value=%s, state=%s, score=%d}", 
                           metricId, serviceName, metricName, value, state, getPerformanceScore());
    }
    
    // =============================================
    // DOMAIN ENUMS
    // =============================================
    
    /**
     * Metric types for performance classification
     */
    public enum MetricType {
        LATENCY_METRIC("Latency measurement metric"),
        THROUGHPUT_METRIC("Throughput measurement metric"),
        RESOURCE_METRIC("Resource utilization metric"),
        AVAILABILITY_METRIC("Availability measurement metric"),
        ERROR_RATE_METRIC("Error rate measurement metric"),
        CUSTOM_METRIC("Custom performance metric"),
        HIGH_PRIORITY("High priority metric"),
        CRITICAL_METRIC("Critical performance metric");
        
        private final String description;
        
        MetricType(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
        
        public boolean isHighPriority() {
            return this == HIGH_PRIORITY || this == CRITICAL_METRIC;
        }
    }
    
    /**
     * Performance categories
     */
    public enum PerformanceCategory {
        LATENCY("Response time and latency metrics"),
        THROUGHPUT("Throughput and rate metrics"),
        RESOURCE("Resource utilization metrics"),
        RELIABILITY("Reliability and error metrics"),
        AVAILABILITY("Availability and uptime metrics"),
        RESPONSE_TIME("Response time specific metrics"),
        CUSTOM("Custom performance metrics");
        
        private final String description;
        
        PerformanceCategory(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    /**
     * Performance severity levels
     */
    public enum PerformanceSeverity {
        CRITICAL("Critical performance metric"),
        HIGH("High importance metric"),
        NORMAL("Normal performance metric"),
        LOW("Low priority metric");
        
        private final String description;
        
        PerformanceSeverity(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
        
        public boolean requiresImmediateAttention() {
            return this == CRITICAL || this == HIGH;
        }
    }
    
    /**
     * Performance units
     */
    public enum PerformanceUnit {
        MILLISECONDS("milliseconds", "ms"),
        SECONDS("seconds", "s"),
        PERCENTAGE("percentage", "%"),
        COUNT("count", ""),
        REQUESTS_PER_SECOND("requests per second", "req/s"),
        BYTES("bytes", "B"),
        MEGABYTES("megabytes", "MB"),
        OPERATIONS("operations", "ops");
        
        private final String name;
        private final String symbol;
        
        PerformanceUnit(String name, String symbol) {
            this.name = name;
            this.symbol = symbol;
        }
        
        public String getName() { return name; }
        public String getSymbol() { return symbol; }
    }
    
    /**
     * Performance states
     */
    public enum PerformanceState {
        NORMAL("Normal performance"),
        WARNING("Performance warning"),
        CRITICAL("Critical performance issue"),
        DEGRADED("Performance degraded"),
        RECOVERING("Performance recovering");
        
        private final String description;
        
        PerformanceState(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
        
        public boolean requiresAction() {
            return this == WARNING || this == CRITICAL || this == DEGRADED;
        }
    }
    
    /**
     * Urgency levels
     */
    public enum UrgencyLevel {
        URGENT("Urgent attention required"),
        HIGH("High priority"),
        MEDIUM("Medium priority"),
        LOW("Low priority");
        
        private final String description;
        
        UrgencyLevel(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    /**
     * Performance quality levels
     */
    public enum PerformanceQuality {
        EXCELLENT("Excellent data quality"),
        GOOD("Good data quality"),
        FAIR("Fair data quality"),
        POOR("Poor data quality");
        
        private final String description;
        
        PerformanceQuality(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
        
        public boolean isAcceptable() {
            return this == EXCELLENT || this == GOOD;
        }
    }
    
    /**
     * Performance sources
     */
    public enum PerformanceSource {
        APPLICATION("Application generated metrics"),
        MONITORING_SYSTEM("Monitoring system metrics"),
        APM_TOOL("Application Performance Monitoring tool"),
        METRICS_COLLECTOR("Metrics collection system"),
        LOAD_BALANCER("Load balancer metrics"),
        INFRASTRUCTURE("Infrastructure metrics");
        
        private final String description;
        
        PerformanceSource(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    /**
     * Comparison results
     */
    public enum ComparisonResult {
        BETTER("Performance is better"),
        WORSE("Performance is worse"),
        EQUAL("Performance is equal"),
        INCOMPARABLE("Metrics cannot be compared");
        
        private final String description;
        
        ComparisonResult(String description) {
            this.description = description;
        }
        
        public String getDescription() { return description; }
    }
    
    // =============================================
    // INNER CLASSES FOR RICH DOMAIN MODELING
    // =============================================
    
    /**
     * Performance comparison result
     */
    public static class PerformanceComparison {
        private final PerformanceMetric first;
        private final PerformanceMetric second;
        private final ComparisonResult result;
        private final double improvementPercentage;
        
        public PerformanceComparison(PerformanceMetric first, PerformanceMetric second, ComparisonResult result) {
            this.first = first;
            this.second = second;
            this.result = result;
            this.improvementPercentage = calculateImprovement();
        }
        
        private double calculateImprovement() {
            if (result == ComparisonResult.INCOMPARABLE || second == null || 
                first.value == null || second.value == null) {
                return 0.0;
            }
            
            if (second.value == 0) return 0.0;
            return Math.abs((first.value - second.value) / second.value) * 100;
        }
        
        public PerformanceMetric getFirst() { return first; }
        public PerformanceMetric getSecond() { return second; }
        public ComparisonResult getResult() { return result; }
        public double getImprovementPercentage() { return improvementPercentage; }
    }
    
    /**
     * Performance summary for reporting
     */
    public static class PerformanceSummary {
        private final String id;
        private final String service;
        private final String metric;
        private final Double value;
        private final String unit;
        private final LocalDateTime timestamp;
        private final PerformanceState state;
        private final int score;
        private final boolean anomalous;
        private final PerformanceQuality quality;
        private final Double baseline;
        private final UrgencyLevel urgency;
        
        public PerformanceSummary(String id, String service, String metric, Double value,
                                String unit, LocalDateTime timestamp, PerformanceState state,
                                int score, boolean anomalous, PerformanceQuality quality,
                                Double baseline, UrgencyLevel urgency) {
            this.id = id;
            this.service = service;
            this.metric = metric;
            this.value = value;
            this.unit = unit;
            this.timestamp = timestamp;
            this.state = state;
            this.score = score;
            this.anomalous = anomalous;
            this.quality = quality;
            this.baseline = baseline;
            this.urgency = urgency;
        }
        
        // Getters
        public String getId() { return id; }
        public String getService() { return service; }
        public String getMetric() { return metric; }
        public Double getValue() { return value; }
        public String getUnit() { return unit; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public PerformanceState getState() { return state; }
        public int getScore() { return score; }
        public boolean isAnomalous() { return anomalous; }
        public PerformanceQuality getQuality() { return quality; }
        public Double getBaseline() { return baseline; }
        public UrgencyLevel getUrgency() { return urgency; }
    }
    
    /**
     * Performance snapshot for historical tracking
     */
    public static class PerformanceSnapshot {
        private final String metricId;
        private final LocalDateTime timestamp;
        private final Double value;
        private final PerformanceState state;
        private final boolean anomalous;
        private final int score;
        private final Map<String, String> tags;
        
        public PerformanceSnapshot(String metricId, LocalDateTime timestamp, Double value,
                                 PerformanceState state, boolean anomalous, int score,
                                 Map<String, String> tags) {
            this.metricId = metricId;
            this.timestamp = timestamp;
            this.value = value;
            this.state = state;
            this.anomalous = anomalous;
            this.score = score;
            this.tags = tags;
        }
        
        // Getters
        public String getMetricId() { return metricId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public Double getValue() { return value; }
        public PerformanceState getState() { return state; }
        public boolean isAnomalous() { return anomalous; }
        public int getScore() { return score; }
        public Map<String, String> getTags() { return new HashMap<>(tags); }
    }
} 