package com.gogidix.aiservices.aireporting.infrastructure.governance;

import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aireporting.infrastructure.metrics.ReportingMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class GovernanceTest {
    private ThresholdValidator v;
    private ComplianceReportGenerator g;

    @BeforeEach
    void setup() {
        var m = new ReportingMetrics(new SimpleMeterRegistry());
        v = new ThresholdValidator(m);
        g = new ComplianceReportGenerator(m, v);
    }

    @Test
    void validateAll() { assertThat(v.validateAllThresholds()).isNotNull(); }
    @Test
    void degraded() { assertThat(v.isDegraded()).isFalse(); }
    @Test
    void health() { assertThat(v.getHealthStatus()).isNotNull(); }
    @Test
    void threshold() { assertThat(v.validateThreshold(ThresholdValidator.ThresholdType.P95_GENERATION_LATENCY)).isNotNull(); }
    @Test
    void enums() { assertThat(ThresholdValidator.ThresholdType.values()).isNotEmpty(); assertThat(ThresholdValidator.HealthStatus.values()).hasSize(3); }
    @Test
    void report() { assertThat(g.generateReport()).isNotNull(); }
    @Test
    void reportToMap() { assertThat(g.generateReport().toMap()).containsKey("reportId"); }
    @Test
    void reportSetters() { var r = new ComplianceReportGenerator.GovernanceReport(); r.setReportId("id"); assertThat(r.getReportId()).isEqualTo("id"); }
    @Test
    void sloSetters() { var s = new ComplianceReportGenerator.SloComplianceSection(); s.setP95LatencyMs(100); s.setOverallCompliant(true); assertThat(s.isOverallCompliant()).isTrue(); }
    @Test
    void perfSetters() { var p = new ComplianceReportGenerator.PerformanceMetricsSection(); p.setTotalRequests(10L); assertThat(p.getTotalRequests()).isEqualTo(10L); }
    @Test
    void statusSetters() { var s = new ComplianceReportGenerator.GovernanceStatus(); s.setStatus("OK"); assertThat(s.getStatus()).isEqualTo("OK"); }
}
