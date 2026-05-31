package com.gogidix.aiservices.airecommendationservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class RecommendationMetricsGenTest {
    private RecommendationMetrics metrics;

    @BeforeEach
    void setup() { metrics = new RecommendationMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementRecommendationTotal() { metrics.incrementRecommendationTotal(); }

    @Test
    void incrementRecommendationSuccess() { metrics.incrementRecommendationSuccess(); }

    @Test
    void incrementRecommendationFailure() { metrics.incrementRecommendationFailure(); }

    @Test
    void incrementSimilarItems() { metrics.incrementSimilarItems(); }

    @Test
    void incrementTrendingItems() { metrics.incrementTrendingItems(); }

    @Test
    void incrementInteractionTracked() { metrics.incrementInteractionTracked(); }

    @Test
    void recordRecommendationTime() { metrics.recordRecommendationTime(100); }

    @Test
    void recordDatabaseSaveTime() { metrics.recordDatabaseSaveTime(100); }

    @Test
    void stopRecommendationTimer() { var s = metrics.startRecommendationTimer(); metrics.stopRecommendationTimer(s); }

    @Test
    void stopMlPredictionTimer() { var s = metrics.startMlPredictionTimer(); metrics.stopMlPredictionTimer(s); }

    @Test
    void stopDatabaseSaveTimer() { var s = metrics.startDatabaseSaveTimer(); metrics.stopDatabaseSaveTimer(s); }

    @Test
    void getRecommendationLatencyP95() { assertThat(metrics.getRecommendationLatencyP95()).isNotNull(); }

    @Test
    void getMlPredictionLatencyP95() { assertThat(metrics.getMlPredictionLatencyP95()).isNotNull(); }

    @Test
    void getRecommendationLatencyP99() { assertThat(metrics.getRecommendationLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
