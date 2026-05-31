package com.gogidix.monitoring.alertmanagementservice.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AlertHistory Domain Model Tests")
class AlertHistoryTest {

    @Test
    @DisplayName("Should create alert history with builder")
    void shouldCreateAlertHistoryWithBuilder() {
        // Given
        Instant now = Instant.now();

        // When
        AlertHistory history = AlertHistory.builder()
                .id("history-123")
                .alertId("alert-456")
                .tenantId("tenant-1")
                .stateChangeType(AlertHistory.StateChangeType.ACKNOWLEDGED)
                .previousState(Alert.AlertStatus.OPEN.name())
                .newState(Alert.AlertStatus.ACKNOWLEDGED.name())
                .changedBy("user-1")
                .comment("Looking into this")
                .context(Map.of("source", "api"))
                .changedAt(now)
                .build();

        // Then
        assertEquals("history-123", history.getId());
        assertEquals("alert-456", history.getAlertId());
        assertEquals("tenant-1", history.getTenantId());
        assertEquals(AlertHistory.StateChangeType.ACKNOWLEDGED, history.getStateChangeType());
        assertEquals(Alert.AlertStatus.OPEN.name(), history.getPreviousState());
        assertEquals(Alert.AlertStatus.ACKNOWLEDGED.name(), history.getNewState());
        assertEquals("user-1", history.getChangedBy());
        assertEquals("Looking into this", history.getComment());
        assertEquals(Map.of("source", "api"), history.getContext());
        assertEquals(now, history.getChangedAt());
    }

