package com.gogidix.aiservices.aivoiceservice.infrastructure.governance;

import com.gogidix.aiservices.aivoiceservice.infrastructure.metrics.VoiceMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

class GovernanceGenTest {
    private VoiceMetrics m; private ThresholdValidator v; private ComplianceReportGenerator g;
    @BeforeEach void setup() { m = new VoiceMetrics(new SimpleMeterRegistry()); v = new ThresholdValidator(m); g = new ComplianceReportGenerator(m, v); }
    @Test void validateAll() { var r = v.validateAllThresholds(); assertThat(r.isCompliant()).isTrue(); assertThat(r.getCheckCount()).isEqualTo(4); }
    @Test void degraded() { assertThat(v.isDegraded()).isFalse(); }
    @Test void health() { assertThat(v.getHealthStatus()).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY); }
    @Test void report() { assertThat(g.generateReport().getReportId()).startsWith("GOV-"); }
    @Test void map() { assertThat(g.generateReport().toMap()).containsKey("reportId"); }
    @Test void variance() { var cr = new ThresholdValidator.ComplianceReport(); cr.addCheck("t", 600, 500, false, "ms"); assertThat(cr.getChecks().get(0).getVariance()).isEqualTo(100.0); }
    @Test void enums() { assertThat(ThresholdValidator.ThresholdType.values()).hasSize(4); assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
}
