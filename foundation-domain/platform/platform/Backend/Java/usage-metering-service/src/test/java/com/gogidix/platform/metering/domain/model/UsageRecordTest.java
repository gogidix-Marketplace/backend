package com.gogidix.platform.metering.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

/**
 * Domain model tests for UsageRecord.
 * Tests usage record lifecycle and aggregation eligibility.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UsageRecord Domain Model Tests")
class UsageRecordTest {

    private UsageRecord usageRecord;

    @BeforeEach
    void setUp() {
        usageRecord = UsageRecord.builder()
            .id("record-123")
            .tenantId("tenant-123")
            .metricName("api_requests")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("100"))
            .unit("requests")
            .eventTime(LocalDateTime.now())
            .receivedAt(LocalDateTime.now())
            .dimensions(new HashMap<>())
            .metadata(new HashMap<>())
            .serviceName("api-gateway")
            .resourceId("resource-123")
            .userId("user-123")
            .correlationId("corr-123")
            .build();
    }

    @Test
    @DisplayName("Should identify recent record as aggregatable")
    void isAggregatable_RecentRecord_ReturnsTrue() {
        // Arrange - record is from now (recent)

        // Act
        boolean result = usageRecord.isAggregatable();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should identify record from yesterday as aggregatable")
    void isAggregatable_Yesterday_ReturnsTrue() {
        // Arrange
        usageRecord.setEventTime(LocalDateTime.now().minusDays(1));

        // Act
        boolean result = usageRecord.isAggregatable();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should identify record from 80 days ago as aggregatable")
    void isAggregatable_80DaysOld_ReturnsTrue() {
        // Arrange
        usageRecord.setEventTime(LocalDateTime.now().minusDays(80));

        // Act
        boolean result = usageRecord.isAggregatable();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should identify record from 91 days ago as not aggregatable")
    void isAggregatable_91DaysOld_ReturnsFalse() {
        // Arrange
        usageRecord.setEventTime(LocalDateTime.now().minusDays(91));

        // Act
        boolean result = usageRecord.isAggregatable();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should identify record exactly at retention cutoff as not aggregatable")
    void isAggregatable_Exactly90Days_ReturnsFalse() {
        // Arrange
        usageRecord.setEventTime(LocalDateTime.now().minusDays(90).minusSeconds(1));

        // Act
        boolean result = usageRecord.isAggregatable();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should identify record just before cutoff as aggregatable")
    void isAggregatable_JustBefore90Days_ReturnsTrue() {
        // Arrange
        usageRecord.setEventTime(LocalDateTime.now().minusDays(90).plusSeconds(1));

        // Act
        boolean result = usageRecord.isAggregatable();

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Builder should create valid usage record")
    void builder_ValidUsageRecord() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-456")
            .metricName("storage_bytes")
            .metricType(UsageRecord.MetricType.GAUGE)
            .quantity(new BigDecimal("1024"))
            .unit("bytes")
            .eventTime(LocalDateTime.now())
            .serviceName("storage-service")
            .build();

        // Assert
        assertThat(record.getTenantId()).isEqualTo("tenant-456");
        assertThat(record.getMetricName()).isEqualTo("storage_bytes");
        assertThat(record.getMetricType()).isEqualTo(UsageRecord.MetricType.GAUGE);
        assertThat(record.getQuantity()).isEqualByComparingTo("1024");
        assertThat(record.getUnit()).isEqualTo("bytes");
        assertThat(record.getServiceName()).isEqualTo("storage-service");
    }

    @Test
    @DisplayName("Should handle counter metric type")
    void builder_CounterMetricType() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("total_requests")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("5000"))
            .eventTime(LocalDateTime.now())
            .build();

        // Assert
        assertThat(record.getMetricType()).isEqualTo(UsageRecord.MetricType.COUNTER);
    }

    @Test
    @DisplayName("Should handle gauge metric type")
    void builder_GaugeMetricType() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("current_connections")
            .metricType(UsageRecord.MetricType.GAUGE)
            .quantity(new BigDecimal("42"))
            .eventTime(LocalDateTime.now())
            .build();

        // Assert
        assertThat(record.getMetricType()).isEqualTo(UsageRecord.MetricType.GAUGE);
    }

    @Test
    @DisplayName("Should handle histogram metric type")
    void builder_HistogramMetricType() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("response_time")
            .metricType(UsageRecord.MetricType.HISTOGRAM)
            .quantity(new BigDecimal("125"))
            .unit("ms")
            .eventTime(LocalDateTime.now())
            .build();

        // Assert
        assertThat(record.getMetricType()).isEqualTo(UsageRecord.MetricType.HISTOGRAM);
        assertThat(record.getUnit()).isEqualTo("ms");
    }

    @Test
    @DisplayName("Should handle dimensions map")
    void builder_Dimensions() {
        // Arrange
        HashMap<String, Object> dimensions = new HashMap<>();
        dimensions.put("method", "GET");
        dimensions.put("endpoint", "/api/v1/users");
        dimensions.put("status", "200");

        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("api_requests")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("1"))
            .eventTime(LocalDateTime.now())
            .dimensions(dimensions)
            .build();

        // Assert
        assertThat(record.getDimensions()).hasSize(3);
        assertThat(record.getDimensions().get("method")).isEqualTo("GET");
        assertThat(record.getDimensions().get("endpoint")).isEqualTo("/api/v1/users");
    }

    @Test
    @DisplayName("Should handle metadata map")
    void builder_Metadata() {
        // Arrange
        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("source", "external");
        metadata.put("version", "1.0");

        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("custom_metric")
            .metricType(UsageRecord.MetricType.GAUGE)
            .quantity(new BigDecimal("10"))
            .eventTime(LocalDateTime.now())
            .metadata(metadata)
            .build();

        // Assert
        assertThat(record.getMetadata()).hasSize(2);
        assertThat(record.getMetadata().get("source")).isEqualTo("external");
    }

    @Test
    @DisplayName("Should set receivedAt to current time when null on persist")
    void onCreate_NullReceivedAt_SetsToNow() {
        // Arrange
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("test_metric")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("1"))
            .eventTime(LocalDateTime.now())
            .receivedAt(null)
            .build();

        // Act
        record.onCreate();

        // Assert
        assertThat(record.getReceivedAt()).isNotNull();
        assertThat(record.getReceivedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should not overwrite receivedAt when already set on persist")
    void onCreate_ExistingReceivedAt_DoesNotOverwrite() {
        // Arrange
        LocalDateTime originalTime = LocalDateTime.now().minusHours(1);
        usageRecord.setReceivedAt(originalTime);

        // Act
        usageRecord.onCreate();

        // Assert
        assertThat(usageRecord.getReceivedAt()).isEqualTo(originalTime);
    }

    @Test
    @DisplayName("Should handle large quantity values")
    void builder_LargeQuantity() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("total_bytes_processed")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("999999999999.9999"))
            .eventTime(LocalDateTime.now())
            .build();

        // Assert
        assertThat(record.getQuantity()).isEqualByComparingTo("999999999999.9999");
    }

    @Test
    @DisplayName("Should handle decimal quantity values")
    void builder_DecimalQuantity() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("cpu_usage")
            .metricType(UsageRecord.MetricType.GAUGE)
            .quantity(new BigDecimal("42.8567"))
            .eventTime(LocalDateTime.now())
            .build();

        // Assert
        assertThat(record.getQuantity()).isEqualByComparingTo("42.8567");
    }

    @Test
    @DisplayName("Should handle null service name")
    void builder_NullServiceName() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("metric")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("1"))
            .eventTime(LocalDateTime.now())
            .serviceName(null)
            .build();

        // Assert
        assertThat(record.getServiceName()).isNull();
    }

    @Test
    @DisplayName("Should handle null user ID")
    void builder_NullUserId() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("metric")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("1"))
            .eventTime(LocalDateTime.now())
            .userId(null)
            .build();

        // Assert
        assertThat(record.getUserId()).isNull();
    }

    @Test
    @DisplayName("Should handle empty dimensions")
    void builder_EmptyDimensions() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("metric")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("1"))
            .eventTime(LocalDateTime.now())
            .dimensions(new HashMap<>())
            .build();

        // Assert
        assertThat(record.getDimensions()).isEmpty();
    }

    @Test
    @DisplayName("Should handle correlation ID for tracing")
    void builder_CorrelationId() {
        // Act
        UsageRecord record = UsageRecord.builder()
            .tenantId("tenant-1")
            .metricName("metric")
            .metricType(UsageRecord.MetricType.COUNTER)
            .quantity(new BigDecimal("1"))
            .eventTime(LocalDateTime.now())
            .correlationId("req-abc-123-def")
            .build();

        // Assert
        assertThat(record.getCorrelationId()).isEqualTo("req-abc-123-def");
    }
}