    @Test
    @DisplayName("Should create history with CREATED state change type")
    void shouldCreateHistoryWithCreatedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.CREATED)
                .previousState(null)
                .newState(Alert.AlertStatus.OPEN.name())
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.CREATED, history.getStateChangeType());
        assertEquals(Alert.AlertStatus.OPEN.name(), history.getNewState());
        assertNull(history.getPreviousState());
    }

    @Test
    @DisplayName("Should create history with RESOLVED state change type")
    void shouldCreateHistoryWithResolvedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.RESOLVED)
                .previousState(Alert.AlertStatus.ACKNOWLEDGED.name())
                .newState(Alert.AlertStatus.RESOLVED.name())
                .changedBy("user-1")
                .comment("Fixed the issue")
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.RESOLVED, history.getStateChangeType());
        assertEquals(Alert.AlertStatus.ACKNOWLEDGED.name(), history.getPreviousState());
        assertEquals(Alert.AlertStatus.RESOLVED.name(), history.getNewState());
        assertEquals("user-1", history.getChangedBy());
        assertEquals("Fixed the issue", history.getComment());
    }

    @Test
    @DisplayName("Should create history with CLOSED state change type")
    void shouldCreateHistoryWithClosedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.CLOSED)
                .previousState(Alert.AlertStatus.RESOLVED.name())
                .newState(Alert.AlertStatus.CLOSED.name())
                .changedBy("user-1")
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.CLOSED, history.getStateChangeType());
        assertEquals(Alert.AlertStatus.CLOSED.name(), history.getNewState());
    }

    @Test
    @DisplayName("Should create history with REOPENED state change type")
    void shouldCreateHistoryWithReopenedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.REOPENED)
                .previousState(Alert.AlertStatus.RESOLVED.name())
                .newState(Alert.AlertStatus.OPEN.name())
                .changedBy("system")
                .comment("Threshold exceeded again")
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.REOPENED, history.getStateChangeType());
        assertEquals("system", history.getChangedBy());
        assertEquals("Threshold exceeded again", history.getComment());
    }

    @Test
    @DisplayName("Should create history with COMMENT_ADDED state change type")
    void shouldCreateHistoryWithCommentAddedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.COMMENT_ADDED)
                .previousState(Alert.AlertStatus.OPEN.name())
                .newState(Alert.AlertStatus.OPEN.name())
                .changedBy("user-1")
                .comment("Additional notes")
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.COMMENT_ADDED, history.getStateChangeType());
        assertEquals(Alert.AlertStatus.OPEN.name(), history.getPreviousState());
        assertEquals(Alert.AlertStatus.OPEN.name(), history.getNewState());
    }

    @Test
    @DisplayName("Should create history with NOTIFICATION_SENT state change type")
    void shouldCreateHistoryWithNotificationSentStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.NOTIFICATION_SENT)
                .previousState(Alert.AlertStatus.OPEN.name())
                .newState(Alert.AlertStatus.OPEN.name())
                .changedBy("system")
                .context(Map.of("channel", "email", "recipient", "admin@example.com"))
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.NOTIFICATION_SENT, history.getStateChangeType());
        assertEquals("system", history.getChangedBy());
        assertEquals(Map.of("channel", "email", "recipient", "admin@example.com"), history.getContext());
    }

    @Test
    @DisplayName("Should create history with NOTIFICATION_FAILED state change type")
    void shouldCreateHistoryWithNotificationFailedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.NOTIFICATION_FAILED)
                .previousState(Alert.AlertStatus.OPEN.name())
                .newState(Alert.AlertStatus.OPEN.name())
                .changedBy("system")
                .comment("Failed to send email: connection timeout")
                .context(Map.of("channel", "email", "error", "timeout"))
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.NOTIFICATION_FAILED, history.getStateChangeType());
        assertEquals("Failed to send email: connection timeout", history.getComment());
    }

    @Test
    @DisplayName("Should create history with ESCALATED state change type")
    void shouldCreateHistoryWithEscalatedStateChangeType() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .stateChangeType(AlertHistory.StateChangeType.ESCALATED)
                .previousState(Alert.AlertStatus.OPEN.name())
                .newState(Alert.AlertStatus.OPEN.name())
                .changedBy("system")
                .comment("Escalated to on-call engineer")
                .context(Map.of(
                        "previousSeverity", "HIGH",
                        "newSeverity", "CRITICAL",
                        "escalationLevel", "2"
                ))
                .build();

        // Then
        assertEquals(AlertHistory.StateChangeType.ESCALATED, history.getStateChangeType());
        assertEquals("Escalated to on-call engineer", history.getComment());
        assertEquals(3, history.getContext().size());
    }

    @Test
    @DisplayName("Should set all fields with no-args constructor")
    void shouldSetAllFieldsWithNoArgsConstructor() {
        // Given
        Instant now = Instant.now();
        AlertHistory history = new AlertHistory();

        // When
        history.setId("history-123");
        history.setAlertId("alert-456");
        history.setTenantId("tenant-1");
        history.setStateChangeType(AlertHistory.StateChangeType.CREATED);
        history.setPreviousState("OPEN");
        history.setNewState("ACKNOWLEDGED");
        history.setChangedBy("user-1");
        history.setComment("Test comment");
        history.setContext(Map.of("key", "value"));
        history.setChangedAt(now);

        // Then
        assertEquals("history-123", history.getId());
        assertEquals("alert-456", history.getAlertId());
        assertEquals("tenant-1", history.getTenantId());
        assertEquals(AlertHistory.StateChangeType.CREATED, history.getStateChangeType());
        assertEquals("OPEN", history.getPreviousState());
        assertEquals("ACKNOWLEDGED", history.getNewState());
        assertEquals("user-1", history.getChangedBy());
        assertEquals("Test comment", history.getComment());
        assertEquals(Map.of("key", "value"), history.getContext());
        assertEquals(now, history.getChangedAt());
    }

    @Test
    @DisplayName("Should create history with all fields using all-args constructor")
    void shouldCreateHistoryWithAllFieldsUsingAllArgsConstructor() {
        // Given
        Instant now = Instant.now();

        // When
        AlertHistory history = new AlertHistory(
                "history-123",
                "alert-456",
                "tenant-1",
                AlertHistory.StateChangeType.ACKNOWLEDGED,
                Alert.AlertStatus.OPEN.name(),
                Alert.AlertStatus.ACKNOWLEDGED.name(),
                "user-1",
                "Comment",
                Map.of("key", "value"),
                now
        );

        // Then
        assertEquals("history-123", history.getId());
        assertEquals("alert-456", history.getAlertId());
        assertEquals("tenant-1", history.getTenantId());
        assertEquals(AlertHistory.StateChangeType.ACKNOWLEDGED, history.getStateChangeType());
        assertEquals(Alert.AlertStatus.OPEN.name(), history.getPreviousState());
        assertEquals(Alert.AlertStatus.ACKNOWLEDGED.name(), history.getNewState());
        assertEquals("user-1", history.getChangedBy());
        assertEquals("Comment", history.getComment());
        assertEquals(Map.of("key", "value"), history.getContext());
        assertEquals(now, history.getChangedAt());
    }

    @Test
    @DisplayName("Should handle null context")
    void shouldHandleNullContext() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .context(null)
                .build();

        // Then
        assertNull(history.getContext());
    }

    @Test
    @DisplayName("Should handle empty context")
    void shouldHandleEmptyContext() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .context(Map.of())
                .build();

        // Then
        assertNotNull(history.getContext());
        assertTrue(history.getContext().isEmpty());
    }

    @Test
    @DisplayName("Should handle null comment")
    void shouldHandleNullComment() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .comment(null)
                .build();

        // Then
        assertNull(history.getComment());
    }

    @Test
    @DisplayName("Should handle empty comment")
    void shouldHandleEmptyComment() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .comment("")
                .build();

        // Then
        assertEquals("", history.getComment());
    }

    @Test
    @DisplayName("Should handle system as changed by user")
    void shouldHandleSystemAsChangedByUser() {
        // Given
        AlertHistory history = AlertHistory.builder()
                .changedBy("system")
                .stateChangeType(AlertHistory.StateChangeType.CREATED)
                .build();

        // Then
        assertEquals("system", history.getChangedBy());
        assertEquals(AlertHistory.StateChangeType.CREATED, history.getStateChangeType());
    }

    @Test
    @DisplayName("Should preserve all state change types in enum")
    void shouldPreserveAllStateChangeTypesInEnum() {
        // Given
        AlertHistory.StateChangeType[] types = AlertHistory.StateChangeType.values();

        // Then
        assertEquals(9, types.length);
        assertEquals(AlertHistory.StateChangeType.CREATED, types[0]);
        assertEquals(AlertHistory.StateChangeType.ACKNOWLEDGED, types[1]);
        assertEquals(AlertHistory.StateChangeType.RESOLVED, types[2]);
        assertEquals(AlertHistory.StateChangeType.CLOSED, types[3]);
        assertEquals(AlertHistory.StateChangeType.REOPENED, types[4]);
        assertEquals(AlertHistory.StateChangeType.COMMENT_ADDED, types[5]);
        assertEquals(AlertHistory.StateChangeType.NOTIFICATION_SENT, types[6]);
        assertEquals(AlertHistory.StateChangeType.NOTIFICATION_FAILED, types[7]);
        assertEquals(AlertHistory.StateChangeType.ESCALATED, types[8]);
    }
}
