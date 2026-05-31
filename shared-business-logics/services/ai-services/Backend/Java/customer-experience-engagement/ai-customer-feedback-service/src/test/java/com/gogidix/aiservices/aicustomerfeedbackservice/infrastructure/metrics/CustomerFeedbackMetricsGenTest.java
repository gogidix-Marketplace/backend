package com.gogidix.aiservices.aicustomerfeedbackservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class CustomerFeedbackMetricsGenTest {
    private CustomerFeedbackMetrics metrics;

    @BeforeEach
    void setup() { metrics = new CustomerFeedbackMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementFeedbackTotal() { metrics.incrementFeedbackTotal(); }

    @Test
    void incrementFeedbackSuccess() { metrics.incrementFeedbackSuccess(); }

    @Test
    void incrementFeedbackFailure() { metrics.incrementFeedbackFailure(); }

    @Test
    void incrementFeedbackProcessed() { metrics.incrementFeedbackProcessed(); }

    @Test
    void recordFeedbackTime() { metrics.recordFeedbackTime(100); }

    @Test
    void stopFeedbackTimer() { var s = metrics.startFeedbackTimer(); metrics.stopFeedbackTimer(s); }

    @Test
    void getFeedbackLatencyP95() { assertThat(metrics.getFeedbackLatencyP95()).isNotNull(); }

    @Test
    void getNlpProcessingLatencyP95() { assertThat(metrics.getNlpProcessingLatencyP95()).isNotNull(); }

    @Test
    void getFeedbackLatencyP99() { assertThat(metrics.getFeedbackLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
