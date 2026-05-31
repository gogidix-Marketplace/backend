package com.gogidix.aiservices.aitranslationservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class TranslationMetricsGenTest {
    private TranslationMetrics metrics;

    @BeforeEach
    void setup() { metrics = new TranslationMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementTranslationTotal() { metrics.incrementTranslationTotal(); }

    @Test
    void incrementTranslationSuccess() { metrics.incrementTranslationSuccess(); }

    @Test
    void incrementTranslationFailure() { metrics.incrementTranslationFailure(); }

    @Test
    void incrementLanguageDetections() { metrics.incrementLanguageDetections(); }

    @Test
    void incrementBatchTranslations() { metrics.incrementBatchTranslations(); }

    @Test
    void recordTranslationTime() { metrics.recordTranslationTime(100); }

    @Test
    void stopTranslationTimer() { var s = metrics.startTranslationTimer(); metrics.stopTranslationTimer(s); }

    @Test
    void recordLanguageDetectionTime() { metrics.recordLanguageDetectionTime(50); }

    @Test
    void stopLanguageDetectionTimer() { var s = metrics.startLanguageDetectionTimer(); metrics.stopLanguageDetectionTimer(s); }

    @Test
    void sloMethods() {
        assertThat(metrics.getTranslationLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getTranslationLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getLanguageDetectionLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }
}
