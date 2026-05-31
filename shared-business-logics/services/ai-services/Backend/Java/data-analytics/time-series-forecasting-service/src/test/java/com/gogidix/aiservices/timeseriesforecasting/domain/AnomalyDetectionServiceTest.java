package com.gogidix.aiservices.timeseriesforecasting.domain;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class AnomalyDetectionServiceTest {
    private AnomalyDetectionService s;

    @BeforeEach
    void setup() { s = new AnomalyDetectionService(); }

    private List<TimeSeriesDataPoint> makeData(int count) {
        var pts = new ArrayList<TimeSeriesDataPoint>();
        for (int i = 0; i < count; i++) {
            pts.add(new TimeSeriesDataPoint(LocalDateTime.now().minusDays(count - i), (double)(i * 10)));
        }
        return pts;
    }

    @Test
    void detectAnomalies() {
        var result = s.detectAnomalies(makeData(50), 0.95);
        assertThat(result).isNotNull();
    }

    @Test
    void detectAnomaliesWithDefault() {
        var result = s.detectAnomalies(makeData(50));
        assertThat(result).isNotNull();
    }

    @Test
    void anomalyRecord() {
        var a = new AnomalyDetectionService.Anomaly(LocalDateTime.now(), 100.0, 2.5, 50.0, AnomalyDetectionService.AnomalySeverity.HIGH);
        assertThat(a.getValue()).isEqualTo(100.0);
        assertThat(a.getSeverity()).isEqualTo(AnomalyDetectionService.AnomalySeverity.HIGH);
    }

    @Test
    void anomalySeverityEnum() {
        assertThat(AnomalyDetectionService.AnomalySeverity.values()).isNotEmpty();
    }
}
