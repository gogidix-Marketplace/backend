package com.gogidix.aiservices.aimanagementservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Model Management Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ModelManagementMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter modelRegistrationTotalCounter;
    private final Counter modelRegistrationSuccessCounter;
    private final Counter modelRegistrationFailureCounter;
    private final Counter modelLoadedCounter;
    private final Counter modelUnloadedCounter;
    private final Counter modelDeployedCounter;
    private final Counter modelRetiredCounter;

    // Timers
    private final Timer modelRegistrationTimer;
    private final Timer modelLoadingTimer;
    private final Timer modelDeploymentTimer;

    public ModelManagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.modelRegistrationTotalCounter = Counter.builder("ai.model.registration.total")
                .description("Total number of model registration requests")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        this.modelRegistrationSuccessCounter = Counter.builder("ai.model.registration.success")
                .description("Number of successful model registrations")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        this.modelRegistrationFailureCounter = Counter.builder("ai.model.registration.failure")
                .description("Number of failed model registrations")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        this.modelLoadedCounter = Counter.builder("ai.model.loaded")
                .description("Number of models loaded")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        this.modelUnloadedCounter = Counter.builder("ai.model.unloaded")
                .description("Number of models unloaded")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        this.modelDeployedCounter = Counter.builder("ai.model.deployed")
                .description("Number of models deployed")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        this.modelRetiredCounter = Counter.builder("ai.model.retired")
                .description("Number of models retired")
                .tag("service", "ai-model-management")
                .register(meterRegistry);

        // Initialize timers
        this.modelRegistrationTimer = Timer.builder("ai.model.registration.duration")
                .description("Model registration processing time")
                .tag("service", "ai-model-management")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelLoadingTimer = Timer.builder("ai.model.loading.duration")
                .description("Model loading time")
                .tag("service", "ai-model-management")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.modelDeploymentTimer = Timer.builder("ai.model.deployment.duration")
                .description("Model deployment time")
                .tag("service", "ai-model-management")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementModelRegistrationTotal() {
        modelRegistrationTotalCounter.increment();
    }

    public void incrementModelRegistrationSuccess() {
        modelRegistrationSuccessCounter.increment();
    }

    public void incrementModelRegistrationFailure() {
        modelRegistrationFailureCounter.increment();
    }

    public void incrementModelLoaded() {
        modelLoadedCounter.increment();
    }

    public void incrementModelUnloaded() {
        modelUnloadedCounter.increment();
    }

    public void incrementModelDeployed() {
        modelDeployedCounter.increment();
    }

    public void incrementModelRetired() {
        modelRetiredCounter.increment();
    }

    // Timer methods
    public void recordModelRegistrationTime(long durationMs) {
        modelRegistrationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startModelRegistrationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelRegistrationTimer(Timer.Sample sample) {
        sample.stop(modelRegistrationTimer);
    }

    public void recordModelLoadingTime(long durationMs) {
        modelLoadingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startModelLoadingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelLoadingTimer(Timer.Sample sample) {
        sample.stop(modelLoadingTimer);
    }

    public void recordModelDeploymentTime(long durationMs) {
        modelDeploymentTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startModelDeploymentTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelDeploymentTimer(Timer.Sample sample) {
        sample.stop(modelDeploymentTimer);
    }

    // SLO compliance methods
    public double getModelRegistrationLatencyP95() {
        return modelRegistrationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getModelRegistrationLatencyP99() {
        return modelRegistrationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getModelLoadingLatencyP95() {
        return modelLoadingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) modelRegistrationTotalCounter.count();
        long failures = (long) modelRegistrationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
