package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.governance;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.leadgenerationaiservice.infrastructure.metrics.LeadGenerationMetrics; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
class ComplianceReportGeneratorTest {
    private ComplianceReportGenerator gen;
    @BeforeEach void setup() { var reg = new SimpleMeterRegistry(); var m = new LeadGenerationMetrics(reg); var v = new ThresholdValidator(m); gen = new ComplianceReportGenerator(m, v); }
    @Test void generateReport() { var r = gen.generateReport(); assertThat(r).isNotNull(); assertThat(r.getReportId()).startsWith("GOV-"); }
    @Test void toMap() { assertThat(gen.generateReport().toMap()).containsKey("reportId"); }
    @Test void reportSetters() { var r = new ComplianceReportGenerator.GovernanceReport(); r.setReportId("id"); assertThat(r.getReportId()).isEqualTo("id"); }
    @Test void sloSetters() { var s = new ComplianceReportGenerator.SloComplianceSection(); s.setP95LatencyMs(100); s.setOverallCompliant(true); assertThat(s.isOverallCompliant()).isTrue(); }
    @Test void perfSetters() { var p = new ComplianceReportGenerator.PerformanceMetricsSection(); p.setTotalRequests(10L); assertThat(p.getTotalRequests()).isEqualTo(10L); }
    @Test void statusSetters() { var s = new ComplianceReportGenerator.GovernanceStatus(); s.setStatus("OK"); assertThat(s.getStatus()).isEqualTo("OK"); }
}
