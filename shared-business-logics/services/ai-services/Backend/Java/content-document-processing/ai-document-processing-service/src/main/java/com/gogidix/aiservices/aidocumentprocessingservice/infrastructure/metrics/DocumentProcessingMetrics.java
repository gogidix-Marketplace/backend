package com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Document Processing Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds for Content Processing:
 * - P95 Latency: < 1000ms (document)
 * - P99 Latency: < 2000ms (document)
 * - Error Rate: < 2%
 */
@Component
public class DocumentProcessingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter processingTotalCounter;
    private final Counter processingSuccessCounter;
    private final Counter processingFailureCounter;
    private final Counter documentsProcessedCounter;
    private final Counter pagesProcessedCounter;
    private final Counter validationFailedCounter;

    // Timers
    private final Timer processingTimer;
    private final Timer ocrProcessingTimer;
    private final Timer databaseSaveTimer;

    public DocumentProcessingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.processingTotalCounter = Counter.builder("document.processing.total")
                .description("Total number of document processing requests")
                .tag("service", "ai-document-processing")
                .register(meterRegistry);

        this.processingSuccessCounter = Counter.builder("document.processing.success")
                .description("Number of successful document processing requests")
                .tag("service", "ai-document-processing")
                .register(meterRegistry);

        this.processingFailureCounter = Counter.builder("document.processing.failure")
                .description("Number of failed document processing requests")
                .tag("service", "ai-document-processing")
                .register(meterRegistry);

        this.documentsProcessedCounter = Counter.builder("document.processing.documents")
                .description("Number of documents processed")
                .tag("service", "ai-document-processing")
                .register(meterRegistry);

        this.pagesProcessedCounter = Counter.builder("document.processing.pages")
                .description("Number of pages processed")
                .tag("service", "ai-document-processing")
                .register(meterRegistry);

        this.validationFailedCounter = Counter.builder("document.processing.validation.failed")
                .description("Number of validation failures")
                .tag("service", "ai-document-processing")
                .register(meterRegistry);

        // Initialize timers
        this.processingTimer = Timer.builder("document.processing.duration")
                .description("Document processing time")
                .tag("service", "ai-document-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.ocrProcessingTimer = Timer.builder("document.processing.ocr.duration")
                .description("OCR processing time")
                .tag("service", "ai-document-processing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("document.processing.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-document-processing")
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

    public void incrementDocumentsProcessed() {
        documentsProcessedCounter.increment();
    }

    public void incrementPagesProcessed(int count) {
        pagesProcessedCounter.increment(count);
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

    public Timer.Sample startOcrProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOcrProcessingTimer(Timer.Sample sample) {
        sample.stop(ocrProcessingTimer);
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

    public double getOcrProcessingLatencyP95() {
        return ocrProcessingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
