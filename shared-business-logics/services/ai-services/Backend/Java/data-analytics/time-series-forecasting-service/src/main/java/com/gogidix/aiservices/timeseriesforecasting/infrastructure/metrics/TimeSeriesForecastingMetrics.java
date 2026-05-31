package com.gogidix.aiservices.timeseriesforecasting.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Time Series Forecasting Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class TimeSeriesForecastingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter forecastingTotalCounter;
    private final Counter forecastingSuccessCounter;
    private final Counter forecastingFailureCounter;
    private final Counter modelTrainingCounter;
    private final Counter seasonalityAnalysisCounter;

    // Timers
    private final Timer forecastingTimer;
    private final Timer modelTrainingTimer;
    private final Timer seasonalityAnalysisTimer;
    private final Timer dataPreprocessingTimer;

    public TimeSeriesForecastingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.forecastingTotalCounter = Counter.builder("timeseries.forecasting.total")
                .description("Total number of time series forecasting requests")
                .tag("service", "time-series-forecasting")
                .register(meterRegistry);

        this.forecastingSuccessCounter = Counter.builder("timeseries.forecasting.success")
                .description("Number of successful forecasting requests")
                .tag("service", "time-series-forecasting")
                .register(meterRegistry);

        this.forecastingFailureCounter = Counter.builder("timeseries.forecasting.failure")
                .description("Number of failed forecasting requests")
                .tag("service", "time-series-forecasting")
                .register(meterRegistry);

        this.modelTrainingCounter = Counter.builder("timeseries.model.training")
                .description("Number of model training operations")
                .tag("service", "time-series-forecasting")
                .register(meterRegistry);

        this.seasonalityAnalysisCounter = Counter.builder("timeseries.seasonality.analysis")
                .description("Number of seasonality analysis operations")
                .tag("service", "time-series-forecasting")
                .register(meterRegistry);

        // Initialize timers
        this.forecastingTimer = Timer.builder("timeseries.forecasting.duration")
                .description("Time series forecasting processing time")
                .tag("service", "time-series-forecasting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelTrainingTimer = Timer.builder("timeseries.model.training.duration")
                .description("Model training processing time")
                .tag("service", "time-series-forecasting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.seasonalityAnalysisTimer = Timer.builder("timeseries.seasonality.analysis.duration")
                .description("Seasonality analysis processing time")
                .tag("service", "time-series-forecasting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.dataPreprocessingTimer = Timer.builder("timeseries.data.preprocessing.duration")
                .description("Data preprocessing processing time")
                .tag("service", "time-series-forecasting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementForecastingTotal() {
        forecastingTotalCounter.increment();
    }

    public void incrementForecastingSuccess() {
        forecastingSuccessCounter.increment();
    }

    public void incrementForecastingFailure() {
        forecastingFailureCounter.increment();
    }

    public void incrementModelTraining() {
        modelTrainingCounter.increment();
    }

    public void incrementSeasonalityAnalysis() {
        seasonalityAnalysisCounter.increment();
    }

    // Timer methods
    public void recordForecastingTime(long durationMs) {
        forecastingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startForecastingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastingTimer(Timer.Sample sample) {
        sample.stop(forecastingTimer);
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

    public void recordSeasonalityAnalysisTime(long durationMs) {
        seasonalityAnalysisTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startSeasonalityAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSeasonalityAnalysisTimer(Timer.Sample sample) {
        sample.stop(seasonalityAnalysisTimer);
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
    public double getForecastingLatencyP95() {
        return forecastingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getForecastingLatencyP99() {
        return forecastingTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getModelTrainingLatencyP95() {
        return modelTrainingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) forecastingTotalCounter.count();
        long failures = (long) forecastingFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
