package com.gogidix.dashboard.realtime.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class StreamMetricsTest {

    @Test
    void builder_createsMetrics() {
        StreamMetrics metrics = StreamMetrics.builder()
                .withMessagesProcessed(1000)
                .withBytesTransferred(50000)
                .withSubscriberCount(5)
                .withMessageRate(100)
                .withBufferUtilization(0.5)
                .withHealth(StreamHealth.HEALTHY)
                .withUptime(Duration.ofHours(2))
                .build();

        assertEquals(1000, metrics.getMessagesProcessed());
        assertEquals(50000, metrics.getBytesTransferred());
        assertEquals(5, metrics.getSubscriberCount());
        assertEquals(100, metrics.getMessageRate());
        assertEquals(0.5, metrics.getBufferUtilization());
        assertEquals(StreamHealth.HEALTHY, metrics.getHealth());
        assertEquals(Duration.ofHours(2), metrics.getUptime());
    }

    @Test
    void getErrorRate_returnsZeroByDefault() {
        assertEquals(0.0, StreamMetrics.builder().build().getErrorRate());
    }

    @Test
    void getThroughputRate_equalsMessageRate() {
        StreamMetrics metrics = StreamMetrics.builder().withMessageRate(500).build();
        assertEquals(500, metrics.getThroughputRate());
    }

    @Test
    void getDataQualityScore_returnsDefault() {
        assertEquals(0.95, StreamMetrics.builder().build().getDataQualityScore());
    }

    @Test
    void getProcessingLag_returnsDefault() {
        assertEquals(Duration.ofMillis(100), StreamMetrics.builder().build().getProcessingLag());
    }

    @Test
    void getAverageLatency_returnsDefault() {
        assertEquals(50L, StreamMetrics.builder().build().getAverageLatency());
    }

    @Test
    void getCpuUtilization_returnsDefault() {
        assertEquals(0.3, StreamMetrics.builder().build().getCpuUtilization());
    }
}
