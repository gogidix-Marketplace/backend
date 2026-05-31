package com.gogidix.dashboard.aggregation.domain.model;

import java.util.List;
import java.util.Arrays;

/**
 * Aggregation Function Enumeration
 * 
 * Defines statistical functions for metric aggregation with business logic
 */
public enum AggregationFunction {
    SUM("Sum", "Total sum of all values", AggregationType.ADDITIVE, true, false),
    COUNT("Count", "Number of data points", AggregationType.ADDITIVE, true, false),
    AVERAGE("Average", "Arithmetic mean of values", AggregationType.AVERAGE, true, true),
    MEDIAN("Median", "Middle value when sorted", AggregationType.PERCENTILE, false, true),
    MODE("Mode", "Most frequently occurring value", AggregationType.STATISTICAL, false, true),
    MIN("Minimum", "Smallest value in the set", AggregationType.EXTREMUM, false, true),
    MAX("Maximum", "Largest value in the set", AggregationType.EXTREMUM, false, true),
    RANGE("Range", "Difference between max and min", AggregationType.DERIVED, false, true),
    VARIANCE("Variance", "Measure of value spread", AggregationType.STATISTICAL, false, true),
    STANDARD_DEVIATION("Standard Deviation", "Square root of variance", AggregationType.STATISTICAL, false, true),
    PERCENTILE_95("95th Percentile", "Value below which 95% of data falls", AggregationType.PERCENTILE, false, true),
    PERCENTILE_99("99th Percentile", "Value below which 99% of data falls", AggregationType.PERCENTILE, false, true),
    FIRST("First", "First value in time series", AggregationType.TEMPORAL, false, false),
    LAST("Last", "Last value in time series", AggregationType.TEMPORAL, false, false),
    RATE("Rate", "Change per unit time", AggregationType.DERIVED, true, true),
    WEIGHTED_AVERAGE("Weighted Average", "Average with importance weights", AggregationType.WEIGHTED, true, true),
    DISTINCT_COUNT("Distinct Count", "Number of unique values", AggregationType.CARDINALITY, true, false);
    
    private final String displayName;
    private final String description;
    private final AggregationType type;
    private final boolean isAdditive;
    private final boolean requiresMultipleValues;
    
    AggregationFunction(String displayName, String description, AggregationType type,
                       boolean isAdditive, boolean requiresMultipleValues) {
        this.displayName = displayName;
        this.description = description;
        this.type = type;
        this.isAdditive = isAdditive;
        this.requiresMultipleValues = requiresMultipleValues;
    }
    
    /**
     * Get display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get detailed description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Get aggregation type
     */
    public AggregationType getType() {
        return type;
    }
    
    /**
     * Check if function is additive (can be combined across dimensions)
     */
    public boolean isAdditive() {
        return isAdditive;
    }
    
    /**
     * Check if function requires multiple values to be meaningful
     */
    public boolean requiresMultipleValues() {
        return requiresMultipleValues;
    }
    
    /**
     * Check if function is suitable for real-time aggregation
     */
    public boolean isSuitableForRealTime() {
        return this == SUM || this == COUNT || this == AVERAGE || 
               this == MIN || this == MAX || this == FIRST || this == LAST;
    }
    
    /**
     * Check if function is suitable for trend analysis
     */
    public boolean isSuitableForTrendAnalysis() {
        return this == SUM || this == COUNT || this == AVERAGE || 
               this == MIN || this == MAX || this == RATE;
    }
    
    /**
     * Check if function is suitable for alerting
     */
    public boolean isSuitableForAlerting() {
        return this == SUM || this == COUNT || this == AVERAGE || 
               this == MIN || this == MAX || this == PERCENTILE_95 || this == PERCENTILE_99;
    }
    
    /**
     * Check if function provides statistical insights
     */
    public boolean providesStatisticalInsights() {
        return type == AggregationType.STATISTICAL || type == AggregationType.PERCENTILE;
    }
    
    /**
     * Check if function can be computed incrementally
     */
    public boolean canComputeIncrementally() {
        return this == SUM || this == COUNT || this == MIN || this == MAX || 
               this == FIRST || this == LAST;
    }
    
    /**
     * Get computational complexity score (1-5, where 5 is most complex)
     */
    public int getComputationalComplexity() {
        switch (type) {
            case ADDITIVE:
                return 1; // Simple arithmetic
            case EXTREMUM:
            case TEMPORAL:
                return 2; // Simple comparison
            case AVERAGE:
            case WEIGHTED:
                return 3; // Requires division
            case PERCENTILE:
                return 4; // Requires sorting
            case STATISTICAL:
            case DERIVED:
            case CARDINALITY:
                return 5; // Complex calculations
            default:
                return 3;
        }
    }
    
    /**
     * Get memory requirement score (1-5, where 5 requires most memory)
     */
    public int getMemoryRequirement() {
        switch (this) {
            case SUM:
            case COUNT:
            case MIN:
            case MAX:
            case FIRST:
            case LAST:
                return 1; // Constant memory
            case AVERAGE:
            case RATE:
                return 2; // Minimal state
            case WEIGHTED_AVERAGE:
                return 3; // Weights storage
            case MEDIAN:
            case MODE:
            case PERCENTILE_95:
            case PERCENTILE_99:
                return 4; // Requires data storage
            case VARIANCE:
            case STANDARD_DEVIATION:
            case RANGE:
            case DISTINCT_COUNT:
                return 5; // Requires all values
            default:
                return 3;
        }
    }
    
