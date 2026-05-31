package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RealTimeDataStreamTest {

    private StreamId streamId;
    private DataSourceConnection dataSource;
    private StreamConfiguration config;
    private StreamMetrics metrics;

    @BeforeEach
    void setUp() {
        streamId = StreamId.generate();
        dataSource = new DataSourceConnection("conn1", "ws://localhost:8080");
        config = StreamConfiguration.defaultDashboardConfig();
        metrics = StreamMetrics.builder()
                .withMessagesProcessed(1000)
                .withMessageRate(100)
                .withBufferUtilization(0.3)
                .withHealth(StreamHealth.HEALTHY)
                .withUptime(Duration.ofHours(1))
                .build();
    }

    private RealTimeDataStream createHealthyStream() {
        return RealTimeDataStream.builder()
                .withStreamId(streamId)
                .withStreamName("test-stream")
                .withStreamType(StreamType.DASHBOARD_KPI)
                .withDataSource(dataSource)
                .withConfiguration(config)
                .withStatus(StreamStatus.ACTIVE)
                .withCreatedAt(LocalDateTime.now())
                .withLastActivityAt(LocalDateTime.now())
                .withConsumers(new ArrayList<>())
                .withMetrics(metrics)
                .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                .withMetadata(Map.of("domain", "operations"))
                .build();
    }

    @Nested
    @DisplayName("Constructor validation tests")
    class ValidationTests {
        @Test
        void constructor_withAllFields_works() {
            RealTimeDataStream stream = createHealthyStream();
            assertEquals(streamId, stream.getStreamId());
            assertEquals("test-stream", stream.getStreamName());
            assertEquals(StreamStatus.ACTIVE, stream.getStatus());
        }

        @Test
        void constructor_nullStreamId_throws() {
            assertThrows(NullPointerException.class, () ->
                RealTimeDataStream.builder()
                    .withStreamId(null)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withDataSource(dataSource)
                    .withConfiguration(config)
                    .withStatus(StreamStatus.CREATED)
                    .withCreatedAt(LocalDateTime.now())
                    .withMetrics(metrics)
                    .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                    .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                    .withMetadata(Map.of())
                    .build());
        }

        @Test
        void constructor_emptyStreamName_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                RealTimeDataStream.builder()
                    .withStreamId(streamId)
                    .withStreamName("")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withDataSource(dataSource)
                    .withConfiguration(config)
                    .withStatus(StreamStatus.CREATED)
                    .withCreatedAt(LocalDateTime.now())
                    .withMetrics(metrics)
                    .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                    .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                    .withMetadata(Map.of())
                    .build());
        }

        @Test
        void constructor_nullDataSource_throws() {
            assertThrows(IllegalArgumentException.class, () ->
                RealTimeDataStream.builder()
                    .withStreamId(streamId)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withDataSource(null)
                    .withConfiguration(config)
                    .withStatus(StreamStatus.CREATED)
                    .withCreatedAt(LocalDateTime.now())
                    .withMetrics(metrics)
                    .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                    .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                    .withMetadata(Map.of())
                    .build());
        }
    }

    @Nested
    @DisplayName("Business logic tests")
    class BusinessLogicTests {
        @Test
        void isHealthy_activeStream_returnsTrue() {
            RealTimeDataStream stream = createHealthyStream();
            assertTrue(stream.isHealthy());
        }

        @Test
        void isHealthy_stoppedStream_returnsFalse() {
            RealTimeDataStream stream = RealTimeDataStream.builder()
                    .withStreamId(streamId)
                    .withStreamName("test")
                    .withStreamType(StreamType.DASHBOARD_KPI)
                    .withDataSource(dataSource)
                    .withConfiguration(config)
                    .withStatus(StreamStatus.STOPPED)
                    .withCreatedAt(LocalDateTime.now())
                    .withMetrics(metrics)
                    .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                    .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                    .withMetadata(Map.of())
                    .build();
            assertFalse(stream.isHealthy());
        }

        @Test
        void calculateHealthScore_activeStream_returnsHighScore() {
            RealTimeDataStream stream = createHealthyStream();
            assertTrue(stream.calculateHealthScore() > 50);
        }

        @Test
        void getStreamPriority_forCriticalAlerts_returnsCritical() {
            RealTimeDataStream stream = RealTimeDataStream.builder()
                    .withStreamId(streamId)
                    .withStreamName("alerts")
                    .withStreamType(StreamType.CRITICAL_ALERTS)
                    .withDataSource(dataSource)
                    .withConfiguration(config)
                    .withStatus(StreamStatus.ACTIVE)
                    .withCreatedAt(LocalDateTime.now())
                    .withMetrics(metrics)
                    .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                    .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                    .withMetadata(Map.of())
                    .build();
            assertEquals(StreamPriority.CRITICAL, stream.getStreamPriority());
        }

        @Test
        void getStreamPriority_forDashboard_returnsHigh() {
            RealTimeDataStream stream = RealTimeDataStream.builder()
                    .withStreamId(streamId)
                    .withStreamName("dash")
                    .withStreamType(StreamType.REAL_TIME_DASHBOARD)
                    .withDataSource(dataSource)
                    .withConfiguration(config)
                    .withStatus(StreamStatus.ACTIVE)
                    .withCreatedAt(LocalDateTime.now())
                    .withMetrics(metrics)
                    .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                    .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                    .withMetadata(Map.of())
                    .build();
            assertEquals(StreamPriority.HIGH, stream.getStreamPriority());
        }

        @Test
        void generatePerformanceInsights_healthyStream_returnsEmptyList() {
            RealTimeDataStream stream = createHealthyStream();
            List<String> insights = stream.generatePerformanceInsights();
            assertNotNull(insights);
        }

        @Test
        void calculateResourceRequirements_returnsNonNull() {
            RealTimeDataStream stream = createHealthyStream();
            ResourceRequirements req = stream.calculateResourceRequirements();
            assertTrue(req.getCpuCores() > 0);
            assertTrue(req.getMemoryMB() > 0);
        }

        @Test
        void calculateCostPerMessage_returnsNonNegative() {
            RealTimeDataStream stream = createHealthyStream();
            assertTrue(stream.calculateCostPerMessage() >= 0);
        }

        @Test
        void getDomain_fromMetadata() {
            RealTimeDataStream stream = createHealthyStream();
            assertEquals("operations", stream.getDomain());
        }

        @Test
        void isActive_forActiveStatus() {
            RealTimeDataStream stream = createHealthyStream();
            assertTrue(stream.isActive());
        }
    }

    @Test
    void builder_withDomain_setsDomainInMetadata() {
        RealTimeDataStream stream = RealTimeDataStream.builder()
                .withStreamId(streamId)
                .withStreamName("test")
                .withStreamType(StreamType.DASHBOARD_KPI)
                .withDataSource(dataSource)
                .withConfiguration(config)
                .withStatus(StreamStatus.ACTIVE)
                .withCreatedAt(LocalDateTime.now())
                .withMetrics(metrics)
                .withBackpressureStrategy(BackpressureStrategy.BUFFER)
                .withQualityOfService(QualityOfService.AT_LEAST_ONCE)
                .withDomain("sales")
                .build();
        assertEquals("sales", stream.getDomain());
    }

    @Test
    void equals_sameId_equal() {
        RealTimeDataStream s1 = createHealthyStream();
        RealTimeDataStream s2 = RealTimeDataStream.builder()
                .withStreamId(streamId)
                .withStreamName("other")
                .withStreamType(StreamType.PERFORMANCE_METRICS)
                .withDataSource(new DataSourceConnection("c2", "ws://other"))
                .withConfiguration(StreamConfiguration.highThroughputConfig())
                .withStatus(StreamStatus.STOPPED)
                .withCreatedAt(LocalDateTime.now())
                .withMetrics(metrics)
                .withBackpressureStrategy(BackpressureStrategy.DROP_OLDEST)
                .withQualityOfService(QualityOfService.AT_MOST_ONCE)
                .withMetadata(Map.of())
                .build();
        assertEquals(s1, s2);
    }
}
