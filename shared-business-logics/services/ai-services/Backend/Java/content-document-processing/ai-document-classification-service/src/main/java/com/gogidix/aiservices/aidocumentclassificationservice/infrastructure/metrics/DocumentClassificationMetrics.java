package com.gogidix.aiservices.aidocumentclassificationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Document Classification Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds:
 * - P95 Latency: < 500ms
 * - P99 Latency: < 1000ms
 * - Error Rate: < 1%
 */
@Component
public class DocumentClassificationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter classificationTotalCounter;
    private final Counter classificationSuccessCounter;
    private final Counter classificationFailureCounter;
    private final Counter documentsClassifiedCounter;
    private final Counter highConfidenceCounter;
    private final Counter lowConfidenceCounter;

    // Timers
    private final Timer classificationTimer;
    private final Timer mlPredictionTimer;
    private final Timer databaseSaveTimer;

    public DocumentClassificationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.document.classification prefix
        this.classificationTotalCounter = Counter.builder("ai.document.classification.total")
                .description("Total number of document classification requests")
                .tag("service", "ai-document-classification")
                .register(meterRegistry);

        this.classificationSuccessCounter = Counter.builder("ai.document.classification.success")
                .description("Number of successful document classification requests")
                .tag("service", "ai-document-classification")
                .register(meterRegistry);

        this.classificationFailureCounter = Counter.builder("ai.document.classification.failure")
                .description("Number of failed document classification requests")
                .tag("service", "ai-document-classification")
                .register(meterRegistry);

        this.documentsClassifiedCounter = Counter.builder("ai.document.classification.documents")
                .description("Number of documents classified")
                .tag("service", "ai-document-classification")
                .register(meterRegistry);

        this.highConfidenceCounter = Counter.builder("ai.document.classification.high.confidence")
                .description("Number of high confidence classifications")
                .tag("service", "ai-document-classification")
                .register(meterRegistry);

        this.lowConfidenceCounter = Counter.builder("ai.document.classification.low.confidence")
                .description("Number of low confidence classifications")
                .tag("service", "ai-document-classification")
                .register(meterRegistry);

        // Initialize timers
        this.classificationTimer = Timer.builder("ai.document.classification.duration")
                .description("Document classification processing time")
                .tag("service", "ai-document-classification")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.mlPredictionTimer = Timer.builder("ai.document.classification.ml.prediction.duration")
                .description("ML model prediction time for document classification")
                .tag("service", "ai-document-classification")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("ai.document.classification.database.save.duration")
                .description("Database save operation time for classification results")
                .tag("service", "ai-document-classification")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementClassificationTotal() {
        classificationTotalCounter.increment();
    }

    public void incrementClassificationSuccess() {
        classificationSuccessCounter.increment();
    }

    public void incrementClassificationFailure() {
        classificationFailureCounter.increment();
    }

    public void incrementDocumentsClassified() {
        documentsClassifiedCounter.increment();
    }

    public void incrementHighConfidence() {
        highConfidenceCounter.increment();
    }

    public void incrementLowConfidence() {
        lowConfidenceCounter.increment();
    }

    // Timer methods
    public void recordClassificationTime(long durationMs) {
        classificationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startClassificationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopClassificationTimer(Timer.Sample sample) {
        sample.stop(classificationTimer);
    }

    public Timer.Sample startMlPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMlPredictionTimer(Timer.Sample sample) {
        sample.stop(mlPredictionTimer);
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
    public double getClassificationLatencyP95() {
        return classificationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getClassificationLatencyP99() {
        return classificationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getMlPredictionLatencyP95() {
        return mlPredictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) classificationTotalCounter.count();
        long failures = (long) classificationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
