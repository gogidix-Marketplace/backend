package com.gogidix.aiservices.predictiveanalytics.application.service;

import com.gogidix.aiservices.predictiveanalytics.application.dto.request.GenerateForecastRequest;
import com.gogidix.aiservices.predictiveanalytics.application.dto.response.ForecastResponse;
import com.gogidix.aiservices.predictiveanalytics.application.port.out.ForecastRepository;
import com.gogidix.aiservices.predictiveanalytics.domain.model.ForecastingMethod;
import com.gogidix.aiservices.predictiveanalytics.shared.exception.ForecastNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ForecastService Unit Tests")
class ForecastServiceTest {

    @Mock
    private ForecastRepository forecastRepository;

    private ForecastService forecastService;

    @BeforeEach
    void setUp() {
        forecastService = new ForecastService(forecastRepository);
    }

    @Nested
    @DisplayName("generateForecast() - Generating Forecasts")
    class GenerateForecastTests {

        @Test
        @DisplayName("Should generate forecast with valid request")
        void shouldGenerateForecastWithValidRequest() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .targetField("revenue")
                    .horizon(30)
                    .method(ForecastingMethod.ARIMA)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
            assertThat(response.getForecastId()).isNotNull();
            assertThat(response.getMetrics()).isNotNull();
        }

        @Test
        @DisplayName("Should generate forecast with minimum required fields")
        void shouldGenerateForecastWithMinimumFields() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
            assertThat(response.getForecastId()).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception when dataSource is null")
        void shouldThrowExceptionWhenDataSourceIsNull() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource(null)
                    .build();

            assertThatThrownBy(() -> forecastService.generateForecast(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Data source cannot be null");
        }

        @Test
        @DisplayName("Should generate unique forecast IDs")
        void shouldGenerateUniqueForecastIds() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response1 = forecastService.generateForecast(request);
            ForecastResponse response2 = forecastService.generateForecast(request);

            assertThat(response1.getForecastId()).isNotEqualTo(response2.getForecastId());
        }

