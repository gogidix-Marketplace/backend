package com.gogidix.dashboard.aggregation.domain.model;

import java.time.Duration;

/**
 * Time Granularity Enumeration
 * 
 * Defines time granularities for metric aggregation with business rules
 */
public enum TimeGranularity {
    REAL_TIME("Real-time", Duration.ofMinutes(1), Duration.ofMinutes(5), 2),
    MINUTE("Per Minute", Duration.ofMinutes(1), Duration.ofMinutes(10), 5),
    FIVE_MINUTE("5-Minute", Duration.ofMinutes(5), Duration.ofMinutes(20), 10),
    FIFTEEN_MINUTE("15-Minute", Duration.ofMinutes(15), Duration.ofHours(1), 30),
    HOURLY("Hourly", Duration.ofHours(1), Duration.ofHours(4), 60),
    DAILY("Daily", Duration.ofDays(1), Duration.ofDays(2), 1440),
    WEEKLY("Weekly", Duration.ofDays(7), Duration.ofDays(10), 10080),
    MONTHLY("Monthly", Duration.ofDays(30), Duration.ofDays(35), 43200),
    QUARTERLY("Quarterly", Duration.ofDays(90), Duration.ofDays(100), 129600),
    YEARLY("Yearly", Duration.ofDays(365), Duration.ofDays(380), 525600);
    
    private final String displayName;
    private final Duration defaultDuration;
    private final Duration maxStaleness;
    private final long expectedFreshnessMinutes;
    
    TimeGranularity(String displayName, Duration defaultDuration, Duration maxStaleness, long expectedFreshnessMinutes) {
        this.displayName = displayName;
        this.defaultDuration = defaultDuration;
        this.maxStaleness = maxStaleness;
        this.expectedFreshnessMinutes = expectedFreshnessMinutes;
    }
    
    /**
     * Get display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get default duration for this granularity
     */
    public Duration getDefaultDuration() {
        return defaultDuration;
    }
    
    /**
     * Get maximum staleness before data is considered outdated
     */
    public Duration getMaxStaleness() {
        return maxStaleness;
    }
    
    /**
     * Get expected freshness in minutes
     */
    public long getExpectedFreshnessMinutes() {
        return expectedFreshnessMinutes;
    }
    
    /**
     * Check if this granularity is real-time or near real-time
     */
    public boolean isRealTime() {
        return this == REAL_TIME || this == MINUTE || this == FIVE_MINUTE;
    }
    
    /**
     * Check if this granularity is high frequency
     */
    public boolean isHighFrequency() {
        return this == REAL_TIME || this == MINUTE || this == FIVE_MINUTE || 
               this == FIFTEEN_MINUTE || this == HOURLY;
    }
    
    /**
     * Check if this granularity is strategic (long-term)
     */
    public boolean isStrategic() {
        return this == MONTHLY || this == QUARTERLY || this == YEARLY;
    }
    
    /**
     * Check if this granularity is suitable for operational monitoring
     */
    public boolean isOperational() {
        return isHighFrequency() || this == DAILY || this == WEEKLY;
    }
    
    /**
     * Check if a duration is valid for this granularity
     */
    public boolean isValidDuration(Duration duration) {
        // Allow some tolerance for duration matching
        long actualMinutes = duration.toMinutes();
        long expectedMinutes = defaultDuration.toMinutes();
        
        // For real-time and minute granularities, allow exact match or multiples
        if (isRealTime()) {
            return actualMinutes >= expectedMinutes && actualMinutes <= expectedMinutes * 60;
        }
        
        // For other granularities, allow 10% tolerance
        double tolerance = expectedMinutes * 0.1;
        return Math.abs(actualMinutes - expectedMinutes) <= tolerance ||
               actualMinutes % expectedMinutes == 0; // Allow multiples
    }
    
    /**
     * Check if this granularity can be split into smaller granularity
     */
    public boolean canSplitInto(TimeGranularity smaller) {
        if (this.ordinal() <= smaller.ordinal()) {
            return false; // Can't split into same or larger granularity
        }
        
        long thisMinutes = this.defaultDuration.toMinutes();
        long smallerMinutes = smaller.defaultDuration.toMinutes();
        
        return thisMinutes % smallerMinutes == 0; // Must be evenly divisible
    }
    
    /**
     * Check if this granularity can be aggregated into larger granularity
     */
    public boolean canAggregateInto(TimeGranularity larger) {
        return larger.canSplitInto(this);
    }
    
    /**
     * Get the next larger granularity for rolling up data
     */
    public TimeGranularity getNextLarger() {
        switch (this) {
            case REAL_TIME:
            case MINUTE:
                return FIVE_MINUTE;
            case FIVE_MINUTE:
                return FIFTEEN_MINUTE;
            case FIFTEEN_MINUTE:
                return HOURLY;
            case HOURLY:
                return DAILY;
            case DAILY:
                return WEEKLY;
            case WEEKLY:
                return MONTHLY;
            case MONTHLY:
                return QUARTERLY;
            case QUARTERLY:
                return YEARLY;
            case YEARLY:
                return YEARLY; // Already the largest
            default:
                return this;
        }
    }
    
