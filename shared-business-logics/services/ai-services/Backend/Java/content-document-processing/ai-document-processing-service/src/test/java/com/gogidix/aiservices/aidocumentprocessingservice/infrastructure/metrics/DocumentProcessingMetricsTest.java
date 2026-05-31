package com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.metrics;
import org.junit.jupiter.api.*; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class DocumentProcessingMetricsTest {
    private DocumentProcessingMetrics m;
    @BeforeEach void setup() { m = new DocumentProcessingMetrics(new SimpleMeterRegistry()); }
    @Test void counters() { m.incrementProcessingTotal(); m.incrementProcessingSuccess(); m.incrementProcessingFailure(); m.incrementDocumentsProcessed(); m.incrementPagesProcessed(10); m.incrementValidationFailed(); }
    @Test void timers() { m.recordProcessingTime(100); m.recordDatabaseSaveTime(50); }
    @Test void samples() { var s1 = m.startProcessingTimer(); m.stopProcessingTimer(s1); var s2 = m.startOcrProcessingTimer(); m.stopOcrProcessingTimer(s2); var s3 = m.startDatabaseSaveTimer(); m.stopDatabaseSaveTimer(s3); }
    @Test void slo() { assertThat(m.getProcessingLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getProcessingLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getOcrProcessingLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
