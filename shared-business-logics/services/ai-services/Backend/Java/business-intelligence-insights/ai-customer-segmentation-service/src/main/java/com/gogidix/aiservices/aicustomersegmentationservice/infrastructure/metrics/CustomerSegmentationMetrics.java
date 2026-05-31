package com.gogidix.aiservices.aicustomersegmentationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Customer Segmentation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class CustomerSegmentationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter segmentationTotalCounter;
    private final Counter segmentationSuccessCounter;
    private final Counter segmentationFailureCounter;
    private final Counter segmentCreatedCounter;
    private final Counter segmentUpdatedCounter;
    private final Counter segmentDeletedCounter;
    private final Counter customerAnalyzedCounter;
    private final Counter customerAddedToSegmentCounter;
    private final Counter customerRemovedFromSegmentCounter;

    // Timers
    private final Timer segmentationTimer;
    private final Timer segmentAnalysisTimer;
    private final Timer segmentCreationTimer;
    private final Timer segmentUpdateTimer;
    private final Timer customerProcessingTimer;

    public CustomerSegmentationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.segmentationTotalCounter = Counter.builder("segmentation.total")
                .description("Total number of segmentation requests")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.segmentationSuccessCounter = Counter.builder("segmentation.success")
                .description("Number of successful segmentation requests")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.segmentationFailureCounter = Counter.builder("segmentation.failure")
                .description("Number of failed segmentation requests")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.segmentCreatedCounter = Counter.builder("segment.created")
                .description("Number of segments created")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.segmentUpdatedCounter = Counter.builder("segment.updated")
                .description("Number of segments updated")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.segmentDeletedCounter = Counter.builder("segment.deleted")
                .description("Number of segments deleted")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.customerAnalyzedCounter = Counter.builder("customer.analyzed")
                .description("Number of customers analyzed")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.customerAddedToSegmentCounter = Counter.builder("customer.added.to.segment")
                .description("Number of customers added to segments")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.customerRemovedFromSegmentCounter = Counter.builder("customer.removed.from.segment")
                .description("Number of customers removed from segments")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        // Initialize timers
        this.segmentationTimer = Timer.builder("segmentation.duration")
                .description("Customer segmentation processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.segmentAnalysisTimer = Timer.builder("segment.analysis.duration")
                .description("Segment analysis processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentCreationTimer = Timer.builder("segment.creation.duration")
                .description("Segment creation time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentUpdateTimer = Timer.builder("segment.update.duration")
                .description("Segment update time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.customerProcessingTimer = Timer.builder("customer.processing.duration")
                .description("Customer processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementSegmentationTotal() {
        segmentationTotalCounter.increment();
    }

    public void incrementSegmentationSuccess() {
        segmentationSuccessCounter.increment();
    }

    public void incrementSegmentationFailure() {
        segmentationFailureCounter.increment();
    }

    public void incrementSegmentCreated() {
        segmentCreatedCounter.increment();
    }

    public void incrementSegmentUpdated() {
        segmentUpdatedCounter.increment();
    }

    public void incrementSegmentDeleted() {
        segmentDeletedCounter.increment();
    }

    public void incrementCustomerAnalyzed() {
        customerAnalyzedCounter.increment();
    }

    public void incrementCustomerAddedToSegment() {
        customerAddedToSegmentCounter.increment();
    }

    public void incrementCustomerRemovedFromSegment() {
        customerRemovedFromSegmentCounter.increment();
    }

    // Timer methods
    public void recordSegmentationTime(long durationMs) {
        segmentationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startSegmentationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSegmentationTimer(Timer.Sample sample) {
        sample.stop(segmentationTimer);
    }

    public Timer.Sample startSegmentAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSegmentAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentAnalysisTimer);
    }

    public Timer.Sample startSegmentCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSegmentCreationTimer(Timer.Sample sample) {
        sample.stop(segmentCreationTimer);
    }

    public Timer.Sample startSegmentUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSegmentUpdateTimer(Timer.Sample sample) {
        sample.stop(segmentUpdateTimer);
    }

    public Timer.Sample startCustomerProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopCustomerProcessingTimer(Timer.Sample sample) {
        sample.stop(customerProcessingTimer);
    }

    // SLO compliance methods
    public double getSegmentationLatencyP95() {
        return segmentationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getSegmentationLatencyP99() {
        return segmentationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getSegmentAnalysisLatencyP95() {
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
