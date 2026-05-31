package com.gogidix.aiservices.aiproductrecommendationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Product Recommendation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ProductRecommendationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter segmentationTotalCounter;
    private final Counter segmentationSuccessCounter;
    private final Counter segmentationFailureCounter;
    private final Counter segmentCreatedCounter;
    private final Counter segmentUpdatedCounter;
    private final Counter segmentDeletedCounter;
    private final Counter customerAnalyzedCounter;
    private final Counter customerAddedToRecommendationCounter;
    private final Counter customerRemovedFromRecommendationCounter;

    // Timers
    private final Timer segmentationTimer;
    private final Timer segmentAnalysisTimer;
    private final Timer segmentCreationTimer;
    private final Timer segmentUpdateTimer;
    private final Timer customerProcessingTimer;

    public ProductRecommendationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.segmentationTotalCounter = Counter.builder("segmentation.total")
                .description("Total number of segmentation requests")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.segmentationSuccessCounter = Counter.builder("segmentation.success")
                .description("Number of successful segmentation requests")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.segmentationFailureCounter = Counter.builder("segmentation.failure")
                .description("Number of failed segmentation requests")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.segmentCreatedCounter = Counter.builder("segment.created")
                .description("Number of segments created")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.segmentUpdatedCounter = Counter.builder("segment.updated")
                .description("Number of segments updated")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.segmentDeletedCounter = Counter.builder("segment.deleted")
                .description("Number of segments deleted")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.customerAnalyzedCounter = Counter.builder("customer.analyzed")
                .description("Number of customers analyzed")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.customerAddedToRecommendationCounter = Counter.builder("customer.added.to.segment")
                .description("Number of customers added to segments")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        this.customerRemovedFromRecommendationCounter = Counter.builder("customer.removed.from.segment")
                .description("Number of customers removed from segments")
                .tag("service", "ai-product-recommendationation")
                .register(meterRegistry);

        // Initialize timers
        this.segmentationTimer = Timer.builder("segmentation.duration")
                .description("Product segmentation processing time")
                .tag("service", "ai-product-recommendationation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.segmentAnalysisTimer = Timer.builder("segment.analysis.duration")
                .description("Recommendation analysis processing time")
                .tag("service", "ai-product-recommendationation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentCreationTimer = Timer.builder("segment.creation.duration")
                .description("Recommendation creation time")
                .tag("service", "ai-product-recommendationation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentUpdateTimer = Timer.builder("segment.update.duration")
                .description("Recommendation update time")
                .tag("service", "ai-product-recommendationation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.customerProcessingTimer = Timer.builder("customer.processing.duration")
                .description("Product processing time")
                .tag("service", "ai-product-recommendationation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementRecommendationTotal() {
        segmentationTotalCounter.increment();
    }

    public void incrementRecommendationSuccess() {
        segmentationSuccessCounter.increment();
    }

    public void incrementRecommendationFailure() {
        segmentationFailureCounter.increment();
    }

    public void incrementRecommendationCreated() {
        segmentCreatedCounter.increment();
    }

    public void incrementRecommendationUpdated() {
        segmentUpdatedCounter.increment();
    }

    public void incrementRecommendationDeleted() {
        segmentDeletedCounter.increment();
    }

    public void incrementProductAnalyzed() {
        customerAnalyzedCounter.increment();
    }

    public void incrementProductAddedToRecommendation() {
        customerAddedToRecommendationCounter.increment();
    }

    public void incrementProductRemovedFromRecommendation() {
        customerRemovedFromRecommendationCounter.increment();
    }

    // Timer methods
    public void recordRecommendationTime(long durationMs) {
        segmentationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startRecommendationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationTimer(Timer.Sample sample) {
        sample.stop(segmentationTimer);
    }

    public Timer.Sample startRecommendationAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentAnalysisTimer);
    }

    public Timer.Sample startRecommendationCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationCreationTimer(Timer.Sample sample) {
        sample.stop(segmentCreationTimer);
    }

    public Timer.Sample startRecommendationUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecommendationUpdateTimer(Timer.Sample sample) {
        sample.stop(segmentUpdateTimer);
    }

    public Timer.Sample startProductProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProductProcessingTimer(Timer.Sample sample) {
        sample.stop(customerProcessingTimer);
    }

    // SLO compliance methods
    public double getRecommendationLatencyP95() {
        return segmentationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getRecommendationLatencyP99() {
        return segmentationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getRecommendationAnalysisLatencyP95() {
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
