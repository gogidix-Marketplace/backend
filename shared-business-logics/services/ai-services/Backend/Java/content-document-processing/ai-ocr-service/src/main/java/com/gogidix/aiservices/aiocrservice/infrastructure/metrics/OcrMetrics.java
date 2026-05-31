package com.gogidix.aiservices.aiocrservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for OCR Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds:
 * - P95 Latency: < 500ms
 * - P99 Latency: < 1000ms
 * - Error Rate: < 1%
 */
@Component
public class OcrMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter ocrTotalCounter;
    private final Counter ocrSuccessCounter;
    private final Counter ocrFailureCounter;
    private final Counter pagesProcessedCounter;
    private final Counter charactersRecognizedCounter;
    private final Counter preprocessingFailedCounter;

    // Timers
    private final Timer ocrTimer;
    private final Timer preprocessingTimer;
    private final Timer recognitionTimer;
    private final Timer databaseSaveTimer;

    public OcrMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.ocr prefix
        this.ocrTotalCounter = Counter.builder("ai.ocr.total")
                .description("Total number of OCR requests")
                .tag("service", "ai-ocr")
                .register(meterRegistry);

        this.ocrSuccessCounter = Counter.builder("ai.ocr.success")
                .description("Number of successful OCR requests")
                .tag("service", "ai-ocr")
                .register(meterRegistry);

        this.ocrFailureCounter = Counter.builder("ai.ocr.failure")
                .description("Number of failed OCR requests")
                .tag("service", "ai-ocr")
                .register(meterRegistry);

        this.pagesProcessedCounter = Counter.builder("ai.ocr.pages")
                .description("Number of pages processed")
                .tag("service", "ai-ocr")
                .register(meterRegistry);

        this.charactersRecognizedCounter = Counter.builder("ai.ocr.characters")
                .description("Number of characters recognized")
                .tag("service", "ai-ocr")
                .register(meterRegistry);

        this.preprocessingFailedCounter = Counter.builder("ai.ocr.preprocessing.failed")
                .description("Number of preprocessing failures")
                .tag("service", "ai-ocr")
                .register(meterRegistry);

        // Initialize timers
        this.ocrTimer = Timer.builder("ai.ocr.duration")
                .description("OCR processing time")
                .tag("service", "ai-ocr")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.preprocessingTimer = Timer.builder("ai.ocr.preprocessing.duration")
                .description("Image preprocessing time")
                .tag("service", "ai-ocr")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.recognitionTimer = Timer.builder("ai.ocr.recognition.duration")
                .description("Text recognition time")
                .tag("service", "ai-ocr")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("ai.ocr.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-ocr")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementOcrTotal() {
        ocrTotalCounter.increment();
    }

    public void incrementOcrSuccess() {
        ocrSuccessCounter.increment();
    }

    public void incrementOcrFailure() {
        ocrFailureCounter.increment();
    }

    public void incrementPagesProcessed() {
        pagesProcessedCounter.increment();
    }

    public void incrementCharactersRecognized(int count) {
        charactersRecognizedCounter.increment(count);
    }

    public void incrementPreprocessingFailed() {
        preprocessingFailedCounter.increment();
    }

    // Timer methods
    public void recordOcrTime(long durationMs) {
        ocrTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startOcrTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOcrTimer(Timer.Sample sample) {
        sample.stop(ocrTimer);
    }

    public Timer.Sample startPreprocessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopPreprocessingTimer(Timer.Sample sample) {
        sample.stop(preprocessingTimer);
    }

    public Timer.Sample startRecognitionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopRecognitionTimer(Timer.Sample sample) {
        sample.stop(recognitionTimer);
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
    public double getOcrLatencyP95() {
        return ocrTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getOcrLatencyP99() {
        return ocrTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getRecognitionLatencyP95() {
        return recognitionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) ocrTotalCounter.count();
        long failures = (long) ocrFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
