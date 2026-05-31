package com.gogidix.aiservices.timeseriesforecasting.domain.model;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class TimeSeriesForecastModelTest {
    private List<TimeSeriesForecast.TimeSeriesPoint> makePoints(int count) {
        var pts = new ArrayList<TimeSeriesForecast.TimeSeriesPoint>();
        for (int i = 0; i < count; i++) pts.add(new TimeSeriesForecast.TimeSeriesPoint("2024-01-" + String.format("%02d", (i % 28) + 1), i * 10.0));
        return pts;
    }

    @Test
    void create() {
        var ts = TimeSeriesForecast.create(makePoints(25), 7, Frequency.DAILY);
        assertThat(ts.getForecastId()).isNotNull();
        assertThat(ts.getStatus()).isEqualTo(TimeSeriesForecast.Status.PROCESSING);
    }

    @Test
    void complete() {
        var ts = TimeSeriesForecast.create(makePoints(25), 7, Frequency.DAILY);
        ts.complete(List.of(new TimeSeriesForecast.ForecastValue("2024-02-01", 15.0, 12.0, 18.0)));
        assertThat(ts.getStatus()).isEqualTo(TimeSeriesForecast.Status.COMPLETED);
    }

    @Test
    void withSeasonality() {
        var ts = TimeSeriesForecast.create(makePoints(25), 7, Frequency.DAILY);
        var ts2 = ts.withSeasonality(true);
        assertThat(ts2.isIncludeSeasonality()).isTrue();
    }

    @Test
    void statusEnum() { assertThat(TimeSeriesForecast.Status.values()).hasSize(3); }

    @Test
    void frequencyEnum() { assertThat(Frequency.values()).hasSize(4); }

    @Test
    void dataPoint() {
        var p = new TimeSeriesForecast.TimeSeriesPoint("2024-01-01", 42.0);
        assertThat(p.getValue()).isEqualTo(42.0);
    }

    @Test
    void accuracyMetrics() {
        var m = new TimeSeriesForecast.AccuracyMetrics(1.0, 2.0, 0.1);
        assertThat(m.getMae()).isEqualTo(1.0);
    }
}
