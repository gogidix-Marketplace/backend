package com.gogidix.aiservices.aiprediction.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Prediction Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class PredictionMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter predictionTotalCounter;
    private final Counter predictionSuccessCounter;
    private final Counter predictionFailureCounter;
    private final Counter batchPredictionCounter;
    private final Counter highConfidencePredictionCounter;
    private final Counter lowConfidencePredictionCounter;

    // Timers
    private final Timer predictionTimer;
    private final Timer modelInferenceTimer;
    private final Timer batchPredictionTimer;

    public PredictionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.predictionTotalCounter = Counter.builder("prediction.total")
                .description("Total number of prediction requests")
                .tag("service", "ai-prediction")
                .register(meterRegistry);

        this.predictionSuccessCounter = Counter.builder("prediction.success")
                .description("Number of successful prediction requests")
                .tag("service", "ai-prediction")
                .register(meterRegistry);

        this.predictionFailureCounter = Counter.builder("prediction.failure")
                .description("Number of failed prediction requests")
                .tag("service", "ai-prediction")
                .register(meterRegistry);

        this.batchPredictionCounter = Counter.builder("prediction.batch.total")
                .description("Number of batch prediction requests")
                .tag("service", "ai-prediction")
                .register(meterRegistry);

        this.highConfidencePredictionCounter = Counter.builder("prediction.high.confidence")
                .description("Number of high confidence predictions")
                .tag("service", "ai-prediction")
                .register(meterRegistry);

        this.lowConfidencePredictionCounter = Counter.builder("prediction.low.confidence")
                .description("Number of low confidence predictions")
                .tag("service", "ai-prediction")
                .register(meterRegistry);

        // Initialize timers
        this.predictionTimer = Timer.builder("prediction.duration")
                .description("Prediction processing time")
                .tag("service", "ai-prediction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelInferenceTimer = Timer.builder("prediction.model.inference.duration")
                .description("Model inference time")
                .tag("service", "ai-prediction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.batchPredictionTimer = Timer.builder("prediction.batch.duration")
                .description("Batch prediction processing time")
                .tag("service", "ai-prediction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementPredictionTotal() {
        predictionTotalCounter.increment();
    }

    public void incrementPredictionSuccess() {
        predictionSuccessCounter.increment();
    }

    public void incrementPredictionFailure() {
        predictionFailureCounter.increment();
    }

    public void incrementBatchPrediction() {
        batchPredictionCounter.increment();
    }

    public void incrementHighConfidencePrediction() {
        highConfidencePredictionCounter.increment();
    }

    public void incrementLowConfidencePrediction() {
        lowConfidencePredictionCounter.increment();
    }

    // Timer methods
    public void recordPredictionTime(long durationMs) {
        predictionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionTimer(Timer.Sample sample) {
        sample.stop(predictionTimer);
    }

    public Timer.Sample startModelInferenceTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelInferenceTimer(Timer.Sample sample) {
        sample.stop(modelInferenceTimer);
    }

    public Timer.Sample startBatchPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopBatchPredictionTimer(Timer.Sample sample) {
        sample.stop(batchPredictionTimer);
    }

    // SLO compliance methods
    public double getPredictionLatencyP95() {
        return predictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getPredictionLatencyP99() {
        return predictionTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getModelInferenceLatencyP95() {
        return modelInferenceTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) predictionTotalCounter.count();
        long failures = (long) predictionFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
