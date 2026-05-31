package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AggregationFunctionTest {

    @Test
    void allValuesHaveDisplayNames() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertNotNull(fn.getDisplayName());
            assertNotNull(fn.getDescription());
            assertNotNull(fn.getType());
        }
    }

    @Nested
    @DisplayName("Suitability tests")
    class SuitabilityTests {
        @Test
        void isSuitableForRealTime_correctSet() {
            assertTrue(AggregationFunction.SUM.isSuitableForRealTime());
            assertTrue(AggregationFunction.COUNT.isSuitableForRealTime());
            assertTrue(AggregationFunction.FIRST.isSuitableForRealTime());
            assertFalse(AggregationFunction.MEDIAN.isSuitableForRealTime());
        }

        @Test
        void isSuitableForTrendAnalysis_correctSet() {
            assertTrue(AggregationFunction.RATE.isSuitableForTrendAnalysis());
            assertFalse(AggregationFunction.MODE.isSuitableForTrendAnalysis());
        }

        @Test
        void isSuitableForAlerting_correctSet() {
            assertTrue(AggregationFunction.PERCENTILE_95.isSuitableForAlerting());
            assertFalse(AggregationFunction.MODE.isSuitableForAlerting());
        }

        @Test
        void providesStatisticalInsights_correctSet() {
            assertTrue(AggregationFunction.STANDARD_DEVIATION.providesStatisticalInsights());
            assertTrue(AggregationFunction.PERCENTILE_95.providesStatisticalInsights());
            assertFalse(AggregationFunction.SUM.providesStatisticalInsights());
        }

        @Test
        void canComputeIncrementally_correctSet() {
            assertTrue(AggregationFunction.SUM.canComputeIncrementally());
            assertTrue(AggregationFunction.COUNT.canComputeIncrementally());
            assertFalse(AggregationFunction.AVERAGE.canComputeIncrementally());
        }
    }

    @Nested
    @DisplayName("Classification tests")
    class ClassificationTests {
        @Test
        void isAdditive_correctlyClassified() {
            assertTrue(AggregationFunction.SUM.isAdditive());
            assertTrue(AggregationFunction.COUNT.isAdditive());
            assertFalse(AggregationFunction.MEDIAN.isAdditive());
        }

        @Test
        void requiresMultipleValues_correctlyClassified() {
            assertFalse(AggregationFunction.SUM.requiresMultipleValues());
            assertTrue(AggregationFunction.AVERAGE.requiresMultipleValues());
        }

        @Test
        void isBusinessKPI_correctSet() {
            assertTrue(AggregationFunction.SUM.isBusinessKPI());
            assertTrue(AggregationFunction.RATE.isBusinessKPI());
            assertFalse(AggregationFunction.MEDIAN.isBusinessKPI());
        }

        @Test
        void isTechnicalMonitoring_correctSet() {
            assertTrue(AggregationFunction.PERCENTILE_95.isTechnicalMonitoring());
            assertFalse(AggregationFunction.SUM.isTechnicalMonitoring());
        }

        @Test
        void handlesNullValues_correctSet() {
            assertTrue(AggregationFunction.SUM.handlesNullValues());
            assertFalse(AggregationFunction.MEDIAN.handlesNullValues());
        }
    }

    @Test
    void getComputationalComplexity_returnsPositive() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertTrue(fn.getComputationalComplexity() > 0);
        }
    }

    @Test
    void getMemoryRequirement_returnsPositive() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertTrue(fn.getMemoryRequirement() > 0);
        }
    }

    @Test
    void getMinimumDataPoints_returnsPositive() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertTrue(fn.getMinimumDataPoints() > 0);
        }
    }

    @Test
    void getCompatibleFunctions_returnsNonEmpty() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertFalse(fn.getCompatibleFunctions().isEmpty());
        }
    }

    @Test
    void getRecommendedGranularities_returnsNonEmpty() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertFalse(fn.getRecommendedGranularities().isEmpty());
        }
    }

    @Test
    void getDataSensitivity_returnsNonNull() {
        for (AggregationFunction fn : AggregationFunction.values()) {
            assertNotNull(fn.getDataSensitivity());
        }
    }

    @Test
    void formatResult_returnsFormatted() {
        String result = AggregationFunction.COUNT.formatResult(42.0, "items");
        assertNotNull(result);
        assertTrue(result.contains("42"));
    }

    @Test
    void toString_returnsDisplayName() {
        assertEquals("Sum", AggregationFunction.SUM.toString());
    }
}
