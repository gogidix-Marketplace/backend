package com.gogidix.aiservices.aivoiceassistantservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class VoiceAssistantMetricsGenTest {
    private VoiceAssistantMetrics metrics;

    @BeforeEach
    void setup() { metrics = new VoiceAssistantMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementAssistantTotal() { metrics.incrementAssistantTotal(); }

    @Test
    void incrementAssistantSuccess() { metrics.incrementAssistantSuccess(); }

    @Test
    void incrementAssistantFailure() { metrics.incrementAssistantFailure(); }

    @Test
    void incrementVoiceCommandsProcessed() { metrics.incrementVoiceCommandsProcessed(); }

    @Test
    void recordAssistantTime() { metrics.recordAssistantTime(100); }

    @Test
    void stopAssistantTimer() { var s = metrics.startAssistantTimer(); metrics.stopAssistantTimer(s); }

    @Test
    void getAssistantLatencyP95() { assertThat(metrics.getAssistantLatencyP95()).isNotNull(); }

    @Test
    void getSpeechToTextLatencyP95() { assertThat(metrics.getSpeechToTextLatencyP95()).isNotNull(); }

    @Test
    void getAssistantLatencyP99() { assertThat(metrics.getAssistantLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
