package com.gogidix.aiservices.aisecurityanalysisservice.domain.policy;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanType;

import java.time.Duration;
import java.time.Instant;

public class SecurityAnalysisPolicy {

    private static final int MAX_CONCURRENT_SCANS = 5;
    private static final int CURRENT_SCANS = 0;

    public void validateTarget(String target) {
        if (target == null || target.isBlank()) {
            throw new IllegalArgumentException("Target cannot be empty");
        }
        // Add more validation as needed
    }

    public void checkConcurrentScanLimit() {
        if (CURRENT_SCANS >= MAX_CONCURRENT_SCANS) {
            throw new IllegalStateException("Maximum concurrent scans limit reached");
        }
    }

    public Instant estimateCompletionTime(ScanType scanType) {
        Duration duration = switch (scanType) {
            case FULL -> Duration.ofHours(2);
            case QUICK -> Duration.ofMinutes(15);
            case CUSTOM -> Duration.ofMinutes(30);
        };
        return Instant.now().plus(duration);
    }
}
