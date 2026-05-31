package com.gogidix.aiservices.aicontentgenerationservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ContentGenerationMetricsGenTest {
    private ContentGenerationMetrics metrics;

    @BeforeEach
    void setup() { metrics = new ContentGenerationMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementGenerationTotal() { metrics.incrementGenerationTotal(); }

    @Test
    void incrementGenerationSuccess() { metrics.incrementGenerationSuccess(); }

    @Test
    void incrementGenerationFailure() { metrics.incrementGenerationFailure(); }

    @Test
    void incrementContentGenerated() { metrics.incrementContentGenerated(); }

    @Test
    void incrementContentOptimized() { metrics.incrementContentOptimized(); }

    @Test
    void incrementTemplateUsed() { metrics.incrementTemplateUsed(); }

    @Test
    void recordGenerationTime() { metrics.recordGenerationTime(100); }

    @Test
    void stopGenerationTimer() { var s = metrics.startGenerationTimer(); metrics.stopGenerationTimer(s); }

    @Test
    void stopAiGenerationTimer() { var s = metrics.startAiGenerationTimer(); metrics.stopAiGenerationTimer(s); }

    @Test
    void stopOptimizationTimer() { var s = metrics.startOptimizationTimer(); metrics.stopOptimizationTimer(s); }

    @Test
    void getGenerationLatencyP95() { assertThat(metrics.getGenerationLatencyP95()).isNotNull(); }

    @Test
    void getAiGenerationLatencyP95() { assertThat(metrics.getAiGenerationLatencyP95()).isNotNull(); }

    @Test
    void getGenerationLatencyP99() { assertThat(metrics.getGenerationLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
