package com.gogidix.aiservices.aibusinessautomationservice.infrastructure.governance;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aibusinessautomationservice.infrastructure.metrics.BusinessAutomationMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class ThresholdValidatorTest {
    private ThresholdValidator v;
    @BeforeEach
    void setup() { v = new ThresholdValidator(new BusinessAutomationMetrics(new SimpleMeterRegistry())); }
    @Test void validateAll() { assertThat(v.validateAllThresholds()).isNotNull(); }
    @Test void isDegraded() { assertThat(v.isDegraded()).isFalse(); }
    @Test void health() { assertThat(v.getHealthStatus()).isNotNull(); }
    @Test void validateThreshold() { assertThat(v.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY)).isNotNull(); }
    @Test void enums() { assertThat(ThresholdValidator.ThresholdType.values()).isNotEmpty(); assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
}
