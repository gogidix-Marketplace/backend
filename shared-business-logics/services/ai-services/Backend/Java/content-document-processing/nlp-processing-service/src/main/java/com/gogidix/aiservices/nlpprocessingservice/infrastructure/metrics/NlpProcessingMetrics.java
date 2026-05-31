package com.gogidix.aiservices.nlpprocessingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for NLP Processing Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds for Content Processing:
 * - P95 Latency: < 300ms (NLP)
 * - P99 Latency: < 500ms (NLP)
 * - Error Rate: < 2%
 */
@Component
public class NlpProcessingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter processingTotalCounter;
    private final Counter processingSuccessCounter;
    private final Counter processingFailureCounter;
    private final Counter textsProcessedCounter;
    private final Counter tokensProcessedCounter;
    private final Counter validationFailedCounter;

    // Timers
    private final Timer processingTimer;
    private final Timer analysisTimer;
    private final Timer databaseSaveTimer;

    public NlpProcessingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.processingTotalCounter = Counter.builder("nlp.processing.total")
                .description("Total number of NLP processing requests")
                .tag("service", "nlp-processing")
                .register(meterRegistry);

        this.processingSuccessCounter = Counter.builder("nlp.processing.success")
                .description("Number of successful NLP processing requests")
                .tag("service", "nlp-processing")
                .register(meterRegistry);

        this.processingFailureCounter = Counter.builder("nlp.processing.failure")
                .description("Number of failed NLP processing requests")
                .tag("service", "nlp-processing")
                .register(meterRegistry);

        this.textsProcessedCounter = Counter.builder("nlp.processing.texts")
                .description("Number of texts processed")
                .tag("service", "nlp-processing")
                .register(meterRegistry);

        this.tokensProcessedCounter = Counter.builder("nlp.processing.tokens")
                .description("Number of tokens processed")
                .tag("service", "nlp-processing")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("nlp.processing.validation.failed")
                .description("Number of validation failures")
                .tag("service", "nlp-processing")
                .register(meterRegistry);

        // Initialize timers
        this.processingTimer = Timer.builder("nlp.processing.duration")
                .description("NLP processing time")
                .tag("service", "nlp-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.analysisTimer = Timer.builder("nlp.analysis.duration")
                .description("NLP analysis time")
                .tag("service", "nlp-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("nlp.database.save.duration")
                .description("Database save operation time")
                .tag("service", "nlp-processing")
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

    public void incrementTextsProcessed() {
        textsProcessedCounter.increment();
    }

    public void incrementTokensProcessed(int count) {
        tokensProcessedCounter.increment(count);
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

    public Timer.Sample startAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisTimer(Timer.Sample sample) {
        sample.stop(analysisTimer);
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

    public double getAnalysisLatencyP95() {
        return analysisTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
