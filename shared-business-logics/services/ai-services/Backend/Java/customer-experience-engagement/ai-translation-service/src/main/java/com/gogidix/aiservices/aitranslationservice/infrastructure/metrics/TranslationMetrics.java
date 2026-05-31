package com.gogidix.aiservices.aitranslationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TranslationMetrics {

    private final MeterRegistry meterRegistry;
    private final Counter translationTotalCounter;
    private final Counter translationSuccessCounter;
    private final Counter translationFailureCounter;
    private final Counter languageDetectionsCounter;
    private final Counter batchTranslationsCounter;
    private final Timer translationTimer;
    private final Timer languageDetectionTimer;

    public TranslationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.translationTotalCounter = Counter.builder("ai.translation.total")
                .description("Total translation requests")
                .tag("service", "ai-translation")
                .register(meterRegistry);

        this.translationSuccessCounter = Counter.builder("ai.translation.success")
                .description("Successful translations")
                .tag("service", "ai-translation")
                .register(meterRegistry);

        this.translationFailureCounter = Counter.builder("ai.translation.failure")
                .description("Failed translations")
                .tag("service", "ai-translation")
                .register(meterRegistry);

        this.languageDetectionsCounter = Counter.builder("ai.translation.detections")
                .description("Language detections performed")
                .tag("service", "ai-translation")
                .register(meterRegistry);

        this.batchTranslationsCounter = Counter.builder("ai.translation.batch")
                .description("Batch translations performed")
                .tag("service", "ai-translation")
                .register(meterRegistry);

        this.translationTimer = Timer.builder("ai.translation.duration")
                .description("Translation processing time")
                .tag("service", "ai-translation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.languageDetectionTimer = Timer.builder("ai.translation.detection.duration")
                .description("Language detection time")
                .tag("service", "ai-translation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);
    }

    public void incrementTranslationTotal() { translationTotalCounter.increment(); }
    public void incrementTranslationSuccess() { translationSuccessCounter.increment(); }
    public void incrementTranslationFailure() { translationFailureCounter.increment(); }
    public void incrementLanguageDetections() { languageDetectionsCounter.increment(); }
    public void incrementBatchTranslations() { batchTranslationsCounter.increment(); }

    public void recordTranslationTime(long durationMs) { translationTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public Timer.Sample startTranslationTimer() { return Timer.start(meterRegistry); }
    public void stopTranslationTimer(Timer.Sample sample) { sample.stop(translationTimer); }

    public void recordLanguageDetectionTime(long durationMs) { languageDetectionTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public Timer.Sample startLanguageDetectionTimer() { return Timer.start(meterRegistry); }
    public void stopLanguageDetectionTimer(Timer.Sample sample) { sample.stop(languageDetectionTimer); }

    public double getTranslationLatencyP95() { return translationTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getTranslationLatencyP99() { return translationTimer.percentile(0.99, TimeUnit.MILLISECONDS); }
    public double getLanguageDetectionLatencyP95() { return languageDetectionTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getErrorRate() {
        long total = (long) translationTotalCounter.count();
        long failures = (long) translationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
