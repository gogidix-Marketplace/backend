package com.gogidix.aiservices.voicerecognitionservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class VoiceRecognitionMetricsGenTest {
    private VoiceRecognitionMetrics m;
    @BeforeEach void setup() { m = new VoiceRecognitionMetrics(new SimpleMeterRegistry()); }
    @Test void incTotal() { m.incrementRecognitionTotal(); }
    @Test void incSuccess() { m.incrementRecognitionSuccess(); }
    @Test void incFailure() { m.incrementRecognitionFailure(); }
    @Test void recordTime() { m.recordRecognitionTime(100); }
    @Test void timer() { var s = m.startRecognitionTimer(); m.stopRecognitionTimer(s); }
    @Test void slo() { assertThat(m.getRecognitionLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getRecognitionLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
