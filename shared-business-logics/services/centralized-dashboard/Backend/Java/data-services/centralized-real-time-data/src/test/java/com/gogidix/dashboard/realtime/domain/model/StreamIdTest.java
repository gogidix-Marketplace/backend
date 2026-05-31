package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamIdTest {

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void generate_createsUniqueIds() {
            StreamId id1 = StreamId.generate();
            StreamId id2 = StreamId.generate();
            assertNotEquals(id1, id2);
        }

        @Test
        void of_createsStreamId() {
            StreamId id = StreamId.of("test-id");
            assertEquals("test-id", id.getValue());
        }

        @Test
        void fromString_createsStreamId() {
            StreamId id = StreamId.fromString("my-stream");
            assertEquals("my-stream", id.getValue());
        }
    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {
        @Test
        void of_nullValue_throws() {
            assertThrows(NullPointerException.class, () -> StreamId.of(null));
        }

        @Test
        void of_emptyValue_throws() {
            assertThrows(IllegalArgumentException.class, () -> StreamId.of(""));
        }

        @Test
        void of_whitespace_throws() {
            assertThrows(IllegalArgumentException.class, () -> StreamId.of("   "));
        }
    }

    @Test
    void equals_sameValue_equal() {
        assertEquals(StreamId.of("a"), StreamId.of("a"));
    }

    @Test
    void equals_differentValue_notEqual() {
        assertNotEquals(StreamId.of("a"), StreamId.of("b"));
    }

    @Test
    void hashCode_sameValue_sameHash() {
        assertEquals(StreamId.of("test").hashCode(), StreamId.of("test").hashCode());
    }

    @Test
    void toString_returnsValue() {
        assertEquals("test", StreamId.of("test").toString());
    }
}