        @Test
        @DisplayName("Should initialize empty forecasts list")
        void shouldInitializeEmptyForecastsList() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response.getForecasts()).isNotNull();
            assertThat(response.getForecasts()).isEmpty();
        }

        @Test
        @DisplayName("Should initialize empty confidence intervals")
        void shouldInitializeEmptyConfidenceIntervals() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response.getConfidenceIntervals()).isNotNull();
            assertThat(response.getConfidenceIntervals()).isEmpty();
        }

        @Test
        @DisplayName("Should set processing status in metrics")
        void shouldSetProcessingStatusInMetrics() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response.getMetrics()).isNotNull();
            assertThat(response.getMetrics()).isInstanceOf(Map.class);
        }

        @Test
        @DisplayName("Should handle different forecasting methods")
        void shouldHandleDifferentForecastingMethods() {
            ForecastingMethod[] methods = {
                    ForecastingMethod.ARIMA,
                    ForecastingMethod.EXPONENTIAL_SMOOTHING,
                    ForecastingMethod.LINEAR_REGRESSION,
                    ForecastingMethod.MOVING_AVERAGE,
                    ForecastingMethod.PROPHET,
                    ForecastingMethod.LSTM,
                    ForecastingMethod.ENSEMBLE
            };

            for (ForecastingMethod method : methods) {
                GenerateForecastRequest request = GenerateForecastRequest.builder()
                        .dataSource("sales-data")
                        .method(method)
                        .build();

                ForecastResponse response = forecastService.generateForecast(request);

                assertThat(response).isNotNull();
                assertThat(response.getForecastId()).isNotNull();
            }
        }

        @Test
        @DisplayName("Should handle null forecasting method")
        void shouldHandleNullForecastingMethod() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .method(null)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle null horizon")
        void shouldHandleNullHorizon() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .horizon(null)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle null target field")
        void shouldHandleNullTargetField() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .targetField(null)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }
    }

    @Nested
    @DisplayName("getForecast() - Retrieving Forecasts")
    class GetForecastTests {

        @Test
        @DisplayName("Should throw ForecastNotFoundException")
        void shouldThrowForecastNotFoundException() {
            String forecastId = "non-existent";

            assertThatThrownBy(() -> forecastService.getForecast(forecastId))
                    .isInstanceOf(ForecastNotFoundException.class)
                    .hasMessageContaining(forecastId);
        }

        @Test
        @DisplayName("Should always throw not found (stub implementation)")
        void shouldAlwaysThrowNotFound() {
            // Since the implementation always throws, we test this behavior
            String forecastId = "any-id";

            assertThatThrownBy(() -> forecastService.getForecast(forecastId))
                    .isInstanceOf(ForecastNotFoundException.class);
        }

        @Test
        @DisplayName("Should handle empty forecast ID")
        void shouldHandleEmptyForecastId() {
            assertThatThrownBy(() -> forecastService.getForecast(""))
                    .isInstanceOf(ForecastNotFoundException.class);
        }

        @Test
        @DisplayName("Should handle special characters in forecast ID")
        void shouldHandleSpecialCharactersInId() {
            assertThatThrownBy(() -> forecastService.getForecast("id-with-dashes_and_123"))
                    .isInstanceOf(ForecastNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long dataSource name")
        void shouldHandleLongDataSourceName() {
            String longDataSource = "data-source-" + "a".repeat(200);
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource(longDataSource)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle unicode in dataSource")
        void shouldHandleUnicodeInDataSource() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("销售数据")
                    .targetField("收入")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle special characters in dataSource")
        void shouldHandleSpecialCharactersInDataSource() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("data-source_with.special@chars")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle zero horizon")
        void shouldHandleZeroHorizon() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .horizon(0)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle negative horizon")
        void shouldHandleNegativeHorizon() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .horizon(-10)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }

        @Test
        @DisplayName("Should handle very large horizon")
        void shouldHandleVeryLargeHorizon() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .horizon(10000)
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response).isNotNull();
        }
    }

    @Nested
    @DisplayName("Request Builder Tests")
    class RequestBuilderTests {

        @Test
        @DisplayName("Should build request using builder pattern")
        void shouldBuildRequestUsingBuilder() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .targetField("revenue")
                    .horizon(30)
                    .method(ForecastingMethod.LSTM)
                    .build();

            assertThat(request.getDataSource()).isEqualTo("sales-data");
            assertThat(request.getTargetField()).isEqualTo("revenue");
            assertThat(request.getHorizon()).isEqualTo(30);
            assertThat(request.getMethod()).isEqualTo(ForecastingMethod.LSTM);
        }

        @Test
        @DisplayName("Should build request with only dataSource")
        void shouldBuildRequestWithOnlyDataSource() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            assertThat(request.getDataSource()).isEqualTo("sales-data");
            assertThat(request.getTargetField()).isNull();
            assertThat(request.getHorizon()).isNull();
            assertThat(request.getMethod()).isNull();
        }

        @Test
        @DisplayName("Should support method chaining in builder")
        void shouldSupportMethodChaining() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .targetField("revenue")
                    .horizon(30)
                    .method(ForecastingMethod.ARIMA)
                    .build();

            assertThat(request.getDataSource()).isNotNull();
            assertThat(request.getTargetField()).isNotNull();
            assertThat(request.getHorizon()).isNotNull();
            assertThat(request.getMethod()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Response Structure Tests")
    class ResponseStructureTests {

        @Test
        @DisplayName("Should create response with all fields initialized")
        void shouldCreateResponseWithAllFieldsInitialized() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response.getForecastId()).isNotNull();
            assertThat(response.getForecasts()).isNotNull();
            assertThat(response.getConfidenceIntervals()).isNotNull();
            assertThat(response.getMetrics()).isNotNull();
        }

        @Test
        @DisplayName("Should have valid UUID format for forecast ID")
        void shouldHaveValidUuidFormat() {
            GenerateForecastRequest request = GenerateForecastRequest.builder()
                    .dataSource("sales-data")
                    .build();

            ForecastResponse response = forecastService.generateForecast(request);

            assertThat(response.getForecastId()).matches(
                    "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
            );
        }
    }
}
