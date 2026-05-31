package com.gogidix.shared.courier.pricing.domain.entity;

/**
 * Enumeration of service types
 * Defines delivery service categories
 */
public enum ServiceType {
    /**
     * Standard delivery - regular delivery timeframe
     */
    STANDARD,

    /**
     * Express delivery - faster delivery option
     */
    EXPRESS,

    /**
     * Same day delivery - delivery within the same day
     */
    SAME_DAY,

    /**
     * Scheduled delivery - pre-specified delivery time
     */
    SCHEDULED,

    /**
     * Overnight delivery - next day delivery
     */
    OVERNIGHT
}
