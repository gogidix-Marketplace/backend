package com.gogidix.shared.infrastructure.services.observability.audit.domain.model;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuditLog domain model.
 */
@DisplayName("AuditLog Domain Model Tests")
class AuditLogTest {

    @Test
    @DisplayName("Should create audit log with builder")
    void shouldCreateAuditLogWithBuilder() {
        Map<String, Object> changes = new HashMap<>();
        changes.put("status", "ACTIVE");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("ip", "192.168.1.1");

        AuditLog auditLog = AuditLog.builder()
                .id("audit-123")
                .tenantId(TenantId.of("tenant-001"))
                .userId("user-123")
                .username("john.doe")
                .action(AuditLog.AuditAction.CREATE)
                .entityType(AuditLog.AuditEntityType.USER)
                .entityId("entity-123")
                .entityName("John Doe")
                .description("Created new user")
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .success(true)
                .errorMessage(null)
                .changes(changes)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals("audit-123", auditLog.getId());
        assertEquals("tenant-001", auditLog.getTenantId().getValue());
        assertEquals("user-123", auditLog.getUserId());
        assertEquals("john.doe", auditLog.getUsername());
        assertEquals(AuditLog.AuditAction.CREATE, auditLog.getAction());
        assertEquals(AuditLog.AuditEntityType.USER, auditLog.getEntityType());
        assertEquals("entity-123", auditLog.getEntityId());
        assertEquals("John Doe", auditLog.getEntityName());
        assertEquals("Created new user", auditLog.getDescription());
        assertEquals("192.168.1.1", auditLog.getIpAddress());
        assertEquals("Mozilla/5.0", auditLog.getUserAgent());
        assertTrue(auditLog.isSuccess());
        assertNull(auditLog.getErrorMessage());
        assertEquals(changes, auditLog.getChanges());
        assertEquals(metadata, auditLog.getMetadata());
        assertNotNull(auditLog.getTimestamp());
    }

    @Test
    @DisplayName("Should create audit log with default values")
    void shouldCreateAuditLogWithDefaults() {
        AuditLog auditLog = new AuditLog();

        assertNull(auditLog.getId());
        assertNull(auditLog.getTenantId());
        assertNull(auditLog.getUserId());
        assertNull(auditLog.getUsername());
        assertNull(auditLog.getAction());
        assertNull(auditLog.getEntityType());
        assertNull(auditLog.getEntityId());
        assertNull(auditLog.getEntityName());
        assertNull(auditLog.getDescription());
        assertNull(auditLog.getIpAddress());
        assertNull(auditLog.getUserAgent());
        assertFalse(auditLog.isSuccess()); // default for boolean is false
        assertNull(auditLog.getErrorMessage());
        assertNull(auditLog.getChanges());
        assertNull(auditLog.getMetadata());
        assertNull(auditLog.getTimestamp());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllProperties() {
        AuditLog auditLog = new AuditLog();
        Map<String, Object> changes = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();

        auditLog.setId("id-456");
        auditLog.setTenantId(TenantId.of("tenant-789"));
        auditLog.setUserId("user-456");
        auditLog.setUsername("jane.doe");
        auditLog.setAction(AuditLog.AuditAction.UPDATE);
        auditLog.setEntityType(AuditLog.AuditEntityType.TENANT);
        auditLog.setEntityId("entity-456");
        auditLog.setEntityName("Updated Entity");
        auditLog.setDescription("Updated entity");
        auditLog.setIpAddress("10.0.0.1");
        auditLog.setUserAgent("Chrome/1.0");
        auditLog.setSuccess(true);
        auditLog.setErrorMessage(null);
        auditLog.setChanges(changes);
        auditLog.setMetadata(metadata);
        auditLog.setTimestamp(LocalDateTime.now());

        assertEquals("id-456", auditLog.getId());
        assertEquals("tenant-789", auditLog.getTenantId().getValue());
        assertEquals("user-456", auditLog.getUserId());
        assertEquals("jane.doe", auditLog.getUsername());
        assertEquals(AuditLog.AuditAction.UPDATE, auditLog.getAction());
        assertEquals(AuditLog.AuditEntityType.TENANT, auditLog.getEntityType());
        assertEquals("entity-456", auditLog.getEntityId());
        assertEquals("Updated Entity", auditLog.getEntityName());
        assertEquals("Updated entity", auditLog.getDescription());
        assertEquals("10.0.0.1", auditLog.getIpAddress());
        assertEquals("Chrome/1.0", auditLog.getUserAgent());
        assertTrue(auditLog.isSuccess());
        assertEquals(changes, auditLog.getChanges());
        assertEquals(metadata, auditLog.getMetadata());
        assertNotNull(auditLog.getTimestamp());
    }

    @Test
    @DisplayName("Should create audit log with all args constructor")
    void shouldCreateAuditLogWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Object> changes = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();

        AuditLog auditLog = new AuditLog(
                "audit-123",
                TenantId.of("tenant-001"),
                "user-123",
                "john.doe",
                AuditLog.AuditAction.DELETE,
                AuditLog.AuditEntityType.ROLE,
                "entity-123",
                "Role Name",
                "Deleted role",
                "127.0.0.1",
                "Agent",
                true,
                null,
                changes,
                metadata,
                now
        );

        assertEquals("audit-123", auditLog.getId());
        assertEquals(AuditLog.AuditAction.DELETE, auditLog.getAction());
        assertEquals(AuditLog.AuditEntityType.ROLE, auditLog.getEntityType());
    }

