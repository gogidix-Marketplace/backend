package com.gogidix.transaction.audit.unit.domain;

import com.gogidix.transaction.audit.domain.model.AuditLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLog Domain Model Tests")
class AuditLogTest {

    @Test
    @DisplayName("Should create audit log with builder")
    void shouldCreateAuditLogWithBuilder() {
        // Given
        UUID id = UUID.randomUUID();
        LocalDateTime timestamp = LocalDateTime.now();

        // When
        AuditLog auditLog = AuditLog.builder()
                .id(id)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .timestamp(timestamp)
                .description("Transaction created")
                .build();

        // Then
        assertNotNull(auditLog);
        assertEquals(id, auditLog.getId());
        assertEquals("tenant-001", auditLog.getTenantId());
        assertEquals("Transaction", auditLog.getEntityType());
        assertEquals("txn-12345", auditLog.getEntityId());
        assertEquals("CREATE", auditLog.getAction());
        assertEquals("user-001", auditLog.getActorId());
        assertEquals("USER", auditLog.getActorType());
        assertEquals("INFO", auditLog.getSeverity());
        assertEquals("SUCCESS", auditLog.getStatus());
        assertEquals(timestamp, auditLog.getTimestamp());
        assertEquals("Transaction created", auditLog.getDescription());
    }

    @Test
    @DisplayName("Should return true for critical audit log")
    void shouldReturnTrueForCriticalAuditLog() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .severity("CRITICAL")
                .build();

        // When
        boolean isCritical = auditLog.isCritical();

