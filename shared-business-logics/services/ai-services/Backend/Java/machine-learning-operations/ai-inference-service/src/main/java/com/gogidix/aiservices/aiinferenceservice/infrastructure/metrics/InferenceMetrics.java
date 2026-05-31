package com.gogidix.aiservices.aiinferenceservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Inference Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class InferenceMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter inferenceTotalCounter;
    private final Counter inferenceSuccessCounter;
    private final Counter inferenceFailureCounter;
    private final Counter modelLoadedCounter;
    private final Counter modelUnloadedCounter;
    private final Counter highConfidenceCounter;
    private final Counter lowConfidenceCounter;

    // Timers
    private final Timer inferenceTimer;
    private final Timer modelLoadingTimer;
    private final Timer predictionTimer;

    public InferenceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.inferenceTotalCounter = Counter.builder("ai.inference.total")
                .description("Total number of inference requests")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        this.inferenceSuccessCounter = Counter.builder("ai.inference.success")
                .description("Number of successful inference requests")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        this.inferenceFailureCounter = Counter.builder("ai.inference.failure")
                .description("Number of failed inference requests")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        this.modelLoadedCounter = Counter.builder("ai.model.loaded")
                .description("Number of models loaded")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        this.modelUnloadedCounter = Counter.builder("ai.model.unloaded")
                .description("Number of models unloaded")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        this.highConfidenceCounter = Counter.builder("ai.inference.high.confidence")
                .description("Number of high confidence inferences")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        this.lowConfidenceCounter = Counter.builder("ai.inference.low.confidence")
                .description("Number of low confidence inferences")
                .tag("service", "ai-inference")
                .register(meterRegistry);

        // Initialize timers
        this.inferenceTimer = Timer.builder("ai.inference.duration")
                .description("Inference processing time")
                .tag("service", "ai-inference")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelLoadingTimer = Timer.builder("ai.model.loading.duration")
                .description("Model loading time")
                .tag("service", "ai-inference")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.predictionTimer = Timer.builder("ai.prediction.duration")
                .description("ML model prediction time")
                .tag("service", "ai-inference")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementInferenceTotal() {
        inferenceTotalCounter.increment();
    }

    public void incrementInferenceSuccess() {
        inferenceSuccessCounter.increment();
    }

    public void incrementInferenceFailure() {
        inferenceFailureCounter.increment();
    }

    public void incrementModelLoaded() {
        modelLoadedCounter.increment();
    }

    public void incrementModelUnloaded() {
        modelUnloadedCounter.increment();
    }

    public void incrementHighConfidence() {
        highConfidenceCounter.increment();
    }

    public void incrementLowConfidence() {
        lowConfidenceCounter.increment();
    }

    // Timer methods
    public void recordInferenceTime(long durationMs) {
        inferenceTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startInferenceTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopInferenceTimer(Timer.Sample sample) {
        sample.stop(inferenceTimer);
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

    public void recordPredictionTime(long durationMs) {
        predictionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionTimer(Timer.Sample sample) {
        sample.stop(predictionTimer);
    }

    // SLO compliance methods
    public double getInferenceLatencyP95() {
        return inferenceTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getInferenceLatencyP99() {
        return inferenceTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getPredictionLatencyP95() {
        return predictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) inferenceTotalCounter.count();
        long failures = (long) inferenceFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
