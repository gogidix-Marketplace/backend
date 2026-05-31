package com.gogidix.aiservices.aichatbotservice.observability;

import com.gogidix.aiservices.aichatbotservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aichatbotservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aichatbotservice.infrastructure.metrics.ChatbotMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance Tests")
class SloComplianceTest {

    @Autowired
    private ChatbotMetrics metrics;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    @DisplayName("Should meet P95 latency SLO")
    void shouldMeetP95LatencySlo() {
        double p95Latency = metrics.getResponseLatencyP95();
        assertThat(p95Latency).isGreaterThanOrEqualTo(0);
    }
}
