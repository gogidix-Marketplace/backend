package com.gogidix.aiservices.intelligenceanalysisservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for IntelligenceReport Analysis Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class IntelligenceAnalysisationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter segmentationTotalCounter;
    private final Counter segmentationSuccessCounter;
    private final Counter segmentationFailureCounter;
    private final Counter segmentCreatedCounter;
    private final Counter segmentUpdatedCounter;
    private final Counter segmentDeletedCounter;
    private final Counter customerAnalyzedCounter;
    private final Counter customerAddedToAnalysisCounter;
    private final Counter customerRemovedFromAnalysisCounter;

    // Timers
    private final Timer segmentationTimer;
    private final Timer segmentAnalysisTimer;
    private final Timer segmentCreationTimer;
    private final Timer segmentUpdateTimer;
    private final Timer customerProcessingTimer;

    public IntelligenceAnalysisationMetrics(MeterRegistry meterRegistry) {
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

        this.customerAddedToAnalysisCounter = Counter.builder("customer.added.to.segment")
                .description("Number of customers added to segments")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        this.customerRemovedFromAnalysisCounter = Counter.builder("customer.removed.from.segment")
                .description("Number of customers removed from segments")
                .tag("service", "ai-customer-segmentation")
                .register(meterRegistry);

        // Initialize timers
        this.segmentationTimer = Timer.builder("segmentation.duration")
                .description("IntelligenceReport segmentation processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.segmentAnalysisTimer = Timer.builder("segment.analysis.duration")
                .description("Analysis analysis processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentCreationTimer = Timer.builder("segment.creation.duration")
                .description("Analysis creation time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.segmentUpdateTimer = Timer.builder("segment.update.duration")
                .description("Analysis update time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.customerProcessingTimer = Timer.builder("customer.processing.duration")
                .description("IntelligenceReport processing time")
                .tag("service", "ai-customer-segmentation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementAnalysisTotal() {
        segmentationTotalCounter.increment();
    }

    public void incrementAnalysisSuccess() {
        segmentationSuccessCounter.increment();
    }

    public void incrementAnalysisFailure() {
        segmentationFailureCounter.increment();
    }

    public void incrementAnalysisCreated() {
        segmentCreatedCounter.increment();
    }

    public void incrementAnalysisUpdated() {
        segmentUpdatedCounter.increment();
    }

    public void incrementAnalysisDeleted() {
        segmentDeletedCounter.increment();
    }

    public void incrementIntelligenceReportAnalyzed() {
        customerAnalyzedCounter.increment();
    }

    public void incrementIntelligenceReportAddedToAnalysis() {
        customerAddedToAnalysisCounter.increment();
    }

    public void incrementIntelligenceReportRemovedFromAnalysis() {
        customerRemovedFromAnalysisCounter.increment();
    }

    // Timer methods
    public void recordAnalysisTime(long durationMs) {
        segmentationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentationTimer);
    }

    public Timer.Sample startAnalysisAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisAnalysisTimer(Timer.Sample sample) {
        sample.stop(segmentAnalysisTimer);
    }

    public Timer.Sample startAnalysisCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisCreationTimer(Timer.Sample sample) {
        sample.stop(segmentCreationTimer);
    }

    public Timer.Sample startAnalysisUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisUpdateTimer(Timer.Sample sample) {
        sample.stop(segmentUpdateTimer);
    }

    public Timer.Sample startIntelligenceReportProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopIntelligenceReportProcessingTimer(Timer.Sample sample) {
        sample.stop(customerProcessingTimer);
    }

    // SLO compliance methods
    public double getAnalysisLatencyP95() {
        return segmentationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAnalysisLatencyP99() {
        return segmentationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getAnalysisAnalysisLatencyP95() {
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
