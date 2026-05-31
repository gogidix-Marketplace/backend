package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportIdTest {

    @Test
    void generate_createsId() {
        ReportId id = ReportId.generate();
        assertNotNull(id);
        assertNotNull(id.getValue());
    }

    @Test
    void of_createsId() {
        ReportId id = ReportId.of("test-id");
        assertEquals("test-id", id.getValue());
    }

    @Test
    void fromString_createsId() {
        ReportId id = ReportId.fromString("my-id");
        assertEquals("my-id", id.getValue());
    }

    @Test
    void of_null_throws() {
        assertThrows(NullPointerException.class, () -> ReportId.of(null));
    }

    @Test
    void of_empty_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReportId.of(""));
    }

    @Test
    void of_blank_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReportId.of("   "));
    }

    @Test
    void equals_sameValue() {
        ReportId id1 = ReportId.of("abc");
        ReportId id2 = ReportId.of("abc");
        assertEquals(id1, id2);
    }

    @Test
    void equals_differentValue() {
        ReportId id1 = ReportId.of("abc");
        ReportId id2 = ReportId.of("def");
        assertNotEquals(id1, id2);
    }

    @Test
    void hashCode_same() {
        assertEquals(ReportId.of("x").hashCode(), ReportId.of("x").hashCode());
    }

    @Test
    void toString_returnsValue() {
        assertEquals("test", ReportId.of("test").toString());
    }

    @Test
    void generate_uniqueIds() {
        assertNotEquals(ReportId.generate(), ReportId.generate());
    }
}
