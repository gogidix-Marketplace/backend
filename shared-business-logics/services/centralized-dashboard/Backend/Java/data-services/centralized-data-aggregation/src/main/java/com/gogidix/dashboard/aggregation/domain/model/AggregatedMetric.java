package com.gogidix.dashboard.aggregation.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Aggregated Metric Domain Entity
 * 
 * Core business entity for data aggregation with sophisticated business rules
 */
public class AggregatedMetric {
    private final MetricId id;
    private final MetricType type;
    private final String name;
    private final String dimension;
    private final MetricValue value;
    private final TimeWindow timeWindow;
    private final AggregationFunction aggregationFunction;
    private final LocalDateTime createdAt;
    private final Map<String, Object> attributes;
    private final DataQuality dataQuality;
    private final AggregationSource source;
    
    public AggregatedMetric(MetricId id, MetricType type, String name, String dimension,
                           MetricValue value, TimeWindow timeWindow, AggregationFunction aggregationFunction,
                           LocalDateTime createdAt, Map<String, Object> attributes,
                           DataQuality dataQuality, AggregationSource source) {
        this.id = Objects.requireNonNull(id, "Metric ID cannot be null");
        this.type = Objects.requireNonNull(type, "Metric type cannot be null");
        this.name = validateName(name);
        this.dimension = validateDimension(dimension);
        this.value = Objects.requireNonNull(value, "Metric value cannot be null");
        this.timeWindow = Objects.requireNonNull(timeWindow, "Time window cannot be null");
        this.aggregationFunction = Objects.requireNonNull(aggregationFunction, "Aggregation function cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created timestamp cannot be null");
        this.attributes = Map.copyOf(attributes != null ? attributes : Map.of());
        this.dataQuality = Objects.requireNonNull(dataQuality, "Data quality cannot be null");
        this.source = Objects.requireNonNull(source, "Aggregation source cannot be null");
    }
    
    /**
     * Check if this metric is suitable for alerting
     */
    public boolean isSuitableForAlerting() {
        return dataQuality.isHighQuality() && 
               timeWindow.isRecent() &&
               value.isValid() &&
               type.supportsAlerting();
    }
    
    /**
     * Check if this metric requires immediate attention
     */
    public boolean requiresImmediateAttention() {
        return isSuitableForAlerting() && 
               value.isOutsideNormalRange() &&
               dataQuality.getConfidenceLevel() > 0.9;
    }
    
    /**
     * Check if metric data is current
     */
    public boolean isDataCurrent() {
        return timeWindow.isWithinExpectedFreshness();
    }
    
    /**
     * Check if metric can be compared with others of same type
     */
    public boolean isComparableWith(AggregatedMetric other) {
        return this.type == other.type &&
               this.dimension.equals(other.dimension) &&
               this.aggregationFunction == other.aggregationFunction &&
               this.timeWindow.isCompatibleWith(other.timeWindow);
    }
    
    /**
     * Calculate percentage change from another metric
     */
    public Double calculatePercentageChange(AggregatedMetric baseline) {
        if (!isComparableWith(baseline)) {
            return null;
        }
        
        return value.calculatePercentageChange(baseline.value);
    }
    
    /**
     * Check if this metric shows significant change from baseline
     */
    public boolean showsSignificantChange(AggregatedMetric baseline, double threshold) {
        Double percentageChange = calculatePercentageChange(baseline);
        return percentageChange != null && Math.abs(percentageChange) >= threshold;
    }
    
    /**
     * Get metric freshness in minutes
     */
    public long getFreshnessMinutes() {
        return timeWindow.getFreshnessMinutes();
    }
    
    /**
     * Check if metric needs re-aggregation
     */
    public boolean needsReAggregation() {
        return !isDataCurrent() || dataQuality.isLow();
    }
    
    /**
     * Get business criticality level
     */
    public CriticalityLevel getCriticalityLevel() {
        if (type.isBusinessCritical() && dimension.contains("revenue") || dimension.contains("customer")) {
            return CriticalityLevel.HIGH;
        }
        if (type.isOperational() && dataQuality.isHighQuality()) {
            return CriticalityLevel.MEDIUM;
        }
        return CriticalityLevel.LOW;
    }
    
    /**
     * Create updated metric with new value (immutable update)
     */
    public AggregatedMetric withNewValue(MetricValue newValue, LocalDateTime timestamp) {
        TimeWindow newTimeWindow = timeWindow.updateEndTime(timestamp);
        DataQuality updatedQuality = dataQuality.withNewDataPoint();
        
        return new AggregatedMetric(id, type, name, dimension, newValue, newTimeWindow,
                                  aggregationFunction, timestamp, attributes, updatedQuality, source);
    }
    
    /**
     * Add attribute to the metric (creates new instance)
     */
    public AggregatedMetric withAttribute(String key, Object value) {
        Map<String, Object> newAttributes = new java.util.HashMap<>(attributes);
        newAttributes.put(key, value);
        
        return new AggregatedMetric(id, type, name, dimension, this.value, timeWindow,
                                  aggregationFunction, createdAt, newAttributes, dataQuality, source);
    }
    
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Metric name cannot be null or empty");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Metric name cannot exceed 255 characters");
        }
        return name.trim();
    }
    
    private String validateDimension(String dimension) {
        if (dimension == null || dimension.trim().isEmpty()) {
            throw new IllegalArgumentException("Dimension cannot be null or empty");
        }
        if (dimension.length() > 100) {
            throw new IllegalArgumentException("Dimension cannot exceed 100 characters");
        }
        return dimension.trim();
    }
    
    // Getters
    public MetricId getId() { return id; }
    public MetricType getType() { return type; }
    public String getName() { return name; }
    public String getDimension() { return dimension; }
    public MetricValue getValue() { return value; }
    public TimeWindow getTimeWindow() { return timeWindow; }
    public AggregationFunction getAggregationFunction() { return aggregationFunction; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Map<String, Object> getAttributes() { return attributes; }
    public DataQuality getDataQuality() { return dataQuality; }
    public AggregationSource getSource() { return source; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregatedMetric that = (AggregatedMetric) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("AggregatedMetric{%s: %s %s [%s]}", 
                           name, value, aggregationFunction, timeWindow);
    }
    
    /**
     * Criticality level enumeration
     */
    public enum CriticalityLevel {
        HIGH("High", 1),
        MEDIUM("Medium", 2),
        LOW("Low", 3);
        
        private final String displayName;
        private final int priority;
        
        CriticalityLevel(String displayName, int priority) {
            this.displayName = displayName;
            this.priority = priority;
        }
        
        public String getDisplayName() { return displayName; }
        public int getPriority() { return priority; }
        
        public boolean isHigherThan(CriticalityLevel other) {
            return this.priority < other.priority;
        }
    }
}