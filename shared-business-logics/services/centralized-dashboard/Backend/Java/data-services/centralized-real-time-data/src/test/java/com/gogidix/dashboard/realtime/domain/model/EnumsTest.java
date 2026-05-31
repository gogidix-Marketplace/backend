package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Nested
    @DisplayName("StreamType tests")
    class StreamTypeTest {
        @Test
        void allValuesHaveDescriptions() {
            for (StreamType st : StreamType.values()) {
                assertNotNull(st.getDescription());
            }
        }

        @Test
        void requiresHighResources_forCertainTypes() {
            assertTrue(StreamType.REAL_TIME_EVENTS.requiresHighResources());
            assertTrue(StreamType.AGGREGATED_DATA.requiresHighResources());
            assertFalse(StreamType.DASHBOARD_KPI.requiresHighResources());
        }
    }

    @Nested
    @DisplayName("QualityOfService tests")
    class QoSTest {
        @Test
        void levelsAreOrdered() {
            assertTrue(QualityOfService.AT_MOST_ONCE.getLevel() < QualityOfService.AT_LEAST_ONCE.getLevel());
            assertTrue(QualityOfService.AT_LEAST_ONCE.getLevel() < QualityOfService.EXACTLY_ONCE.getLevel());
        }

        @Test
        void getMinDataQualityScore_higherForHigherLevels() {
            assertTrue(QualityOfService.EXACTLY_ONCE.getMinDataQualityScore() > QualityOfService.AT_LEAST_ONCE.getMinDataQualityScore());
            assertTrue(QualityOfService.AT_LEAST_ONCE.getMinDataQualityScore() > QualityOfService.AT_MOST_ONCE.getMinDataQualityScore());
        }

        @Test
        void getMaxLatency_lowerForHigherLevels() {
            assertTrue(QualityOfService.EXACTLY_ONCE.getMaxLatency() < QualityOfService.AT_LEAST_ONCE.getMaxLatency());
            assertTrue(QualityOfService.AT_LEAST_ONCE.getMaxLatency() < QualityOfService.AT_MOST_ONCE.getMaxLatency());
        }
    }

    @Nested
    @DisplayName("BackpressureStrategy tests")
    class BackpressureTest {
        @Test
        void allHaveThresholds() {
            for (BackpressureStrategy bs : BackpressureStrategy.values()) {
                assertTrue(bs.getThreshold() > 0);
                assertTrue(bs.getThreshold() <= 1.0);
            }
        }
    }

    @Nested
    @DisplayName("CompressionType tests")
    class CompressionTest {
        @Test
        void none_hasZeroMinSize() {
            assertEquals(0, CompressionType.NONE.getMinSizeForCompression());
        }

        @Test
        void gzip_hasPositiveMinSize() {
            assertTrue(CompressionType.GZIP.getMinSizeForCompression() > 0);
        }
    }

    @Nested
    @DisplayName("StreamHealth tests")
    class StreamHealthTest {
        @Test
        void allValuesAreAccessible() {
            assertEquals(4, StreamHealth.values().length);
        }
    }

    @Nested
    @DisplayName("DataSourceConnection tests")
    class DataSourceConnectionTest {
        @Test
        void constructor_setsFields() {
            DataSourceConnection conn = new DataSourceConnection("c1", "ws://localhost");
            assertEquals("c1", conn.getConnectionId());
            assertEquals("ws://localhost", conn.getSourceUrl());
            assertTrue(conn.isActive());
            assertTrue(conn.isConnected());
            assertNotNull(conn.getConnectedAt());
        }

        @Test
        void connect_doesNotThrow() {
            assertDoesNotThrow(() -> new DataSourceConnection("c1", "url").connect());
        }

        @Test
        void disconnect_doesNotThrow() {
            assertDoesNotThrow(() -> new DataSourceConnection("c1", "url").disconnect());
        }
    }
}
