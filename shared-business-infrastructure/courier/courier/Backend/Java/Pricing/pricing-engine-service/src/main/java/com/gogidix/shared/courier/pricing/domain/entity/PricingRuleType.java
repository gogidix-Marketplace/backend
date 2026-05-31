package com.gogidix.shared.courier.pricing.domain.entity;

/**
 * Enumeration of pricing rule types
 * Defines the strategy categories for pricing calculations
 */
public enum PricingRuleType {
    /**
     * Base rate pricing - standard fare calculation
     */
    BASE_RATE,

    /**
     * Distance-based pricing - per-kilometer charges
     */
    DISTANCE_BASED,

    /**
     * Time-based pricing - per-minute or hourly charges
     */
    TIME_BASED,

    /**
     * Surge pricing - dynamic multiplier during high demand
     */
    SURGE,

    /**
     * Discount pricing - promotional or loyalty discounts
     */
    DISCOUNT,

    /**
     * Weight-based pricing - per-kilogram charges
     */
    WEIGHT_BASED,

    /**
     * Tiered pricing - progressive rates based on distance/weight
     */
    TIERED,

    /**
     * Flat rate pricing - fixed price for specific routes
     */
    FLAT_RATE
}