    /**
     * Check if function output is typically used for business KPIs
     */
    public boolean isBusinessKPI() {
        return this == SUM || this == COUNT || this == AVERAGE || this == RATE;
    }
    
    /**
     * Check if function output is typically used for technical monitoring
     */
    public boolean isTechnicalMonitoring() {
        return this == MIN || this == MAX || this == PERCENTILE_95 || 
               this == PERCENTILE_99 || this == STANDARD_DEVIATION;
    }
    
    /**
     * Get compatible aggregation functions that can be combined
     */
    public List<AggregationFunction> getCompatibleFunctions() {
        switch (this) {
            case SUM:
                return Arrays.asList(COUNT, AVERAGE, RATE);
            case COUNT:
                return Arrays.asList(SUM, AVERAGE, DISTINCT_COUNT);
            case AVERAGE:
                return Arrays.asList(SUM, COUNT, MIN, MAX, MEDIAN);
            case MIN:
                return Arrays.asList(MAX, RANGE, AVERAGE);
            case MAX:
                return Arrays.asList(MIN, RANGE, AVERAGE);
            case PERCENTILE_95:
                return Arrays.asList(PERCENTILE_99, MAX, MEDIAN);
            case PERCENTILE_99:
                return Arrays.asList(PERCENTILE_95, MAX);
            default:
                return Arrays.asList(this);
        }
    }
    
    /**
     * Get recommended time granularities for this function
     */
    public List<TimeGranularity> getRecommendedGranularities() {
        if (isSuitableForRealTime()) {
            return Arrays.asList(TimeGranularity.REAL_TIME, TimeGranularity.MINUTE, 
                               TimeGranularity.FIVE_MINUTE, TimeGranularity.HOURLY);
        }
        
        if (providesStatisticalInsights()) {
            return Arrays.asList(TimeGranularity.HOURLY, TimeGranularity.DAILY, 
                               TimeGranularity.WEEKLY);
        }
        
        return Arrays.asList(TimeGranularity.HOURLY, TimeGranularity.DAILY);
    }
    
    /**
     * Get data quality impact (how sensitive to outliers/missing data)
     */
    public DataSensitivity getDataSensitivity() {
        switch (this) {
            case SUM:
            case COUNT:
                return DataSensitivity.LOW; // Robust to outliers
            case AVERAGE:
            case RATE:
                return DataSensitivity.MEDIUM; // Moderately sensitive
            case MIN:
            case MAX:
            case PERCENTILE_95:
            case PERCENTILE_99:
                return DataSensitivity.HIGH; // Very sensitive to outliers
            case MEDIAN:
            case MODE:
                return DataSensitivity.LOW; // Robust to outliers
            default:
                return DataSensitivity.MEDIUM;
        }
    }
    
    /**
     * Check if function can handle missing/null values gracefully
     */
    public boolean handlesNullValues() {
        return this == SUM || this == COUNT || this == AVERAGE || 
               this == MIN || this == MAX || this == DISTINCT_COUNT;
    }
    
    /**
     * Get minimum number of data points required for meaningful result
     */
    public int getMinimumDataPoints() {
        switch (this) {
            case COUNT:
            case FIRST:
            case LAST:
                return 1;
            case SUM:
            case MIN:
            case MAX:
            case DISTINCT_COUNT:
                return 1; // Meaningful with 1, but better with more
            case AVERAGE:
            case RATE:
                return 2; // Need at least 2 for meaningful average/rate
            case MEDIAN:
            case MODE:
            case RANGE:
                return 3; // Need several values for statistical meaning
            case VARIANCE:
            case STANDARD_DEVIATION:
            case PERCENTILE_95:
            case PERCENTILE_99:
                return 10; // Need substantial data for reliable statistics
            default:
                return 2;
        }
    }
    
    /**
     * Format result for display based on function type
     */
    public String formatResult(double value, String unit) {
        switch (this) {
            case PERCENTILE_95:
            case PERCENTILE_99:
                return String.format("%.2f %s (%s)", value, unit != null ? unit : "", displayName);
            case VARIANCE:
            case STANDARD_DEVIATION:
                return String.format("σ = %.3f %s", value, unit != null ? unit : "");
            case RATE:
                return String.format("%.2f %s/s", value, unit != null ? unit : "");
            case COUNT:
            case DISTINCT_COUNT:
                return String.format("%.0f items", value);
            default:
                return String.format("%.2f %s", value, unit != null ? unit : "");
        }
    }
    
    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Aggregation type classification
     */
    public enum AggregationType {
        ADDITIVE,      // Can be added together (SUM, COUNT)
        AVERAGE,       // Average-based calculations
        EXTREMUM,      // Min/Max operations
        PERCENTILE,    // Percentile-based statistics
        STATISTICAL,   // Statistical measures (variance, std dev)
        TEMPORAL,      // Time-based (first, last)
        DERIVED,       // Calculated from other values
        WEIGHTED,      // Weighted calculations
        CARDINALITY    // Distinct value counting
    }
    
    /**
     * Data sensitivity classification
     */
    public enum DataSensitivity {
        LOW,    // Robust to outliers and missing data
        MEDIUM, // Moderately sensitive
        HIGH    // Very sensitive to data quality issues
    }
}