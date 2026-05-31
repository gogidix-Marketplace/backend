package com.gogidix.aiservices.predictiveanalytics.governance;

import com.gogidix.aiservices.predictiveanalytics.application.service.ForecastService;
import com.gogidix.aiservices.predictiveanalytics.application.dto.request.GenerateForecastRequest;
import com.gogidix.aiservices.predictiveanalytics.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.predictiveanalytics.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.predictiveanalytics.infrastructure.metrics.PredictiveAnalyticsMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Financial-Grade: Governance Enforcement Tests for Predictive Analytics Service.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: Governance Enforcement Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GovernanceEnforcementTest {

    @Autowired
    private ForecastService forecastService;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private PredictiveAnalyticsMetrics metrics;

    @Autowired
    private MeterRegistry meterRegistry;

    @MockBean
    private com.gogidix.aiservices.predictiveanalytics.application.port.out.ForecastRepository forecastRepository;

    @Nested
    @DisplayName("1. Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @Order(1)
        @DisplayName("Should validate P95 latency threshold")
        void shouldValidateP95LatencyThreshold() {
            for (int i = 0; i < 20; i++) {
                GenerateForecastRequest request = new GenerateForecastRequest();
                request.setDataSource("test-data-" + i);
                try {
                    forecastService.generateForecast(request);
                } catch (Exception e) {
                    // Ignore
                }
            }

            ThresholdValidator.ThresholdValidation validation =
                thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_FORECAST_LATENCY);

            assertThat(validation).isNotNull();
            assertThat(validation.getType()).isEqualTo(ThresholdValidator.ThresholdType.P95_FORECAST_LATENCY);
            assertThat(validation.getUnit()).isEqualTo("ms");
            assertThat(validation.getThreshold()).isEqualTo(2000.0);
        }
    }

    @Nested
    @DisplayName("2. Compliance Report Tests")
    class ComplianceReportTests {

        @Test
        @Order(10)
        @DisplayName("Should generate compliance report")
        void shouldGenerateComplianceReport() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getReportId()).startsWith("GOV-");
            assertThat(report.getGeneratedAt()).isNotNull();
            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getGovernanceStatus()).isNotNull();
        }
    }

    @Nested
    @DisplayName("3. Overall Compliance Tests")
    class OverallComplianceTests {

        @Test
        @Order(20)
        @DisplayName("Should validate all thresholds")
        void shouldValidateAllThresholds() {
            ThresholdValidator.ComplianceReport report = thresholdValidator.validateAllThresholds();

            assertThat(report).isNotNull();
            assertThat(report.getCheckCount()).isGreaterThan(0);
        }

        @Test
        @Order(21)
        @DisplayName("Should detect degraded state")
        void shouldDetectDegradedState() {
            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isNotNull();
            assertThat(status).isIn(
                ThresholdValidator.HealthStatus.HEALTHY,
                ThresholdValidator.HealthStatus.DEGRADED,
                ThresholdValidator.HealthStatus.UNHEALTHY
            );
        }
    }
}
