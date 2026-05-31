package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MetricIdTest {

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void of_createsMetricId() {
            MetricId id = MetricId.of("test-id");
            assertEquals("test-id", id.getValue());
        }

        @Test
        void generate_createsUUIDFormatId() {
            MetricId id = MetricId.generate();
            assertTrue(id.isUUIDFormat());
        }

        @Test
        void fromComponents_createsCompositeId() {
            MetricId id = MetricId.fromComponents("Revenue", "North America", "Daily");
            assertTrue(id.isComposite());
        }
    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {
        @Test
        void of_withNull_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> MetricId.of(null));
        }

        @Test
        void of_withEmpty_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> MetricId.of(""));
        }

        @Test
        void of_withTooLong_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> MetricId.of("a".repeat(256)));
        }

        @Test
        void of_withWhitespace_trimsValue() {
            MetricId id = MetricId.of("  test  ");
            assertEquals("test", id.getValue());
        }
    }

    @Nested
    @DisplayName("Composite ID tests")
    class CompositeTests {
        @Test
        void isComposite_withUnderscore_returnsTrue() {
            assertTrue(MetricId.of("a_b").isComposite());
        }

        @Test
        void isComposite_withoutUnderscore_returnsFalse() {
            assertFalse(MetricId.of("abc").isComposite());
        }

        @Test
        void extractMetricType_returnsFirstPart() {
            MetricId id = MetricId.of("revenue_north_daily");
            assertEquals("revenue", id.extractMetricType());
        }

        @Test
        void extractMetricType_nonComposite_returnsNull() {
            assertNull(MetricId.of("simple").extractMetricType());
        }

        @Test
        void extractDimension_returnsSecondPart() {
            MetricId id = MetricId.of("revenue_north_daily");
            assertEquals("north", id.extractDimension());
        }

        @Test
        void extractDimension_nonComposite_returnsNull() {
            assertNull(MetricId.of("simple").extractDimension());
        }
    }

    @Test
    void equalsAndHashCode_sameValue_equal() {
        MetricId id1 = MetricId.of("test");
        MetricId id2 = MetricId.of("test");
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equals_differentValue_notEqual() {
        assertNotEquals(MetricId.of("a"), MetricId.of("b"));
    }

    @Test
    void toString_returnsValue() {
        assertEquals("test", MetricId.of("test").toString());
    }
}
