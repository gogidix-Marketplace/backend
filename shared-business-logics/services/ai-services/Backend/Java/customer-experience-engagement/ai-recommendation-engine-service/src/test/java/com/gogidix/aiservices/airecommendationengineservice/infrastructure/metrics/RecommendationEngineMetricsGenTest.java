package com.gogidix.aiservices.airecommendationengineservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class RecommendationEngineMetricsGenTest {
    private RecommendationEngineMetrics metrics;

    @BeforeEach
    void setup() { metrics = new RecommendationEngineMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementRecommendationTotal() { metrics.incrementRecommendationTotal(); }

    @Test
    void incrementRecommendationSuccess() { metrics.incrementRecommendationSuccess(); }

    @Test
    void incrementRecommendationFailure() { metrics.incrementRecommendationFailure(); }

    @Test
    void incrementRecommendationsGenerated() { metrics.incrementRecommendationsGenerated(); }

    @Test
    void recordRecommendationTime() { metrics.recordRecommendationTime(100); }

    @Test
    void stopRecommendationTimer() { var s = metrics.startRecommendationTimer(); metrics.stopRecommendationTimer(s); }

    @Test
    void getRecommendationLatencyP95() { assertThat(metrics.getRecommendationLatencyP95()).isNotNull(); }

    @Test
    void getMlInferenceLatencyP95() { assertThat(metrics.getMlInferenceLatencyP95()).isNotNull(); }

    @Test
    void getRecommendationLatencyP99() { assertThat(metrics.getRecommendationLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
