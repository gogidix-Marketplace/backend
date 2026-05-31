package com.gogidix.dashboard.performance.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetricIdTest {

    @Test
    void generate_createsNonNullId() {
        MetricId id = MetricId.generate();
        assertNotNull(id);
        assertNotNull(id.getValue());
        assertFalse(id.getValue().isEmpty());
    }

    @Test
    void of_createsIdFromValue() {
        MetricId id = MetricId.of("test-id-123");
        assertEquals("test-id-123", id.getValue());
    }

    @Test
    void fromString_createsIdFromValue() {
        MetricId id = MetricId.fromString("my-metric");
        assertEquals("my-metric", id.getValue());
    }

    @Test
    void of_nullValue_throwsException() {
        assertThrows(NullPointerException.class, () -> MetricId.of(null));
    }

    @Test
    void of_emptyValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> MetricId.of(""));
    }

    @Test
    void of_blankValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> MetricId.of("   "));
    }

    @Test
    void equals_sameValue() {
        MetricId id1 = MetricId.of("abc");
        MetricId id2 = MetricId.of("abc");
        assertEquals(id1, id2);
    }

    @Test
    void equals_differentValue() {
        MetricId id1 = MetricId.of("abc");
        MetricId id2 = MetricId.of("def");
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_sameValue() {
        MetricId id1 = MetricId.of("test");
        MetricId id2 = MetricId.of("test");
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void toString_returnsValue() {
        MetricId id = MetricId.of("my-id");
        assertEquals("my-id", id.toString());
    }

    @Test
    void generate_createsUniqueIds() {
        MetricId id1 = MetricId.generate();
        MetricId id2 = MetricId.generate();
        assertNotEquals(id1, id2);
    }
}
