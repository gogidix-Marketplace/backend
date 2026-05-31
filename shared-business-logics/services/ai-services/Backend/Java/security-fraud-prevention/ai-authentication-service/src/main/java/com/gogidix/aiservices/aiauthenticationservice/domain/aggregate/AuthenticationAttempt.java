package com.gogidix.aiservices.aiauthenticationservice.domain.aggregate;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.*;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class AuthenticationAttempt {
    private final UUID attemptId;
    private UUID userId;
    private final String username;
    private final Instant timestamp;
    private final String sessionId;
    private AttemptStatus status;
    private FailureReason failureReason;
    private AuthenticationMethod method;
    private String ipAddress;
    private String userAgent;
    private String deviceInfo;
    private String location;
    private String deviceFingerprint;
    private Double riskScore;
    private boolean suspicious;
    private String suspiciousReason;
    private String mfaCode;
    private String mfaMethod;
    private int mfaAttempts;
    private int failedAttemptCount;
    private Instant completedAt;
    private Instant lockoutUntil;
    private final Map<String, String> metadata;

    public static AuthenticationAttempt create(String username) {
        return AuthenticationAttempt.builder()
                .attemptId(UUID.randomUUID())
                .username(username)
                .timestamp(Instant.now())
                .status(AttemptStatus.PENDING)
                .method(AuthenticationMethod.PASSWORD)
                .failedAttemptCount(0)
                .mfaAttempts(0)
                .metadata(new HashMap<>())
                .build();
    }

    public static AuthenticationAttempt createWithUserId(UUID userId) {
        return AuthenticationAttempt.builder()
                .attemptId(UUID.randomUUID())
                .userId(userId)
                .timestamp(Instant.now())
                .status(AttemptStatus.PENDING)
                .method(AuthenticationMethod.PASSWORD)
                .failedAttemptCount(0)
                .mfaAttempts(0)
                .metadata(new HashMap<>())
                .build();
    }

    public void markSuccessful() {
        this.status = AttemptStatus.SUCCESS;
        this.completedAt = Instant.now();
        this.failedAttemptCount = 0;
    }

    public void markFailed(FailureReason reason) {
        this.status = AttemptStatus.FAILED;
        this.failureReason = reason;
        this.completedAt = Instant.now();
        this.failedAttemptCount++;
    }

    public void requireMfa(String mfaMethod) {
        this.status = AttemptStatus.MFA_REQUIRED;
        this.mfaMethod = mfaMethod;
    }

    public void markBlocked(FailureReason reason) {
        this.status = AttemptStatus.BLOCKED;
        this.failureReason = reason;
    }

    public void invalidMfaAttempt() {
        this.mfaAttempts++;
    }

    public boolean isMfaLocked() {
        return this.mfaAttempts >= 3;
    }

    public boolean isPending() {
        return this.status == AttemptStatus.PENDING;
    }

    public boolean isSuccessful() {
        return this.status == AttemptStatus.SUCCESS;
    }

    public boolean isBlocked() {
        return this.status == AttemptStatus.BLOCKED;
    }

    public boolean isLockedOut() {
        return this.lockoutUntil != null && this.lockoutUntil.isAfter(Instant.now());
    }

    public int getRemainingAttempts() {
        return Math.max(0, 5 - this.failedAttemptCount);
    }

    public void setRiskScore(double riskScore) {
        if (riskScore < 0.0 || riskScore > 1.0) {
            throw new IllegalArgumentException("Risk score must be between 0 and 1");
        }
        this.riskScore = riskScore;
    }

    public void flagSuspicious(String reason) {
        this.suspicious = true;
        this.suspiciousReason = reason;
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    public String getMetadata(String key) {
        return this.metadata.get(key);
    }

    public boolean validateMfaCode(String providedCode) {
        return this.mfaCode != null && this.mfaCode.equals(providedCode);
    }

    public Long getDuration() {
        if (completedAt == null) {
            return null;
        }
        return Duration.between(timestamp, completedAt).toMillis();
    }

    public boolean isNewDevice() {
        return this.deviceFingerprint != null && !this.deviceFingerprint.startsWith("known_");
    }
}
