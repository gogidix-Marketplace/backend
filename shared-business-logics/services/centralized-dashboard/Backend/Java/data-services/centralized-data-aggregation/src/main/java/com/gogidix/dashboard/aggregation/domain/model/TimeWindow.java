package com.gogidix.dashboard.aggregation.domain.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Time Window Value Object
 * 
 * Represents time boundaries for metric aggregation with business logic
 */
public class TimeWindow {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final TimeGranularity granularity;
    private final Duration duration;
    
    public TimeWindow(LocalDateTime startTime, LocalDateTime endTime, TimeGranularity granularity) {
        this.startTime = Objects.requireNonNull(startTime, "Start time cannot be null");
        this.endTime = Objects.requireNonNull(endTime, "End time cannot be null");
        this.granularity = Objects.requireNonNull(granularity, "Granularity cannot be null");
        
        validateTimeWindow();
        this.duration = Duration.between(startTime, endTime);
    }
    
    public static TimeWindow of(LocalDateTime startTime, LocalDateTime endTime, TimeGranularity granularity) {
        return new TimeWindow(startTime, endTime, granularity);
    }
    
    public static TimeWindow lastHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourAgo = now.minusHours(1);
        return new TimeWindow(hourAgo, now, TimeGranularity.HOURLY);
    }
    
    public static TimeWindow lastDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayAgo = now.minusDays(1);
        return new TimeWindow(dayAgo, now, TimeGranularity.DAILY);
    }
    
    public static TimeWindow lastWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minusWeeks(1);
        return new TimeWindow(weekAgo, now, TimeGranularity.WEEKLY);
    }
    
    public static TimeWindow currentMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return new TimeWindow(monthStart, now, TimeGranularity.MONTHLY);
    }
    
    public static TimeWindow currentQuarter() {
        LocalDateTime now = LocalDateTime.now();
        int quarterStart = ((now.getMonthValue() - 1) / 3) * 3 + 1;
        LocalDateTime quarterStartTime = now.withMonth(quarterStart).withDayOfMonth(1)
            .withHour(0).withMinute(0).withSecond(0);
        return new TimeWindow(quarterStartTime, now, TimeGranularity.QUARTERLY);
    }
    
    private void validateTimeWindow() {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time cannot be after end time");
        }
        
        // Validate granularity matches duration
        Duration actualDuration = Duration.between(startTime, endTime);
        if (!granularity.isValidDuration(actualDuration)) {
            throw new IllegalArgumentException("Duration doesn't match granularity: " + actualDuration + " for " + granularity);
        }
    }
    
    /**
     * Check if time window is recent (within expected freshness)
     */
    public boolean isRecent() {
        LocalDateTime now = LocalDateTime.now();
        Duration timeSinceEnd = Duration.between(endTime, now);
        return timeSinceEnd.compareTo(granularity.getMaxStaleness()) <= 0;
    }
    
    /**
     * Check if time window is within expected freshness
     */
    public boolean isWithinExpectedFreshness() {
        return getFreshnessMinutes() <= granularity.getExpectedFreshnessMinutes();
    }
    
    /**
     * Get freshness in minutes (how old the end time is)
     */
    public long getFreshnessMinutes() {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.MINUTES.between(endTime, now);
    }
    
    /**
     * Check if time window is stale
     */
    public boolean isStale() {
        return !isRecent();
    }
    
    /**
     * Check if time window overlaps with another
     */
    public boolean overlapsWith(TimeWindow other) {
        return !this.endTime.isBefore(other.startTime) && !this.startTime.isAfter(other.endTime);
    }
    
    /**
     * Check if time window is compatible with another (same granularity and duration)
     */
    public boolean isCompatibleWith(TimeWindow other) {
        return this.granularity == other.granularity && 
               this.duration.equals(other.duration);
    }
    
    /**
     * Check if time window contains a specific timestamp
     */
    public boolean contains(LocalDateTime timestamp) {
        return !timestamp.isBefore(startTime) && !timestamp.isAfter(endTime);
    }
    
    /**
     * Check if time window is complete (ended in the past)
     */
    public boolean isComplete() {
        return endTime.isBefore(LocalDateTime.now());
    }
    
    /**
     * Check if time window is current (includes current time)
     */
    public boolean isCurrent() {
        LocalDateTime now = LocalDateTime.now();
        return contains(now) || endTime.isAfter(now.minusMinutes(1));
    }
    
    /**
     * Get the next time window after this one
     */
    public TimeWindow getNext() {
        Duration windowDuration = granularity.getDefaultDuration();
        LocalDateTime nextStart = this.endTime;
        LocalDateTime nextEnd = nextStart.plus(windowDuration);
        
        return new TimeWindow(nextStart, nextEnd, granularity);
    }
    
    /**
     * Get the previous time window before this one
     */
    public TimeWindow getPrevious() {
        Duration windowDuration = granularity.getDefaultDuration();
        LocalDateTime prevEnd = this.startTime;
        LocalDateTime prevStart = prevEnd.minus(windowDuration);
        
        return new TimeWindow(prevStart, prevEnd, granularity);
    }
    
    /**
     * Update end time (for extending ongoing aggregation)
     */
    public TimeWindow updateEndTime(LocalDateTime newEndTime) {
        if (newEndTime.isBefore(startTime)) {
            throw new IllegalArgumentException("New end time cannot be before start time");
        }
        return new TimeWindow(startTime, newEndTime, granularity);
    }
    
    /**
     * Split time window into smaller granularity windows
     */
    public java.util.List<TimeWindow> splitInto(TimeGranularity smallerGranularity) {
        if (!granularity.canSplitInto(smallerGranularity)) {
            throw new IllegalArgumentException("Cannot split " + granularity + " into " + smallerGranularity);
        }
        
        java.util.List<TimeWindow> windows = new java.util.ArrayList<>();
        Duration smallerDuration = smallerGranularity.getDefaultDuration();
        
        LocalDateTime current = startTime;
        while (current.isBefore(endTime)) {
            LocalDateTime windowEnd = current.plus(smallerDuration);
            if (windowEnd.isAfter(endTime)) {
                windowEnd = endTime;
            }
            windows.add(new TimeWindow(current, windowEnd, smallerGranularity));
            current = windowEnd;
        }
        
        return windows;
    }
    
    /**
     * Get overlap duration with another time window
     */
    public Duration getOverlapDuration(TimeWindow other) {
        if (!overlapsWith(other)) {
            return Duration.ZERO;
        }
        
        LocalDateTime overlapStart = startTime.isAfter(other.startTime) ? startTime : other.startTime;
        LocalDateTime overlapEnd = endTime.isBefore(other.endTime) ? endTime : other.endTime;
        
        return Duration.between(overlapStart, overlapEnd);
    }
    
    /**
     * Get window position (0.0 = start, 1.0 = end)
     */
    public double getPositionAt(LocalDateTime timestamp) {
        if (!contains(timestamp)) {
            return timestamp.isBefore(startTime) ? 0.0 : 1.0;
        }
        
        Duration totalDuration = Duration.between(startTime, endTime);
        Duration elapsedDuration = Duration.between(startTime, timestamp);
        
        return elapsedDuration.toMillis() / (double) totalDuration.toMillis();
    }
    
    /**
     * Format time window for display
     */
    public String formatForDisplay() {
        return String.format("%s - %s (%s)", 
                           startTime.toString(), endTime.toString(), granularity);
    }
    
    /**
     * Get business day coverage (excludes weekends for applicable granularities)
     */
    public int getBusinessDaysCovered() {
        if (granularity == TimeGranularity.REAL_TIME || granularity == TimeGranularity.HOURLY) {
            return 0; // Not applicable for sub-daily granularities
        }
        
        long totalDays = ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate());
        int businessDays = 0;
        
        LocalDateTime current = startTime;
        for (int i = 0; i < totalDays; i++) {
            if (current.getDayOfWeek().getValue() <= 5) { // Monday=1, Friday=5
                businessDays++;
            }
            current = current.plusDays(1);
        }
        
        return businessDays;
    }
    
    // Getters
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public TimeGranularity getGranularity() { return granularity; }
    public Duration getDuration() { return duration; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeWindow that = (TimeWindow) o;
        return Objects.equals(startTime, that.startTime) &&
               Objects.equals(endTime, that.endTime) &&
               granularity == that.granularity;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, granularity);
    }
    
    @Override
    public String toString() {
        return formatForDisplay();
    }
}