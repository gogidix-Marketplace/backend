package com.gogidix.aiservices.aibusinessautomationservice.infrastructure.governance;
import org.junit.jupiter.api.*;
import com.gogidix.aiservices.aibusinessautomationservice.infrastructure.metrics.BusinessAutomationMetrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class ComplianceReportGeneratorTest {
    private ComplianceReportGenerator g;
    @BeforeEach
    void setup() {
        var m = new BusinessAutomationMetrics(new SimpleMeterRegistry());
        g = new ComplianceReportGenerator(m, new ThresholdValidator(m));
    }
    @Test void generate() { var r = g.generateReport(); assertThat(r).isNotNull(); assertThat(r.getReportId()).startsWith("GOV-"); }
    @Test void toMap() { assertThat(g.generateReport().toMap()).containsKey("reportId"); }
    @Test void reportSetters() { var r = new ComplianceReportGenerator.GovernanceReport(); r.setReportId("id"); assertThat(r.getReportId()).isEqualTo("id"); }
    @Test void sloSetters() { var s = new ComplianceReportGenerator.SloComplianceSection(); s.setP95LatencyMs(100); s.setOverallCompliant(true); assertThat(s.isOverallCompliant()).isTrue(); }
    @Test void perfSetters() { var p = new ComplianceReportGenerator.PerformanceMetricsSection(); p.setTotalRequests(10L); assertThat(p.getTotalRequests()).isEqualTo(10L); }
    @Test void statusSetters() { var s = new ComplianceReportGenerator.GovernanceStatus(); s.setStatus("OK"); assertThat(s.getStatus()).isEqualTo("OK"); }
}
