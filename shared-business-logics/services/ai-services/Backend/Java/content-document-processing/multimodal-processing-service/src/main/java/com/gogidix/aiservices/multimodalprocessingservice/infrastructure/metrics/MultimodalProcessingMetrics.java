package com.gogidix.aiservices.multimodalprocessingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Multimodal Processing Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds for Content Processing:
 * - P95 Latency: < 2000ms (multimodal)
 * - P99 Latency: < 5000ms (multimodal)
 * - Error Rate: < 2%
 */
@Component
public class MultimodalProcessingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter processingTotalCounter;
    private final Counter processingSuccessCounter;
    private final Counter processingFailureCounter;
    private final Counter multimodalProcessedCounter;
    private final Counter embeddingGeneratedCounter;
    private final Counter validationFailedCounter;

    // Timers
    private final Timer processingTimer;
    private final Timer embeddingTimer;
    private final Timer databaseSaveTimer;

    public MultimodalProcessingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.processingTotalCounter = Counter.builder("multimodal.processing.total")
                .description("Total number of multimodal processing requests")
                .tag("service", "multimodal-processing")
                .register(meterRegistry);

        this.processingSuccessCounter = Counter.builder("multimodal.processing.success")
                .description("Number of successful multimodal processing requests")
                .tag("service", "multimodal-processing")
                .register(meterRegistry);

        this.processingFailureCounter = Counter.builder("multimodal.processing.failure")
                .description("Number of failed multimodal processing requests")
                .tag("service", "multimodal-processing")
                .register(meterRegistry);

        this.multimodalProcessedCounter = Counter.builder("multimodal.processing.items")
                .description("Number of multimodal items processed")
                .tag("service", "multimodal-processing")
                .register(meterRegistry);

        this.embeddingGeneratedCounter = Counter.builder("multimodal.embedding.generated")
                .description("Number of embeddings generated")
                .tag("service", "multimodal-processing")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("multimodal.processing.validation.failed")
                .description("Number of validation failures")
                .tag("service", "multimodal-processing")
                .register(meterRegistry);

        // Initialize timers
        this.processingTimer = Timer.builder("multimodal.processing.duration")
                .description("Multimodal processing time")
                .tag("service", "multimodal-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.embeddingTimer = Timer.builder("multimodal.embedding.duration")
                .description("Embedding generation time")
                .tag("service", "multimodal-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("multimodal.database.save.duration")
                .description("Database save operation time")
                .tag("service", "multimodal-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementProcessingTotal() {
        processingTotalCounter.increment();
    }

    public void incrementProcessingSuccess() {
        processingSuccessCounter.increment();
    }

    public void incrementProcessingFailure() {
        processingFailureCounter.increment();
    }

    public void incrementMultimodalProcessed() {
        multimodalProcessedCounter.increment();
    }

    public void incrementEmbeddingGenerated() {
        embeddingGeneratedCounter.increment();
    }

    public void incrementValidationFailed() {
        validationFailedCounter.increment();
    }

    // Timer methods
    public void recordProcessingTime(long durationMs) {
        processingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProcessingTimer(Timer.Sample sample) {
        sample.stop(processingTimer);
    }

    public Timer.Sample startEmbeddingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopEmbeddingTimer(Timer.Sample sample) {
        sample.stop(embeddingTimer);
    }

    public void recordDatabaseSaveTime(long durationMs) {
        databaseSaveTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDatabaseSaveTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDatabaseSaveTimer(Timer.Sample sample) {
        sample.stop(databaseSaveTimer);
    }

    // SLO compliance methods
    public double getProcessingLatencyP95() {
        return processingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getProcessingLatencyP99() {
        return processingTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getEmbeddingLatencyP95() {
        return embeddingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) processingTotalCounter.count();
        long failures = (long) processingFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
