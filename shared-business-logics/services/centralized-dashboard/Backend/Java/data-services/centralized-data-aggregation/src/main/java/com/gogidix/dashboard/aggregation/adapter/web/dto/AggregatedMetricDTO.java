package com.gogidix.dashboard.aggregation.adapter.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Aggregated Metric DTO
 * 
 * Data Transfer Object for aggregated metrics
 * Includes validation and JSON serialization configuration
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AggregatedMetricDTO {
    
    private String id;
    
    @NotBlank(message = "Metric name is required")
    private String metricName;
    
    @NotNull(message = "Value is required")
    private Double value;
    
    private String unit;
    
    @NotNull(message = "Aggregation function is required")
    private String aggregationFunction;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime windowStart;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime windowEnd;
    
    private String granularity;
    
    private Map<String, String> dimensions;
    
    private Map<String, Object> metadata;
    
    @Min(0)
    @Max(100)
    private Double confidenceScore;
    
    private Long sampleSize;
    
    private Double minValue;
    
    private Double maxValue;
    
    private Double avgValue;
    
    private Double stdDeviation;
    
    private String sourceDomain;
    
    private String dataQualityStatus;
    
    private Boolean isStale;
    
    private Boolean hasAnomalies;
    
    // Constructors
    public AggregatedMetricDTO() {}
    
    public AggregatedMetricDTO(String id, String metricName, Double value) {
        this.id = id;
        this.metricName = metricName;
        this.value = value;
        this.timestamp = LocalDateTime.now();
    }
    
    // Builder pattern
    public static class Builder {
        private AggregatedMetricDTO dto = new AggregatedMetricDTO();
        
        public Builder withId(String id) {
            dto.id = id;
            return this;
        }
        
        public Builder withMetricName(String metricName) {
            dto.metricName = metricName;
            return this;
        }
        
        public Builder withValue(Double value) {
            dto.value = value;
            return this;
        }
        
        public Builder withUnit(String unit) {
            dto.unit = unit;
            return this;
        }
        
        public Builder withAggregationFunction(String function) {
            dto.aggregationFunction = function;
            return this;
        }
        
        public Builder withTimestamp(LocalDateTime timestamp) {
            dto.timestamp = timestamp;
            return this;
        }
        
        public Builder withTimeWindow(LocalDateTime start, LocalDateTime end) {
            dto.windowStart = start;
            dto.windowEnd = end;
            return this;
        }
        
        public Builder withGranularity(String granularity) {
            dto.granularity = granularity;
            return this;
        }
        
        public Builder withDimensions(Map<String, String> dimensions) {
            dto.dimensions = dimensions;
            return this;
        }
        
        public Builder withMetadata(Map<String, Object> metadata) {
            dto.metadata = metadata;
            return this;
        }
        
        public Builder withConfidenceScore(Double score) {
            dto.confidenceScore = score;
            return this;
        }
        
        public Builder withStatistics(Long sampleSize, Double min, Double max, Double avg, Double stdDev) {
            dto.sampleSize = sampleSize;
            dto.minValue = min;
            dto.maxValue = max;
            dto.avgValue = avg;
            dto.stdDeviation = stdDev;
            return this;
        }
        
        public Builder withQualityIndicators(String qualityStatus, Boolean isStale, Boolean hasAnomalies) {
            dto.dataQualityStatus = qualityStatus;
            dto.isStale = isStale;
            dto.hasAnomalies = hasAnomalies;
            return this;
        }
        
        public AggregatedMetricDTO build() {
            return dto;
        }
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    
    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }
    
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    
    public String getAggregationFunction() { return aggregationFunction; }
    public void setAggregationFunction(String aggregationFunction) { this.aggregationFunction = aggregationFunction; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public LocalDateTime getWindowStart() { return windowStart; }
    public void setWindowStart(LocalDateTime windowStart) { this.windowStart = windowStart; }
    
    public LocalDateTime getWindowEnd() { return windowEnd; }
    public void setWindowEnd(LocalDateTime windowEnd) { this.windowEnd = windowEnd; }
    
    public String getGranularity() { return granularity; }
    public void setGranularity(String granularity) { this.granularity = granularity; }
    
    public Map<String, String> getDimensions() { return dimensions; }
    public void setDimensions(Map<String, String> dimensions) { this.dimensions = dimensions; }
    
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    
    public Long getSampleSize() { return sampleSize; }
    public void setSampleSize(Long sampleSize) { this.sampleSize = sampleSize; }
    
    public Double getMinValue() { return minValue; }
    public void setMinValue(Double minValue) { this.minValue = minValue; }
    
    public Double getMaxValue() { return maxValue; }
    public void setMaxValue(Double maxValue) { this.maxValue = maxValue; }
    
    public Double getAvgValue() { return avgValue; }
    public void setAvgValue(Double avgValue) { this.avgValue = avgValue; }
    
    public Double getStdDeviation() { return stdDeviation; }
    public void setStdDeviation(Double stdDeviation) { this.stdDeviation = stdDeviation; }
    
    public String getSourceDomain() { return sourceDomain; }
    public void setSourceDomain(String sourceDomain) { this.sourceDomain = sourceDomain; }
    
    public String getDataQualityStatus() { return dataQualityStatus; }
    public void setDataQualityStatus(String dataQualityStatus) { this.dataQualityStatus = dataQualityStatus; }
    
    public Boolean getIsStale() { return isStale; }
    public void setIsStale(Boolean isStale) { this.isStale = isStale; }
    
    public Boolean getHasAnomalies() { return hasAnomalies; }
    public void setHasAnomalies(Boolean hasAnomalies) { this.hasAnomalies = hasAnomalies; }
}