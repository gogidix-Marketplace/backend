package com.gogidix.dashboard.performance.domain.model;

/**
 * Metric Type Enumeration
 * 
 * Defines different types of performance metrics with their characteristics
 */
public enum MetricType {
    RESPONSE_TIME("Response Time", "ms", false, true, 1000),
    THROUGHPUT("Throughput", "req/s", true, false, 10000),
    ERROR_RATE("Error Rate", "%", false, true, 5),
    CPU_USAGE("CPU Usage", "%", false, false, 100),
    MEMORY_USAGE("Memory Usage", "%", false, false, 100),
    DISK_USAGE("Disk Usage", "%", false, false, 100),
    NETWORK_IO("Network I/O", "MB/s", true, false, 1000),
    REQUEST_RATE("Request Rate", "req/s", true, false, 10000),
    AVAILABILITY("Availability", "%", true, true, 100),
    LATENCY_P50("P50 Latency", "ms", false, true, 500),
    LATENCY_P95("P95 Latency", "ms", false, true, 1000),
    LATENCY_P99("P99 Latency", "ms", false, true, 2000),
    DATABASE_CONNECTIONS("Database Connections", "count", false, false, 1000),
    CACHE_HIT_RATE("Cache Hit Rate", "%", true, false, 100),
    QUEUE_SIZE("Queue Size", "count", false, false, 10000),
    JVM_HEAP_USAGE("JVM Heap Usage", "MB", false, false, 8192),
    GARBAGE_COLLECTION_TIME("GC Time", "ms", false, true, 100),
    THREAD_COUNT("Thread Count", "count", false, false, 500),
    CONNECTION_POOL_SIZE("Connection Pool Size", "count", false, false, 200),
    TRANSACTION_RATE("Transaction Rate", "tps", true, false, 10000);
    
    private final String displayName;
    private final String unit;
    private final boolean higherBetter;
    private final boolean criticalMetric;
    private final double typicalMax;
    
    MetricType(String displayName, String unit, boolean higherBetter, 
               boolean criticalMetric, double typicalMax) {
        this.displayName = displayName;
        this.unit = unit;
        this.higherBetter = higherBetter;
        this.criticalMetric = criticalMetric;
        this.typicalMax = typicalMax;
    }
    
    public String getDisplayName() { return displayName; }
    public String getUnit() { return unit; }
    public boolean isHigherBetter() { return higherBetter; }
    public boolean isCriticalMetric() { return criticalMetric; }
    public double getTypicalMax() { return typicalMax; }
    
    /**
     * Get default warning threshold percentage
     */
    public double getDefaultWarningThreshold() {
        if (higherBetter) {
            return typicalMax * 0.7; // 70% of max for metrics where higher is better
        } else {
            return typicalMax * 0.8; // 80% of max for metrics where lower is better
        }
    }
    
    /**
     * Get default critical threshold percentage
     */
    public double getDefaultCriticalThreshold() {
        if (higherBetter) {
            return typicalMax * 0.5; // 50% of max for metrics where higher is better
        } else {
            return typicalMax * 0.95; // 95% of max for metrics where lower is better
        }
    }
}