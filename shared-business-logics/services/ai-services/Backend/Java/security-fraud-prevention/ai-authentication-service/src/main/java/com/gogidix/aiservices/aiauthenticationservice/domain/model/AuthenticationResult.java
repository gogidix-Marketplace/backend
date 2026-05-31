package com.gogidix.aiservices.aiauthenticationservice.domain.model;

import com.gogidix.aiservices.aiauthenticationservice.shared.exception.AuthenticationException;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class AuthenticationResult {
    private final String tenantId;
    private final UUID userId;
    private final String sessionId;
    private final String accessToken;
    private final String refreshToken;
    private final int expiresIn;
    private final Set<String> roles;
    private final AuthenticationStatus status;
    private final String failureReason;
    private final String mfaMethod;
    private final BiometricType biometricType;
    private final Double confidenceScore;
    private final Double riskScore;
    private final RiskLevel riskLevel;
    private final Integer remainingAttempts;
    private final Instant lockUntil;
    private final Instant sessionCreatedAt;
    private final Instant sessionExpiresAt;
    private final String ipAddress;
    private final String location;
    private final String deviceFingerprint;
    private final boolean newDevice;
    private final boolean unusualLocation;
    private final String authenticationMethod;

    private AuthenticationResult(Builder builder) {
        this.tenantId = builder.tenantId;
        this.userId = builder.userId;
        this.sessionId = builder.sessionId;
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
        this.expiresIn = builder.expiresIn;
        this.roles = builder.roles;
        this.status = builder.status;
        this.failureReason = builder.failureReason;
        this.mfaMethod = builder.mfaMethod;
        this.biometricType = builder.biometricType;
        this.confidenceScore = builder.confidenceScore;
        this.riskScore = builder.riskScore;
        this.riskLevel = builder.riskLevel;
        this.remainingAttempts = builder.remainingAttempts;
        this.lockUntil = builder.lockUntil;
        this.sessionCreatedAt = builder.sessionCreatedAt;
        this.sessionExpiresAt = builder.sessionExpiresAt;
        this.ipAddress = builder.ipAddress;
        this.location = builder.location;
        this.deviceFingerprint = builder.deviceFingerprint;
        this.newDevice = builder.newDevice;
        this.unusualLocation = builder.unusualLocation;
        this.authenticationMethod = builder.authenticationMethod;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.tenantId = this.tenantId;
        builder.userId = this.userId;
        builder.sessionId = this.sessionId;
        builder.accessToken = this.accessToken;
        builder.refreshToken = this.refreshToken;
        builder.expiresIn = this.expiresIn;
        builder.roles = this.roles;
        builder.status = this.status;
        builder.failureReason = this.failureReason;
        builder.mfaMethod = this.mfaMethod;
        builder.biometricType = this.biometricType;
        builder.confidenceScore = this.confidenceScore;
        builder.riskScore = this.riskScore;
        builder.riskLevel = this.riskLevel;
        builder.remainingAttempts = this.remainingAttempts;
        builder.lockUntil = this.lockUntil;
        builder.sessionCreatedAt = this.sessionCreatedAt;
        builder.sessionExpiresAt = this.sessionExpiresAt;
        builder.ipAddress = this.ipAddress;
        builder.location = this.location;
        builder.deviceFingerprint = this.deviceFingerprint;
        builder.newDevice = this.newDevice;
        builder.unusualLocation = this.unusualLocation;
        builder.authenticationMethod = this.authenticationMethod;
        return builder;
    }

    // Getters
    public String getTenantId() { return tenantId; }
    public UUID getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public int getExpiresIn() { return expiresIn; }
    public Set<String> getRoles() { return roles; }
    public AuthenticationStatus getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
    public String getMfaMethod() { return mfaMethod; }
    public BiometricType getBiometricType() { return biometricType; }
    public Double getConfidenceScore() { return confidenceScore; }
    public Double getRiskScore() { return riskScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public Integer getRemainingAttempts() { return remainingAttempts; }
    public Instant getLockUntil() { return lockUntil; }
    public Instant getSessionCreatedAt() { return sessionCreatedAt; }
    public Instant getSessionExpiresAt() { return sessionExpiresAt; }
    public String getIpAddress() { return ipAddress; }
    public String getLocation() { return location; }
    public String getDeviceFingerprint() { return deviceFingerprint; }
    public boolean isNewDevice() { return newDevice; }
    public boolean isUnusualLocation() { return unusualLocation; }
    public String getAuthenticationMethod() { return authenticationMethod; }

    public boolean isAuthenticated() {
        return status == AuthenticationStatus.AUTHENTICATED;
    }

    public boolean isMfaRequired() {
        return status == AuthenticationStatus.MFA_REQUIRED;
    }

    public boolean isLocked() {
        return status == AuthenticationStatus.ACCOUNT_LOCKED;
    }

    public boolean isHighRiskBlocked() {
        return status == AuthenticationStatus.HIGH_RISK_BLOCKED;
    }

    public boolean isSessionValid() {
        return sessionExpiresAt != null && sessionExpiresAt.isAfter(Instant.now());
    }

    public static AuthenticationResult success(String userId, String sessionId) {
        return success(userId, sessionId, Set.of("USER"));
    }

    public static AuthenticationResult success(String userId, String sessionId, Set<String> roles) {
        Instant now = Instant.now();
        return AuthenticationResult.builder()
                .userId(UUID.fromString(userId))
                .sessionId(sessionId)
                .accessToken(generateToken())
                .refreshToken(generateRefreshToken())
                .expiresIn(3600)
                .roles(roles)
                .status(AuthenticationStatus.AUTHENTICATED)
                .sessionCreatedAt(now)
                .sessionExpiresAt(now.plusSeconds(3600))
                .remainingAttempts(5)
                .riskScore(0.1)
                .riskLevel(RiskLevel.LOW)
                .authenticationMethod("PASSWORD")
                .build();
    }

    public static AuthenticationResult failed(AuthenticationStatus status, String reason) {
        return AuthenticationResult.builder()
                .status(status)
                .failureReason(reason)
                .remainingAttempts(4)
                .build();
    }

    public static AuthenticationResult withAttempts(AuthenticationStatus status, String reason, int remainingAttempts) {
        return AuthenticationResult.builder()
                .status(status)
                .failureReason(reason)
                .remainingAttempts(remainingAttempts)
                .build();
    }

    public static AuthenticationResult mfaRequired(String userId, String mfaMethod) {
        return AuthenticationResult.builder()
                .userId(UUID.fromString(userId))
                .status(AuthenticationStatus.MFA_REQUIRED)
                .mfaMethod(mfaMethod)
                .remainingAttempts(5)
                .build();
    }

    public static AuthenticationResult accountLocked(String userId, Instant lockUntil) {
        return AuthenticationResult.builder()
                .userId(UUID.fromString(userId))
                .status(AuthenticationStatus.ACCOUNT_LOCKED)
                .lockUntil(lockUntil)
                .remainingAttempts(0)
                .build();
    }

    public static AuthenticationResult biometricSuccess(String userId, String sessionId, BiometricType biometricType) {
        return biometricSuccess(userId, sessionId, biometricType, 0.95);
    }

    public static AuthenticationResult biometricSuccess(String userId, String sessionId, BiometricType biometricType, double confidenceScore) {
        if (confidenceScore < 0.7) {
            throw new AuthenticationException("Biometric confidence too low");
        }
        Instant now = Instant.now();
        return AuthenticationResult.builder()
                .userId(UUID.fromString(userId))
                .sessionId(sessionId)
                .accessToken(generateToken())
                .refreshToken(generateRefreshToken())
                .expiresIn(3600)
                .roles(Set.of("USER"))
                .status(AuthenticationStatus.AUTHENTICATED)
                .biometricType(biometricType)
                .confidenceScore(confidenceScore)
                .sessionCreatedAt(now)
                .sessionExpiresAt(now.plusSeconds(3600))
                .authenticationMethod("BIOMETRIC")
                .build();
    }

    public static AuthenticationResult withRiskAssessment(String userId, String sessionId, double riskScore) {
        Instant now = Instant.now();
        RiskLevel riskLevel = classifyRisk(riskScore);

        if (riskScore > 0.75) {
            return AuthenticationResult.builder()
                    .userId(UUID.fromString(userId))
                    .sessionId(sessionId)
                    .status(AuthenticationStatus.HIGH_RISK_BLOCKED)
                    .riskScore(riskScore)
                    .riskLevel(riskLevel)
                    .failureReason("High risk detected")
                    .build();
        } else if (riskScore > 0.33) {
            return AuthenticationResult.builder()
                    .userId(UUID.fromString(userId))
                    .sessionId(sessionId)
                    .status(AuthenticationStatus.MFA_REQUIRED)
                    .mfaMethod("SMS")
                    .riskScore(riskScore)
                    .riskLevel(riskLevel)
                    .build();
        } else {
            return AuthenticationResult.builder()
                    .userId(UUID.fromString(userId))
                    .sessionId(sessionId)
                    .accessToken(generateToken())
                    .refreshToken(generateRefreshToken())
                    .expiresIn(3600)
                    .roles(Set.of("USER"))
                    .status(AuthenticationStatus.AUTHENTICATED)
                    .riskScore(riskScore)
                    .riskLevel(riskLevel)
                    .sessionCreatedAt(now)
                    .sessionExpiresAt(now.plusSeconds(3600))
                    .authenticationMethod("PASSWORD")
                    .build();
        }
    }

    public static AuthenticationResult withDeviceTracking(String userId, String sessionId, String deviceFingerprint) {
        AuthenticationResult base = success(userId, sessionId);
        return base.toBuilder()
                .deviceFingerprint(deviceFingerprint)
                .newDevice(true)
                .build();
    }

    public static AuthenticationResult withKnownDevice(String userId, String sessionId, String deviceFingerprint) {
        AuthenticationResult base = success(userId, sessionId);
        return base.toBuilder()
                .deviceFingerprint(deviceFingerprint)
                .newDevice(false)
                .build();
    }

    public static AuthenticationResult withIpTracking(String userId, String sessionId, String ipAddress) {
        AuthenticationResult base = success(userId, sessionId);
        return base.toBuilder()
                .ipAddress(ipAddress)
                .build();
    }

    public static AuthenticationResult withLocationTracking(String userId, String sessionId, String location) {
        AuthenticationResult base = success(userId, sessionId);
        return base.toBuilder()
                .location(location)
                .build();
    }

    public static AuthenticationResult withUnusualLocation(String userId, String sessionId, String location) {
        AuthenticationResult base = success(userId, sessionId);
        return base.toBuilder()
                .location(location)
                .unusualLocation(true)
                .build();
    }

    public static AuthenticationResult withSessionExpiry(AuthenticationResult result, Instant expiryTime) {
        return result.toBuilder()
                .sessionExpiresAt(expiryTime)
                .build();
    }

    private static RiskLevel classifyRisk(double score) {
        if (score <= 0.33) return RiskLevel.LOW;
        if (score <= 0.75) return RiskLevel.MEDIUM;
        return RiskLevel.HIGH;
    }

    private static String generateToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "") +
               "." +
               java.util.UUID.randomUUID().toString().replace("-", "") +
               "." +
               java.util.UUID.randomUUID().toString().replace("-", "");
    }

    private static String generateRefreshToken() {
        return java.util.UUID.randomUUID().toString().replace("-", "") +
               java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public static class Builder {
        private String tenantId = "default"; // Default tenant for backward compatibility
        private UUID userId;
        private String sessionId;
        private String accessToken;
        private String refreshToken;
        private Integer expiresIn;
        private Set<String> roles;
        private AuthenticationStatus status;
        private String failureReason;
        private String mfaMethod;
        private BiometricType biometricType;
        private Double confidenceScore;
        private Double riskScore;
        private RiskLevel riskLevel;
        private Integer remainingAttempts;
        private Instant lockUntil;
        private Instant sessionCreatedAt;
        private Instant sessionExpiresAt;
        private String ipAddress;
        private String location;
        private String deviceFingerprint;
        private Boolean newDevice;
        private Boolean unusualLocation;
        private String authenticationMethod;

        public Builder() {}

        public Builder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
        public Builder userId(UUID userId) { this.userId = userId; return this; }
        public Builder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public Builder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
        public Builder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }
        public Builder expiresIn(Integer expiresIn) { this.expiresIn = expiresIn; return this; }
        public Builder roles(Set<String> roles) { this.roles = roles; return this; }
        public Builder status(AuthenticationStatus status) { this.status = status; return this; }
        public Builder failureReason(String failureReason) { this.failureReason = failureReason; return this; }
        public Builder mfaMethod(String mfaMethod) { this.mfaMethod = mfaMethod; return this; }
        public Builder biometricType(BiometricType biometricType) { this.biometricType = biometricType; return this; }
        public Builder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public Builder riskScore(Double riskScore) { this.riskScore = riskScore; return this; }
        public Builder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public Builder remainingAttempts(Integer remainingAttempts) { this.remainingAttempts = remainingAttempts; return this; }
        public Builder lockUntil(Instant lockUntil) { this.lockUntil = lockUntil; return this; }
        public Builder sessionCreatedAt(Instant sessionCreatedAt) { this.sessionCreatedAt = sessionCreatedAt; return this; }
        public Builder sessionExpiresAt(Instant sessionExpiresAt) { this.sessionExpiresAt = sessionExpiresAt; return this; }
        public Builder ipAddress(String ipAddress) { this.ipAddress = ipAddress; return this; }
        public Builder location(String location) { this.location = location; return this; }
        public Builder deviceFingerprint(String deviceFingerprint) { this.deviceFingerprint = deviceFingerprint; return this; }
        public Builder newDevice(boolean newDevice) { this.newDevice = newDevice; return this; }
        public Builder unusualLocation(boolean unusualLocation) { this.unusualLocation = unusualLocation; return this; }
        public Builder authenticationMethod(String authenticationMethod) { this.authenticationMethod = authenticationMethod; return this; }

        private int getExpiresIn() { return expiresIn != null ? expiresIn : 3600; }

        public AuthenticationResult build() {
            return new AuthenticationResult(this);
        }
    }
}
