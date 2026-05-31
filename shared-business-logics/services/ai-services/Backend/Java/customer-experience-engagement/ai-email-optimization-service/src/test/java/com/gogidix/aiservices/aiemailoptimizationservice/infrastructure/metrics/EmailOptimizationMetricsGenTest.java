package com.gogidix.aiservices.aiemailoptimizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class EmailOptimizationMetricsGenTest {
    private EmailOptimizationMetrics metrics;

    @BeforeEach
    void setup() { metrics = new EmailOptimizationMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementOptimizationTotal() { metrics.incrementOptimizationTotal(); }

    @Test
    void incrementOptimizationSuccess() { metrics.incrementOptimizationSuccess(); }

    @Test
    void incrementOptimizationFailure() { metrics.incrementOptimizationFailure(); }

    @Test
    void incrementEmailsOptimized() { metrics.incrementEmailsOptimized(); }

    @Test
    void recordOptimizationTime() { metrics.recordOptimizationTime(100); }

    @Test
    void stopOptimizationTimer() { var s = metrics.startOptimizationTimer(); metrics.stopOptimizationTimer(s); }

    @Test
    void getOptimizationLatencyP95() { assertThat(metrics.getOptimizationLatencyP95()).isNotNull(); }

    @Test
    void getAiAnalysisLatencyP95() { assertThat(metrics.getAiAnalysisLatencyP95()).isNotNull(); }

    @Test
    void getOptimizationLatencyP99() { assertThat(metrics.getOptimizationLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
