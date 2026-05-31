package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.aggregate.AuthenticationAttempt;
import com.gogidix.aiservices.aiauthenticationservice.domain.model.RiskLevel;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class RiskAssessmentPolicy {
    private static final double RISK_THRESHOLD_LOW = 0.33;
    private static final double RISK_THRESHOLD_HIGH = 0.75;
    private static final int RAPID_ATTEMPT_THRESHOLD = 3;
    private static final long RAPID_ATTEMPT_WINDOW_SECONDS = 60;
    private static final long MIN_IMPOSSIBLE_TRAVEL_SECONDS = 300; // 5 minutes

    private static final Set<String> BLOCKED_IPS = Set.of(
            "103.21.244.0", "103.21.244.1"
    );

    private static final Set<String> KNOWN_LOCATIONS = Set.of(
            "New York, US", "Los Angeles, US", "Chicago, US"
    );

    public double assessRisk(AuthenticationAttempt attempt) {
        double riskScore = 0.0;

        // IP-based risk
        if (attempt.getIpAddress() != null) {
            if (BLOCKED_IPS.contains(attempt.getIpAddress())) {
                riskScore += 0.8;
            }
        }

        // Location-based risk
        if (attempt.getLocation() != null && !KNOWN_LOCATIONS.contains(attempt.getLocation())) {
            riskScore += 0.3;
        }

        // New device risk
        if (attempt.isNewDevice()) {
            riskScore += 0.2;
        }

        // Device fingerprint risk
        if (attempt.getDeviceFingerprint() == null) {
            riskScore += 0.1;
        }

        // User agent risk
        if (attempt.getUserAgent() == null || attempt.getUserAgent().isEmpty()) {
            riskScore += 0.1;
        }

        // Cap at 1.0
        return Math.min(riskScore, 1.0);
    }

    public boolean isRapidSuccessiveAttempts(List<AuthenticationAttempt> attempts) {
        if (attempts.size() < RAPID_ATTEMPT_THRESHOLD) {
            return false;
        }

        Instant oldestAttempt = attempts.get(0).getTimestamp();
        Instant newestAttempt = attempts.get(attempts.size() - 1).getTimestamp();

        return Duration.between(oldestAttempt, newestAttempt).getSeconds() < RAPID_ATTEMPT_WINDOW_SECONDS;
    }

    public boolean isImpossibleTravel(AuthenticationAttempt attempt1, AuthenticationAttempt attempt2) {
        if (attempt1.getLocation() == null || attempt2.getLocation() == null) {
            return false;
        }

        if (attempt1.getLocation().equals(attempt2.getLocation())) {
            return false;
        }

        long timeDiffSeconds = Math.abs(Duration.between(attempt1.getTimestamp(), attempt2.getTimestamp()).getSeconds());

        // If locations are very different (different continents likely) and time difference is small
        boolean veryDifferentLocations = !attempt1.getLocation().split(",")[1].trim()
                .equals(attempt2.getLocation().split(",")[1].trim());

        return veryDifferentLocations && timeDiffSeconds < MIN_IMPOSSIBLE_TRAVEL_SECONDS;
    }

    public RiskLevel classifyRisk(double score) {
        if (score <= RISK_THRESHOLD_LOW) return RiskLevel.LOW;
        if (score <= RISK_THRESHOLD_HIGH) return RiskLevel.MEDIUM;
        return RiskLevel.HIGH;
    }
}
