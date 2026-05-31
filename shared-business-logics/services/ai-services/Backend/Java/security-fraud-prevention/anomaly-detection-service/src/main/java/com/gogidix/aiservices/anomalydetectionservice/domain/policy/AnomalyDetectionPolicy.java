package com.gogidix.aiservices.anomalydetectionservice.domain.policy;

import com.gogidix.aiservices.anomalydetectionservice.domain.model.DetectionAlgorithm;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class AnomalyDetectionPolicy {

    private static final Duration MAX_TIME_RANGE = Duration.ofDays(30);

    public void validateTimeRange(Instant start, Instant end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time must be provided");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (Duration.between(start, end).compareTo(MAX_TIME_RANGE) > 0) {
            throw new IllegalArgumentException("Time range exceeds maximum limit");
        }
    }

    public void validateAlgorithms(List<DetectionAlgorithm> algorithms) {
        if (algorithms == null || algorithms.isEmpty()) {
            throw new IllegalArgumentException("At least one algorithm must be specified");
        }
    }
}
