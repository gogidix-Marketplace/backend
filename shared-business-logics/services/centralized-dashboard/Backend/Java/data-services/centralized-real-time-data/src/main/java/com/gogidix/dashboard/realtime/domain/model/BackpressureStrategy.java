package com.gogidix.dashboard.realtime.domain.model;

/**
 * Backpressure Strategy Enumeration
 */
public enum BackpressureStrategy {
    BUFFER(0.8),
    DROP_OLDEST(0.9),
    REJECT_NEW(0.7);

    private final double threshold;

    BackpressureStrategy(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }
}
