package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.governance;

import com.gogidix.aiservices.aifeaturestoreservice.infrastructure.metrics.FeatureStoreMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComplianceReportGeneratorTest {

    @Mock
    private FeatureStoreMetrics metrics;

    @Mock
    private ThresholdValidator thresholdValidator;

    @Mock
    private MeterRegistry meterRegistry;

    private ComplianceReportGenerator reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
        when(metrics.getMeterRegistry()).thenReturn(meterRegistry);
    }

    @Test
    void generateReport_WhenCalled_ReturnsValidReport() {
        // Arrange
        when(metrics.getStoreLatencyP95()).thenReturn(300.0);
        when(metrics.getStoreLatencyP99()).thenReturn(600.0);
        when(metrics.getErrorRate()).thenReturn(0.005);
        when(thresholdValidator.getHealthStatus()).thenReturn(ThresholdValidator.HealthStatus.HEALTHY);

        // Act
        var report = reportGenerator.generateReport();

        // Assert
        assertThat(report).isNotNull();
        assertThat(report.getReportId()).isNotNull();
        assertThat(report.getGeneratedAt()).isNotNull();
        assertThat(report.getSloCompliance()).isNotNull();
        assertThat(report.getPerformanceMetrics()).isNotNull();
        assertThat(report.getGovernanceStatus()).isNotNull();
    }

    @Test
    void generateReport_WhenSloCompliant_ReturnsCompliantStatus() {
        // Arrange
        when(metrics.getStoreLatencyP95()).thenReturn(400.0);
        when(metrics.getStoreLatencyP99()).thenReturn(800.0);
        when(metrics.getErrorRate()).thenReturn(0.008);
        when(thresholdValidator.getHealthStatus()).thenReturn(ThresholdValidator.HealthStatus.HEALTHY);

        // Act
        var report = reportGenerator.generateReport();

        // Assert
        assertThat(report.getSloCompliance().isOverallCompliant()).isTrue();
        assertThat(report.getGovernanceStatus().getStatus()).isEqualTo("COMPLIANT");
    }

    @Test
    void generateReport_WhenSloNotCompliant_ReturnsWarningStatus() {
        // Arrange
        when(metrics.getStoreLatencyP95()).thenReturn(600.0);
        when(metrics.getStoreLatencyP99()).thenReturn(1200.0);
        when(metrics.getErrorRate()).thenReturn(0.02);
        when(thresholdValidator.getHealthStatus()).thenReturn(ThresholdValidator.HealthStatus.DEGRADED);

        // Act
        var report = reportGenerator.generateReport();

        // Assert
        assertThat(report.getSloCompliance().isOverallCompliant()).isFalse();
        assertThat(report.getGovernanceStatus().getStatus()).isEqualTo("WARNING");
    }

    @Test
    void generateReport_WhenUnhealthy_ReturnsNonCompliantStatus() {
        // Arrange
        when(metrics.getStoreLatencyP95()).thenReturn(800.0);
        when(metrics.getStoreLatencyP99()).thenReturn(1500.0);
        when(metrics.getErrorRate()).thenReturn(0.05);
        when(thresholdValidator.getHealthStatus()).thenReturn(ThresholdValidator.HealthStatus.UNHEALTHY);

        // Act
        var report = reportGenerator.generateReport();

        // Assert
        assertThat(report.getGovernanceStatus().getStatus()).isEqualTo("NON-COMPLIANT");
        assertThat(report.getGovernanceStatus().getSeverity()).isEqualTo("CRITICAL");
    }

    @Test
    void toMap_WhenCalled_ReturnsValidMap() {
        // Arrange
        when(metrics.getStoreLatencyP95()).thenReturn(300.0);
        when(metrics.getStoreLatencyP99()).thenReturn(600.0);
        when(metrics.getErrorRate()).thenReturn(0.005);
        when(thresholdValidator.getHealthStatus()).thenReturn(ThresholdValidator.HealthStatus.HEALTHY);

        // Act
        var report = reportGenerator.generateReport();
        var map = report.toMap();

        // Assert
        assertThat(map).isNotNull();
        assertThat(map.containsKey("reportId")).isTrue();
        assertThat(map.containsKey("generatedAt")).isTrue();
        assertThat(map.containsKey("sloCompliance")).isTrue();
        assertThat(map.containsKey("performanceMetrics")).isTrue();
        assertThat(map.containsKey("governanceStatus")).isTrue();
    }
}
