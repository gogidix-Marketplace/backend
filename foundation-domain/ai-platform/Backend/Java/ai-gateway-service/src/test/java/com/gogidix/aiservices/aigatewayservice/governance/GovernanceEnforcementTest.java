package com.gogidix.aiservices.aigatewayservice.governance;

import com.gogidix.aiservices.aigatewayservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aigatewayservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aigatewayservice.infrastructure.metrics.GatewayMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Governance enforcement tests for financial-grade certification.
 * Tests that service properly enforces operational thresholds and SLO compliance.
 */
@DisplayName("Governance Enforcement Tests")
class GovernanceEnforcementTest {

    private GatewayMetrics metrics;
    private ThresholdValidator thresholdValidator;
    private ComplianceReportGenerator reportGenerator;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        metrics = new GatewayMetrics(meterRegistry);
        thresholdValidator = new ThresholdValidator(metrics);
        reportGenerator = new ComplianceReportGenerator(metrics, thresholdValidator);
    }

    @Nested
    @DisplayName("Overall Compliance Tests")
    class OverallComplianceTests {

        @Test
        @DisplayName("Should enforce P95 latency threshold of 500ms")
        void shouldEnforceP95LatencyThreshold() {
            // Record latencies within threshold
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(400, TimeUnit.MILLISECONDS);
            timer.record(350, TimeUnit.MILLISECONDS);
            timer.record(450, TimeUnit.MILLISECONDS);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation.isPassed()).isTrue();
            assertThat(validation.getActualValue()).isLessThanOrEqualTo(validation.getThreshold());
        }

        @Test
        @DisplayName("Should enforce error rate threshold of 1%")
        void shouldEnforceErrorRateThreshold() {
            // Record requests with 0.5% error rate (within threshold)
            meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(1000);
            meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(5);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation.isPassed()).isTrue();
            assertThat(validation.getActualValue()).isLessThanOrEqualTo(validation.getThreshold());
        }

        @Test
        @DisplayName("Should detect non-compliance when P95 latency exceeds 500ms")
        void shouldDetectNonComplianceWhenP95Exceeded() {
            // Record latencies above threshold
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(600, TimeUnit.MILLISECONDS);
            timer.record(550, TimeUnit.MILLISECONDS);
            timer.record(650, TimeUnit.MILLISECONDS);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P95_LATENCY);

            assertThat(validation.isPassed()).isFalse();
            assertThat(validation.getActualValue()).isGreaterThan(validation.getThreshold());
        }

        @Test
        @DisplayName("Should detect non-compliance when error rate exceeds 1%")
        void shouldDetectNonComplianceWhenErrorRateExceeded() {
            // Record requests with 2% error rate (above threshold)
            meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(1000);
            meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(20);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ERROR_RATE);

            assertThat(validation.isPassed()).isFalse();
            assertThat(validation.getActualValue()).isGreaterThan(validation.getThreshold());
        }
    }

    @Nested
    @DisplayName("Compliance Report Tests")
    class ComplianceReportTests {

        @Test
        @DisplayName("Should generate compliance report with all required sections")
        void shouldGenerateComplianceReportWithAllSections() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getReportId()).isNotNull();
            assertThat(report.getGeneratedAt()).isNotNull();
            assertThat(report.getSloCompliance()).isNotNull();
            assertThat(report.getPerformanceMetrics()).isNotNull();
            assertThat(report.getGovernanceStatus()).isNotNull();
        }

        @Test
        @DisplayName("Should include all latency metrics in compliance report")
        void shouldIncludeAllLatencyMetricsInReport() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo.getP95GatewayLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP99GatewayLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP95RoutingLatencyMs()).isGreaterThan(0);
            assertThat(slo.getP95FilterLatencyMs()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should include availability metrics in compliance report")
        void shouldIncludeAvailabilityMetricsInReport() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();
            ComplianceReportGenerator.SloComplianceSection slo = report.getSloCompliance();

            assertThat(slo.getAvailability()).isGreaterThan(0);
            assertThat(slo.getAvailability()).isLessThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should indicate COMPLIANT status when all thresholds met")
        void shouldIndicateCompliantStatusWhenAllThresholdsMet() {
            recordCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGovernanceStatus().getStatus()).isEqualTo("COMPLIANT");
            assertThat(report.getGovernanceStatus().getSeverity()).isEqualTo("INFO");
        }

        @Test
        @DisplayName("Should indicate NON-COMPLIANT status when thresholds not met")
        void shouldIndicateNonCompliantStatusWhenThresholdsNotMet() {
            recordNonCompliantMetrics();

            ComplianceReportGenerator.GovernanceReport report = reportGenerator.generateReport();

            assertThat(report.getGovernanceStatus().getStatus()).isIn("WARNING", "NON-COMPLIANT");
            assertThat(report.getSloCompliance().isOverallCompliant()).isFalse();
        }
    }

    @Nested
    @DisplayName("Threshold Validation Tests")
    class ThresholdValidationTests {

        @Test
        @DisplayName("Should validate routing latency threshold of 100ms")
        void shouldValidateRoutingLatencyThreshold() {
            Timer timer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
            timer.record(80, TimeUnit.MILLISECONDS);
            timer.record(90, TimeUnit.MILLISECONDS);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.ROUTING_LATENCY);

            assertThat(validation.isPassed()).isTrue();
            assertThat(validation.getThreshold()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("Should validate filter latency threshold of 50ms")
        void shouldValidateFilterLatencyThreshold() {
            Timer timer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
            timer.record(40, TimeUnit.MILLISECONDS);
            timer.record(45, TimeUnit.MILLISECONDS);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.FILTER_LATENCY);

            assertThat(validation.isPassed()).isTrue();
            assertThat(validation.getThreshold()).isEqualTo(50.0);
        }

        @Test
        @DisplayName("Should validate P99 latency threshold of 1000ms")
        void shouldValidateP99LatencyThreshold() {
            Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
            timer.record(800, TimeUnit.MILLISECONDS);
            timer.record(900, TimeUnit.MILLISECONDS);

            ThresholdValidator.ThresholdValidation validation =
                    thresholdValidator.validateThreshold(ThresholdValidator.ThresholdType.P99_LATENCY);

            assertThat(validation.isPassed()).isTrue();
            assertThat(validation.getThreshold()).isEqualTo(1000.0);
        }
    }

    @Nested
    @DisplayName("Threshold Enforcement Tests")
    class ThresholdEnforcementTests {

        @Test
        @DisplayName("Should enforce degraded state when any threshold fails")
        void shouldEnforceDegradedStateWhenThresholdFails() {
            recordNonCompliantP95();

            boolean isDegraded = thresholdValidator.isDegraded();

            assertThat(isDegraded).isTrue();
        }

        @Test
        @DisplayName("Should return HEALTHY status when all thresholds pass")
        void shouldReturnHealthyStatusWhenAllThresholdsPass() {
            recordCompliantMetrics();

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.HEALTHY);
        }

        @Test
        @DisplayName("Should return DEGRADED status when 75% of thresholds pass")
        void shouldReturnDegradedStatusWhen75PercentPass() {
            recordPartiallyCompliantMetrics();

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.DEGRADED);
        }

        @Test
        @DisplayName("Should return UNHEALTHY status when less than 75% thresholds pass")
        void shouldReturnUnhealthyStatusWhenLessThan75PercentPass() {
            recordSeverelyNonCompliantMetrics();

            ThresholdValidator.HealthStatus status = thresholdValidator.getHealthStatus();

            assertThat(status).isEqualTo(ThresholdValidator.HealthStatus.UNHEALTHY);
        }
    }

    private void recordCompliantMetrics() {
        // Gateway latency within threshold
        Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        gatewayTimer.record(400, TimeUnit.MILLISECONDS);
        gatewayTimer.record(350, TimeUnit.MILLISECONDS);
        gatewayTimer.record(300, TimeUnit.MILLISECONDS);

        // Routing latency within threshold
        Timer routingTimer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
        routingTimer.record(80, TimeUnit.MILLISECONDS);
        routingTimer.record(70, TimeUnit.MILLISECONDS);

        // Filter latency within threshold
        Timer filterTimer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
        filterTimer.record(40, TimeUnit.MILLISECONDS);
        filterTimer.record(30, TimeUnit.MILLISECONDS);

        // Requests with acceptable error rate
        meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(1000);
        meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(5);
        meterRegistry.counter("ai.gateway.requests.success", "service", "ai-gateway").increment(995);
    }

    private void recordNonCompliantMetrics() {
        // Gateway latency above threshold
        Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        gatewayTimer.record(600, TimeUnit.MILLISECONDS);
        gatewayTimer.record(650, TimeUnit.MILLISECONDS);

        meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(1000);
        meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(20);
    }

    private void recordNonCompliantP95() {
        Timer timer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        timer.record(600, TimeUnit.MILLISECONDS);
        timer.record(550, TimeUnit.MILLISECONDS);

        meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(100);
        meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(5);
    }

    private void recordPartiallyCompliantMetrics() {
        // 4 of 5 thresholds pass (80%)
        Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        gatewayTimer.record(600, TimeUnit.MILLISECONDS); // P95 fail

        Timer routingTimer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
        routingTimer.record(80, TimeUnit.MILLISECONDS); // pass

        Timer filterTimer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
        filterTimer.record(40, TimeUnit.MILLISECONDS); // pass

        meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(100);
        meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(0); // pass
    }

    private void recordSeverelyNonCompliantMetrics() {
        // Only 2 of 5 thresholds pass (40%)
        Timer gatewayTimer = meterRegistry.timer("ai.gateway.latency", "service", "ai-gateway");
        gatewayTimer.record(700, TimeUnit.MILLISECONDS); // P95 fail

        Timer routingTimer = meterRegistry.timer("ai.gateway.routing.duration", "service", "ai-gateway");
        routingTimer.record(150, TimeUnit.MILLISECONDS); // fail

        Timer filterTimer = meterRegistry.timer("ai.gateway.filter.duration", "service", "ai-gateway");
        filterTimer.record(80, TimeUnit.MILLISECONDS); // fail

        meterRegistry.counter("ai.gateway.requests.total", "service", "ai-gateway").increment(100);
        meterRegistry.counter("ai.gateway.requests.failure", "service", "ai-gateway").increment(5); // pass
    }
}
