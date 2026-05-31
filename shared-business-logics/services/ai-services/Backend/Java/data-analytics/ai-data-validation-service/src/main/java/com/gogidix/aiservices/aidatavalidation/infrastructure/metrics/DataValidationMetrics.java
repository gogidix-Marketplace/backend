package com.gogidix.aiservices.aidatavalidation.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Data Validation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class DataValidationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter validationTotalCounter;
    private final Counter validationSuccessCounter;
    private final Counter validationFailureCounter;
    private final Counter validationPassedCounter;
    private final Counter validationFailedCounter;
    private final Counter dataQualityIssuesCounter;

    // Timers
    private final Timer validationTimer;
    private final Timer schemaValidationTimer;
    private final Timer ruleExecutionTimer;

    public DataValidationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.validationTotalCounter = Counter.builder("data.validation.total")
                .description("Total number of validation requests")
                .tag("service", "ai-data-validation")
                .register(meterRegistry);

        this.validationSuccessCounter = Counter.builder("data.validation.success")
                .description("Number of successful validation requests")
                .tag("service", "ai-data-validation")
                .register(meterRegistry);

        this.validationFailureCounter = Counter.builder("data.validation.failure")
                .description("Number of failed validation requests")
                .tag("service", "ai-data-validation")
                .register(meterRegistry);

        this.validationPassedCounter = Counter.builder("data.validation.passed")
                .description("Number of validations that passed")
                .tag("service", "ai-data-validation")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("data.validation.failed")
                .description("Number of validations that failed")
                .tag("service", "ai-data-validation")
                .register(meterRegistry);

        this.dataQualityIssuesCounter = Counter.builder("data.validation.quality.issues")
                .description("Number of data quality issues detected")
                .tag("service", "ai-data-validation")
                .register(meterRegistry);

        // Initialize timers
        this.validationTimer = Timer.builder("data.validation.duration")
                .description("Data validation processing time")
                .tag("service", "ai-data-validation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.schemaValidationTimer = Timer.builder("data.validation.schema.duration")
                .description("Schema validation time")
                .tag("service", "ai-data-validation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.ruleExecutionTimer = Timer.builder("data.validation.rule.duration")
                .description("Validation rule execution time")
                .tag("service", "ai-data-validation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementValidationTotal() {
        validationTotalCounter.increment();
    }

    public void incrementValidationSuccess() {
        validationSuccessCounter.increment();
    }

    public void incrementValidationFailure() {
        validationFailureCounter.increment();
    }

    public void incrementValidationPassed() {
        validationPassedCounter.increment();
    }

    public void incrementValidationFailed() {
        validationFailedCounter.increment();
    }

    public void incrementDataQualityIssue() {
        dataQualityIssuesCounter.increment();
    }

    // Timer methods
    public void recordValidationTime(long durationMs) {
        validationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopValidationTimer(Timer.Sample sample) {
        sample.stop(validationTimer);
    }

    public Timer.Sample startSchemaValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSchemaValidationTimer(Timer.Sample sample) {
        sample.stop(schemaValidationTimer);
    }

    public Timer.Sample startRuleExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRuleExecutionTimer(Timer.Sample sample) {
        sample.stop(ruleExecutionTimer);
    }

    // SLO compliance methods
    public double getValidationLatencyP95() {
        return validationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getValidationLatencyP99() {
        return validationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getSchemaValidationLatencyP95() {
        return schemaValidationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) validationTotalCounter.count();
        long failures = (long) validationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
