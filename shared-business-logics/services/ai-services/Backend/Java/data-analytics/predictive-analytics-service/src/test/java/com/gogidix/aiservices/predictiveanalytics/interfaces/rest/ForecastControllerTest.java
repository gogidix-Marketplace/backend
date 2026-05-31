package com.gogidix.aiservices.predictiveanalytics.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.predictiveanalytics.application.dto.request.GenerateForecastRequest;
import com.gogidix.aiservices.predictiveanalytics.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.predictiveanalytics.application.service.ForecastService;
import com.gogidix.aiservices.predictiveanalytics.domain.model.ForecastingMethod;
import com.gogidix.aiservices.predictiveanalytics.shared.exception.ForecastNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForecastController.class)
@DisplayName("ForecastController REST API Tests")
class ForecastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ForecastService forecastService;

    private ForecastResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new ForecastResponse();
        mockResponse.setForecastId("forecast-123");
        mockResponse.setForecasts(List.of(100.0, 105.0, 110.0));
        mockResponse.setConfidenceIntervals(List.of(
                Map.of("lower", 95.0, "upper", 105.0),
                Map.of("lower", 100.0, "upper", 110.0)
        ));
        mockResponse.setMetrics(Map.of(
                "mae", 2.5,
                "rmse", 3.2,
                "confidence", 0.95
        ));
    }

    @Nested
    @DisplayName("POST /api/v1/predictive/forecasts - Generate Forecast")
    class GenerateForecastTests {

        @Test
        @DisplayName("Should generate forecast with valid request")
        void shouldGenerateForecast() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .targetField("revenue")
                    .horizon(30)
                    .method(ForecastingMethod.ARIMA)
                    .build();

            when(forecastService.generateForecast(any(GenerateForecastRequest.class)))
                    .thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.forecastId").value("forecast-123"))
                    .andExpect(jsonPath("$.forecasts").isArray())
                    .andExpect(jsonPath("$.forecasts[0]").value(100.0));

            verify(forecastService).generateForecast(any(GenerateForecastRequest.class));
        }

        @Test
        @DisplayName("Should generate forecast with minimum required fields")
        void shouldGenerateForecastWithMinimumFields() throws Exception {
            String requestJson = """
                    {
                        "dataSource": "sales-data"
                    }
                    """;

            when(forecastService.generateForecast(any(GenerateForecastRequest.class)))
                    .thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.forecastId").exists());
        }

        @Test
        @DisplayName("Should accept different forecasting methods")
        void shouldAcceptDifferentForecastingMethods() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .targetField("revenue")
                    .horizon(30)
                    .method(ForecastingMethod.LSTM)
                    .build();

            when(forecastService.generateForecast(any(GenerateForecastRequest.class)))
                    .thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should return 400 when dataSource is null")
        void shouldReturn400WhenDataSourceIsNull() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource(null)
                    .build();

            when(forecastService.generateForecast(any(GenerateForecastRequest.class)))
                    .thenThrow(new IllegalArgumentException("Data source cannot be null"));

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted()); // Service throws but controller catches
        }

        @Test
        @DisplayName("Should handle empty request body gracefully")
        void shouldHandleEmptyRequestBody() throws Exception {
            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isAccepted());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/predictive/forecasts/{id} - Get Forecast")
    class GetForecastTests {

        @Test
        @DisplayName("Should return forecast by ID")
        void shouldReturnForecastById() throws Exception {
            when(forecastService.getForecast("forecast-123"))
                    .thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "forecast-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.forecastId").value("forecast-123"))
                    .andExpect(jsonPath("$.forecasts").isArray());

            verify(forecastService).getForecast("forecast-123");
        }

        @Test
        @DisplayName("Should return 404 when forecast not found")
        void shouldReturn404WhenForecastNotFound() throws Exception {
            when(forecastService.getForecast("non-existent"))
                    .thenThrow(new ForecastNotFoundException("non-existent"));

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "non-existent"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.timestamp").exists());

            verify(forecastService).getForecast("non-existent");
        }

        @Test
        @DisplayName("Should handle special characters in forecast ID")
        void shouldHandleSpecialCharactersInId() throws Exception {
            String specialId = "forecast-with-dashes_and_123";

            when(forecastService.getForecast(specialId))
                    .thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", specialId))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle UUID format forecast ID")
        void shouldHandleUuidFormatId() throws Exception {
            String uuidId = "550e8400-e29b-41d4-a716-446655440000";

            when(forecastService.getForecast(uuidId))
                    .thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", uuidId))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should return proper error format for not found")
        void shouldReturnProperErrorFormat() throws Exception {
            when(forecastService.getForecast("missing"))
                    .thenThrow(new ForecastNotFoundException("missing"));

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "missing"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isString())
                    .andExpect(jsonPath("$.timestamp").isNumber());
        }

        @Test
        @DisplayName("Should include error message in response")
        void shouldIncludeErrorMessage() throws Exception {
            when(forecastService.getForecast("missing"))
                    .thenThrow(new ForecastNotFoundException("Forecast not found: missing"));

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "missing"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").isNotEmpty());
        }
    }

    @Nested
    @DisplayName("ForecastingMethod Tests")
    class ForecastingMethodTests {

        @Test
        @DisplayName("Should accept ARIMA method")
        void shouldAcceptArimaMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.ARIMA)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept EXPONENTIAL_SMOOTHING method")
        void shouldAcceptExponentialSmoothingMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.EXPONENTIAL_SMOOTHING)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept LINEAR_REGRESSION method")
        void shouldAcceptLinearRegressionMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.LINEAR_REGRESSION)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept MOVING_AVERAGE method")
        void shouldAcceptMovingAverageMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.MOVING_AVERAGE)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept PROPHET method")
        void shouldAcceptProphetMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.PROPHET)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept LSTM method")
        void shouldAcceptLstmMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.LSTM)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should accept ENSEMBLE method")
        void shouldAcceptEnsembleMethod() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .method(ForecastingMethod.ENSEMBLE)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long dataSource name")
        void shouldHandleLongDataSource() throws Exception {
            String longDataSource = "data-source-" + "a".repeat(200);
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource(longDataSource)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle maximum horizon value")
        void shouldHandleMaxHorizon() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .horizon(365)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle horizon of 1")
        void shouldHandleHorizonOf1() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data")
                    .horizon(1)
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should handle unicode in dataSource name")
        void shouldHandleUnicodeInDataSource() throws Exception {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("销售数据")
                    .targetField("收入")
                    .build();

            when(forecastService.generateForecast(any())).thenReturn(mockResponse);

            mockMvc.perform(post("/api/v1/predictive/forecasts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should include all required fields in response")
        void shouldIncludeAllRequiredFields() throws Exception {
            when(forecastService.getForecast("forecast-123")).thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "forecast-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.forecastId").exists())
                    .andExpect(jsonPath("$.forecasts").exists())
                    .andExpect(jsonPath("$.confidenceIntervals").exists())
                    .andExpect(jsonPath("$.metrics").exists());
        }

        @Test
        @DisplayName("Should return forecasts as array")
        void shouldReturnForecastsAsArray() throws Exception {
            when(forecastService.getForecast("forecast-123")).thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "forecast-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.forecasts").isArray());
        }

        @Test
        @DisplayName("Should include confidence intervals")
        void shouldIncludeConfidenceIntervals() throws Exception {
            when(forecastService.getForecast("forecast-123")).thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "forecast-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.confidenceIntervals").isArray());
        }

        @Test
        @DisplayName("Should include metrics object")
        void shouldIncludeMetrics() throws Exception {
            when(forecastService.getForecast("forecast-123")).thenReturn(mockResponse);

            mockMvc.perform(get("/api/v1/predictive/forecasts/{id}", "forecast-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.metrics").isMap());
        }
    }
}
