package com.gogidix.aiservices.aitrainingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Training Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class TrainingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter trainingJobTotalCounter;
    private final Counter trainingJobSuccessCounter;
    private final Counter trainingJobFailureCounter;
    private final Counter trainingJobStartedCounter;
    private final Counter trainingJobCompletedCounter;
    private final Counter trainingJobCancelledCounter;

    // Timers
    private final Timer trainingJobTimer;
    private final Timer modelTrainingTimer;
    private final Timer fineTuningTimer;

    public TrainingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.trainingJobTotalCounter = Counter.builder("ai.training.job.total")
                .description("Total number of training job requests")
                .tag("service", "ai-training")
                .register(meterRegistry);

        this.trainingJobSuccessCounter = Counter.builder("ai.training.job.success")
                .description("Number of successful training jobs")
                .tag("service", "ai-training")
                .register(meterRegistry);

        this.trainingJobFailureCounter = Counter.builder("ai.training.job.failure")
                .description("Number of failed training jobs")
                .tag("service", "ai-training")
                .register(meterRegistry);

        this.trainingJobStartedCounter = Counter.builder("ai.training.job.started")
                .description("Number of training jobs started")
                .tag("service", "ai-training")
                .register(meterRegistry);

        this.trainingJobCompletedCounter = Counter.builder("ai.training.job.completed")
                .description("Number of training jobs completed")
                .tag("service", "ai-training")
                .register(meterRegistry);

        this.trainingJobCancelledCounter = Counter.builder("ai.training.job.cancelled")
                .description("Number of training jobs cancelled")
                .tag("service", "ai-training")
                .register(meterRegistry);

        // Initialize timers
        this.trainingJobTimer = Timer.builder("ai.training.job.duration")
                .description("Training job processing time")
                .tag("service", "ai-training")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelTrainingTimer = Timer.builder("ai.model.training.duration")
                .description("Model training time")
                .tag("service", "ai-training")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.fineTuningTimer = Timer.builder("ai.fine.tuning.duration")
                .description("Fine-tuning operation time")
                .tag("service", "ai-training")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementTrainingJobTotal() {
        trainingJobTotalCounter.increment();
    }

    public void incrementTrainingJobSuccess() {
        trainingJobSuccessCounter.increment();
    }

    public void incrementTrainingJobFailure() {
        trainingJobFailureCounter.increment();
    }

    public void incrementTrainingJobStarted() {
        trainingJobStartedCounter.increment();
    }

    public void incrementTrainingJobCompleted() {
        trainingJobCompletedCounter.increment();
    }

    public void incrementTrainingJobCancelled() {
        trainingJobCancelledCounter.increment();
    }

    // Timer methods
    public void recordTrainingJobTime(long durationMs) {
        trainingJobTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startTrainingJobTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTrainingJobTimer(Timer.Sample sample) {
        sample.stop(trainingJobTimer);
    }

    public void recordModelTrainingTime(long durationMs) {
        modelTrainingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startModelTrainingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelTrainingTimer(Timer.Sample sample) {
        sample.stop(modelTrainingTimer);
    }

    public void recordFineTuningTime(long durationMs) {
        fineTuningTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startFineTuningTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopFineTuningTimer(Timer.Sample sample) {
        sample.stop(fineTuningTimer);
    }

    // SLO compliance methods
    public double getTrainingJobLatencyP95() {
        return trainingJobTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getTrainingJobLatencyP99() {
        return trainingJobTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getModelTrainingLatencyP95() {
        return modelTrainingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) trainingJobTotalCounter.count();
        long failures = (long) trainingJobFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
