package com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.governance;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.supplychainoptimizationservice.infrastructure.metrics.SupplyChainMetrics; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class ThresholdValidatorTest {
    private ThresholdValidator v;
    @BeforeEach void setup() { v = new ThresholdValidator(new SupplyChainMetrics(new SimpleMeterRegistry())); }
    @Test void validateAll() { var r = v.validateAllThresholds(); assertThat(r).isNotNull(); assertThat(r.getCheckCount()).isGreaterThan(0); }
    @Test void isDegraded() { assertThat(v.isDegraded()).isFalse(); }
    @Test void health() { assertThat(v.getHealthStatus()).isNotNull(); }
    @Test void validateThreshold() { var r = v.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY); assertThat(r).isNotNull(); }
    @Test void enums() { assertThat(ThresholdValidator.ThresholdType.values()).isNotEmpty(); assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
}
