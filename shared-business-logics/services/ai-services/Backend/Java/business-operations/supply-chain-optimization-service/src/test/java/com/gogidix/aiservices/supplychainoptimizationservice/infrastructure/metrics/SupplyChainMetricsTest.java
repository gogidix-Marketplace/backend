package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.metrics;
import org.junit.jupiter.api.*; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class SupplyChainMetricsTest {
    private SupplyChainMetrics m;
    @BeforeEach void setup() { m = new SupplyChainMetrics(new SimpleMeterRegistry()); }
    @Test void counters() { m.incrementOptimizationTotal(); m.incrementOptimizationSuccess(); m.incrementOptimizationFailure(); m.incrementRecommendationsGenerated(); m.recordCostSavings(100.0); }
    @Test void timers() { m.recordOptimizationTime(100); m.recordRecommendationTime(50); }
    @Test void slo() { assertThat(m.getOptimizationLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getOptimizationLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getAiPredictionLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
    @Test void timerSamples() { var s1 = m.startOptimizationTimer(); m.stopOptimizationTimer(s1); var s2 = m.startAiPredictionTimer(); m.stopAiPredictionTimer(s2); var s3 = m.startRecommendationTimer(); m.stopRecommendationTimer(s3); }
}
