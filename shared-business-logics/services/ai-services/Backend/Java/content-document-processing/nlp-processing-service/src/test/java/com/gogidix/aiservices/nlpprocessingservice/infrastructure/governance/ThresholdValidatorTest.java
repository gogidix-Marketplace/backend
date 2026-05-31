package com.gogidix.aiservices.nlpprocessingservice.infrastructure.governance;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.nlpprocessingservice.infrastructure.metrics.NlpProcessingMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class ThresholdValidatorTest {
    private ThresholdValidator v;
    @BeforeEach void setup() { v = new ThresholdValidator(new NlpProcessingMetrics(new SimpleMeterRegistry())); }
    @Test void validateAll() { assertThat(v.validateAllThresholds()).isNotNull(); }
    @Test void isDegraded() { assertThat(v.isDegraded()).isFalse(); }
    @Test void health() { assertThat(v.getHealthStatus()).isNotNull(); }
    @Test void validateThreshold() { assertThat(v.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY)).isNotNull(); }
    @Test void enums() { assertThat(ThresholdValidator.ThresholdType.values()).isNotEmpty(); assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
}
