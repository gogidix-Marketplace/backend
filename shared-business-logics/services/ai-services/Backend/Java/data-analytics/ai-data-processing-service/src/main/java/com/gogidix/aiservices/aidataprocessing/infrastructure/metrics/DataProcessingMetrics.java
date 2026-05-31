package com.gogidix.aiservices.aidataprocessing.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Data Processing Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class DataProcessingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter processingTotalCounter;
    private final Counter processingSuccessCounter;
    private final Counter processingFailureCounter;
    private final Counter validationPassedCounter;
    private final Counter validationFailedCounter;
    private final Counter transformationAppliedCounter;

    // Timers
    private final Timer processingTimer;
    private final Timer validationTimer;
    private final Timer transformationTimer;
    private final Timer batchProcessingTimer;

    public DataProcessingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.processingTotalCounter = Counter.builder("ai.data.processing.total")
                .description("Total number of data processing requests")
                .tag("service", "ai-data-processing")
                .register(meterRegistry);

        this.processingSuccessCounter = Counter.builder("ai.data.processing.success")
                .description("Number of successful data processing requests")
                .tag("service", "ai-data-processing")
                .register(meterRegistry);

        this.processingFailureCounter = Counter.builder("ai.data.processing.failure")
                .description("Number of failed data processing requests")
                .tag("service", "ai-data-processing")
                .register(meterRegistry);

        this.validationPassedCounter = Counter.builder("ai.data.processing.validation.passed")
                .description("Number of passed validations")
                .tag("service", "ai-data-processing")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("ai.data.processing.validation.failed")
                .description("Number of failed validations")
                .tag("service", "ai-data-processing")
                .register(meterRegistry);

        this.transformationAppliedCounter = Counter.builder("ai.data.processing.transformation.applied")
                .description("Number of transformations applied")
                .tag("service", "ai-data-processing")
                .register(meterRegistry);

        // Initialize timers
        this.processingTimer = Timer.builder("ai.data.processing.duration")
                .description("Data processing time")
                .tag("service", "ai-data-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.validationTimer = Timer.builder("ai.data.processing.validation.duration")
                .description("Validation processing time")
                .tag("service", "ai-data-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.transformationTimer = Timer.builder("ai.data.processing.transformation.duration")
                .description("Transformation processing time")
                .tag("service", "ai-data-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.batchProcessingTimer = Timer.builder("ai.data.processing.batch.duration")
                .description("Batch processing time")
                .tag("service", "ai-data-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementProcessingTotal() {
        processingTotalCounter.increment();
    }

    public void incrementProcessingSuccess() {
        processingSuccessCounter.increment();
    }

    public void incrementProcessingFailure() {
        processingFailureCounter.increment();
    }

    public void incrementValidationPassed() {
        validationPassedCounter.increment();
    }

    public void incrementValidationFailed() {
        validationFailedCounter.increment();
    }

    public void incrementTransformationApplied() {
        transformationAppliedCounter.increment();
    }

    // Timer methods
    public void recordProcessingTime(long durationMs) {
        processingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProcessingTimer(Timer.Sample sample) {
        sample.stop(processingTimer);
    }

    public Timer.Sample startValidationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopValidationTimer(Timer.Sample sample) {
        sample.stop(validationTimer);
    }

    public void recordTransformationTime(long durationMs) {
        transformationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startTransformationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTransformationTimer(Timer.Sample sample) {
        sample.stop(transformationTimer);
    }

    public void recordBatchProcessingTime(long durationMs) {
        batchProcessingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startBatchProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopBatchProcessingTimer(Timer.Sample sample) {
        sample.stop(batchProcessingTimer);
    }

    // SLO compliance methods
    public double getProcessingLatencyP95() {
        return processingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getProcessingLatencyP99() {
        return processingTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getValidationLatencyP95() {
        return validationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) processingTotalCounter.count();
        long failures = (long) processingFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
