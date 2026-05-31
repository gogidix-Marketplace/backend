package com.gogidix.aiservices.aisecurityservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Security Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class SecurityServiceMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter encryptionTotalCounter;
    private final Counter encryptionSuccessCounter;
    private final Counter encryptionFailureCounter;
    private final Counter decryptionTotalCounter;
    private final Counter keyGenerationCounter;
    private final Counter keyRotationCounter;

    // Timers
    private final Timer encryptionTimer;
    private final Timer decryptionTimer;
    private final Timer keyGenerationTimer;

    public SecurityServiceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.encryptionTotalCounter = Counter.builder("security.encryption.total")
                .description("Total number of encryption operations")
                .tag("service", "ai-security")
                .register(meterRegistry);

        this.encryptionSuccessCounter = Counter.builder("security.encryption.success")
                .description("Number of successful encryptions")
                .tag("service", "ai-security")
                .register(meterRegistry);

        this.encryptionFailureCounter = Counter.builder("security.encryption.failure")
                .description("Number of failed encryptions")
                .tag("service", "ai-security")
                .register(meterRegistry);

        this.decryptionTotalCounter = Counter.builder("security.decryption.total")
                .description("Total number of decryption operations")
                .tag("service", "ai-security")
                .register(meterRegistry);

        this.keyGenerationCounter = Counter.builder("security.key.generation")
                .description("Number of keys generated")
                .tag("service", "ai-security")
                .register(meterRegistry);

        this.keyRotationCounter = Counter.builder("security.key.rotation")
                .description("Number of keys rotated")
                .tag("service", "ai-security")
                .register(meterRegistry);

        this.encryptionTimer = Timer.builder("security.encryption.duration")
                .description("Encryption operation time")
                .tag("service", "ai-security")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.decryptionTimer = Timer.builder("security.decryption.duration")
                .description("Decryption operation time")
                .tag("service", "ai-security")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.keyGenerationTimer = Timer.builder("security.key.generation.duration")
                .description("Key generation time")
                .tag("service", "ai-security")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementEncryptionTotal() { encryptionTotalCounter.increment(); }
    public void incrementEncryptionSuccess() { encryptionSuccessCounter.increment(); }
    public void incrementEncryptionFailure() { encryptionFailureCounter.increment(); }
    public void incrementDecryptionTotal() { decryptionTotalCounter.increment(); }
    public void incrementKeyGeneration() { keyGenerationCounter.increment(); }
    public void incrementKeyRotation() { keyRotationCounter.increment(); }

    public void recordEncryptionTime(long durationMs) { encryptionTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public void recordDecryptionTime(long durationMs) { decryptionTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public void recordKeyGenerationTime(long durationMs) { keyGenerationTimer.record(durationMs, TimeUnit.MILLISECONDS); }

    public Timer.Sample startEncryptionTimer() { return Timer.start(meterRegistry); }
    public void stopEncryptionTimer(Timer.Sample sample) { sample.stop(encryptionTimer); }
    public Timer.Sample startDecryptionTimer() { return Timer.start(meterRegistry); }
    public void stopDecryptionTimer(Timer.Sample sample) { sample.stop(decryptionTimer); }

    public double getEncryptionLatencyP95() { return encryptionTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getDecryptionLatencyP95() { return decryptionTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getErrorRate() {
        long total = (long) encryptionTotalCounter.count();
        long failures = (long) encryptionFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
