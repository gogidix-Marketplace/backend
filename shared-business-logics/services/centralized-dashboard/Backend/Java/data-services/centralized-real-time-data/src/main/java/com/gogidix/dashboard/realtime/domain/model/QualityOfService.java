package com.gogidix.dashboard.realtime.domain.model;

/**
 * Quality of Service - defines QoS levels for stream delivery
 */
public enum QualityOfService {
    AT_MOST_ONCE(0, "Fire and forget"),
    AT_LEAST_ONCE(1, "Acknowledged delivery"),
    EXACTLY_ONCE(2, "Guaranteed delivery");

    private final int level;
    private final String description;

    QualityOfService(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Get minimum data quality score required for this QoS level
     */
    public double getMinDataQualityScore() {
        switch (this) {
            case EXACTLY_ONCE:
                return 0.99;
            case AT_LEAST_ONCE:
                return 0.95;
            case AT_MOST_ONCE:
            default:
                return 0.80;
        }
    }

    /**
     * Get maximum latency allowed for this QoS level (in milliseconds)
     */
    public long getMaxLatency() {
        switch (this) {
            case EXACTLY_ONCE:
                return 100; // 100ms for guaranteed delivery
            case AT_LEAST_ONCE:
                return 500; // 500ms for acknowledged delivery
            case AT_MOST_ONCE:
            default:
                return 1000; // 1000ms for fire and forget
        }
    }
}

