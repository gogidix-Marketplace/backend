package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MetricValueTest {

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void of_doubleAndUnit_createsValue() {
            MetricValue mv = MetricValue.of(42.5, "kg");
            assertTrue(mv.isAvailable());
            assertEquals(42.5, mv.getNumericValue());
            assertEquals("kg", mv.getUnit());
        }

        @Test
        void of_nullDouble_returnsUnavailable() {
            MetricValue mv = MetricValue.of((Double) null, "kg");
            assertFalse(mv.isAvailable());
        }

        @Test
        void of_long_createsValue() {
            MetricValue mv = MetricValue.of(100L, "items");
            assertTrue(mv.isAvailable());
            assertEquals(100.0, mv.getNumericValue());
        }

        @Test
        void of_nullLong_returnsUnavailable() {
            MetricValue mv = MetricValue.of((Long) null, "items");
            assertFalse(mv.isAvailable());
        }

        @Test
        void percentage_createsValue() {
            MetricValue mv = MetricValue.percentage(75.5);
            assertEquals(75.5, mv.getNumericValue());
            assertEquals("%", mv.getUnit());
        }

        @Test
        void percentage_outOfRange_throws() {
            assertThrows(IllegalArgumentException.class, () -> MetricValue.percentage(150.0));
        }

        @Test
        void count_createsValue() {
            MetricValue mv = MetricValue.count(50L);
            assertEquals(50.0, mv.getNumericValue());
        }

        @Test
        void count_negative_throws() {
            assertThrows(IllegalArgumentException.class, () -> MetricValue.count(-1L));
        }

        @Test
        void currency_createsValue() {
            MetricValue mv = MetricValue.currency(99.99, "USD");
            assertEquals(99.99, mv.getNumericValue());
        }

        @Test
        void currency_invalidCode_throws() {
            assertThrows(IllegalArgumentException.class, () -> MetricValue.currency(10.0, "US"));
        }

        @Test
        void duration_createsValue() {
            MetricValue mv = MetricValue.duration(1500.0);
            assertEquals(1500.0, mv.getNumericValue());
            assertEquals("ms", mv.getUnit());
        }

        @Test
        void rate_createsValue() {
            MetricValue mv = MetricValue.rate(100.0, "req");
            assertEquals("req/s", mv.getUnit());
        }

        @Test
        void unavailable_createsUnavailableValue() {
            MetricValue mv = MetricValue.unavailable();
            assertFalse(mv.isAvailable());
            assertFalse(mv.isValid());
        }

        @Test
        void of_infiniteValue_throws() {
            assertThrows(IllegalArgumentException.class, () -> MetricValue.of(Double.POSITIVE_INFINITY, "x"));
        }

        @Test
        void of_nanValue_throws() {
            assertThrows(IllegalArgumentException.class, () -> MetricValue.of(Double.NaN, "x"));
        }

        @Test
        void of_longUnit_throws() {
            assertThrows(IllegalArgumentException.class, () -> MetricValue.of(1.0, "a".repeat(21)));
        }
    }

    @Nested
    @DisplayName("Value check tests")
    class ValueCheckTests {
        @Test
        void isZero_trueForZero() {
            assertTrue(MetricValue.of(0.0, "u").isZero());
        }

        @Test
        void isPositive_trueForPositive() {
            assertTrue(MetricValue.of(5.0, "u").isPositive());
        }

        @Test
        void isNegative_trueForNegative() {
            assertTrue(MetricValue.of(-5.0, "u").isNegative());
        }

        @Test
        void isGreaterThan_works() {
            MetricValue a = MetricValue.of(10.0, "u");
            MetricValue b = MetricValue.of(5.0, "u");
            assertTrue(a.isGreaterThan(b));
            assertFalse(b.isGreaterThan(a));
        }

        @Test
        void isLessThan_works() {
            MetricValue a = MetricValue.of(5.0, "u");
            MetricValue b = MetricValue.of(10.0, "u");
            assertTrue(a.isLessThan(b));
        }

        @Test
        void comparison_withUnavailable_returnsFalse() {
            MetricValue a = MetricValue.of(10.0, "u");
            MetricValue b = MetricValue.unavailable();
            assertFalse(a.isGreaterThan(b));
            assertFalse(a.isLessThan(b));
        }
    }

    @Nested
    @DisplayName("Normal range tests")
    class RangeTests {
        @Test
        void isOutsideNormalRange_outside_returnsTrue() {
            MetricValue.ValueRange range = new MetricValue.ValueRange(0.0, 100.0);
            MetricValue mv = MetricValue.of(150.0, "u", range, 2);
            assertTrue(mv.isOutsideNormalRange());
        }

        @Test
        void isWithinNormalRange_inside_returnsTrue() {
            MetricValue.ValueRange range = new MetricValue.ValueRange(0.0, 100.0);
            MetricValue mv = MetricValue.of(50.0, "u", range, 2);
            assertTrue(mv.isWithinNormalRange());
        }

        @Test
        void isOutsideNormalRange_noRange_returnsFalse() {
            MetricValue mv = MetricValue.of(999.0, "u");
            assertFalse(mv.isOutsideNormalRange());
        }
    }

    @Nested
    @DisplayName("Arithmetic tests")
    class ArithmeticTests {
        @Test
        void add_sameUnits_works() {
            MetricValue a = MetricValue.of(10.0, "u");
            MetricValue b = MetricValue.of(5.0, "u");
            assertEquals(15.0, a.add(b).getNumericValue());
        }

        @Test
        void add_differentUnits_throws() {
            MetricValue a = MetricValue.of(10.0, "u1");
            MetricValue b = MetricValue.of(5.0, "u2");
            assertThrows(IllegalArgumentException.class, () -> a.add(b));
        }

        @Test
        void subtract_sameUnits_works() {
            MetricValue a = MetricValue.of(10.0, "u");
            MetricValue b = MetricValue.of(3.0, "u");
            assertEquals(7.0, b.subtract(a).getNumericValue());
        }

        @Test
        void multiply_works() {
            MetricValue mv = MetricValue.of(10.0, "u");
            assertEquals(50.0, mv.multiply(5.0).getNumericValue());
        }

        @Test
        void divide_works() {
            MetricValue mv = MetricValue.of(100.0, "u");
            assertEquals(50.0, mv.divide(2.0).getNumericValue());
        }

        @Test
        void divide_byZero_returnsUnavailable() {
            MetricValue mv = MetricValue.of(100.0, "u");
            assertFalse(mv.divide(0.0).isAvailable());
        }

        @Test
        void percentageChange_works() {
            MetricValue old = MetricValue.of(100.0, "u");
            MetricValue cur = MetricValue.of(150.0, "u");
            assertEquals(50.0, cur.calculatePercentageChange(old));
        }

        @Test
        void percentageChange_zeroBaseline_returnsNull() {
            MetricValue zero = MetricValue.of(0.0, "u");
            MetricValue cur = MetricValue.of(100.0, "u");
            assertNull(cur.calculatePercentageChange(zero));
        }
    }

    @Test
    void getNumericValueOrDefault_unavailable_returnsDefault() {
        MetricValue mv = MetricValue.unavailable();
        assertEquals(42.0, mv.getNumericValueOrDefault(42.0));
    }

    @Test
    void getNumericValueOrDefault_available_returnsValue() {
        MetricValue mv = MetricValue.of(10.0, "u");
        assertEquals(10.0, mv.getNumericValueOrDefault(42.0));
    }

    @Test
    void getNumericValue_unavailable_throws() {
        assertThrows(IllegalStateException.class, () -> MetricValue.unavailable().getNumericValue());
    }

    @Test
    void formatForDisplay_available_showsValue() {
        assertEquals("10.00 kg", MetricValue.of(10.0, "kg").formatForDisplay());
    }

    @Test
    void formatForDisplay_unavailable_showsNA() {
        assertEquals("N/A", MetricValue.unavailable().formatForDisplay());
    }

    @Test
    void equalsAndHashCode_work() {
        MetricValue a = MetricValue.of(10.0, "u");
        MetricValue b = MetricValue.of(10.0, "u");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void toString_formatsForDisplay() {
        MetricValue mv = MetricValue.of(10.0, "kg");
        assertEquals(mv.formatForDisplay(), mv.toString());
    }
}
