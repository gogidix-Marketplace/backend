package com.gogidix.aiservices.aisummarizationservice.infrastructure.metrics;
import org.junit.jupiter.api.*; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class SummarizationMetricsTest {
    private SummarizationMetrics m;
    @BeforeEach void setup() { m = new SummarizationMetrics(new SimpleMeterRegistry()); }
    @Test void counters() { m.incrementSummarizationTotal(); m.incrementSummarizationSuccess(); m.incrementSummarizationFailure(); m.incrementDocumentsSummarized(); m.incrementTokensProcessed(100); m.incrementValidationFailed(); }
    @Test void timers() { m.recordSummarizationTime(100); m.recordDatabaseSaveTime(50); }
    @Test void samples() { var s1 = m.startSummarizationTimer(); m.stopSummarizationTimer(s1); var s2 = m.startNlpProcessingTimer(); m.stopNlpProcessingTimer(s2); var s3 = m.startDatabaseSaveTimer(); m.stopDatabaseSaveTimer(s3); }
    @Test void slo() { assertThat(m.getSummarizationLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getSummarizationLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getNlpProcessingLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