    @Test
    @DisplayName("Should handle all AuditAction enum values")
    void shouldHandleAllAuditActionEnums() {
        assertEquals(14, AuditLog.AuditAction.values().length);
        assertEquals(AuditLog.AuditAction.CREATE, AuditLog.AuditAction.valueOf("CREATE"));
        assertEquals(AuditLog.AuditAction.READ, AuditLog.AuditAction.valueOf("READ"));
        assertEquals(AuditLog.AuditAction.UPDATE, AuditLog.AuditAction.valueOf("UPDATE"));
        assertEquals(AuditLog.AuditAction.DELETE, AuditLog.AuditAction.valueOf("DELETE"));
        assertEquals(AuditLog.AuditAction.LOGIN, AuditLog.AuditAction.valueOf("LOGIN"));
        assertEquals(AuditLog.AuditAction.LOGOUT, AuditLog.AuditAction.valueOf("LOGOUT"));
        assertEquals(AuditLog.AuditAction.EXPORT, AuditLog.AuditAction.valueOf("EXPORT"));
        assertEquals(AuditLog.AuditAction.IMPORT, AuditLog.AuditAction.valueOf("IMPORT"));
        assertEquals(AuditLog.AuditAction.APPROVE, AuditLog.AuditAction.valueOf("APPROVE"));
        assertEquals(AuditLog.AuditAction.REJECT, AuditLog.AuditAction.valueOf("REJECT"));
        assertEquals(AuditLog.AuditAction.ASSIGN, AuditLog.AuditAction.valueOf("ASSIGN"));
        assertEquals(AuditLog.AuditAction.UNASSIGN, AuditLog.AuditAction.valueOf("UNASSIGN"));
        assertEquals(AuditLog.AuditAction.ARCHIVE, AuditLog.AuditAction.valueOf("ARCHIVE"));
        assertEquals(AuditLog.AuditAction.RESTORE, AuditLog.AuditAction.valueOf("RESTORE"));
    }

    @Test
    @DisplayName("Should handle all AuditEntityType enum values")
    void shouldHandleAllAuditEntityTypeEnums() {
        assertEquals(8, AuditLog.AuditEntityType.values().length);
        assertEquals(AuditLog.AuditEntityType.USER, AuditLog.AuditEntityType.valueOf("USER"));
        assertEquals(AuditLog.AuditEntityType.ROLE, AuditLog.AuditEntityType.valueOf("ROLE"));
        assertEquals(AuditLog.AuditEntityType.PERMISSION, AuditLog.AuditEntityType.valueOf("PERMISSION"));
        assertEquals(AuditLog.AuditEntityType.TENANT, AuditLog.AuditEntityType.valueOf("TENANT"));
        assertEquals(AuditLog.AuditEntityType.FILE, AuditLog.AuditEntityType.valueOf("FILE"));
        assertEquals(AuditLog.AuditEntityType.NOTIFICATION, AuditLog.AuditEntityType.valueOf("NOTIFICATION"));
        assertEquals(AuditLog.AuditEntityType.CONFIGURATION, AuditLog.AuditEntityType.valueOf("CONFIGURATION"));
        assertEquals(AuditLog.AuditEntityType.SYSTEM, AuditLog.AuditEntityType.valueOf("SYSTEM"));
    }

    @Test
    @DisplayName("Should create audit log for failed action")
    void shouldCreateAuditLogForFailedAction() {
        AuditLog auditLog = AuditLog.builder()
                .action(AuditLog.AuditAction.CREATE)
                .success(false)
                .errorMessage("Validation failed")
                .build();

        assertFalse(auditLog.isSuccess());
        assertEquals("Validation failed", auditLog.getErrorMessage());
    }

    @Test
    @DisplayName("Should create audit log with null optional fields")
    void shouldCreateAuditLogWithNullOptionalFields() {
        AuditLog auditLog = AuditLog.builder()
                .id("audit-123")
                .tenantId(TenantId.of("tenant-001"))
                .userId("user-123")
                .action(AuditLog.AuditAction.READ)
                .entityType(AuditLog.AuditEntityType.USER)
                .timestamp(LocalDateTime.now())
                .build();

        assertEquals("audit-123", auditLog.getId());
        assertNull(auditLog.getUsername());
        assertNull(auditLog.getEntityId());
        assertNull(auditLog.getEntityName());
        assertNull(auditLog.getDescription());
        assertNull(auditLog.getIpAddress());
        assertNull(auditLog.getUserAgent());
        assertNull(auditLog.getErrorMessage());
        assertNull(auditLog.getChanges());
        assertNull(auditLog.getMetadata());
    }

    @Test
    @DisplayName("Should handle LOGIN and LOGOUT actions")
    void shouldHandleLoginAndLogoutActions() {
        AuditLog loginLog = AuditLog.builder()
                .action(AuditLog.AuditAction.LOGIN)
                .userId("user-123")
                .description("User logged in")
                .build();

        AuditLog logoutLog = AuditLog.builder()
                .action(AuditLog.AuditAction.LOGOUT)
                .userId("user-123")
                .description("User logged out")
                .build();

        assertEquals(AuditLog.AuditAction.LOGIN, loginLog.getAction());
        assertEquals(AuditLog.AuditAction.LOGOUT, logoutLog.getAction());
    }

    @Test
    @DisplayName("Should create audit log with changes and metadata")
    void shouldCreateAuditLogWithChangesAndMetadata() {
        Map<String, Object> changes = new HashMap<>();
        changes.put("field1", "oldValue");
        changes.put("field2", "newValue");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("requestId", "req-123");
        metadata.put("sessionId", "sess-456");

        AuditLog auditLog = AuditLog.builder()
                .changes(changes)
                .metadata(metadata)
                .build();

        assertEquals(changes, auditLog.getChanges());
        assertEquals(metadata, auditLog.getMetadata());
        assertEquals("oldValue", auditLog.getChanges().get("field1"));
        assertEquals("req-123", auditLog.getMetadata().get("requestId"));
    }
}
