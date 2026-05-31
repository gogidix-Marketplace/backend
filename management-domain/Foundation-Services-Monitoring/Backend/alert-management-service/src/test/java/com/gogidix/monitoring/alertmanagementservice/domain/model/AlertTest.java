package com.gogidix.monitoring.alertmanagementservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Alert Domain Model Tests")
class AlertTest {

    @Test
    @DisplayName("Should create alert with builder")
    void shouldCreateAlertWithBuilder() {
        // Given
        Instant now = Instant.now();

        // When
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .ruleId("rule-456")
                .ruleName("High CPU Usage")
                .serviceName("order-service")
                .metricName("cpu.usage")
                .severity(AlertRule.AlertSeverity.HIGH)
                .status(Alert.AlertStatus.OPEN)
                .message("CPU usage exceeded threshold")
                .triggerValue(85.5)
                .threshold(80.0)
                .triggeredAt(now)
                .notificationStatus(Alert.NotificationStatus.PENDING)
                .notificationAttempts(0)
                .context(Map.of("host", "server-1"))
                .tags(Map.of("environment", "production"))
                .createdAt(now)
                .build();

        // Then
        assertEquals("alert-123", alert.getId());
        assertEquals("tenant-1", alert.getTenantId());
        assertEquals("rule-456", alert.getRuleId());
        assertEquals("High CPU Usage", alert.getRuleName());
        assertEquals("order-service", alert.getServiceName());
        assertEquals("cpu.usage", alert.getMetricName());
        assertEquals(AlertRule.AlertSeverity.HIGH, alert.getSeverity());
        assertEquals(Alert.AlertStatus.OPEN, alert.getStatus());
        assertEquals("CPU usage exceeded threshold", alert.getMessage());
        assertEquals(85.5, alert.getTriggerValue());
        assertEquals(80.0, alert.getThreshold());
        assertEquals(now, alert.getTriggeredAt());
        assertEquals(Alert.NotificationStatus.PENDING, alert.getNotificationStatus());
        assertEquals(0, alert.getNotificationAttempts());
        assertEquals(Map.of("host", "server-1"), alert.getContext());
        assertEquals(Map.of("environment", "production"), alert.getTags());
        assertEquals(now, alert.getCreatedAt());
    }

    @Test
    @DisplayName("Should acknowledge alert")
    void shouldAcknowledgeAlert() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .status(Alert.AlertStatus.OPEN)
                .build();

        // When
        alert.acknowledge("user-1", "Investigating the issue");

