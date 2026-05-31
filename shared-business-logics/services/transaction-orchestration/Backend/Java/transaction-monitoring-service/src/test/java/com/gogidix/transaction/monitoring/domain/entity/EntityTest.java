package com.gogidix.transaction.monitoring.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Alert Tests")
class AlertTest {

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        Alert a = Alert.builder().title("Test Alert").alertType(Alert.AlertType.PERFORMANCE)
            .severity(Alert.AlertSeverity.WARNING).build();
        assertEquals("Test Alert", a.getTitle());
        assertEquals(Alert.AlertStatus.OPEN, a.getStatus());
        assertNotNull(a.getId());
    }

    @Test
    @DisplayName("Should use no-args constructor")
    void shouldUseNoArgsConstructor() { assertNotNull(new Alert().getId()); }

    @Test
    @DisplayName("Should test enums")
    void shouldTestEnums() {
        assertEquals(8, Alert.AlertType.values().length);
        assertEquals(4, Alert.AlertSeverity.values().length);
        assertEquals(4, Alert.AlertStatus.values().length);
    }

    @Test
    @DisplayName("Should acknowledge")
    void shouldAcknowledge() {
        Alert a = Alert.builder().build();
        a.acknowledge("admin");
        assertEquals(Alert.AlertStatus.ACKNOWLEDGED, a.getStatus());
        assertEquals("admin", a.getAcknowledgedBy());
        assertNotNull(a.getAcknowledgedAt());
    }

    @Test
    @DisplayName("Should resolve")
    void shouldResolve() {
        Alert a = Alert.builder().build();
        a.resolve("admin", "fixed");
        assertEquals(Alert.AlertStatus.RESOLVED, a.getStatus());
        assertEquals("admin", a.getResolvedBy());
        assertEquals("fixed", a.getResolutionNotes());
    }

    @Test
    @DisplayName("Should suppress")
    void shouldSuppress() {
        Alert a = Alert.builder().build();
        a.suppress();
        assertEquals(Alert.AlertStatus.SUPPRESSED, a.getStatus());
    }

    @Test
    @DisplayName("Should isOpen")
    void shouldIsOpen() {
        assertTrue(Alert.builder().status(Alert.AlertStatus.OPEN).build().isOpen());
        assertFalse(Alert.builder().status(Alert.AlertStatus.RESOLVED).build().isOpen());
    }

    @Test
    @DisplayName("Should isCritical")
    void shouldIsCritical() {
        assertTrue(Alert.builder().severity(Alert.AlertSeverity.CRITICAL).build().isCritical());
        assertFalse(Alert.builder().severity(Alert.AlertSeverity.WARNING).build().isCritical());
    }

    @Test
    @DisplayName("Should set dates on onCreate")
    void shouldSetDatesOnCreate() {
        Alert a = new Alert();
        a.onCreate();
        assertNotNull(a.getCreatedAt());
        assertEquals(Alert.AlertStatus.OPEN, a.getStatus());
    }
}

@DisplayName("TransactionMetrics Tests")
class TransactionMetricsTest {

    @Test
    @DisplayName("Should create with builder")
    void shouldCreateWithBuilder() {
        TransactionMetrics m = TransactionMetrics.builder()
            .transactionId(UUID.randomUUID()).metricType(TransactionMetrics.MetricType.RESPONSE_TIME)
            .metricName("resp").metricValue(BigDecimal.valueOf(150.5)).build();
        assertNotNull(m.getId());
        assertEquals(BigDecimal.valueOf(150.5), m.getMetricValue());
    }

    @Test
    @DisplayName("Should test enums")
    void shouldTestEnums() {
        assertEquals(12, TransactionMetrics.MetricType.values().length);
        assertEquals(3, TransactionMetrics.MetricSeverity.values().length);
    }

    @Test
    @DisplayName("Should exceedsWarningThreshold")
    void shouldExceedWarningThreshold() {
        TransactionMetrics m = TransactionMetrics.builder()
            .metricValue(BigDecimal.valueOf(200)).thresholdWarning(BigDecimal.valueOf(100)).build();
        assertTrue(m.exceedsWarningThreshold());
    }

    @Test
    @DisplayName("Should not exceed warning when below")
    void shouldNotExceedWarning() {
        TransactionMetrics m = TransactionMetrics.builder()
            .metricValue(BigDecimal.valueOf(50)).thresholdWarning(BigDecimal.valueOf(100)).build();
        assertFalse(m.exceedsWarningThreshold());
    }

    @Test
    @DisplayName("Should exceedsCriticalThreshold")
    void shouldExceedsCriticalThreshold() {
        TransactionMetrics m = TransactionMetrics.builder()
            .metricValue(BigDecimal.valueOf(500)).thresholdCritical(BigDecimal.valueOf(200)).build();
        assertTrue(m.exceedsCriticalThreshold());
    }

    @Test
    @DisplayName("Should set defaults on onCreate")
    void shouldSetDefaults() {
        TransactionMetrics m = new TransactionMetrics();
        m.onCreate();
        assertNotNull(m.getTimestamp());
        assertEquals(TransactionMetrics.MetricSeverity.NORMAL, m.getSeverity());
    }
}
