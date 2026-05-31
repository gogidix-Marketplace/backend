package com.gogidix.centralizeddashboard.analytics.aggregation.model;

/**
 * Enum representing different types of metrics
 */
public enum MetricType {
    // Mathematical metrics
    COUNT,          // Simple count metric (e.g., number of orders)
    SUM,            // Sum of values (e.g., total revenue)
    AVERAGE,        // Average value (e.g., average order value)
    MIN,            // Minimum value
    MAX,            // Maximum value
    MEDIAN,         // Median value
    PERCENTILE,     // Percentile value (e.g., 95th percentile)
    RATE,           // Rate of change (e.g., conversion rate)
    RATIO,          // Ratio between two values (e.g., return rate)
    UNIQUE_COUNT,   // Count of unique values (e.g., unique users)
    HISTOGRAM,      // Distribution of values
    
    // Business metrics
    REVENUE,        // Revenue metrics
    USERS,          // User-related metrics
    ORDERS,         // Order-related metrics
    CONVERSION_RATE, // Conversion rate metrics
    ENGAGEMENT,     // Engagement metrics
    PERFORMANCE,    // Performance metrics
    INVENTORY,      // Inventory metrics
    DELIVERY,       // Delivery metrics
    USER_ACTIVITY,  // User activity metrics
    
    CUSTOM          // Custom metric type
} 