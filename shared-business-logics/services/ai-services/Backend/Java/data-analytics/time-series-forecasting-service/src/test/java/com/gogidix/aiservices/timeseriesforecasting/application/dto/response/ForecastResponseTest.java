package com.gogidix.aiservices.timeseriesforecasting.application.dto.response;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;

class ForecastResponseTest {
    @Test
    void noArg() {
        var r = new ForecastResponse();
        assertThat(r).isNotNull();
    }

    @Test
    void fullConstructor() {
        var r = new ForecastResponse("id", List.of(), Map.of());
        assertThat(r.getForecastId()).isEqualTo("id");
    }

    @Test
    void builder() {
        var r = ForecastResponse.builder()
            .forecastId("id")
            .forecasts(List.of())
            .accuracyMetrics(Map.of("mae", 0.5))
            .build();
        assertThat(r.getForecastId()).isEqualTo("id");
    }

    @Test
    void setters() {
        var r = new ForecastResponse();
        r.setForecastId("id");
        r.setForecasts(List.of());
        r.setAccuracyMetrics(Map.of());
        assertThat(r.getForecastId()).isEqualTo("id");
    }
}
