package com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.metrics;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class SentimentAnalysisMetricsGenTest {
    private SentimentAnalysisMetrics metrics;

    @BeforeEach
    void setup() { metrics = new SentimentAnalysisMetrics(new SimpleMeterRegistry()); }

    @Test
    void incrementAnalysisTotal() { metrics.incrementAnalysisTotal(); }

    @Test
    void incrementAnalysisSuccess() { metrics.incrementAnalysisSuccess(); }

    @Test
    void incrementAnalysisFailure() { metrics.incrementAnalysisFailure(); }

    @Test
    void incrementSentimentsAnalyzed() { metrics.incrementSentimentsAnalyzed(); }

    @Test
    void recordAnalysisTime() { metrics.recordAnalysisTime(100); }

    @Test
    void stopAnalysisTimer() { var s = metrics.startAnalysisTimer(); metrics.stopAnalysisTimer(s); }

    @Test
    void getAnalysisLatencyP95() { assertThat(metrics.getAnalysisLatencyP95()).isNotNull(); }

    @Test
    void getAiInferenceLatencyP95() { assertThat(metrics.getAiInferenceLatencyP95()).isNotNull(); }

    @Test
    void getAnalysisLatencyP99() { assertThat(metrics.getAnalysisLatencyP99()).isNotNull(); }

    @Test
    void getErrorRate() { assertThat(metrics.getErrorRate()).isNotNull(); }

    @Test
    void getMeterRegistry() { assertThat(metrics.getMeterRegistry()).isNotNull(); }

}
