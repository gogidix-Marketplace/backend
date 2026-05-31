package com.gogidix.aiservices.aichurnpredictionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Customer Prediction Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ChurnPredictionMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter segmentationTotalCounter;
    private final Counter segmentationSuccessCounter;
    private final Counter segmentationFailureCounter;
    private final Counter segmentCreatedCounter;
    private final Counter segmentUpdatedCounter;
    private final Counter segmentDeletedCounter;
    private final Counter customerAnalyzedCounter;
    private final Counter customerAddedToPredictionCounter;
    private final Counter customerRemovedFromPredictionCounter;

    // Timers
    private final Timer segmentationTimer;
    private final Timer segmentAnalysisTimer;
    private final Timer segmentCreationTimer;
    private final Timer segmentUpdateTimer;
    private final Timer customerProcessingTimer;

    public ChurnPredictionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.segmentationTotalCounter = Counter.builder("segmentation.total")
                .description("Total number of segmentation requests")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.segmentationSuccessCounter = Counter.builder("segmentation.success")
                .description("Number of successful segmentation requests")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.segmentationFailureCounter = Counter.builder("segmentation.failure")
                .description("Number of failed segmentation requests")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.segmentCreatedCounter = Counter.builder("segment.created")
                .description("Number of segments created")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.segmentUpdatedCounter = Counter.builder("segment.updated")
                .description("Number of segments updated")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.segmentDeletedCounter = Counter.builder("segment.deleted")
                .description("Number of segments deleted")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.customerAnalyzedCounter = Counter.builder("customer.analyzed")
                .description("Number of customers analyzed")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.customerAddedToPredictionCounter = Counter.builder("customer.added.to.segment")
                .description("Number of customers added to segments")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        this.customerRemovedFromPredictionCounter = Counter.builder("customer.removed.from.segment")
                .description("Number of customers removed from segments")
                .tag("service", "ai-churn-predictionation")
                .register(meterRegistry);

        // Initialize timers
        this.segmentationTimer = Timer.builder("segmentation.duration")
                .description("Churn predictionation processing time")
                .tag("service", "ai-churn-predictionation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.segmentAnalysisTimer = Timer.builder("segment.analysis.duration")
                .description("Prediction analysis processing time")
                .tag("service", "ai-churn-predictionation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentCreationTimer = Timer.builder("segment.creation.duration")
                .description("Prediction creation time")
                .tag("service", "ai-churn-predictionation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentUpdateTimer = Timer.builder("segment.update.duration")
                .description("Prediction update time")
                .tag("service", "ai-churn-predictionation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.customerProcessingTimer = Timer.builder("customer.processing.duration")
                .description("Customer processing time")
                .tag("service", "ai-churn-predictionation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementPredictionTotal() {
        segmentationTotalCounter.increment();
    }

    public void incrementPredictionSuccess() {
        segmentationSuccessCounter.increment();
    }

    public void incrementPredictionFailure() {
        segmentationFailureCounter.increment();
    }

    public void incrementPredictionCreated() {
        segmentCreatedCounter.increment();
    }

    public void incrementPredictionUpdated() {
        segmentUpdatedCounter.increment();
    }

    public void incrementPredictionDeleted() {
        segmentDeletedCounter.increment();
    }

    public void incrementCustomerAnalyzed() {
        customerAnalyzedCounter.increment();
    }

    public void incrementModelAddedToPrediction() {
        customerAddedToPredictionCounter.increment();
    }

    public void incrementCustomerRemovedFromPrediction() {
        customerRemovedFromPredictionCounter.increment();
    }

    // Timer methods
    public void recordPredictionTime(long durationMs) {
        segmentationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionTimer(Timer.Sample sample) {
        sample.stop(segmentationTimer);
    }

    public Timer.Sample startPredictionAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentAnalysisTimer);
    }

    public Timer.Sample startPredictionCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionCreationTimer(Timer.Sample sample) {
        sample.stop(segmentCreationTimer);
    }

    public Timer.Sample startPredictionUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPredictionUpdateTimer(Timer.Sample sample) {
        sample.stop(segmentUpdateTimer);
    }

    public Timer.Sample startCustomerProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopCustomerProcessingTimer(Timer.Sample sample) {
        sample.stop(customerProcessingTimer);
    }

    // SLO compliance methods
    public double getPredictionLatencyP95() {
        return segmentationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getPredictionLatencyP99() {
        return segmentationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getPredictionAnalysisLatencyP95() {
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
