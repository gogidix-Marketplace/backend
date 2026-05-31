package com.gogidix.aiservices.aiuserprofilingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for User Profileation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class UserProfileationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter segmentationTotalCounter;
    private final Counter segmentationSuccessCounter;
    private final Counter segmentationFailureCounter;
    private final Counter segmentCreatedCounter;
    private final Counter segmentUpdatedCounter;
    private final Counter segmentDeletedCounter;
    private final Counter customerAnalyzedCounter;
    private final Counter customerAddedToProfileCounter;
    private final Counter customerRemovedFromProfileCounter;

    // Timers
    private final Timer segmentationTimer;
    private final Timer segmentAnalysisTimer;
    private final Timer segmentCreationTimer;
    private final Timer segmentUpdateTimer;
    private final Timer customerProcessingTimer;

    public UserProfileationMetrics(MeterRegistry meterRegistry) {
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

        this.customerAddedToProfileCounter = Counter.builder("customer.added.to.segment")
                .description("Number of customers added to segments")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.customerRemovedFromProfileCounter = Counter.builder("customer.removed.from.segment")
                .description("Number of customers removed from segments")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        // Initialize timers
        this.segmentationTimer = Timer.builder("segmentation.duration")
                .description("User segmentation processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.segmentAnalysisTimer = Timer.builder("segment.analysis.duration")
                .description("Profile analysis processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentCreationTimer = Timer.builder("segment.creation.duration")
                .description("Profile creation time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentUpdateTimer = Timer.builder("segment.update.duration")
                .description("Profile update time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.customerProcessingTimer = Timer.builder("customer.processing.duration")
                .description("User processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementProfileationTotal() {
        segmentationTotalCounter.increment();
    }

    public void incrementProfileationSuccess() {
        segmentationSuccessCounter.increment();
    }

    public void incrementProfileationFailure() {
        segmentationFailureCounter.increment();
    }

    public void incrementProfileCreated() {
        segmentCreatedCounter.increment();
    }

    public void incrementProfileUpdated() {
        segmentUpdatedCounter.increment();
    }

    public void incrementProfileDeleted() {
        segmentDeletedCounter.increment();
    }

    public void incrementUserAnalyzed() {
        customerAnalyzedCounter.increment();
    }

    public void incrementUserAddedToProfile() {
        customerAddedToProfileCounter.increment();
    }

    public void incrementUserRemovedFromProfile() {
        customerRemovedFromProfileCounter.increment();
    }

    // Timer methods
    public void recordProfileationTime(long durationMs) {
        segmentationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startProfileationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProfileationTimer(Timer.Sample sample) {
        sample.stop(segmentationTimer);
    }

    public Timer.Sample startProfileAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProfileAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentAnalysisTimer);
    }

    public Timer.Sample startProfileCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProfileCreationTimer(Timer.Sample sample) {
        sample.stop(segmentCreationTimer);
    }

    public Timer.Sample startProfileUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProfileUpdateTimer(Timer.Sample sample) {
        sample.stop(segmentUpdateTimer);
    }

    public Timer.Sample startUserProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopUserProcessingTimer(Timer.Sample sample) {
        sample.stop(customerProcessingTimer);
    }

    // SLO compliance methods
    public double getProfileationLatencyP95() {
        return segmentationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getProfileationLatencyP99() {
        return segmentationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getProfileAnalysisLatencyP95() {
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
