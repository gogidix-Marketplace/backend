package com.gogidix.aiservices.aianalyticsdashboard.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Metric Domain Model Tests")
class MetricTest {

    private static final String VALID_NAME = "total_revenue";
    private static final double VALID_VALUE = 100000.0;

    @Nested
    @DisplayName("Metric Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create metric with valid parameters")
        void shouldCreateWithValidParameters() {
            Metric metric = Metric.create(VALID_NAME, VALID_VALUE);

            assertThat(metric).isNotNull();
            assertThat(metric.getName()).isEqualTo(VALID_NAME);
            assertThat(metric.getValue()).isEqualTo(VALID_VALUE);
            assertThat(metric.getTimestamp()).isNotNull();
        }

        @Test
        @DisplayName("Should reject null name")
        void shouldRejectNullName() {
            assertThatThrownBy(() -> Metric.create(null, VALID_VALUE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be null");
        }

        @Test
        @DisplayName("Should reject empty name")
        void shouldRejectEmptyName() {
            assertThatThrownBy(() -> Metric.create("", VALID_VALUE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("name cannot be empty");
        }

        @Test
        @DisplayName("Should accept negative values")
        void shouldAcceptNegativeValues() {
            Metric metric = Metric.create(VALID_NAME, -1000.0);

            assertThat(metric.getValue()).isEqualTo(-1000.0);
        }
    }

    @Nested
    @DisplayName("Trend Calculation Tests")
    class TrendCalculationTests {

        @Test
        @DisplayName("Should detect upward trend")
        void shouldDetectUpwardTrend() {
            Metric metric = Metric.create(VALID_NAME, 100.0);
            metric.setPreviousValue(80.0);

            metric.calculateTrend();

            assertThat(metric.getTrend()).isEqualTo(Trend.UP);
            assertThat(metric.getChange()).isPositive();
        }

        @Test
        @DisplayName("Should detect downward trend")
        void shouldDetectDownwardTrend() {
            Metric metric = Metric.create(VALID_NAME, 80.0);
            metric.setPreviousValue(100.0);

            metric.calculateTrend();

            assertThat(metric.getTrend()).isEqualTo(Trend.DOWN);
            assertThat(metric.getChange()).isNegative();
        }

        @Test
        @DisplayName("Should detect stable trend when change is small")
        void shouldDetectStableTrend() {
            Metric metric = Metric.create(VALID_NAME, 102.0);
            metric.setPreviousValue(100.0);

            metric.calculateTrend();

            assertThat(metric.getTrend()).isEqualTo(Trend.STABLE);
        }

        @ParameterizedTest
        @CsvSource({"90.0, 100.0, DOWN", "110.0, 100.0, UP", "101.0, 100.0, STABLE"})
        @DisplayName("Should calculate trend correctly")
        void shouldCalculateTrend(double current, double previous, Trend expectedTrend) {
            Metric metric = Metric.create(VALID_NAME, current);
            metric.setPreviousValue(previous);

            metric.calculateTrend();

            assertThat(metric.getTrend()).isEqualTo(expectedTrend);
        }
    }

    @Nested
    @DisplayName("Percentage Change Tests")
    class PercentageChangeTests {

        @Test
        @DisplayName("Should calculate positive percentage change")
        void shouldCalculatePositiveChange() {
            Metric metric = Metric.create(VALID_NAME, 150.0);
            metric.setPreviousValue(100.0);

            metric.calculateTrend();

            assertThat(metric.getChange()).isEqualTo(50.0);
        }

        @Test
        @DisplayName("Should calculate negative percentage change")
        void shouldCalculateNegativeChange() {
            Metric metric = Metric.create(VALID_NAME, 80.0);
            metric.setPreviousValue(100.0);

            metric.calculateTrend();

            assertThat(metric.getChange()).isEqualTo(-20.0);
        }

        @Test
        @DisplayName("Should handle zero previous value")
        void shouldHandleZeroPreviousValue() {
            Metric metric = Metric.create(VALID_NAME, 100.0);
            metric.setPreviousValue(0.0);

            metric.calculateTrend();

            assertThat(metric.getChange()).isEqualTo(0.0);
            assertThat(metric.getTrend()).isEqualTo(Trend.STABLE);
        }
    }

    @Nested
    @DisplayName("Time Series Tests")
    class TimeSeriesTests {

        @Test
        @DisplayName("Should add time series data point")
        void shouldAddTimeSeriesPoint() {
            Metric metric = Metric.create(VALID_NAME, 100.0);
            Instant timestamp = Instant.now();

            metric.addTimeSeriesPoint(timestamp, 105.0);

            assertThat(metric.getTimeSeries()).hasSize(1);
            assertThat(metric.getTimeSeries().get(0).value()).isEqualTo(105.0);
        }

        @Test
        @DisplayName("Should limit time series history")
        void shouldLimitTimeSeriesHistory() {
            Metric metric = Metric.create(VALID_NAME, 100.0);

            for (int i = 0; i < 1100; i++) {
                metric.addTimeSeriesPoint(Instant.now().plusSeconds(i), (double) i);
            }

            assertThat(metric.getTimeSeries()).hasSize(1000);
        }
    }

    @Nested
    @DisplayName("Aggregation Tests")
    class AggregationTests {

        @Test
        @DisplayName("Should calculate average from time series")
        void shouldCalculateAverage() {
            Metric metric = Metric.create(VALID_NAME, 0.0);
            metric.addTimeSeriesPoint(Instant.now(), 10.0);
            metric.addTimeSeriesPoint(Instant.now(), 20.0);
            metric.addTimeSeriesPoint(Instant.now(), 30.0);

            double average = metric.calculateAverage();

            assertThat(average).isEqualTo(20.0);
        }

        @Test
        @DisplayName("Should calculate sum from time series")
        void shouldCalculateSum() {
            Metric metric = Metric.create(VALID_NAME, 0.0);
            metric.addTimeSeriesPoint(Instant.now(), 10.0);
            metric.addTimeSeriesPoint(Instant.now(), 20.0);
            metric.addTimeSeriesPoint(Instant.now(), 30.0);

            double sum = metric.calculateSum();

            assertThat(sum).isEqualTo(60.0);
        }

        @Test
        @DisplayName("Should find max value")
        void shouldFindMax() {
            Metric metric = Metric.create(VALID_NAME, 0.0);
            metric.addTimeSeriesPoint(Instant.now(), 10.0);
            metric.addTimeSeriesPoint(Instant.now(), 50.0);
            metric.addTimeSeriesPoint(Instant.now(), 30.0);

            double max = metric.calculateMax();

            assertThat(max).isEqualTo(50.0);
        }

        @Test
        @DisplayName("Should find min value")
        void shouldFindMin() {
            Metric metric = Metric.create(VALID_NAME, 0.0);
            metric.addTimeSeriesPoint(Instant.now(), 10.0);
            metric.addTimeSeriesPoint(Instant.now(), 5.0);
            metric.addTimeSeriesPoint(Instant.now(), 30.0);

            double min = metric.calculateMin();

            assertThat(min).isEqualTo(5.0);
        }
    }
}
