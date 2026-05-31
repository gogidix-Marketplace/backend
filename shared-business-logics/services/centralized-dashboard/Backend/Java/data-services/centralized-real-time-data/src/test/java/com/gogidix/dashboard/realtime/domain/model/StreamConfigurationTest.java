package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamConfigurationTest {

    @Nested
    @DisplayName("Builder tests")
    class BuilderTests {
        @Test
        void builder_withDefaults_works() {
            StreamConfiguration config = new StreamConfiguration.Builder().build();
            assertEquals(100, config.getMaxSubscribers());
            assertEquals(1000, config.getMaxMessagesPerSecond());
        }

        @Test
        void builder_customValues_works() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withMaxSubscribers(500)
                    .withMaxMessagesPerSecond(5000)
                    .withBufferSize(50000)
                    .withMaxInactivityDuration(Duration.ofMinutes(5))
                    .withMaxErrorRate(50)
                    .withMaxErrorsPerSubscriber(10)
                    .build();
            assertEquals(500, config.getMaxSubscribers());
            assertEquals(5000, config.getMaxMessagesPerSecond());
        }
    }

    @Nested
    @DisplayName("Validation tests")
    class ValidationTests {
        @Test
        void zeroMaxSubscribers_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new StreamConfiguration.Builder().withMaxSubscribers(0).build());
        }

        @Test
        void zeroMaxMessagesPerSecond_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new StreamConfiguration.Builder().withMaxMessagesPerSecond(0).build());
        }

        @Test
        void zeroBufferSize_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new StreamConfiguration.Builder().withBufferSize(0).build());
        }

        @Test
        void negativeMaxErrorRate_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                new StreamConfiguration.Builder().withMaxErrorRate(-1).build());
        }
    }

    @Nested
    @DisplayName("Factory method tests")
    class FactoryTests {
        @Test
        void defaultDashboardConfig_createsConfig() {
            StreamConfiguration config = StreamConfiguration.defaultDashboardConfig();
            assertEquals(1000, config.getMaxSubscribers());
            assertEquals(10000, config.getMaxMessagesPerSecond());
            assertTrue(config.isEnableMetrics());
        }

        @Test
        void highThroughputConfig_createsConfig() {
            StreamConfiguration config = StreamConfiguration.highThroughputConfig();
            assertEquals(10000, config.getMaxSubscribers());
            assertEquals(100000, config.getMaxMessagesPerSecond());
        }
    }

    @Nested
    @DisplayName("Business method tests")
    class BusinessMethodTests {
        @Test
        void hasTransformations_default_returnsFalse() {
            assertFalse(new StreamConfiguration.Builder().build().hasTransformations());
        }

        @Test
        void hasFilters_default_returnsFalse() {
            assertFalse(new StreamConfiguration.Builder().build().hasFilters());
        }

        @Test
        void getOptimalBufferSize_returnsAtLeastBuffer() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withBufferSize(100)
                    .withMaxMessagesPerSecond(1000)
                    .build();
            assertTrue(config.getOptimalBufferSize() >= 10000); // 1000 * 10
        }

        @Test
        void getBackpressureThreshold_returnsStrategyThreshold() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withBackpressureStrategy(BackpressureStrategy.DROP_OLDEST)
                    .build();
            assertEquals(0.9, config.getBackpressureThreshold());
        }

        @Test
        void shouldCompress_withGzipAndLargePayload() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withCompressionType(CompressionType.GZIP)
                    .build();
            assertTrue(config.shouldCompress(2000));
            assertFalse(config.shouldCompress(500));
        }

        @Test
        void shouldCompress_withNoneCompression() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withCompressionType(CompressionType.NONE)
                    .build();
            assertFalse(config.shouldCompress(10000));
        }

        @Test
        void getExpectedThroughputRate_equalsMaxMessagesPerSecond() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withMaxMessagesPerSecond(5000)
                    .build();
            assertEquals(5000, config.getExpectedThroughputRate());
        }

        @Test
        void getMaxThroughputRate_equalsMaxMessagesPerSecond() {
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withMaxMessagesPerSecond(5000)
                    .build();
            assertEquals(5000, config.getMaxThroughputRate());
        }

        @Test
        void getMaxBufferUtilization_returns80Percent() {
            assertEquals(0.8, new StreamConfiguration.Builder().build().getMaxBufferUtilization());
        }

        @Test
        void getMaxProcessingLag_returns5Seconds() {
            assertEquals(Duration.ofSeconds(5), new StreamConfiguration.Builder().build().getMaxProcessingLag());
        }

        @Test
        void getMaxStaleness_equalsMaxInactivityDuration() {
            Duration dur = Duration.ofMinutes(15);
            StreamConfiguration config = new StreamConfiguration.Builder()
                    .withMaxInactivityDuration(dur)
                    .build();
            assertEquals(dur, config.getMaxStaleness());
        }
    }
}
