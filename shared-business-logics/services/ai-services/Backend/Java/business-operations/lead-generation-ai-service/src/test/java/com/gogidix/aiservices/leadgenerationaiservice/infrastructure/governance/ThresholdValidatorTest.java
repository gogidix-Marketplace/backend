package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.governance;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.leadgenerationaiservice.infrastructure.metrics.LeadGenerationMetrics; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class ThresholdValidatorTest {
    private ThresholdValidator validator;
    @BeforeEach void setup() { var reg = new SimpleMeterRegistry(); var metrics = new LeadGenerationMetrics(reg); validator = new ThresholdValidator(metrics); }
    @Test void validateAll() { var r = validator.validateAllThresholds(); assertThat(r).isNotNull(); assertThat(r.getCheckCount()).isGreaterThan(0); }
    @Test void isDegraded() { assertThat(validator.isDegraded()).isFalse(); }
    @Test void healthStatus() { assertThat(validator.getHealthStatus()).isNotNull(); }
    @Test void validateThreshold() { var v = validator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY); assertThat(v).isNotNull(); }
    @Test void validateThresholdP99() { var v = validator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY); assertThat(v).isNotNull(); }
    @Test void enumTypes() { assertThat(ThresholdValidator.ThresholdType.values()).isNotEmpty(); assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
}
