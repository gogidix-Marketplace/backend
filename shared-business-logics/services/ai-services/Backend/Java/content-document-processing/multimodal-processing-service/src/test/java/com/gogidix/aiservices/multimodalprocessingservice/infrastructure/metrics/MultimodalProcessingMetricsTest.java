package com.gogidix.aiservices.multimodalprocessingservice.infrastructure.metrics;
import org.junit.jupiter.api.*; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class MultimodalProcessingMetricsTest {
    private MultimodalProcessingMetrics m;
    @BeforeEach void setup() { m = new MultimodalProcessingMetrics(new SimpleMeterRegistry()); }
    @Test void counters() { m.incrementProcessingTotal(); m.incrementProcessingSuccess(); m.incrementProcessingFailure(); m.incrementMultimodalProcessed(); m.incrementEmbeddingGenerated(); m.incrementValidationFailed(); }
    @Test void timers() { m.recordProcessingTime(100); m.recordDatabaseSaveTime(50); }
    @Test void samples() { var s1 = m.startProcessingTimer(); m.stopProcessingTimer(s1); var s2 = m.startEmbeddingTimer(); m.stopEmbeddingTimer(s2); var s3 = m.startDatabaseSaveTimer(); m.stopDatabaseSaveTimer(s3); }
    @Test void slo() { assertThat(m.getProcessingLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getProcessingLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getEmbeddingLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
