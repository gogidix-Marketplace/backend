package com.gogidix.aiservices.aichatbotservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class ChatbotMetricsGenTest {
    private ChatbotMetrics metrics;

    @BeforeEach
    void setup() { metrics = new ChatbotMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementChatbotTotal() { metrics.incrementChatbotTotal(); }

    @Test
    void incrementChatbotSuccess() { metrics.incrementChatbotSuccess(); }

    @Test
    void incrementChatbotFailure() { metrics.incrementChatbotFailure(); }

    @Test
    void incrementConversationsHandled() { metrics.incrementConversationsHandled(); }

    @Test
    void recordResponseTime() { metrics.recordResponseTime(100); }

    @Test
    void stopResponseTimer() { var s = metrics.startResponseTimer(); metrics.stopResponseTimer(s); }

    @Test
    void getResponseLatencyP95() { assertThat(metrics.getResponseLatencyP95()).isNotNull(); }

    @Test
    void getNluProcessingLatencyP95() { assertThat(metrics.getNluProcessingLatencyP95()).isNotNull(); }

    @Test
    void getResponseLatencyP99() { assertThat(metrics.getResponseLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
