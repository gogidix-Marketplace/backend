package com.gogidix.aiservices.timeseriesforecasting.domain;

import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class TimeSeriesForecastTest {
    private List<TimeSeriesForecast.TimeSeriesDataPoint> makeData(int count) {
        var pts = new ArrayList<TimeSeriesForecast.TimeSeriesDataPoint>();
        for (int i = 0; i < count; i++) pts.add(new TimeSeriesForecast.TimeSeriesDataPoint(LocalDateTime.now().minusDays(count - i), i * 10.0));
        return pts;
    }

    @Test
    void create() {
        var ts = new TimeSeriesForecast("id", makeData(25), 10,
            TimeSeriesForecast.Frequency.DAILY, true,
            List.of(new TimeSeriesForecast.ForecastPoint(LocalDateTime.now().plusDays(1), 11.0, 9.0, 13.0)),
            new TimeSeriesForecast.AccuracyMetrics(0.5, 0.7, 0.1, 0.8),
            LocalDateTime.now());
        assertThat(ts.getForecastId()).isEqualTo("id");
        assertThat(ts.getForecasts()).hasSize(1);
    }

    @Test
    void frequencyEnum() { assertThat(TimeSeriesForecast.Frequency.values()).hasSize(4); }

    @Test
    void dataPoint() {
        var p = new TimeSeriesForecast.TimeSeriesDataPoint(LocalDateTime.now(), 42.0);
        assertThat(p.getValue()).isEqualTo(42.0);
    }

    @Test
    void forecastPoint() {
        var p = new TimeSeriesForecast.ForecastPoint(LocalDateTime.now(), 10.0, 8.0, 12.0);
        assertThat(p.getLowerBound()).isEqualTo(8.0);
    }

    @Test
    void accuracyMetrics() {
        var m = new TimeSeriesForecast.AccuracyMetrics(1.0, 2.0, 0.1, 0.9);
        assertThat(m.getDirectionAccuracy()).isEqualTo(0.9);
    }
}
