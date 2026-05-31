package com.gogidix.aiservices.aipersonalizationservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class PersonalizationMetricsGenTest {
    private PersonalizationMetrics metrics;

    @BeforeEach
    void setup() { metrics = new PersonalizationMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementPersonalizationTotal() { metrics.incrementPersonalizationTotal(); }

    @Test
    void incrementPersonalizationSuccess() { metrics.incrementPersonalizationSuccess(); }

    @Test
    void incrementPersonalizationFailure() { metrics.incrementPersonalizationFailure(); }

    @Test
    void incrementRecommendationsGenerated() { metrics.incrementRecommendationsGenerated(); }

    @Test
    void incrementUserProfilesMatched() { metrics.incrementUserProfilesMatched(); }

    @Test
    void recordPersonalizationTime() { metrics.recordPersonalizationTime(100); }

    @Test
    void stopPersonalizationTimer() { var s = metrics.startPersonalizationTimer(); metrics.stopPersonalizationTimer(s); }

    @Test
    void stopProfileMatchingTimer() { var s = metrics.startProfileMatchingTimer(); metrics.stopProfileMatchingTimer(s); }

    @Test
    void stopRecommendationTimer() { var s = metrics.startRecommendationTimer(); metrics.stopRecommendationTimer(s); }

    @Test
    void getPersonalizationLatencyP95() { assertThat(metrics.getPersonalizationLatencyP95()).isNotNull(); }

    @Test
    void getProfileMatchingLatencyP95() { assertThat(metrics.getProfileMatchingLatencyP95()).isNotNull(); }

    @Test
    void getPersonalizationLatencyP99() { assertThat(metrics.getPersonalizationLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
