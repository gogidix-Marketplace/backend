package com.gogidix.courier.etaservice.shared.util;

/**
 * Traffic level enumeration with associated multiplier values.
 */
public enum TrafficLevel {
    LOW(1.0, "Light traffic", 10),
    MEDIUM(1.3, "Moderate traffic", 25),
    HIGH(1.7, "Heavy traffic", 40),
    SEVERE(2.2, "Very heavy traffic", Integer.MAX_VALUE);

    private final double multiplier;
    private final String description;
    private final int threshold;

    TrafficLevel(double multiplier, String description, int threshold) {
        this.multiplier = multiplier;
        this.description = description;
        this.threshold = threshold;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getDescription() {
        return description;
    }

    public int getThreshold() {
        return threshold;
    }

    /**
     * Determine traffic level from vehicles per minute.
     *
     * @param vehiclesPerMinute current traffic flow
     * @return the corresponding traffic level
     */
    public static TrafficLevel fromVehiclesPerMinute(int vehiclesPerMinute) {
        for (TrafficLevel level : values()) {
            if (vehiclesPerMinute <= level.threshold) {
                return level;
            }
        }
        return SEVERE;
    }

    /**
     * Determine traffic level from a custom multiplier.
     *
     * @param multiplier the traffic multiplier
     * @return the corresponding traffic level
     */
    public static TrafficLevel fromMultiplier(double multiplier) {
        if (multiplier <= 1.15) return LOW;
        if (multiplier <= 1.5) return MEDIUM;
        if (multiplier <= 2.0) return HIGH;
        return SEVERE;
    }
}
