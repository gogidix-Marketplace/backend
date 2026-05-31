package com.gogidix.dashboard.shared.util;

import com.gogidix.dashboard.shared.constants.DashboardConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KPICalculatorTest {

    @Nested
    @DisplayName("sum tests")
    class SumTests {

        @Test
        void sum_withValidIntegers_returnsCorrectSum() {
            assertEquals(15.0, KPICalculator.sum(Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void sum_withValidDoubles_returnsCorrectSum() {
            assertEquals(15.5, KPICalculator.sum(Arrays.asList(1.5, 2.5, 3.5, 4.0, 4.0)));
        }

        @Test
        void sum_withNullList_returnsZero() {
            assertEquals(0.0, KPICalculator.sum(null));
        }

        @Test
        void sum_withEmptyList_returnsZero() {
            assertEquals(0.0, KPICalculator.sum(Collections.emptyList()));
        }

        @Test
        void sum_withSingleValue_returnsThatValue() {
            assertEquals(42.0, KPICalculator.sum(List.of(42)));
        }
    }

    @Nested
    @DisplayName("average tests")
    class AverageTests {

        @Test
        void average_withValidValues_returnsCorrectAverage() {
            assertEquals(3.0, KPICalculator.average(Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void average_withNullList_returnsZero() {
            assertEquals(0.0, KPICalculator.average(null));
        }

        @Test
        void average_withEmptyList_returnsZero() {
            assertEquals(0.0, KPICalculator.average(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("min tests")
    class MinTests {

        @Test
        void min_withValidValues_returnsMin() {
            assertEquals(1.0, KPICalculator.min(Arrays.asList(5, 3, 1, 4, 2)));
        }

        @Test
        void min_withNullList_returnsZero() {
            assertEquals(0.0, KPICalculator.min(null));
        }

        @Test
        void min_withEmptyList_returnsZero() {
            assertEquals(0.0, KPICalculator.min(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("max tests")
    class MaxTests {

        @Test
        void max_withValidValues_returnsMax() {
            assertEquals(5.0, KPICalculator.max(Arrays.asList(1, 3, 5, 2, 4)));
        }

        @Test
        void max_withNullList_returnsZero() {
            assertEquals(0.0, KPICalculator.max(null));
        }

        @Test
        void max_withEmptyList_returnsZero() {
            assertEquals(0.0, KPICalculator.max(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("count tests")
    class CountTests {

        @Test
        void count_withValidList_returnsSize() {
            assertEquals(5L, KPICalculator.count(Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void count_withNullList_returnsZero() {
            assertEquals(0L, KPICalculator.count(null));
        }

        @Test
        void count_withEmptyList_returnsZero() {
            assertEquals(0L, KPICalculator.count(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("distinctCount tests")
    class DistinctCountTests {

        @Test
        void distinctCount_withDuplicates_returnsDistinctCount() {
            assertEquals(3L, KPICalculator.distinctCount(Arrays.asList(1, 2, 2, 3, 3, 3)));
        }

        @Test
        void distinctCount_withNullList_returnsZero() {
            assertEquals(0L, KPICalculator.distinctCount(null));
        }

        @Test
        void distinctCount_withEmptyList_returnsZero() {
            assertEquals(0L, KPICalculator.distinctCount(Collections.emptyList()));
        }
    }

    @Nested
    @DisplayName("percentageChange tests")
    class PercentageChangeTests {

        @Test
        void percentageChange_withValidValues_returnsCorrectPercentage() {
            assertEquals(50.0, KPICalculator.percentageChange(100.0, 150.0));
        }

        @Test
        void percentageChange_withNegativeChange_returnsNegative() {
            assertEquals(-50.0, KPICalculator.percentageChange(100.0, 50.0));
        }

        @Test
        void percentageChange_withNullOldValue_returnsNull() {
            assertNull(KPICalculator.percentageChange(null, 150.0));
        }

        @Test
        void percentageChange_withNullNewValue_returnsNull() {
            assertNull(KPICalculator.percentageChange(100.0, null));
        }

        @Test
        void percentageChange_withZeroOldValue_returnsNull() {
            assertNull(KPICalculator.percentageChange(0.0, 150.0));
        }
    }

    @Nested
    @DisplayName("percentageOfTarget tests")
    class PercentageOfTargetTests {

        @Test
        void percentageOfTarget_withValidValues_returnsCorrectPercentage() {
            assertEquals(150.0, KPICalculator.percentageOfTarget(150.0, 100.0));
        }

        @Test
        void percentageOfTarget_withNullValue_returnsNull() {
            assertNull(KPICalculator.percentageOfTarget(null, 100.0));
        }

        @Test
        void percentageOfTarget_withNullTarget_returnsNull() {
            assertNull(KPICalculator.percentageOfTarget(150.0, null));
        }

        @Test
        void percentageOfTarget_withZeroTarget_returnsNull() {
            assertNull(KPICalculator.percentageOfTarget(150.0, 0.0));
        }
    }

    @Nested
    @DisplayName("aggregate tests")
    class AggregateTests {

        @Test
        void aggregate_withSumType_returnsSum() {
            assertEquals(15.0, KPICalculator.aggregate("SUM", Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void aggregate_withAvgType_returnsAverage() {
            assertEquals(3.0, KPICalculator.aggregate("AVG", Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void aggregate_withMinType_returnsMin() {
            assertEquals(1.0, KPICalculator.aggregate("MIN", Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void aggregate_withMaxType_returnsMax() {
            assertEquals(5.0, KPICalculator.aggregate("MAX", Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void aggregate_withCountType_returnsCount() {
            assertEquals(5.0, KPICalculator.aggregate("COUNT", Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void aggregate_withDistinctCountType_returnsDistinctCount() {
            assertEquals(3.0, KPICalculator.aggregate("DISTINCT_COUNT", Arrays.asList(1, 1, 2, 2, 3)));
        }

        @Test
        void aggregate_withUnknownType_defaultsToSum() {
            assertEquals(15.0, KPICalculator.aggregate("UNKNOWN", Arrays.asList(1, 2, 3, 4, 5)));
        }

        @Test
        void aggregate_withNullValues_returnsZero() {
            assertEquals(0.0, KPICalculator.aggregate("SUM", null));
        }

        @Test
        void aggregate_withEmptyValues_returnsZero() {
            assertEquals(0.0, KPICalculator.aggregate("SUM", Collections.emptyList()));
        }

        @Test
        void aggregate_withLowercaseType_works() {
            assertEquals(15.0, KPICalculator.aggregate("sum", Arrays.asList(1, 2, 3, 4, 5)));
        }
    }

    @Nested
    @DisplayName("calculateTrend tests")
    class CalculateTrendTests {

        @Test
        void calculateTrend_withIncrease_returnsUp() {
            assertEquals("UP", KPICalculator.calculateTrend(20.0, 10.0));
        }

        @Test
        void calculateTrend_withDecrease_returnsDown() {
            assertEquals("DOWN", KPICalculator.calculateTrend(10.0, 20.0));
        }

        @Test
        void calculateTrend_withSameValues_returnsStable() {
            assertEquals("STABLE", KPICalculator.calculateTrend(10.0, 10.0));
        }

        @Test
        void calculateTrend_withNullCurrent_returnsUnknown() {
            assertEquals("UNKNOWN", KPICalculator.calculateTrend(null, 10.0));
        }

        @Test
        void calculateTrend_withNullPrevious_returnsUnknown() {
            assertEquals("UNKNOWN", KPICalculator.calculateTrend(10.0, null));
        }
    }

    @Nested
    @DisplayName("groupAndAggregate tests")
    class GroupAndAggregateTests {

        @Test
        void groupAndAggregate_withValidInput_returnsGroupedResults() {
            Map<String, List<? extends Number>> grouped = new LinkedHashMap<>();
            grouped.put("A", Arrays.asList(10, 20, 30));
            grouped.put("B", Arrays.asList(5, 15));

            Map<String, Double> result = KPICalculator.groupAndAggregate(grouped, "SUM");

            assertEquals(60.0, result.get("A"));
            assertEquals(20.0, result.get("B"));
        }

        @Test
        void groupAndAggregate_withAvg_returnsAverages() {
            Map<String, List<? extends Number>> grouped = new LinkedHashMap<>();
            grouped.put("A", Arrays.asList(10, 20, 30));

            Map<String, Double> result = KPICalculator.groupAndAggregate(grouped, "AVG");

            assertEquals(20.0, result.get("A"));
        }
    }

    @Nested
    @DisplayName("movingAverage tests")
    class MovingAverageTests {

        @Test
        void movingAverage_withValidInput_returnsCorrectAverages() {
            List<Double> values = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0);
            List<Double> result = KPICalculator.movingAverage(values, 3);

            assertEquals(5, result.size());
            assertEquals(1.0, result.get(0));
            assertEquals(1.5, result.get(1));
            assertEquals(2.0, result.get(2));
        }

        @Test
        void movingAverage_withNullValues_returnsEmptyList() {
            assertTrue(KPICalculator.movingAverage(null, 3).isEmpty());
        }

        @Test
        void movingAverage_withEmptyValues_returnsEmptyList() {
            assertTrue(KPICalculator.movingAverage(Collections.emptyList(), 3).isEmpty());
        }

        @Test
        void movingAverage_withZeroWindow_returnsEmptyList() {
            assertTrue(KPICalculator.movingAverage(Arrays.asList(1.0, 2.0), 0).isEmpty());
        }

        @Test
        void movingAverage_withNegativeWindow_returnsEmptyList() {
            assertTrue(KPICalculator.movingAverage(Arrays.asList(1.0, 2.0), -1).isEmpty());
        }
    }

    @Nested
    @DisplayName("calculateCAGR tests")
    class CalculateCAGRTests {

        @Test
        void calculateCAGR_withValidInput_returnsCorrectCAGR() {
            Double result = KPICalculator.calculateCAGR(100.0, 121.0, 2);
            assertNotNull(result);
            assertEquals(10.0, result, 0.01);
        }

        @Test
        void calculateCAGR_withNullStartValue_returnsNull() {
            assertNull(KPICalculator.calculateCAGR(null, 200.0, 2));
        }

        @Test
        void calculateCAGR_withNullEndValue_returnsNull() {
            assertNull(KPICalculator.calculateCAGR(100.0, null, 2));
        }

        @Test
        void calculateCAGR_withZeroStartValue_returnsNull() {
            assertNull(KPICalculator.calculateCAGR(0.0, 200.0, 2));
        }

        @Test
        void calculateCAGR_withNegativeStartValue_returnsNull() {
            assertNull(KPICalculator.calculateCAGR(-100.0, 200.0, 2));
        }

        @Test
        void calculateCAGR_withZeroPeriods_returnsNull() {
            assertNull(KPICalculator.calculateCAGR(100.0, 200.0, 0));
        }

        @Test
        void calculateCAGR_withNegativePeriods_returnsNull() {
            assertNull(KPICalculator.calculateCAGR(100.0, 200.0, -1));
        }
    }
}
