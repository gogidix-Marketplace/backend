package com.gogidix.aiservices.predictiveanalytics.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Forecast Domain Entity Tests")
class ForecastTest {

    @Test
    @DisplayName("Should create forecast with valid parameters")
    void shouldCreateForecast() {
        // Given
        List<Forecast.ForecastDataPoint> dataPoints = List.of(
            new Forecast.ForecastDataPoint(LocalDateTime.now().plusDays(1), 100.0),
            new Forecast.ForecastDataPoint(LocalDateTime.now().plusDays(2), 105.0)
        );

        // When
        Forecast forecast = new Forecast(
            "forecast-1",
            "data-source-1",
            "sales",
            Forecast.ForecastMethod.ARIMA,
            7,
            dataPoints,
            List.of(),
            new Forecast.ForecastMetrics(5.0, 7.0, 3.0, 0.95),
            LocalDateTime.now()
        );

        // Then
        assertThat(forecast.getForecastId()).isEqualTo("forecast-1");
        assertThat(forecast.getMethod()).isEqualTo(Forecast.ForecastMethod.ARIMA);
        assertThat(forecast.getHorizon()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should throw exception when horizon exceeds maximum")
    void shouldThrowExceptionWhenHorizonTooLarge() {
        // Then
        assertThatThrownBy(() -> new Forecast(
            "forecast-1", "src", "field", Forecast.ForecastMethod.ARIMA,
            400, // Exceeds max of 365
            List.of(new Forecast.ForecastDataPoint(LocalDateTime.now(), 100.0)),
            List.of(),
            new Forecast.ForecastMetrics(1.0, 1.0, 1.0, 0.95),
            LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("horizon");
    }

    @Test
    @DisplayName("Should support all forecast methods")
    void shouldSupportAllMethods() {
        // Then
        assertThat(Forecast.ForecastMethod.values()).contains(
            Forecast.ForecastMethod.ARIMA,
            Forecast.ForecastMethod.PROPHET,
            Forecast.ForecastMethod.LSTM
        );
    }
}