        // Then
        assertTrue(isCritical);
    }

    @Test
    @DisplayName("Should return false for non-critical audit log")
    void shouldReturnFalseForNonCriticalAuditLog() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .severity("INFO")
                .build();

        // When
        boolean isCritical = auditLog.isCritical();

        // Then
        assertFalse(isCritical);
    }

    @Test
    @DisplayName("Should return true for successful audit log")
    void shouldReturnTrueForSuccessfulAuditLog() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .status("SUCCESS")
                .build();

        // When
        boolean isSuccess = auditLog.isSuccess();

        // Then
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Should return false for failed audit log")
    void shouldReturnFalseForFailedAuditLog() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .status("FAILURE")
                .build();

        // When
        boolean isSuccess = auditLog.isSuccess();

        // Then
        assertFalse(isSuccess);
    }

    @Test
    @DisplayName("Should return true for system actor")
    void shouldReturnTrueForSystemActor() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .actorType("SYSTEM")
                .build();

        // When
        boolean isSystemActor = auditLog.isSystemActor();

        // Then
        assertTrue(isSystemActor);
    }

    @Test
    @DisplayName("Should return true for service actor")
    void shouldReturnTrueForServiceActor() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .actorType("SERVICE")
                .build();

        // When
        boolean isSystemActor = auditLog.isSystemActor();

        // Then
        assertTrue(isSystemActor);
    }

    @Test
    @DisplayName("Should return false for user actor when checking system actor")
    void shouldReturnFalseForUserActorWhenCheckingSystemActor() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .actorType("USER")
                .build();

        // When
        boolean isSystemActor = auditLog.isSystemActor();

        // Then
        assertFalse(isSystemActor);
    }

    @Test
    @DisplayName("Should return true for user actor")
    void shouldReturnTrueForUserActor() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .actorType("USER")
                .build();

        // When
        boolean isUserActor = auditLog.isUserActor();

        // Then
        assertTrue(isUserActor);
    }

    @Test
    @DisplayName("Should return false for system actor when checking user actor")
    void shouldReturnFalseForSystemActorWhenCheckingUserActor() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .actorType("SYSTEM")
                .build();

        // When
        boolean isUserActor = auditLog.isUserActor();

        // Then
        assertFalse(isUserActor);
    }

    @Test
    @DisplayName("Should generate correct summary")
    void shouldGenerateCorrectSummary() {
        // Given
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 10, 30);
        AuditLog auditLog = AuditLog.builder()
                .severity("ERROR")
                .entityType("Payment")
                .action("PROCESS")
                .entityId("pay-001")
                .actorId("system")
                .timestamp(timestamp)
                .build();

        // When
        String summary = auditLog.getSummary();

        // Then
        assertNotNull(summary);
        assertTrue(summary.contains("[ERROR]"));
        assertTrue(summary.contains("Payment"));
        assertTrue(summary.contains("PROCESS"));
        assertTrue(summary.contains("pay-001"));
    }

    @Test
    @DisplayName("Should set default values on pre-persist")
    void shouldSetDefaultValuesOnPrePersist() throws Exception {
        // Given
        AuditLog auditLog = new AuditLog();

        // When
        var onCreateMethod = AuditLog.class.getDeclaredMethod("onCreate");
        onCreateMethod.setAccessible(true);
        onCreateMethod.invoke(auditLog);

        // Then
        assertNotNull(auditLog.getCreatedAt());
        assertNotNull(auditLog.getUpdatedAt());
    }

    @Test
    @DisplayName("Should update timestamp on pre-update")
    void shouldUpdateTimestampOnPreUpdate() throws Exception {
        // Given
        AuditLog auditLog = new AuditLog();
        LocalDateTime originalTimestamp = LocalDateTime.now().minusHours(1);
        auditLog.setUpdatedAt(originalTimestamp);

        // When
        var onUpdateMethod = AuditLog.class.getDeclaredMethod("onUpdate");
        onUpdateMethod.setAccessible(true);
        onUpdateMethod.invoke(auditLog);

        // Then
        assertNotNull(auditLog.getUpdatedAt());
        assertTrue(auditLog.getUpdatedAt().isAfter(originalTimestamp) ||
                   auditLog.getUpdatedAt().isEqual(originalTimestamp));
    }

    @Test
    @DisplayName("Should handle null values in summary")
    void shouldHandleNullValuesInSummary() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .severity(null)
                .entityType("TestEntity")
                .action("TEST")
                .entityId("test-001")
                .actorId(null)
                .timestamp(null)
                .build();

        // When
        String summary = auditLog.getSummary();

        // Then
        assertNotNull(summary);
        assertTrue(summary.contains("[INFO]")); // Default severity
        assertTrue(summary.contains("TestEntity"));
        assertTrue(summary.contains("TEST"));
        assertTrue(summary.contains("test-001"));
    }

    @Test
    @DisplayName("Should create audit log with all optional fields")
    void shouldCreateAuditLogWithAllOptionalFields() {
        // Given
        String oldState = "{\"amount\":100}";
        String newState = "{\"amount\":200}";
        String changedFields = "[\"amount\"]";

        // When
        AuditLog auditLog = AuditLog.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("UPDATE")
                .actorId("user-001")
                .actorType("USER")
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .correlationId("corr-001")
                .timestamp(LocalDateTime.now())
                .oldState(oldState)
                .newState(newState)
                .changedFields(changedFields)
                .businessContext("{\"reason\":\"refund\"}")
                .severity("INFO")
                .category("BUSINESS")
                .description("Transaction amount updated")
                .status("SUCCESS")
                .sessionId("sess-001")
                .requestId("req-001")
                .build();

        // Then
        assertNotNull(auditLog);
        assertEquals(oldState, auditLog.getOldState());
        assertEquals(newState, auditLog.getNewState());
        assertEquals(changedFields, auditLog.getChangedFields());
        assertEquals("192.168.1.1", auditLog.getIpAddress());
        assertEquals("Mozilla/5.0", auditLog.getUserAgent());
        assertEquals("corr-001", auditLog.getCorrelationId());
        assertEquals("sess-001", auditLog.getSessionId());
        assertEquals("req-001", auditLog.getRequestId());
    }

    @Test
    @DisplayName("Should handle case-insensitive severity check")
    void shouldHandleCaseInsensitiveSeverityCheck() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .severity("critical") // lowercase
                .build();

        // When
        boolean isCritical = auditLog.isCritical();

        // Then
        assertTrue(isCritical, "Should handle case-insensitive severity values");
    }

    @Test
    @DisplayName("Should handle case-insensitive status check")
    void shouldHandleCaseInsensitiveStatusCheck() {
        // Given
        AuditLog auditLog = AuditLog.builder()
                .status("success") // lowercase
                .build();

        // When
        boolean isSuccess = auditLog.isSuccess();

        // Then
        assertTrue(isSuccess, "Should handle case-insensitive status values");
    }

    @Test
    @DisplayName("Should set default active and deleted flags")
    void shouldSetDefaultActiveAndDeletedFlags() {
        // Given & When
        AuditLog auditLog = AuditLog.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .build();

        // Then
        assertTrue(auditLog.getIsActive(), "isActive should default to true");
        assertFalse(auditLog.getIsDeleted(), "isDeleted should default to false");
    }
}
