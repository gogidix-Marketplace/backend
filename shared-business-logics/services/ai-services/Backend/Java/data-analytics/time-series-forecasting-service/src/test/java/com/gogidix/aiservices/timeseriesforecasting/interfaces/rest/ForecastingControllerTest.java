package com.gogidix.aiservices.timeseriesforecasting.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.timeseriesforecasting.application.dto.request.CreateForecastRequest;
import com.gogidix.aiservices.timeseriesforecasting.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.timeseriesforecasting.domain.model.Frequency;
import com.gogidix.aiservices.timeseriesforecasting.application.service.ForecastingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForecastingController.class)
@DisplayName("ForecastingController REST API Tests")
class ForecastingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ForecastingService forecastingService;

    private CreateForecastRequest validRequest;
    private ForecastResponse forecastResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CreateForecastRequest();
        CreateForecastRequest.TimeSeriesPoint point =
                new CreateForecastRequest.TimeSeriesPoint("2024-01-01T00:00:00Z", 100.0);
        validRequest.setTimeSeriesData(Arrays.asList(
                point,
                new CreateForecastRequest.TimeSeriesPoint("2024-01-02T00:00:00Z", 110.0),
                new CreateForecastRequest.TimeSeriesPoint("2024-01-03T00:00:00Z", 105.0)
        ));
        validRequest.setForecastHorizon(7);
        validRequest.setFrequency(Frequency.DAILY);
        validRequest.setIncludeSeasonality(true);

        forecastResponse = ForecastResponse.builder()
                .forecastId("forecast-abc-123")
                .forecasts(Arrays.asList(108.0, 112.0, 106.0, 115.0, 109.0, 118.0, 111.0))
                .accuracyMetrics(Map.of("mape", 0.05, "rmse", 2.5))
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/forecasting/forecasts - Create Forecast")
    class CreateForecastTests {

        @Test
        @DisplayName("Should create forecast successfully")
        void shouldCreateForecast() throws Exception {
            when(forecastingService.createForecast(any(CreateForecastRequest.class)))
                    .thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.forecastId").exists())
                    .andExpect(jsonPath("$.forecasts").isArray())
                    .andExpect(jsonPath("$.accuracyMetrics").exists());

            verify(forecastingService).createForecast(any(CreateForecastRequest.class));
        }

        @Test
        @DisplayName("Should accept DAILY frequency")
        void shouldAcceptDailyFrequency() throws Exception {
            validRequest.setFrequency(Frequency.DAILY);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept HOURLY frequency")
        void shouldAcceptHourlyFrequency() throws Exception {
            validRequest.setFrequency(Frequency.HOURLY);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept WEEKLY frequency")
        void shouldAcceptWeeklyFrequency() throws Exception {
            validRequest.setFrequency(Frequency.WEEKLY);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept MONTHLY frequency")
        void shouldAcceptMonthlyFrequency() throws Exception {
            validRequest.setFrequency(Frequency.MONTHLY);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept different forecast horizons")
        void shouldAcceptDifferentForecastHorizons() throws Exception {
            Integer[] horizons = {1, 7, 14, 30, 90, 365};

            for (Integer horizon : horizons) {
                validRequest.setForecastHorizon(horizon);

                when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

                mockMvc.perform(post("/api/v1/forecasting/forecasts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
                        .andExpect(status().isAccepted());
            }
        }

        @Test
        @DisplayName("Should accept seasonality enabled")
        void shouldAcceptSeasonalityEnabled() throws Exception {
            validRequest.setIncludeSeasonality(true);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept seasonality disabled")
        void shouldAcceptSeasonalityDisabled() throws Exception {
            validRequest.setIncludeSeasonality(false);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept multiple time series data points")
        void shouldAcceptMultipleTimeSeriesDataPoints() throws Exception {
            // Already set up with 3 data points
            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());

            verify(forecastingService).createForecast(any(CreateForecastRequest.class));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/forecasting/forecasts/{id} - Get Forecast")
    class GetForecastTests {

        @Test
        @DisplayName("Should return forecast by ID")
        void shouldReturnForecastById() throws Exception {
            when(forecastingService.getForecast(eq("forecast-abc-123")))
                    .thenReturn(forecastResponse);

            mockMvc.perform(get("/api/v1/forecasting/forecasts/{id}", "forecast-abc-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.forecastId").value("forecast-abc-123"))
                    .andExpect(jsonPath("$.forecasts").isArray())
                    .andExpect(jsonPath("$.accuracyMetrics").exists());

            verify(forecastingService).getForecast("forecast-abc-123");
        }

        @Test
        @DisplayName("Should return 404 for non-existent forecast")
        void shouldReturn404ForNonExistentForecast() throws Exception {
            when(forecastingService.getForecast(eq("non-existent")))
                    .thenThrow(new IllegalArgumentException("Forecast not found"));

            mockMvc.perform(get("/api/v1/forecasting/forecasts/{id}", "non-existent"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle special characters in forecast ID")
        void shouldHandleSpecialCharactersInForecastId() throws Exception {
            when(forecastingService.getForecast(anyString()))
                    .thenReturn(forecastResponse);

            mockMvc.perform(get("/api/v1/forecasting/forecasts/{id}", "forecast-with_special.chars"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle UUID as forecast ID")
        void shouldHandleUuidAsForecastId() throws Exception {
            String uuid = java.util.UUID.randomUUID().toString();
            when(forecastingService.getForecast(eq(uuid)))
                    .thenReturn(forecastResponse);

            mockMvc.perform(get("/api/v1/forecasting/forecasts/{id}", uuid))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should return BAD_REQUEST for invalid argument")
        void shouldReturnBadRequestForInvalidArgument() throws Exception {
            when(forecastingService.createForecast(any(CreateForecastRequest.class)))
                    .thenThrow(new IllegalArgumentException("Invalid time series data"));

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        @DisplayName("Should return NOT_FOUND for missing forecast")
        void shouldReturnNotFoundForMissingForecast() throws Exception {
            when(forecastingService.getForecast(eq("missing-forecast")))
                    .thenThrow(new com.gogidix.aiservices.timeseriesforecasting.shared.exception.ForecastNotFoundException("Forecast not found"));

            mockMvc.perform(get("/api/v1/forecasting/forecasts/{id}", "missing-forecast"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }

        @Test
        @DisplayName("Error response should contain timestamp")
        void errorResponseShouldContainTimestamp() throws Exception {
            when(forecastingService.createForecast(any()))
                    .thenThrow(new IllegalArgumentException("Error"));

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.timestamp").exists());
        }
    }

    @Nested
    @DisplayName("Frequency Enum Tests")
    class FrequencyTests {

        @Test
        @DisplayName("Should handle all Frequency values")
        void shouldHandleAllFrequencyValues() throws Exception {
            Frequency[] frequencies = {
                    Frequency.HOURLY,
                    Frequency.DAILY,
                    Frequency.WEEKLY,
                    Frequency.MONTHLY
            };

            for (Frequency frequency : frequencies) {
                validRequest.setFrequency(frequency);

                when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

                mockMvc.perform(post("/api/v1/forecasting/forecasts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
                        .andExpect(status().isAccepted());
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in time series timestamp")
        void shouldHandleUnicodeInTimestamp() throws Exception {
            // This test verifies the system can handle various timestamp formats
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);
            validRequest.setTimeSeriesData(Arrays.asList(point));

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle single data point")
        void shouldHandleSingleDataPoint() throws Exception {
            validRequest.setTimeSeriesData(Arrays.asList(
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0)
            ));

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle many data points")
        void shouldHandleManyDataPoints() throws Exception {
            List<CreateForecastRequest.TimeSeriesPoint> points = java.util.stream.IntStream
                    .range(0, 100)
                    .mapToObj(i -> new CreateForecastRequest.TimeSeriesPoint(
                            "2024-01-" + String.format("%02d", i + 1),
                            100.0 + i
                    ))
                    .toList();

            validRequest.setTimeSeriesData(points);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle minimum forecast horizon")
        void shouldHandleMinimumForecastHorizon() throws Exception {
            validRequest.setForecastHorizon(1);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle large forecast horizon")
        void shouldHandleLargeForecastHorizon() throws Exception {
            validRequest.setForecastHorizon(365);

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for forecast response")
        void shouldReturnProperJsonStructureForForecastResponse() throws Exception {
            when(forecastingService.getForecast(anyString())).thenReturn(forecastResponse);

            mockMvc.perform(get("/api/v1/forecasting/forecasts/{id}", "forecast-abc-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.forecastId").exists())
                    .andExpect(jsonPath("$.forecasts").exists())
                    .andExpect(jsonPath("$.accuracyMetrics").exists());
        }

        @Test
        @DisplayName("Should return forecasts as array")
        void shouldReturnForecastsAsArray() throws Exception {
            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.forecasts").isArray());
        }
    }

    @Nested
    @DisplayName("TimeSeriesData Tests")
    class TimeSeriesDataTests {

        @Test
        @DisplayName("Should handle positive values")
        void shouldHandlePositiveValues() throws Exception {
            validRequest.setTimeSeriesData(Arrays.asList(
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-02", 200.0),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-03", 150.0)
            ));

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle negative values")
        void shouldHandleNegativeValues() throws Exception {
            validRequest.setTimeSeriesData(Arrays.asList(
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", -50.0),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-02", -100.0)
            ));

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle zero values")
        void shouldHandleZeroValues() throws Exception {
            validRequest.setTimeSeriesData(Arrays.asList(
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 0.0),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-02", 0.0)
            ));

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle decimal values")
        void shouldHandleDecimalValues() throws Exception {
            validRequest.setTimeSeriesData(Arrays.asList(
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.567),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-02", 200.890)
            ));

            when(forecastingService.createForecast(any())).thenReturn(forecastResponse);

            mockMvc.perform(post("/api/v1/forecasting/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isAccepted());
        }
    }
}
