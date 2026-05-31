package com.gogidix.dashboard.aggregation.domain.port.in;

import com.gogidix.dashboard.aggregation.domain.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Data Aggregation Use Case - Input Port
 * 
 * Provides comprehensive data aggregation operations for dashboard metrics
 * following hexagonal architecture principles
 */
public interface DataAggregationUseCase {
    
    /**
     * Aggregate metrics from multiple sources
     */
    AggregatedMetric aggregateMetrics(AggregateMetricsCommand command);
    
    /**
     * Perform time-based aggregation
     */
    List<AggregatedMetric> aggregateByTimeWindow(TimeWindowAggregationCommand command);
    
    /**
     * Calculate statistical aggregations
     */
    MetricValue calculateStatisticalAggregation(StatisticalAggregationCommand command);
    
    /**
     * Stream real-time aggregations
     */
    void streamAggregations(StreamAggregationCommand command);
    
    /**
     * Get aggregation by ID
     */
    Optional<AggregatedMetric> getAggregation(MetricId id);
    
    /**
     * Query aggregations with filters
     */
    List<AggregatedMetric> queryAggregations(AggregationQuery query);
    
    /**
     * Validate data quality
     */
    DataQuality assessDataQuality(List<AggregatedMetric> metrics);
    
    /**
     * Command: Aggregate Metrics
     */
    class AggregateMetricsCommand {
        private final String metricName;
        private final List<Map<String, Object>> rawData;
        private final AggregationFunction function;
        private final TimeWindow timeWindow;
        private final Map<String, String> dimensions;
        
        public AggregateMetricsCommand(String metricName, List<Map<String, Object>> rawData,
                                      AggregationFunction function, TimeWindow timeWindow,
                                      Map<String, String> dimensions) {
            this.metricName = metricName;
            this.rawData = rawData;
            this.function = function;
            this.timeWindow = timeWindow;
            this.dimensions = dimensions;
        }
        
        // Getters
        public String getMetricName() { return metricName; }
        public List<Map<String, Object>> getRawData() { return rawData; }
        public AggregationFunction getFunction() { return function; }
        public TimeWindow getTimeWindow() { return timeWindow; }
        public Map<String, String> getDimensions() { return dimensions; }
    }
    
    /**
     * Command: Time Window Aggregation
     */
    class TimeWindowAggregationCommand {
        private final String metricName;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final TimeGranularity granularity;
        private final AggregationFunction function;
        
        public TimeWindowAggregationCommand(String metricName, LocalDateTime startTime,
                                           LocalDateTime endTime, TimeGranularity granularity,
                                           AggregationFunction function) {
            this.metricName = metricName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.granularity = granularity;
            this.function = function;
        }
        
        // Getters
        public String getMetricName() { return metricName; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public TimeGranularity getGranularity() { return granularity; }
        public AggregationFunction getFunction() { return function; }
    }
    
    /**
     * Command: Statistical Aggregation
     */
    class StatisticalAggregationCommand {
        private final List<MetricValue> values;
        private final AggregationFunction function;
        private final Map<String, Object> parameters;
        
        public StatisticalAggregationCommand(List<MetricValue> values, 
                                            AggregationFunction function,
                                            Map<String, Object> parameters) {
            this.values = values;
            this.function = function;
            this.parameters = parameters;
        }
        
        // Getters
        public List<MetricValue> getValues() { return values; }
        public AggregationFunction getFunction() { return function; }
        public Map<String, Object> getParameters() { return parameters; }
    }
    
    /**
     * Command: Stream Aggregation
     */
    class StreamAggregationCommand {
        private final String streamId;
        private final String metricName;
        private final TimeGranularity granularity;
        private final AggregationFunction function;
        private final boolean enableBackpressure;
        
        public StreamAggregationCommand(String streamId, String metricName,
                                       TimeGranularity granularity, AggregationFunction function,
                                       boolean enableBackpressure) {
            this.streamId = streamId;
            this.metricName = metricName;
            this.granularity = granularity;
            this.function = function;
            this.enableBackpressure = enableBackpressure;
        }
        
        // Getters
        public String getStreamId() { return streamId; }
        public String getMetricName() { return metricName; }
        public TimeGranularity getGranularity() { return granularity; }
        public AggregationFunction getFunction() { return function; }
        public boolean isEnableBackpressure() { return enableBackpressure; }
    }
    
    /**
     * Query: Aggregation Query
     */
    class AggregationQuery {
        private final String metricNamePattern;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final Map<String, String> dimensionFilters;
        private final Integer limit;
        private final String orderBy;
        
        public AggregationQuery(String metricNamePattern, LocalDateTime startTime,
                               LocalDateTime endTime, Map<String, String> dimensionFilters,
                               Integer limit, String orderBy) {
            this.metricNamePattern = metricNamePattern;
            this.startTime = startTime;
            this.endTime = endTime;
            this.dimensionFilters = dimensionFilters;
            this.limit = limit;
            this.orderBy = orderBy;
        }
        
        // Getters
        public String getMetricNamePattern() { return metricNamePattern; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public Map<String, String> getDimensionFilters() { return dimensionFilters; }
        public Integer getLimit() { return limit; }
        public String getOrderBy() { return orderBy; }
    }
}