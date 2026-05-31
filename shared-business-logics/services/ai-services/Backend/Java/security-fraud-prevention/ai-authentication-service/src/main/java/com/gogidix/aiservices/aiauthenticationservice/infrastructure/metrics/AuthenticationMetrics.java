package com.gogidix.aiservices.aiauthenticationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Authentication Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AuthenticationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter authenticationTotalCounter;
    private final Counter authenticationSuccessCounter;
    private final Counter authenticationFailureCounter;
    private final Counter mfaRequiredCounter;
    private final Counter accountLockedCounter;
    private final Counter highRiskBlockedCounter;
    private final Counter biometricAuthCounter;
    private final Counter passwordAuthCounter;
    private final Counter tokenRefreshCounter;

    // Timers
    private final Timer authenticationTimer;
    private final Timer mfaValidationTimer;
    private final Timer biometricValidationTimer;
    private final Timer tokenGenerationTimer;
    private final Timer databaseLookupTimer;
    private final Timer passwordValidationTimer;

    public AuthenticationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.authenticationTotalCounter = Counter.builder("authentication.total")
                .description("Total number of authentication requests")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.authenticationSuccessCounter = Counter.builder("authentication.success")
                .description("Number of successful authentication requests")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.authenticationFailureCounter = Counter.builder("authentication.failure")
                .description("Number of failed authentication requests")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.mfaRequiredCounter = Counter.builder("authentication.mfa.required")
                .description("Number of authentications requiring MFA")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.accountLockedCounter = Counter.builder("authentication.account.locked")
                .description("Number of account lock events")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.highRiskBlockedCounter = Counter.builder("authentication.highrisk.blocked")
                .description("Number of high-risk authentication attempts blocked")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.biometricAuthCounter = Counter.builder("authentication.biometric.total")
                .description("Number of biometric authentication attempts")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.passwordAuthCounter = Counter.builder("authentication.password.total")
                .description("Number of password authentication attempts")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        this.tokenRefreshCounter = Counter.builder("authentication.token.refresh")
                .description("Number of token refresh requests")
                .tag("service", "ai-authentication")
                .register(meterRegistry);

        // Initialize timers
        this.authenticationTimer = Timer.builder("authentication.duration")
                .description("Full authentication processing time")
                .tag("service", "ai-authentication")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.mfaValidationTimer = Timer.builder("authentication.mfa.duration")
                .description("MFA validation processing time")
                .tag("service", "ai-authentication")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.biometricValidationTimer = Timer.builder("authentication.biometric.duration")
                .description("Biometric validation processing time")
                .tag("service", "ai-authentication")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.tokenGenerationTimer = Timer.builder("authentication.token.generation.duration")
                .description("Token generation time")
                .tag("service", "ai-authentication")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseLookupTimer = Timer.builder("authentication.database.lookup.duration")
                .description("Database lookup time")
                .tag("service", "ai-authentication")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.passwordValidationTimer = Timer.builder("authentication.password.validation.duration")
                .description("Password validation time")
                .tag("service", "ai-authentication")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementAuthenticationTotal() {
        authenticationTotalCounter.increment();
    }

    public void incrementAuthenticationSuccess() {
        authenticationSuccessCounter.increment();
    }

    public void incrementAuthenticationFailure() {
        authenticationFailureCounter.increment();
    }

    public void incrementMfaRequired() {
        mfaRequiredCounter.increment();
    }

    public void incrementAccountLocked() {
        accountLockedCounter.increment();
    }

    public void incrementHighRiskBlocked() {
        highRiskBlockedCounter.increment();
    }

    public void incrementBiometricAuth() {
        biometricAuthCounter.increment();
    }

    public void incrementPasswordAuth() {
        passwordAuthCounter.increment();
    }

    public void incrementTokenRefresh() {
        tokenRefreshCounter.increment();
    }

    // Timer methods
    public void recordAuthenticationTime(long durationMs) {
        authenticationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAuthenticationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAuthenticationTimer(Timer.Sample sample) {
        sample.stop(authenticationTimer);
    }

    public Timer.Sample startMfaValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMfaValidationTimer(Timer.Sample sample) {
        sample.stop(mfaValidationTimer);
    }

    public Timer.Sample startBiometricValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopBiometricValidationTimer(Timer.Sample sample) {
        sample.stop(biometricValidationTimer);
    }

    public Timer.Sample startTokenGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTokenGenerationTimer(Timer.Sample sample) {
        sample.stop(tokenGenerationTimer);
    }

    public Timer.Sample startDatabaseLookupTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDatabaseLookupTimer(Timer.Sample sample) {
        sample.stop(databaseLookupTimer);
    }

    public Timer.Sample startPasswordValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPasswordValidationTimer(Timer.Sample sample) {
        sample.stop(passwordValidationTimer);
    }

    // SLO compliance methods
    public double getAuthenticationLatencyP95() {
        return authenticationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAuthenticationLatencyP99() {
        return authenticationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getBiometricValidationLatencyP95() {
        return biometricValidationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getPasswordValidationLatencyP95() {
        return passwordValidationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) authenticationTotalCounter.count();
        long failures = (long) authenticationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public double getSuccessRate() {
        long total = (long) authenticationTotalCounter.count();
        long successes = (long) authenticationSuccessCounter.count();
        return total > 0 ? (double) successes / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
