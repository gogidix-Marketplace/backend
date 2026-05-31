package com.gogidix.cargo.eventdriven.domain.policy;

public class EventRetryPolicy {
    private int maxRetries;
    private long initialDelayMs;
    private double multiplier;
    private long maxDelayMs;

    public EventRetryPolicy() { this(3, 1000, 2.0, 30000); }
    public EventRetryPolicy(int maxRetries, long initialDelayMs, double multiplier, long maxDelayMs) {
        this.maxRetries = maxRetries; this.initialDelayMs = initialDelayMs;
        this.multiplier = multiplier; this.maxDelayMs = maxDelayMs;
    }
    public long calculateDelay(int retryAttempt) {
        long delay = (long) (initialDelayMs * Math.pow(multiplier, retryAttempt));
        return Math.min(delay, maxDelayMs);
    }
    public boolean shouldRetry(int currentRetries) { return currentRetries < maxRetries; }
    public int getMaxRetries() { return maxRetries; }
    public long getInitialDelayMs() { return initialDelayMs; }
    public double getMultiplier() { return multiplier; }
    public long getMaxDelayMs() { return maxDelayMs; }
}
