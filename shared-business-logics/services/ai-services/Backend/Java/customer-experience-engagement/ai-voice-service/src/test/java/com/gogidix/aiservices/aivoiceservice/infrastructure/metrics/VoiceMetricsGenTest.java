package com.gogidix.aiservices.aivoiceservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class VoiceMetricsGenTest {
    private VoiceMetrics m;
    @BeforeEach void setup() { m = new VoiceMetrics(new SimpleMeterRegistry()); }
    @Test void incTotal() { m.incrementSynthesisTotal(); }
    @Test void incSuccess() { m.incrementSynthesisSuccess(); }
    @Test void incFailure() { m.incrementSynthesisFailure(); }
    @Test void recordTime() { m.recordSynthesisTime(100); }
    @Test void timer() { var s = m.startSynthesisTimer(); m.stopSynthesisTimer(s); }
    @Test void slo() { assertThat(m.getSynthesisLatencyP95()).isGreaterThanOrEqualTo(0); assertThat(m.getSynthesisLatencyP99()).isGreaterThanOrEqualTo(0); assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0); }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
