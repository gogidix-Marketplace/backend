package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AggregatedMetricTest {

    private MetricId id;
    private MetricValue value;
    private TimeWindow timeWindow;
    private DataQuality dataQuality;
    private AggregationSource source;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        id = MetricId.generate();
        value = MetricValue.of(100.0, "USD");
        timeWindow = new TimeWindow(now.minusHours(1), now, TimeGranularity.HOURLY);
        dataQuality = DataQuality.excellent(100);
        source = AggregationSource.database("db1", "MainDB", "jdbc:pg://localhost/db");
    }

    private AggregatedMetric createMetric() {
        return new AggregatedMetric(id, MetricType.BUSINESS_KPI, "Revenue", "revenue",
                value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
    }

    @Nested
    @DisplayName("Constructor validation tests")
    class ValidationTests {
        @Test
        void constructor_withAllFields_works() {
            AggregatedMetric m = createMetric();
            assertEquals(id, m.getId());
            assertEquals("Revenue", m.getName());
            assertEquals("revenue", m.getDimension());
        }

        @Test
        void constructor_nullId_throws() {
            assertThrows(NullPointerException.class, () ->
                new AggregatedMetric(null, MetricType.BUSINESS_KPI, "n", "d",
                    value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source));
        }

        @Test
        void constructor_nullName_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new AggregatedMetric(id, MetricType.BUSINESS_KPI, null, "d",
                    value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source));
        }

        @Test
        void constructor_emptyName_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new AggregatedMetric(id, MetricType.BUSINESS_KPI, "  ", "d",
                    value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source));
        }

        @Test
        void constructor_nameTooLong_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new AggregatedMetric(id, MetricType.BUSINESS_KPI, "n".repeat(256), "d",
                    value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source));
        }

        @Test
        void constructor_nullDimension_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new AggregatedMetric(id, MetricType.BUSINESS_KPI, "n", null,
                    value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source));
        }

        @Test
        void constructor_dimensionTooLong_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new AggregatedMetric(id, MetricType.BUSINESS_KPI, "n", "d".repeat(101),
                    value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source));
        }

        @Test
        void constructor_nullAttributes_usesEmptyMap() {
            AggregatedMetric m = new AggregatedMetric(id, MetricType.BUSINESS_KPI, "n", "d",
                value, timeWindow, AggregationFunction.SUM, now, null, dataQuality, source);
            assertTrue(m.getAttributes().isEmpty());
        }
    }

    @Nested
    @DisplayName("Business method tests")
    class BusinessMethodTests {
        @Test
        void isSuitableForAlerting_highQualityRecentValid_returnsTrue() {
            AggregatedMetric m = createMetric();
            assertTrue(m.isSuitableForAlerting());
        }

        @Test
        void requiresImmediateAttention_outsideRangeHighConfidence() {
            MetricValue.ValueRange range = new MetricValue.ValueRange(0.0, 50.0);
            MetricValue outOfRange = MetricValue.of(150.0, "USD", range, 2);
            DataQuality highConf = new DataQuality(1.0, 1.0, 1.0, 1.0, 1.0, 200, 200, 0, LocalDateTime.now());
            AggregatedMetric m = new AggregatedMetric(id, MetricType.BUSINESS_KPI, "n", "revenue",
                outOfRange, timeWindow, AggregationFunction.SUM, now, Map.of(), highConf, source);
            assertTrue(m.requiresImmediateAttention());
        }

        @Test
        void isComparableWith_sameTypeDimensionFunction() {
            AggregatedMetric m1 = createMetric();
            MetricId id2 = MetricId.generate();
            TimeWindow tw2 = new TimeWindow(now.minusHours(2), now.minusHours(1), TimeGranularity.HOURLY);
            AggregatedMetric m2 = new AggregatedMetric(id2, MetricType.BUSINESS_KPI, "Rev2", "revenue",
                value, tw2, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
            assertTrue(m1.isComparableWith(m2));
        }

        @Test
        void isComparableWith_differentType_returnsFalse() {
            AggregatedMetric m1 = createMetric();
            MetricId id2 = MetricId.generate();
            AggregatedMetric m2 = new AggregatedMetric(id2, MetricType.TECHNICAL, "n", "revenue",
                value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
            assertFalse(m1.isComparableWith(m2));
        }

        @Test
        void calculatePercentageChange_comparableMetrics() {
            AggregatedMetric m1 = createMetric();
            MetricId id2 = MetricId.generate();
            MetricValue val2 = MetricValue.of(150.0, "USD");
            TimeWindow tw2 = new TimeWindow(now.minusHours(2), now.minusHours(1), TimeGranularity.HOURLY);
            AggregatedMetric m2 = new AggregatedMetric(id2, MetricType.BUSINESS_KPI, "n", "revenue",
                val2, tw2, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
            Double change = m2.calculatePercentageChange(m1);
            assertNotNull(change);
            assertEquals(50.0, change, 0.01);
        }

        @Test
        void showsSignificantChange_aboveThreshold() {
            AggregatedMetric baseline = createMetric();
            MetricId id2 = MetricId.generate();
            MetricValue val2 = MetricValue.of(200.0, "USD");
            TimeWindow tw2 = new TimeWindow(now.minusHours(2), now.minusHours(1), TimeGranularity.HOURLY);
            AggregatedMetric current = new AggregatedMetric(id2, MetricType.BUSINESS_KPI, "n", "revenue",
                val2, tw2, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
            assertTrue(current.showsSignificantChange(baseline, 50.0));
        }

        @Test
        void needsReAggregation_whenNotCurrent() {
            TimeWindow oldWindow = new TimeWindow(now.minusDays(2), now.minusDays(2).plusHours(1), TimeGranularity.HOURLY);
            AggregatedMetric m = new AggregatedMetric(id, MetricType.BUSINESS_KPI, "n", "d",
                value, oldWindow, AggregationFunction.SUM, now.minusDays(2), Map.of(), dataQuality, source);
            assertTrue(m.needsReAggregation());
        }

        @Test
        void getCriticalityLevel_businessCriticalWithRevenueDimension() {
            AggregatedMetric m = createMetric();
            assertEquals(AggregatedMetric.CriticalityLevel.HIGH, m.getCriticalityLevel());
        }

        @Test
        void getCriticalityLevel_operationalWithHighQuality() {
            AggregatedMetric m = new AggregatedMetric(id, MetricType.OPERATIONAL, "n", "ops",
                value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
            assertEquals(AggregatedMetric.CriticalityLevel.MEDIUM, m.getCriticalityLevel());
        }

        @Test
        void getCriticalityLevel_lowForOthers() {
            AggregatedMetric m = new AggregatedMetric(id, MetricType.SECURITY, "n", "other",
                value, timeWindow, AggregationFunction.SUM, now, Map.of(), dataQuality, source);
            assertEquals(AggregatedMetric.CriticalityLevel.LOW, m.getCriticalityLevel());
        }
    }

    @Nested
    @DisplayName("Immutable update tests")
    class UpdateTests {
        @Test
        void withNewValue_createsNewInstance() {
            AggregatedMetric m = createMetric();
            MetricValue newVal = MetricValue.of(200.0, "USD");
            AggregatedMetric updated = m.withNewValue(newVal, now);
            assertEquals(200.0, updated.getValue().getNumericValue());
            assertNotEquals(m, updated);
        }

        @Test
        void withAttribute_createsNewInstance() {
            AggregatedMetric m = createMetric();
            AggregatedMetric updated = m.withAttribute("key", "value");
            assertEquals("value", updated.getAttributes().get("key"));
            assertFalse(m.getAttributes().containsKey("key"));
        }
    }

    @Test
    void equals_sameId_equal() {
        AggregatedMetric m1 = createMetric();
        AggregatedMetric m2 = new AggregatedMetric(id, MetricType.TECHNICAL, "other", "dim2",
            MetricValue.of(999.0, "EUR"), timeWindow, AggregationFunction.AVERAGE, now, Map.of("k", "v"), dataQuality, source);
        assertEquals(m1, m2);
    }

    @Test
    void toString_containsName() {
        AggregatedMetric m = createMetric();
        assertTrue(m.toString().contains("Revenue"));
    }

    @Nested
    @DisplayName("CriticalityLevel tests")
    class CriticalityLevelTests {
        @Test
        void isHigherThan_works() {
            assertTrue(AggregatedMetric.CriticalityLevel.HIGH.isHigherThan(AggregatedMetric.CriticalityLevel.MEDIUM));
            assertFalse(AggregatedMetric.CriticalityLevel.LOW.isHigherThan(AggregatedMetric.CriticalityLevel.MEDIUM));
        }

        @Test
        void allValuesHaveDisplayNames() {
            for (AggregatedMetric.CriticalityLevel level : AggregatedMetric.CriticalityLevel.values()) {
                assertNotNull(level.getDisplayName());
                assertTrue(level.getPriority() > 0);
            }
        }
    }
}
