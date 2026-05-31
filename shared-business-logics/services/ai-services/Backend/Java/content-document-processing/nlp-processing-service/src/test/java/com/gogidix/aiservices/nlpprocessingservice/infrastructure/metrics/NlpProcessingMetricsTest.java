package com.gogidix.aiservices.nlpprocessingservice.infrastructure.metrics;
import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class NlpProcessingMetricsTest {
    private NlpProcessingMetrics m;
    @BeforeEach void setup() { m = new NlpProcessingMetrics(new SimpleMeterRegistry()); }
    @Test void counters() { m.incrementProcessingTotal(); m.incrementProcessingSuccess(); m.incrementProcessingFailure(); m.incrementTextsProcessed(); m.incrementTokensProcessed(100); m.incrementValidationFailed(); }
    @Test void timers() { m.recordProcessingTime(100); m.recordDatabaseSaveTime(50); }
    @Test void timerSamples() { var s1 = m.startProcessingTimer(); m.stopProcessingTimer(s1); var s2 = m.startAnalysisTimer(); m.stopAnalysisTimer(s2); var s3 = m.startDatabaseSaveTimer(); m.stopDatabaseSaveTimer(s3); }
    @Test void slo() { assertThat(m.getProcessingLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getProcessingLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getAnalysisLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
