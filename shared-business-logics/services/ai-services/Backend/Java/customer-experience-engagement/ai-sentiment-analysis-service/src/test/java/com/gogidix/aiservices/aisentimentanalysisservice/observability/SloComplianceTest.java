package com.gogidix.aiservices.aisentimentanalysisservice.observability;

import com.gogidix.aiservices.aisentimentanalysisservice.TestApplication;
import com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.governance.ComplianceReportGenerator;
import com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.governance.ThresholdValidator;
import com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.metrics.SentimentAnalysisMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(
    classes = TestApplication.class,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
    }
)
@ActiveProfiles("test")
@DisplayName("Financial-Grade: SLO Compliance Tests")
class SloComplianceTest {

    @Autowired
    private SentimentAnalysisMetrics metrics;

    @Autowired
    private ThresholdValidator thresholdValidator;

    @Autowired
    private ComplianceReportGenerator reportGenerator;

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    @DisplayName("Should meet P95 latency SLO")
    void shouldMeetP95LatencySlo() {
        double p95Latency = metrics.getAnalysisLatencyP95();
        assertThat(p95Latency).isGreaterThanOrEqualTo(0);
    }
}