        // Then
        assertEquals(Alert.AlertStatus.ACKNOWLEDGED, alert.getStatus());
        assertEquals("user-1", alert.getAcknowledgedBy());
        assertEquals("Investigating the issue", alert.getAcknowledgmentComment());
        assertNotNull(alert.getAcknowledgedAt());
        assertNotNull(alert.getUpdatedAt());
    }

    @Test
    @DisplayName("Should resolve alert")
    void shouldResolveAlert() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .status(Alert.AlertStatus.ACKNOWLEDGED)
                .build();

        // When
        alert.resolve("user-1", "Issue fixed by restarting service");

        // Then
        assertEquals(Alert.AlertStatus.RESOLVED, alert.getStatus());
        assertEquals("user-1", alert.getResolvedBy());
        assertEquals("Issue fixed by restarting service", alert.getResolutionComment());
        assertNotNull(alert.getResolvedAt());
        assertNotNull(alert.getUpdatedAt());
    }

    @Test
    @DisplayName("Should auto-resolve open alert")
    void shouldAutoResolveOpenAlert() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .status(Alert.AlertStatus.OPEN)
                .build();

        // When
        alert.autoResolve();

        // Then
        assertEquals(Alert.AlertStatus.RESOLVED, alert.getStatus());
        assertEquals("system", alert.getResolvedBy());
        assertEquals("Automatically resolved", alert.getResolutionComment());
        assertNotNull(alert.getResolvedAt());
        assertNotNull(alert.getUpdatedAt());
    }

    @Test
    @DisplayName("Should auto-resolve acknowledged alert")
    void shouldAutoResolveAcknowledgedAlert() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .status(Alert.AlertStatus.ACKNOWLEDGED)
                .build();

        // When
        alert.autoResolve();

        // Then
        assertEquals(Alert.AlertStatus.RESOLVED, alert.getStatus());
        assertEquals("system", alert.getResolvedBy());
        assertEquals("Automatically resolved", alert.getResolutionComment());
    }

    @Test
    @DisplayName("Should not auto-resolve already resolved alert")
    void shouldNotAutoResolveResolvedAlert() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .status(Alert.AlertStatus.RESOLVED)
                .resolvedBy("user-1")
                .resolutionComment("Original resolution")
                .build();

        // When
        alert.autoResolve();

        // Then
        assertEquals(Alert.AlertStatus.RESOLVED, alert.getStatus());
        assertEquals("user-1", alert.getResolvedBy());
        assertEquals("Original resolution", alert.getResolutionComment());
    }

    @Test
    @DisplayName("Should not auto-resolve closed alert")
    void shouldNotAutoResolveClosedAlert() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .tenantId("tenant-1")
                .status(Alert.AlertStatus.CLOSED)
                .build();

        // When
        alert.autoResolve();

        // Then
        assertEquals(Alert.AlertStatus.CLOSED, alert.getStatus());
        assertNull(alert.getResolvedBy());
        assertNull(alert.getResolutionComment());
    }

    @Test
    @DisplayName("Should return true when alert is open")
    void shouldReturnTrueWhenAlertIsOpen() {
        // Given
        Alert alert = Alert.builder()
                .status(Alert.AlertStatus.OPEN)
                .build();

        // Then
        assertTrue(alert.isOpen());
    }

    @Test
    @DisplayName("Should return false when alert is not open")
    void shouldReturnFalseWhenAlertIsNotOpen() {
        // Given
        Alert alert = Alert.builder()
                .status(Alert.AlertStatus.RESOLVED)
                .build();

        // Then
        assertFalse(alert.isOpen());
    }

    @Test
    @DisplayName("Should return true when alert is acknowledged")
    void shouldReturnTrueWhenAlertIsAcknowledged() {
        // Given
        Alert alert = Alert.builder()
                .status(Alert.AlertStatus.ACKNOWLEDGED)
                .build();

        // Then
        assertTrue(alert.isAcknowledged());
    }

    @Test
    @DisplayName("Should return false when alert is not acknowledged")
    void shouldReturnFalseWhenAlertIsNotAcknowledged() {
        // Given
        Alert alert = Alert.builder()
                .status(Alert.AlertStatus.OPEN)
                .build();

        // Then
        assertFalse(alert.isAcknowledged());
    }

    @Test
    @DisplayName("Should return true when alert is resolved")
    void shouldReturnTrueWhenAlertIsResolved() {
        // Given
        Alert alert = Alert.builder()
                .status(Alert.AlertStatus.RESOLVED)
                .build();

        // Then
        assertTrue(alert.isResolved());
    }

    @Test
    @DisplayName("Should return false when alert is not resolved")
    void shouldReturnFalseWhenAlertIsNotResolved() {
        // Given
        Alert alert = Alert.builder()
                .status(Alert.AlertStatus.OPEN)
                .build();

        // Then
        assertFalse(alert.isResolved());
    }

    @Test
    @DisplayName("Should return true when notification is sent")
    void shouldReturnTrueWhenNotificationIsSent() {
        // Given
        Alert alert = Alert.builder()
                .notificationStatus(Alert.NotificationStatus.SENT)
                .build();

        // Then
        assertTrue(alert.isNotificationSent());
    }

    @Test
    @DisplayName("Should return false when notification is not sent")
    void shouldReturnFalseWhenNotificationIsNotSent() {
        // Given
        Alert alert = Alert.builder()
                .notificationStatus(Alert.NotificationStatus.PENDING)
                .build();

        // Then
        assertFalse(alert.isNotificationSent());
    }

    @Test
    @DisplayName("Should set default values with no-args constructor")
    void shouldSetDefaultValuesWithNoArgsConstructor() {
        // When
        Alert alert = new Alert();

        // Then
        assertEquals(Alert.AlertStatus.OPEN, alert.getStatus());
        assertEquals(Alert.NotificationStatus.PENDING, alert.getNotificationStatus());
        assertEquals(0, alert.getNotificationAttempts());
    }

    @Test
    @DisplayName("Should create alert with all fields using all-args constructor")
    void shouldCreateAlertWithAllFieldsUsingAllArgsConstructor() {
        // Given
        Instant now = Instant.now();

        // When
        Alert alert = new Alert(
                "alert-123",
                "tenant-1",
                "rule-456",
                "High CPU",
                "order-service",
                "cpu.usage",
                AlertRule.AlertSeverity.HIGH,
                Alert.AlertStatus.OPEN,
                "High CPU alert",
                85.5,
                80.0,
                now,
                now,
                "user-1",
                "Ack comment",
                now,
                "user-2",
                "Res comment",
                Alert.NotificationStatus.SENT,
                2,
                Map.of("key", "value"),
                Map.of("tag", "value"),
                now,
                now
        );

        // Then
        assertEquals("alert-123", alert.getId());
        assertEquals("tenant-1", alert.getTenantId());
        assertEquals(Alert.AlertStatus.OPEN, alert.getStatus());
        assertEquals(Alert.NotificationStatus.SENT, alert.getNotificationStatus());
        assertEquals(2, alert.getNotificationAttempts());
    }

    @Test
    @DisplayName("Should update alert status to suppressed")
    void shouldUpdateAlertStatusToSuppressed() {
        // Given
        Alert alert = Alert.builder()
                .id("alert-123")
                .status(Alert.AlertStatus.OPEN)
                .build();

        // When
        alert.setStatus(Alert.AlertStatus.SUPPRESSED);

        // Then
        assertEquals(Alert.AlertStatus.SUPPRESSED, alert.getStatus());
    }

    @Test
    @DisplayName("Should update notification status to retrying")
    void shouldUpdateNotificationStatusToRetrying() {
        // Given
        Alert alert = Alert.builder()
                .notificationStatus(Alert.NotificationStatus.FAILED)
                .notificationAttempts(1)
                .build();

        // When
        alert.setNotificationStatus(Alert.NotificationStatus.RETRYING);
        alert.setNotificationAttempts(2);

        // Then
        assertEquals(Alert.NotificationStatus.RETRYING, alert.getNotificationStatus());
        assertEquals(2, alert.getNotificationAttempts());
    }
}
