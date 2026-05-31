package com.gogidix.aiservices.researchintelligenceservice.infrastructure.governance;
import org.junit.jupiter.api.*; import com.gogidix.aiservices.researchintelligenceservice.infrastructure.metrics.ResearchIntelligenceMetrics; import io.micrometer.core.instrument.*; import io.micrometer.core.instrument.simple.SimpleMeterRegistry; import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
class ComplianceReportGeneratorTest {
    private ResearchIntelligenceMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator generator;
    private SimpleMeterRegistry registry;
    @BeforeEach void setup() {
        registry = new SimpleMeterRegistry();
        metrics = new ResearchIntelligenceMetrics(registry);
        thresholdValidator = new ThresholdValidator(metrics);
        generator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }
    @Test void generateReport() {
        var report = generator.generateReport();
        assertThat(report).isNotNull();
        assertThat(report.getReportId()).startsWith("GOV-");
        assertThat(report.getGeneratedAt()).isNotNull();
        assertThat(report.getSloCompliance()).isNotNull();
        assertThat(report.getPerformanceMetrics()).isNotNull();
        assertThat(report.getGovernanceStatus()).isNotNull();
    }
    @Test void reportToMap() {
        var report = generator.generateReport();
        var map = report.toMap();
        assertThat(map).containsKey("reportId");
        assertThat(map).containsKey("generatedAt");
        assertThat(map).containsKey("sloCompliance");
        assertThat(map).containsKey("performanceMetrics");
        assertThat(map).containsKey("governanceStatus");
    }
    @Test void sloComplianceSection() {
        var report = generator.generateReport();
        var slo = report.getSloCompliance();
        assertThat(slo.getP95LatencyMs()).isNotNull();
        assertThat(slo.isOverallCompliant()).isTrue();
    }
    @Test void performanceMetricsSection() {
        var report = generator.generateReport();
        var perf = report.getPerformanceMetrics();
        assertThat(perf.getTotalRequests()).isEqualTo(0L);
        assertThat(perf.getThroughputRps()).isEqualTo(0.0);
    }
    @Test void governanceStatusCompliant() {
        var report = generator.generateReport();
        var status = report.getGovernanceStatus();
        assertThat(status.getStatus()).isEqualTo("COMPLIANT");
        assertThat(status.getSeverity()).isEqualTo("INFO");
    }
    @Test void governanceReportSetters() {
        var r = new ComplianceReportGenerator.GovernanceReport();
        r.setReportId("id"); r.setGeneratedAt(java.time.Instant.now());
        assertThat(r.getReportId()).isEqualTo("id");
    }
    @Test void sloSectionSetters() {
        var s = new ComplianceReportGenerator.SloComplianceSection();
        s.setP95LatencyMs(100.0); s.setP99LatencyMs(500.0);
        s.setErrorRate(0.5); s.setAvailability(99.9);
        s.setP95LatencyCompliant(true); s.setP99LatencyCompliant(true);
        s.setErrorRateCompliant(true); s.setAvailabilityCompliant(true);
        s.setOverallCompliant(true); s.setP95LatencyVariance(10.0);
        assertThat(s.getP95LatencyMs()).isEqualTo(100.0);
        assertThat(s.getP95LatencyVariance()).isEqualTo(10.0);
    }
    @Test void perfSectionSetters() {
        var p = new ComplianceReportGenerator.PerformanceMetricsSection();
        p.setTotalRequests(10L); p.setSuccessfulRequests(8L); p.setFailedRequests(2L);
        p.setProjectsCreated(5L); p.setProjectsUpdated(3L); p.setProjectsStarted(2L);
        p.setProjectsCompleted(1L); p.setProjectsCancelled(0L);
        p.setFindingsCreated(7L); p.setPublicationsCreated(3L);
        p.setDatasetsCreated(4L); p.setThroughputRps(1.5);
        assertThat(p.getTotalRequests()).isEqualTo(10L);
        assertThat(p.getThroughputRps()).isEqualTo(1.5);
    }
    @Test void governanceStatusSetters() {
        var s = new ComplianceReportGenerator.GovernanceStatus();
        s.setStatus("WARN"); s.setHealthStatus("DEGRADED"); s.setSeverity("WARN"); s.setMessage("msg");
        assertThat(s.getStatus()).isEqualTo("WARN");
    }
}
