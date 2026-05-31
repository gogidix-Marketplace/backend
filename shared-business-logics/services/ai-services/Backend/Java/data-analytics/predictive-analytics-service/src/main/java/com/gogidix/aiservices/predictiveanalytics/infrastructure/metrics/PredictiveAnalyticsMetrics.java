package com.gogidix.aiservices.predictiveanalytics.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Predictive Analytics Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class PredictiveAnalyticsMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter forecastGenerationTotalCounter;
    private final Counter forecastGenerationSuccessCounter;
    private final Counter forecastGenerationFailureCounter;
    private final Counter modelTrainingCounter;
    private final Counter predictionCounter;

    // Timers
    private final Timer forecastGenerationTimer;
    private final Timer modelTrainingTimer;
    private final Timer predictionTimer;
    private final Timer dataPreprocessingTimer;

    public PredictiveAnalyticsMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.forecastGenerationTotalCounter = Counter.builder("forecast.generation.total")
                .description("Total number of forecast generation requests")
                .tag("service", "predictive-analytics")
                .register(meterRegistry);

        this.forecastGenerationSuccessCounter = Counter.builder("forecast.generation.success")
                .description("Number of successful forecast generation requests")
                .tag("service", "predictive-analytics")
                .register(meterRegistry);

        this.forecastGenerationFailureCounter = Counter.builder("forecast.generation.failure")
                .description("Number of failed forecast generation requests")
                .tag("service", "predictive-analytics")
                .register(meterRegistry);

        this.modelTrainingCounter = Counter.builder("model.training")
                .description("Number of model training operations")
                .tag("service", "predictive-analytics")
                .register(meterRegistry);

        this.predictionCounter = Counter.builder("prediction")
                .description("Number of predictions made")
                .tag("service", "predictive-analytics")
                .register(meterRegistry);

        // Initialize timers
        this.forecastGenerationTimer = Timer.builder("forecast.generation.duration")
                .description("Forecast generation processing time")
                .tag("service", "predictive-analytics")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelTrainingTimer = Timer.builder("model.training.duration")
                .description("Model training processing time")
                .tag("service", "predictive-analytics")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.predictionTimer = Timer.builder("prediction.duration")
                .description("Prediction processing time")
                .tag("service", "predictive-analytics")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.dataPreprocessingTimer = Timer.builder("data.preprocessing.duration")
                .description("Data preprocessing processing time")
                .tag("service", "predictive-analytics")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementForecastGenerationTotal() {
        forecastGenerationTotalCounter.increment();
    }

    public void incrementForecastGenerationSuccess() {
        forecastGenerationSuccessCounter.increment();
    }

    public void incrementForecastGenerationFailure() {
        forecastGenerationFailureCounter.increment();
    }

    public void incrementModelTraining() {
        modelTrainingCounter.increment();
    }

    public void incrementPrediction() {
        predictionCounter.increment();
    }

    // Timer methods
    public void recordForecastGenerationTime(long durationMs) {
        forecastGenerationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startForecastGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastGenerationTimer(Timer.Sample sample) {
        sample.stop(forecastGenerationTimer);
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

    public void recordPredictionTime(long durationMs) {
        predictionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionTimer(Timer.Sample sample) {
        sample.stop(predictionTimer);
    }

    public void recordDataPreprocessingTime(long durationMs) {
        dataPreprocessingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDataPreprocessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDataPreprocessingTimer(Timer.Sample sample) {
        sample.stop(dataPreprocessingTimer);
    }

    // SLO compliance methods
    public double getForecastGenerationLatencyP95() {
        return forecastGenerationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getForecastGenerationLatencyP99() {
        return forecastGenerationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getPredictionLatencyP95() {
        return predictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) forecastGenerationTotalCounter.count();
        long failures = (long) forecastGenerationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
