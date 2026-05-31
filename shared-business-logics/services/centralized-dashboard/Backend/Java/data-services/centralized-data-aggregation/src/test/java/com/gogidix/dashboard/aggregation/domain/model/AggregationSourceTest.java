package com.gogidix.dashboard.aggregation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AggregationSourceTest {

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void database_createsDatabaseSource() {
            AggregationSource src = AggregationSource.database("db1", "Main DB", "jdbc:pg://localhost/db");
            assertEquals("db1", src.getSourceId());
            assertEquals("Main DB", src.getSourceName());
            assertEquals(AggregationSource.SourceType.DATABASE, src.getType());
            assertTrue(src.isReliable());
        }

        @Test
        void api_createsApiSource() {
            AggregationSource src = AggregationSource.api("api1", "REST API", "http://localhost/api");
            assertEquals(AggregationSource.SourceType.API, src.getType());
            assertTrue(src.isReliable());
        }

        @Test
        void stream_createsStreamSource() {
            AggregationSource src = AggregationSource.stream("s1", "Kafka Stream");
            assertEquals(AggregationSource.SourceType.STREAM, src.getType());
            assertFalse(src.isReliable());
        }
    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {
        @Test
        void constructor_nullSourceId_throws() {
            assertThrows(NullPointerException.class, () ->
                new AggregationSource(null, "name", AggregationSource.SourceType.DATABASE, "conn", LocalDateTime.now(), true));
        }

        @Test
        void constructor_nullSourceName_throws() {
            assertThrows(NullPointerException.class, () ->
                new AggregationSource("id", null, AggregationSource.SourceType.DATABASE, "conn", LocalDateTime.now(), true));
        }

        @Test
        void constructor_nullType_throws() {
            assertThrows(NullPointerException.class, () ->
                new AggregationSource("id", "name", null, "conn", LocalDateTime.now(), true));
        }

        @Test
        void constructor_nullLastUpdated_throws() {
            assertThrows(NullPointerException.class, () ->
                new AggregationSource("id", "name", AggregationSource.SourceType.DATABASE, "conn", null, true));
        }
    }

    @Test
    void equals_sameId_equal() {
        AggregationSource s1 = AggregationSource.database("id1", "Name1", "c1");
        AggregationSource s2 = AggregationSource.database("id1", "Name2", "c2");
        assertEquals(s1, s2);
    }

    @Test
    void equals_differentId_notEqual() {
        AggregationSource s1 = AggregationSource.database("id1", "Name", "c");
        AggregationSource s2 = AggregationSource.database("id2", "Name", "c");
        assertNotEquals(s1, s2);
    }

    @Test
    void hashCode_sameId_sameHash() {
        AggregationSource s1 = AggregationSource.database("id1", "N1", "c1");
        AggregationSource s2 = AggregationSource.database("id1", "N2", "c2");
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void sourceType_hasAllValues() {
        assertEquals(5, AggregationSource.SourceType.values().length);
        assertNotNull(AggregationSource.SourceType.DATABASE.getDisplayName());
    }
}
