package com.gogidix.aiservices.aidatavalidation.infrastructure.metrics;

import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class DataValidationMetricsTest {
    private DataValidationMetrics m;

    @BeforeEach
    void setup() {
        m = new DataValidationMetrics(new SimpleMeterRegistry());
    }

    @Test
    void counters() {
        m.incrementValidationTotal();
        m.incrementValidationSuccess();
        m.incrementValidationFailure();
        m.incrementValidationPassed();
        m.incrementValidationFailed();
        m.incrementDataQualityIssue();
    }

    @Test
    void records() {
        m.recordValidationTime(100);
    }

    @Test
    void timers() {
        var s0 = m.startValidationTimer();
        m.stopValidationTimer(s0);
        var s1 = m.startSchemaValidationTimer();
        m.stopSchemaValidationTimer(s1);
        var s2 = m.startRuleExecutionTimer();
        m.stopRuleExecutionTimer(s2);
        
    }

    @Test
    void slos() {
        assertThat(m.getValidationLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getValidationLatencyP99()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getSchemaValidationLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getMeterRegistry()).isNotNull();
    }
}
