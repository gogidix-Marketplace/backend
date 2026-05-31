package com.gogidix.aiservices.aidocumentextractionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Document Extraction Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds:
 * - P95 Latency: < 500ms
 * - P99 Latency: < 1000ms
 * - Error Rate: < 1%
 */
@Component
public class DocumentExtractionMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter extractionTotalCounter;
    private final Counter extractionSuccessCounter;
    private final Counter extractionFailureCounter;
    private final Counter fieldsExtractedCounter;
    private final Counter documentsProcessedCounter;
    private final Counter validationFailedCounter;

    // Timers
    private final Timer extractionTimer;
    private final Timer fieldExtractionTimer;
    private final Timer databaseSaveTimer;

    public DocumentExtractionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.document.extraction prefix
        this.extractionTotalCounter = Counter.builder("ai.document.extraction.total")
                .description("Total number of document extraction requests")
                .tag("service", "ai-document-extraction")
                .register(meterRegistry);

        this.extractionSuccessCounter = Counter.builder("ai.document.extraction.success")
                .description("Number of successful document extraction requests")
                .tag("service", "ai-document-extraction")
                .register(meterRegistry);

        this.extractionFailureCounter = Counter.builder("ai.document.extraction.failure")
                .description("Number of failed document extraction requests")
                .tag("service", "ai-document-extraction")
                .register(meterRegistry);

        this.fieldsExtractedCounter = Counter.builder("ai.document.extraction.fields")
                .description("Number of fields extracted from documents")
                .tag("service", "ai-document-extraction")
                .register(meterRegistry);

        this.documentsProcessedCounter = Counter.builder("ai.document.extraction.documents")
                .description("Number of documents processed")
                .tag("service", "ai-document-extraction")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("ai.document.extraction.validation.failed")
                .description("Number of validation failures")
                .tag("service", "ai-document-extraction")
                .register(meterRegistry);

        // Initialize timers
        this.extractionTimer = Timer.builder("ai.document.extraction.duration")
                .description("Document extraction processing time")
                .tag("service", "ai-document-extraction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.fieldExtractionTimer = Timer.builder("ai.document.extraction.field.duration")
                .description("Field extraction time")
                .tag("service", "ai-document-extraction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("ai.document.extraction.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-document-extraction")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementExtractionTotal() {
        extractionTotalCounter.increment();
    }

    public void incrementExtractionSuccess() {
        extractionSuccessCounter.increment();
    }

    public void incrementExtractionFailure() {
        extractionFailureCounter.increment();
    }

    public void incrementFieldsExtracted(int count) {
        fieldsExtractedCounter.increment(count);
    }

    public void incrementDocumentsProcessed() {
        documentsProcessedCounter.increment();
    }

    public void incrementValidationFailed() {
        validationFailedCounter.increment();
    }

    // Timer methods
    public void recordExtractionTime(long durationMs) {
        extractionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startExtractionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopExtractionTimer(Timer.Sample sample) {
        sample.stop(extractionTimer);
    }

    public Timer.Sample startFieldExtractionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopFieldExtractionTimer(Timer.Sample sample) {
        sample.stop(fieldExtractionTimer);
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
    public double getExtractionLatencyP95() {
        return extractionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getExtractionLatencyP99() {
        return extractionTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getFieldExtractionLatencyP95() {
        return fieldExtractionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) extractionTotalCounter.count();
        long failures = (long) extractionFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
