package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TimeGranularityTest {

    @Nested
    @DisplayName("Classification tests")
    class ClassificationTests {
        @Test
        void isRealTime_forRealTimeGranularities() {
            assertTrue(TimeGranularity.REAL_TIME.isRealTime());
            assertTrue(TimeGranularity.MINUTE.isRealTime());
            assertTrue(TimeGranularity.FIVE_MINUTE.isRealTime());
            assertFalse(TimeGranularity.HOURLY.isRealTime());
        }

        @Test
        void isHighFrequency_forHighFreqGranularities() {
            assertTrue(TimeGranularity.HOURLY.isHighFrequency());
            assertFalse(TimeGranularity.DAILY.isHighFrequency());
        }

        @Test
        void isStrategic_forLongTermGranularities() {
            assertTrue(TimeGranularity.MONTHLY.isStrategic());
            assertTrue(TimeGranularity.QUARTERLY.isStrategic());
            assertTrue(TimeGranularity.YEARLY.isStrategic());
            assertFalse(TimeGranularity.DAILY.isStrategic());
        }

        @Test
        void isOperational_forOperationalGranularities() {
            assertTrue(TimeGranularity.DAILY.isOperational());
            assertTrue(TimeGranularity.WEEKLY.isOperational());
            assertFalse(TimeGranularity.MONTHLY.isOperational());
        }
    }

    @Nested
    @DisplayName("Navigation tests")
    class NavigationTests {
        @Test
        void getNextLarger_returnsCorrectGranularity() {
            assertEquals(TimeGranularity.FIVE_MINUTE, TimeGranularity.MINUTE.getNextLarger());
            assertEquals(TimeGranularity.FIFTEEN_MINUTE, TimeGranularity.FIVE_MINUTE.getNextLarger());
            assertEquals(TimeGranularity.HOURLY, TimeGranularity.FIFTEEN_MINUTE.getNextLarger());
            assertEquals(TimeGranularity.DAILY, TimeGranularity.HOURLY.getNextLarger());
            assertEquals(TimeGranularity.WEEKLY, TimeGranularity.DAILY.getNextLarger());
            assertEquals(TimeGranularity.MONTHLY, TimeGranularity.WEEKLY.getNextLarger());
            assertEquals(TimeGranularity.YEARLY, TimeGranularity.YEARLY.getNextLarger());
        }

        @Test
        void getNextSmaller_returnsCorrectGranularity() {
            assertEquals(TimeGranularity.QUARTERLY, TimeGranularity.YEARLY.getNextSmaller());
            assertEquals(TimeGranularity.MONTHLY, TimeGranularity.QUARTERLY.getNextSmaller());
            assertEquals(TimeGranularity.REAL_TIME, TimeGranularity.REAL_TIME.getNextSmaller());
        }
    }

    @Nested
    @DisplayName("Split and aggregate tests")
    class SplitAggregateTests {
        @Test
        void canSplitInto_works() {
            assertTrue(TimeGranularity.DAILY.canSplitInto(TimeGranularity.HOURLY));
            assertFalse(TimeGranularity.HOURLY.canSplitInto(TimeGranularity.DAILY));
            assertFalse(TimeGranularity.HOURLY.canSplitInto(TimeGranularity.HOURLY));
        }

        @Test
        void canAggregateInto_works() {
            assertTrue(TimeGranularity.HOURLY.canAggregateInto(TimeGranularity.DAILY));
            assertFalse(TimeGranularity.DAILY.canAggregateInto(TimeGranularity.HOURLY));
        }
    }

    @Nested
    @DisplayName("Suitability tests")
    class SuitabilityTests {
        @Test
        void isSuitableForAlerting_forHighFrequencyOnly() {
            assertTrue(TimeGranularity.MINUTE.isSuitableForAlerting());
            assertFalse(TimeGranularity.DAILY.isSuitableForAlerting());
        }

        @Test
        void isSuitableForTrendAnalysis_forDailyAndAbove() {
            assertTrue(TimeGranularity.DAILY.isSuitableForTrendAnalysis());
            assertTrue(TimeGranularity.MONTHLY.isSuitableForTrendAnalysis());
            assertFalse(TimeGranularity.HOURLY.isSuitableForTrendAnalysis());
        }

        @Test
        void isSuitableForCapacityPlanning_forWeeklyAndAbove() {
            assertTrue(TimeGranularity.WEEKLY.isSuitableForCapacityPlanning());
            assertFalse(TimeGranularity.DAILY.isSuitableForCapacityPlanning());
        }
    }

    @Test
    void getDataPointsPerDay_returnsCorrectValues() {
        assertEquals(1440, TimeGranularity.MINUTE.getDataPointsPerDay());
        assertEquals(24, TimeGranularity.HOURLY.getDataPointsPerDay());
        assertEquals(1, TimeGranularity.DAILY.getDataPointsPerDay());
    }

    @Test
    void getStorageEfficiencyScore_higherForLargerGranularities() {
        assertTrue(TimeGranularity.YEARLY.getStorageEfficiencyScore() > TimeGranularity.MINUTE.getStorageEfficiencyScore());
    }

    @Test
    void getQueryPerformanceScore_higherForSmallerGranularities() {
        assertTrue(TimeGranularity.REAL_TIME.getQueryPerformanceScore() > TimeGranularity.YEARLY.getQueryPerformanceScore());
    }

    @Test
    void isValidDuration_withExactMatch_returnsTrue() {
        assertTrue(TimeGranularity.HOURLY.isValidDuration(Duration.ofHours(1)));
    }

    @Test
    void isValidDuration_withMultiple_returnsTrue() {
        assertTrue(TimeGranularity.HOURLY.isValidDuration(Duration.ofHours(2)));
    }

    @Test
    void getRecommendedRetentionPeriod_returnsNonNull() {
        for (TimeGranularity g : TimeGranularity.values()) {
            assertNotNull(g.getRecommendedRetentionPeriod());
        }
    }

    @Test
    void getAggregationComplexity_returnsPositive() {
        for (TimeGranularity g : TimeGranularity.values()) {
            assertTrue(g.getAggregationComplexity() > 0);
        }
    }

    @Test
    void toString_returnsDisplayName() {
        assertEquals("Hourly", TimeGranularity.HOURLY.toString());
    }

    @Test
    void allValuesHaveDisplayNames() {
        for (TimeGranularity g : TimeGranularity.values()) {
            assertNotNull(g.getDisplayName());
            assertNotNull(g.getDefaultDuration());
            assertNotNull(g.getMaxStaleness());
        }
    }
}
