package com.gogidix.courier.assignmentservice.domain.model;

import java.time.Instant;
import java.util.Objects;

public final class TimeWindow {

    private final Instant startTime;
    private final Instant endTime;

    public TimeWindow(Instant startTime, Instant endTime) {
        this.startTime = Objects.requireNonNull(startTime, "startTime must not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime must not be null");
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime must be after startTime");
        }
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public boolean contains(Instant instant) {
        return !instant.isBefore(startTime) && !instant.isAfter(endTime);
    }

    public boolean overlaps(TimeWindow other) {
        return !this.endTime.isBefore(other.startTime) && !other.endTime.isBefore(this.startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeWindow that = (TimeWindow) o;
        return Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return "TimeWindow{startTime=" + startTime + ", endTime=" + endTime + '}';
    }
}
