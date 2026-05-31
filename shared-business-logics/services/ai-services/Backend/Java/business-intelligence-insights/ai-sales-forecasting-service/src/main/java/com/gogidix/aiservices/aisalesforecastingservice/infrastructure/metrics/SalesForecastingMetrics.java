package com.gogidix.aiservices.aisalesforecastingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for ForecastModel Forecasting Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class SalesForecastingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter segmentationTotalCounter;
    private final Counter segmentationSuccessCounter;
    private final Counter segmentationFailureCounter;
    private final Counter segmentCreatedCounter;
    private final Counter segmentUpdatedCounter;
    private final Counter segmentDeletedCounter;
    private final Counter customerAnalyzedCounter;
    private final Counter customerAddedToForecastCounter;
    private final Counter customerRemovedFromForecastCounter;

    // Timers
    private final Timer segmentationTimer;
    private final Timer segmentAnalysisTimer;
    private final Timer segmentCreationTimer;
    private final Timer segmentUpdateTimer;
    private final Timer customerProcessingTimer;

    public SalesForecastingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.segmentationTotalCounter = Counter.builder("segmentation.total")
                .description("Total number of segmentation requests")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.segmentationSuccessCounter = Counter.builder("segmentation.success")
                .description("Number of successful segmentation requests")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.segmentationFailureCounter = Counter.builder("segmentation.failure")
                .description("Number of failed segmentation requests")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.segmentCreatedCounter = Counter.builder("segment.created")
                .description("Number of segments created")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.segmentUpdatedCounter = Counter.builder("segment.updated")
                .description("Number of segments updated")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.segmentDeletedCounter = Counter.builder("segment.deleted")
                .description("Number of segments deleted")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.customerAnalyzedCounter = Counter.builder("customer.analyzed")
                .description("Number of customers analyzed")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.customerAddedToForecastCounter = Counter.builder("customer.added.to.segment")
                .description("Number of customers added to segments")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        this.customerRemovedFromForecastCounter = Counter.builder("customer.removed.from.segment")
                .description("Number of customers removed from segments")
                .tag("service", "ai-sales-forecastation")
                .register(meterRegistry);

        // Initialize timers
        this.segmentationTimer = Timer.builder("segmentation.duration")
                .description("ForecastModel segmentation processing time")
                .tag("service", "ai-sales-forecastation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.segmentAnalysisTimer = Timer.builder("segment.analysis.duration")
                .description("Forecast analysis processing time")
                .tag("service", "ai-sales-forecastation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentCreationTimer = Timer.builder("segment.creation.duration")
                .description("Forecast creation time")
                .tag("service", "ai-sales-forecastation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentUpdateTimer = Timer.builder("segment.update.duration")
                .description("Forecast update time")
                .tag("service", "ai-sales-forecastation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.customerProcessingTimer = Timer.builder("customer.processing.duration")
                .description("ForecastModel processing time")
                .tag("service", "ai-sales-forecastation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementForecastingTotal() {
        segmentationTotalCounter.increment();
    }

    public void incrementForecastingSuccess() {
        segmentationSuccessCounter.increment();
    }

    public void incrementForecastingFailure() {
        segmentationFailureCounter.increment();
    }

    public void incrementForecastCreated() {
        segmentCreatedCounter.increment();
    }

    public void incrementForecastUpdated() {
        segmentUpdatedCounter.increment();
    }

    public void incrementForecastDeleted() {
        segmentDeletedCounter.increment();
    }

    public void incrementForecastModelAnalyzed() {
        customerAnalyzedCounter.increment();
    }

    public void incrementForecastModelAddedToForecast() {
        customerAddedToForecastCounter.increment();
    }

    public void incrementForecastModelRemovedFromForecast() {
        customerRemovedFromForecastCounter.increment();
    }

    // Timer methods
    public void recordForecastingTime(long durationMs) {
        segmentationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startForecastingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastingTimer(Timer.Sample sample) {
        sample.stop(segmentationTimer);
    }

    public Timer.Sample startForecastAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentAnalysisTimer);
    }

    public Timer.Sample startForecastCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastCreationTimer(Timer.Sample sample) {
        sample.stop(segmentCreationTimer);
    }

    public Timer.Sample startForecastUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastUpdateTimer(Timer.Sample sample) {
        sample.stop(segmentUpdateTimer);
    }

    public Timer.Sample startForecastModelProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopForecastModelProcessingTimer(Timer.Sample sample) {
        sample.stop(customerProcessingTimer);
    }

    // SLO compliance methods
    public double getForecastingLatencyP95() {
        return segmentationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getForecastingLatencyP99() {
        return segmentationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getForecastAnalysisLatencyP95() {
        return segmentAnalysisTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) segmentationTotalCounter.count();
        long failures = (long) segmentationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