    /**
     * Get the next smaller granularity for drilling down
     */
    public TimeGranularity getNextSmaller() {
        switch (this) {
            case YEARLY:
                return QUARTERLY;
            case QUARTERLY:
                return MONTHLY;
            case MONTHLY:
                return WEEKLY;
            case WEEKLY:
                return DAILY;
            case DAILY:
                return HOURLY;
            case HOURLY:
                return FIFTEEN_MINUTE;
            case FIFTEEN_MINUTE:
                return FIVE_MINUTE;
            case FIVE_MINUTE:
                return MINUTE;
            case MINUTE:
            case REAL_TIME:
                return REAL_TIME; // Already the smallest
            default:
                return this;
        }
    }
    
    /**
     * Get retention period recommendation for this granularity
     */
    public Duration getRecommendedRetentionPeriod() {
        switch (this) {
            case REAL_TIME:
            case MINUTE:
                return Duration.ofDays(1); // 1 day of real-time data
            case FIVE_MINUTE:
                return Duration.ofDays(7); // 1 week of 5-minute data
            case FIFTEEN_MINUTE:
                return Duration.ofDays(30); // 1 month of 15-minute data
            case HOURLY:
                return Duration.ofDays(90); // 3 months of hourly data
            case DAILY:
                return Duration.ofDays(365 * 2); // 2 years of daily data
            case WEEKLY:
                return Duration.ofDays(365 * 5); // 5 years of weekly data
            case MONTHLY:
                return Duration.ofDays(365 * 10); // 10 years of monthly data
            case QUARTERLY:
                return Duration.ofDays(365 * 20); // 20 years of quarterly data
            case YEARLY:
                return Duration.ofDays(365 * 50); // 50 years of yearly data
            default:
                return Duration.ofDays(365);
        }
    }
    
    /**
     * Get storage efficiency score (higher = more efficient for long-term storage)
     */
    public int getStorageEfficiencyScore() {
        switch (this) {
            case YEARLY:
                return 10;
            case QUARTERLY:
                return 9;
            case MONTHLY:
                return 8;
            case WEEKLY:
                return 7;
            case DAILY:
                return 6;
            case HOURLY:
                return 5;
            case FIFTEEN_MINUTE:
                return 4;
            case FIVE_MINUTE:
                return 3;
            case MINUTE:
                return 2;
            case REAL_TIME:
                return 1;
            default:
                return 1;
        }
    }
    
    /**
     * Get query performance score (higher = better for real-time queries)
     */
    public int getQueryPerformanceScore() {
        switch (this) {
            case REAL_TIME:
                return 10;
            case MINUTE:
                return 9;
            case FIVE_MINUTE:
                return 8;
            case FIFTEEN_MINUTE:
                return 7;
            case HOURLY:
                return 6;
            case DAILY:
                return 5;
            case WEEKLY:
                return 4;
            case MONTHLY:
                return 3;
            case QUARTERLY:
                return 2;
            case YEARLY:
                return 1;
            default:
                return 1;
        }
    }
    
    /**
     * Check if this granularity is suitable for alerting
     */
    public boolean isSuitableForAlerting() {
        return isHighFrequency(); // Real-time through hourly are good for alerts
    }
    
    /**
     * Check if this granularity is suitable for trend analysis
     */
    public boolean isSuitableForTrendAnalysis() {
        return this == DAILY || this == WEEKLY || this == MONTHLY || 
               this == QUARTERLY || this == YEARLY;
    }
    
    /**
     * Check if this granularity is suitable for capacity planning
     */
    public boolean isSuitableForCapacityPlanning() {
        return this == WEEKLY || this == MONTHLY || this == QUARTERLY || this == YEARLY;
    }
    
    /**
     * Get data points per day for this granularity
     */
    public int getDataPointsPerDay() {
        switch (this) {
            case REAL_TIME:
            case MINUTE:
                return 1440; // 24 * 60
            case FIVE_MINUTE:
                return 288; // 24 * 12
            case FIFTEEN_MINUTE:
                return 96; // 24 * 4
            case HOURLY:
                return 24;
            case DAILY:
                return 1;
            case WEEKLY:
                return 1; // Less than 1 per day
            case MONTHLY:
                return 1; // Less than 1 per day
            case QUARTERLY:
                return 1; // Less than 1 per day
            case YEARLY:
                return 1; // Less than 1 per day
            default:
                return 1;
        }
    }
    
    /**
     * Get aggregation complexity score (higher = more complex to compute)
     */
    public int getAggregationComplexity() {
        if (isRealTime()) {
            return 1; // Simple, no aggregation needed
        }
        if (isHighFrequency()) {
            return 2; // Moderate aggregation
        }
        if (isStrategic()) {
            return 5; // Complex, requires multiple levels of aggregation
        }
        return 3; // Standard aggregation
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}