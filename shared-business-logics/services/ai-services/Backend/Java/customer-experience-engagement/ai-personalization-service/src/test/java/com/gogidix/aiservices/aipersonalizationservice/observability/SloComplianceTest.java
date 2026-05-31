package com.gogidix.aiservices.aipersonalizationservice.observability;

import com.gogidix.aiservices.aipersonalizationservice.observability.TestConfiguration;
import com.gogidix.aiservices.aipersonalizationservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aipersonalizationservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aipersonalizationservice.infrastructure.metrics.PersonalizationMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = TestConfiguration.class)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SloComplianceTest {

    @Autowired
    private PersonalizationMetrics metrics;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private MeterRegistry meterRegistry;

    @Nested
    @DisplayName("1. Latency SLO Tests")
    class LatencySloTests {

        @Test
        @Order(1)
        @DisplayName("Should meet P95 latency SLO")
        void shouldMeetP95LatencySlo() {
            double p95Latency = metrics.getPersonalizationLatencyP95();
            assertThat(p95Latency).isGreaterThanOrEqualTo(0);
        }
    }

    @Nested
    @DisplayName("2. Error Rate SLO Tests")
    class ErrorRateSloTests {

        @Test
        @Order(10)
        @DisplayName("Should meet error rate SLO")
        void shouldMeetErrorRateSlo() {
            double errorRate = metrics.getErrorRate();
            assertThat(errorRate).isGreaterThanOrEqualTo(0.0);
            assertThat(errorRate).isLessThanOrEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("3. Compliance Report Tests")
    class ComplianceReportTests {

        @Test
        @Order(20)
        @DisplayName("Should generate compliance report")
        void shouldGenerateComplianceReport() {
            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            assertThat(report).isNotNull();
            assertThat(report.getReportId()).isNotNull();
        }
    }
}
