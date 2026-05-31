package com.gogidix.aiservices.aisummarizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Summarization Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds:
 * - P95 Latency: < 500ms
 * - P99 Latency: < 1000ms
 * - Error Rate: < 1%
 */
@Component
public class SummarizationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter summarizationTotalCounter;
    private final Counter summarizationSuccessCounter;
    private final Counter summarizationFailureCounter;
    private final Counter documentsSummarizedCounter;
    private final Counter tokensProcessedCounter;
    private final Counter validationFailedCounter;

    // Timers
    private final Timer summarizationTimer;
    private final Timer nlpProcessingTimer;
    private final Timer databaseSaveTimer;

    public SummarizationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.summarization prefix
        this.summarizationTotalCounter = Counter.builder("ai.summarization.total")
                .description("Total number of summarization requests")
                .tag("service", "ai-summarization")
                .register(meterRegistry);

        this.summarizationSuccessCounter = Counter.builder("ai.summarization.success")
                .description("Number of successful summarization requests")
                .tag("service", "ai-summarization")
                .register(meterRegistry);

        this.summarizationFailureCounter = Counter.builder("ai.summarization.failure")
                .description("Number of failed summarization requests")
                .tag("service", "ai-summarization")
                .register(meterRegistry);

        this.documentsSummarizedCounter = Counter.builder("ai.summarization.documents")
                .description("Number of documents summarized")
                .tag("service", "ai-summarization")
                .register(meterRegistry);

        this.tokensProcessedCounter = Counter.builder("ai.summarization.tokens")
                .description("Number of tokens processed")
                .tag("service", "ai-summarization")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("ai.summarization.validation.failed")
                .description("Number of validation failures")
                .tag("service", "ai-summarization")
                .register(meterRegistry);

        // Initialize timers
        this.summarizationTimer = Timer.builder("ai.summarization.duration")
                .description("Summarization processing time")
                .tag("service", "ai-summarization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.nlpProcessingTimer = Timer.builder("ai.summarization.nlp.duration")
                .description("NLP processing time")
                .tag("service", "ai-summarization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("ai.summarization.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-summarization")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementSummarizationTotal() {
        summarizationTotalCounter.increment();
    }

    public void incrementSummarizationSuccess() {
        summarizationSuccessCounter.increment();
    }

    public void incrementSummarizationFailure() {
        summarizationFailureCounter.increment();
    }

    public void incrementDocumentsSummarized() {
        documentsSummarizedCounter.increment();
    }

    public void incrementTokensProcessed(int count) {
        tokensProcessedCounter.increment(count);
    }

    public void incrementValidationFailed() {
        validationFailedCounter.increment();
    }

    // Timer methods
    public void recordSummarizationTime(long durationMs) {
        summarizationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startSummarizationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopSummarizationTimer(Timer.Sample sample) {
        sample.stop(summarizationTimer);
    }

    public Timer.Sample startNlpProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopNlpProcessingTimer(Timer.Sample sample) {
        sample.stop(nlpProcessingTimer);
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
    public double getSummarizationLatencyP95() {
        return summarizationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getSummarizationLatencyP99() {
        return summarizationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getNlpProcessingLatencyP95() {
        return nlpProcessingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) summarizationTotalCounter.count();
        long failures = (long) summarizationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
