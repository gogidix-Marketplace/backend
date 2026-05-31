package com.gogidix.aiservices.timeseriesforecasting.application.dto.request;

import com.gogidix.aiservices.timeseriesforecasting.domain.model.Frequency;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CreateForecastRequest Tests")
class CreateForecastRequestTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create request with default constructor")
        void shouldCreateWithDefaultConstructor() {
            CreateForecastRequest request = new CreateForecastRequest();

            assertThat(request).isNotNull();
            assertThat(request.getTimeSeriesData()).isNull();
            assertThat(request.getForecastHorizon()).isNull();
            assertThat(request.getFrequency()).isNull();
            assertThat(request.getIncludeSeasonality()).isNull();
        }

        @Test
        @DisplayName("Should create request with all fields using setters")
        void shouldCreateWithAllFieldsUsingSetters() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01T00:00:00Z", 100.0);
            List<CreateForecastRequest.TimeSeriesPoint> data = Arrays.asList(point);

            CreateForecastRequest request = new CreateForecastRequest();
            request.setTimeSeriesData(data);
            request.setForecastHorizon(10);
            request.setFrequency(Frequency.DAILY);
            request.setIncludeSeasonality(true);

            assertThat(request.getTimeSeriesData()).hasSize(1);
            assertThat(request.getForecastHorizon()).isEqualTo(10);
            assertThat(request.getFrequency()).isEqualTo(Frequency.DAILY);
            assertThat(request.getIncludeSeasonality()).isTrue();
        }
    }

    @Nested
    @DisplayName("TimeSeriesPoint Tests")
    class TimeSeriesPointTests {

        @Test
        @DisplayName("Should create point with default constructor")
        void shouldCreatePointWithDefaultConstructor() {
            CreateForecastRequest.TimeSeriesPoint point = new CreateForecastRequest.TimeSeriesPoint();

            assertThat(point).isNotNull();
            assertThat(point.getTimestamp()).isNull();
            assertThat(point.getValue()).isNull();
        }

        @Test
        @DisplayName("Should create point with timestamp and value")
        void shouldCreatePointWithTimestampAndValue() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01T00:00:00Z", 150.5);

            assertThat(point.getTimestamp()).isEqualTo("2024-01-01T00:00:00Z");
            assertThat(point.getValue()).isEqualTo(150.5);
        }

        @Test
        @DisplayName("Should create point with positive value")
        void shouldCreatePointWithPositiveValue() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 200.0);

            assertThat(point.getValue()).isPositive();
        }

        @Test
        @DisplayName("Should create point with negative value")
        void shouldCreatePointWithNegativeValue() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", -50.0);

            assertThat(point.getValue()).isNegative();
        }

        @Test
        @DisplayName("Should create point with zero value")
        void shouldCreatePointWithZeroValue() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 0.0);

            assertThat(point.getValue()).isZero();
        }
    }

    @Nested
    @DisplayName("TimeSeriesPoint Equality Tests")
    class TimeSeriesPointEqualityTests {

        @Test
        @DisplayName("Should be equal when timestamps and values match")
        void shouldBeEqualWhenTimestampsAndValuesMatch() {
            CreateForecastRequest.TimeSeriesPoint point1 =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);
            CreateForecastRequest.TimeSeriesPoint point2 =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);

            assertThat(point1).isEqualTo(point2);
            assertThat(point1.hashCode()).isEqualTo(point2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when timestamps differ")
        void shouldNotBeEqualWhenTimestampsDiffer() {
            CreateForecastRequest.TimeSeriesPoint point1 =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);
            CreateForecastRequest.TimeSeriesPoint point2 =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-02", 100.0);

            assertThat(point1).isNotEqualTo(point2);
        }

        @Test
        @DisplayName("Should not be equal when values differ")
        void shouldNotBeEqualWhenValuesDiffer() {
            CreateForecastRequest.TimeSeriesPoint point1 =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);
            CreateForecastRequest.TimeSeriesPoint point2 =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 200.0);

            assertThat(point1).isNotEqualTo(point2);
        }
    }

    @Nested
    @DisplayName("ForecastHorizon Tests")
    class ForecastHorizonTests {

        @Test
        @DisplayName("Should accept short horizon")
        void shouldAcceptShortHorizon() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setForecastHorizon(5);

            assertThat(request.getForecastHorizon()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should accept medium horizon")
        void shouldAcceptMediumHorizon() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setForecastHorizon(30);

            assertThat(request.getForecastHorizon()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should accept long horizon")
        void shouldAcceptLongHorizon() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setForecastHorizon(365);

            assertThat(request.getForecastHorizon()).isEqualTo(365);
        }
    }

    @Nested
    @DisplayName("Frequency Tests")
    class FrequencyTests {

        @ParameterizedTest
        @EnumSource(Frequency.class)
        @DisplayName("Should accept all Frequency values")
        void shouldAcceptAllFrequencyValues(Frequency frequency) {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setFrequency(frequency);

            assertThat(request.getFrequency()).isEqualTo(frequency);
        }

        @Test
        @DisplayName("Should accept HOURLY frequency")
        void shouldAcceptHourlyFrequency() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setFrequency(Frequency.HOURLY);

            assertThat(request.getFrequency()).isEqualTo(Frequency.HOURLY);
        }

        @Test
        @DisplayName("Should accept DAILY frequency")
        void shouldAcceptDailyFrequency() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setFrequency(Frequency.DAILY);

            assertThat(request.getFrequency()).isEqualTo(Frequency.DAILY);
        }

        @Test
        @DisplayName("Should accept WEEKLY frequency")
        void shouldAcceptWeeklyFrequency() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setFrequency(Frequency.WEEKLY);

            assertThat(request.getFrequency()).isEqualTo(Frequency.WEEKLY);
        }

        @Test
        @DisplayName("Should accept MONTHLY frequency")
        void shouldAcceptMonthlyFrequency() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setFrequency(Frequency.MONTHLY);

            assertThat(request.getFrequency()).isEqualTo(Frequency.MONTHLY);
        }
    }

    @Nested
    @DisplayName("Seasonality Tests")
    class SeasonalityTests {

        @Test
        @DisplayName("Should enable seasonality")
        void shouldEnableSeasonality() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setIncludeSeasonality(true);

            assertThat(request.getIncludeSeasonality()).isTrue();
        }

        @Test
        @DisplayName("Should disable seasonality")
        void shouldDisableSeasonality() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setIncludeSeasonality(false);

            assertThat(request.getIncludeSeasonality()).isFalse();
        }

        @Test
        @DisplayName("Should handle null seasonality flag")
        void shouldHandleNullSeasonalityFlag() {
            CreateForecastRequest request = new CreateForecastRequest();

            assertThat(request.getIncludeSeasonality()).isNull();
        }
    }

    @Nested
    @DisplayName("TimeSeriesData Tests")
    class TimeSeriesDataTests {

        @Test
        @DisplayName("Should set empty time series data")
        void shouldSetEmptyTimeSeriesData() {
            CreateForecastRequest request = new CreateForecastRequest();
            request.setTimeSeriesData(Arrays.asList());

            assertThat(request.getTimeSeriesData()).isEmpty();
        }

        @Test
        @DisplayName("Should set single data point")
        void shouldSetSingleDataPoint() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);

            CreateForecastRequest request = new CreateForecastRequest();
            request.setTimeSeriesData(Arrays.asList(point));

            assertThat(request.getTimeSeriesData()).hasSize(1);
        }

        @Test
        @DisplayName("Should set multiple data points")
        void shouldSetMultipleDataPoints() {
            List<CreateForecastRequest.TimeSeriesPoint> points = Arrays.asList(
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-02", 110.0),
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-03", 105.0)
            );

            CreateForecastRequest request = new CreateForecastRequest();
            request.setTimeSeriesData(points);

            assertThat(request.getTimeSeriesData()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle ISO-8601 timestamp format")
        void shouldHandleIso8601TimestampFormat() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01T00:00:00Z", 100.0);

            assertThat(point.getTimestamp()).contains("T");
            assertThat(point.getTimestamp()).endsWith("Z");
        }

        @Test
        @DisplayName("Should handle date-only timestamp format")
        void shouldHandleDateOnlyTimestampFormat() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 100.0);

            assertThat(point.getTimestamp()).isEqualTo("2024-01-01");
        }

        @Test
        @DisplayName("Should handle very small decimal values")
        void shouldHandleVerySmallDecimalValues() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 0.001);

            assertThat(point.getValue()).isEqualTo(0.001);
        }

        @Test
        @DisplayName("Should handle very large values")
        void shouldHandleVeryLargeValues() {
            CreateForecastRequest.TimeSeriesPoint point =
                    new CreateForecastRequest.TimeSeriesPoint("2024-01-01", 999999.99);

            assertThat(point.getValue()).isEqualTo(999999.99);
        }
    }
}
