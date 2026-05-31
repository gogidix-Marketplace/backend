package com.gogidix.aiservices.aireporting.infrastructure.metrics;

import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class ReportingMetricsTest {
    private ReportingMetrics m;

    @BeforeEach
    void setup() {
        m = new ReportingMetrics(new SimpleMeterRegistry());
    }

    @Test
    void counters() {
        m.incrementReportGenerationTotal();
        m.incrementReportGenerationSuccess();
        m.incrementReportGenerationFailure();
        m.incrementReportDownload();
        m.incrementDataExtraction();
    }

    @Test
    void records() {
        m.recordReportGenerationTime(100);
        m.recordDataExtractionTime(100);
        m.recordReportFormattingTime(100);
    }

    @Test
    void timers() {
        var s0 = m.startReportGenerationTimer();
        m.stopReportGenerationTimer(s0);
        var s1 = m.startDataExtractionTimer();
        m.stopDataExtractionTimer(s1);
        var s2 = m.startReportFormattingTimer();
        m.stopReportFormattingTimer(s2);
        
    }

    @Test
    void slos() {
        assertThat(m.getReportGenerationLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getReportGenerationLatencyP99()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getDataExtractionLatencyP95()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0.0);
        assertThat(m.getMeterRegistry()).isNotNull();
    }
}
