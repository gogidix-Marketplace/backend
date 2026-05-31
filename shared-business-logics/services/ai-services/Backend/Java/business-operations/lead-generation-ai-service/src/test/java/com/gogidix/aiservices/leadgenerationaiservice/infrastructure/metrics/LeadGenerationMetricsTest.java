package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.metrics;
import org.junit.jupiter.api.*; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class LeadGenerationMetricsTest {
    private LeadGenerationMetrics metrics;
    @BeforeEach void setup() { metrics = new LeadGenerationMetrics(new SimpleMeterRegistry()); }
    @Test void counters() { metrics.incrementLeadScoreTotal(); metrics.incrementLeadScoreSuccess(); metrics.incrementLeadScoreFailure(); metrics.incrementLeadCreated(); metrics.incrementLeadConverted(); metrics.incrementHighQualityLead(); }
    @Test void timers() { metrics.recordLeadScoringTime(100); metrics.recordLeadEnrichmentTime(50); }
    @Test void sloMethods() { assertThat(metrics.getLeadScoringLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(metrics.getLeadScoringLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(metrics.getAiPredictionLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(metrics.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void meterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }
    @Test void timerSamples() { var s1 = metrics.startLeadScoringTimer(); metrics.stopLeadScoringTimer(s1); var s2 = metrics.startAiPredictionTimer(); metrics.stopAiPredictionTimer(s2); var s3 = metrics.startLeadEnrichmentTimer(); metrics.stopLeadEnrichmentTimer(s3); }
}
