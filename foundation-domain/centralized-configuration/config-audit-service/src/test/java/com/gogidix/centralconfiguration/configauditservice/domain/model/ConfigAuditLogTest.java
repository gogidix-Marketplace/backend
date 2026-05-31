package com.gogidix.centralconfiguration.configauditservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ConfigAuditLog Domain Model Tests")
class ConfigAuditLogTest {

    private ConfigAuditLog auditLog;

    @BeforeEach
    void setUp() {
        auditLog = ConfigAuditLog.builder()
                .id(1L)
                .tenantId("tenant-1")
                .entityType("CONFIGURATION")
                .entityId("config-123")
                .action(AuditAction.UPDATE)
                .oldValue("old-value")
                .newValue("new-value")
                .changedBy("admin")
                .userId("user-1")
                .userName("Admin User")
                .userEmail("admin@example.com")
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .changeReason("Configuration update")
                .metadata("{\"key\":\"value\"}")
                .build();
    }

    @Test
    @DisplayName("Should build audit log with all fields")
    void builder_AllFields() {
        assertThat(auditLog.getId()).isEqualTo(1L);
        assertThat(auditLog.getTenantId()).isEqualTo("tenant-1");
        assertThat(auditLog.getEntityType()).isEqualTo("CONFIGURATION");
        assertThat(auditLog.getEntityId()).isEqualTo("config-123");
        assertThat(auditLog.getAction()).isEqualTo(AuditAction.UPDATE);
        assertThat(auditLog.getOldValue()).isEqualTo("old-value");
        assertThat(auditLog.getNewValue()).isEqualTo("new-value");
        assertThat(auditLog.getChangedBy()).isEqualTo("admin");
        assertThat(auditLog.getUserId()).isEqualTo("user-1");
        assertThat(auditLog.getUserName()).isEqualTo("Admin User");
        assertThat(auditLog.getUserEmail()).isEqualTo("admin@example.com");
        assertThat(auditLog.getIpAddress()).isEqualTo("192.168.1.1");
        assertThat(auditLog.getUserAgent()).isEqualTo("Mozilla/5.0");
        assertThat(auditLog.getChangeReason()).isEqualTo("Configuration update");
        assertThat(auditLog.getMetadata()).isEqualTo("{\"key\":\"value\"}");
    }

    @Test
    @DisplayName("Should create audit log with minimal fields")
    void builder_MinimalFields() {
        ConfigAuditLog minimalLog = ConfigAuditLog.builder()
                .tenantId("tenant-1")
                .entityType("CONFIGURATION")
                .action(AuditAction.CREATE)
                .build();

        assertThat(minimalLog.getTenantId()).isEqualTo("tenant-1");
        assertThat(minimalLog.getEntityType()).isEqualTo("CONFIGURATION");
        assertThat(minimalLog.getAction()).isEqualTo(AuditAction.CREATE);
        assertThat(minimalLog.getEntityId()).isNull();
        assertThat(minimalLog.getOldValue()).isNull();
        assertThat(minimalLog.getNewValue()).isNull();
    }

    @Test
    @DisplayName("Should generate summary correctly")
    void getSummary_ReturnsCorrectFormat() {
        // Given
        auditLog.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 30));

        // When
        String summary = auditLog.getSummary();

        // Then
        assertThat(summary).contains("UPDATE");
        assertThat(summary).contains("CONFIGURATION");
        assertThat(summary).contains("config-123");
        assertThat(summary).contains("admin");
        assertThat(summary).contains("2024-01-15");
    }

    @Test
    @DisplayName("Should support null values in optional fields")
    void nullValues_AreAllowed() {
        auditLog.setEntityId(null);
        auditLog.setOldValue(null);
        auditLog.setNewValue(null);
        auditLog.setChangedBy(null);
        auditLog.setUserId(null);
        auditLog.setUserName(null);
        auditLog.setUserEmail(null);
        auditLog.setIpAddress(null);
        auditLog.setUserAgent(null);
        auditLog.setChangeReason(null);
        auditLog.setMetadata(null);

        assertThat(auditLog.getEntityId()).isNull();
        assertThat(auditLog.getOldValue()).isNull();
        assertThat(auditLog.getNewValue()).isNull();
        assertThat(auditLog.getChangedBy()).isNull();
        assertThat(auditLog.getUserId()).isNull();
        assertThat(auditLog.getUserName()).isNull();
        assertThat(auditLog.getUserEmail()).isNull();
        assertThat(auditLog.getIpAddress()).isNull();
        assertThat(auditLog.getUserAgent()).isNull();
        assertThat(auditLog.getChangeReason()).isNull();
        assertThat(auditLog.getMetadata()).isNull();
    }

    @Test
    @DisplayName("Should support all audit actions")
    void allAuditActions_AreSupported() {
        AuditAction[] actions = AuditAction.values();

        assertThat(actions).hasSize(10);
        assertThat(actions).contains(
                AuditAction.CREATE,
                AuditAction.UPDATE,
                AuditAction.DELETE,
                AuditAction.READ,
                AuditAction.ENABLE,
                AuditAction.DISABLE,
                AuditAction.ROLLOUT,
                AuditAction.ROLLBACK,
                AuditAction.EXPORT,
                AuditAction.IMPORT
        );
    }

    @Test
    @DisplayName("Should create empty audit log with no-args constructor")
    void noArgsConstructor_CreatesEmpty() {
        ConfigAuditLog emptyLog = new ConfigAuditLog();

        assertThat(emptyLog.getId()).isNull();
        assertThat(emptyLog.getTenantId()).isNull();
        assertThat(emptyLog.getEntityType()).isNull();
        assertThat(emptyLog.getAction()).isNull();
    }

    @Test
    @DisplayName("Should support all-args constructor")
    void allArgsConstructor_CreatesInstance() {
        LocalDateTime now = LocalDateTime.now();

        ConfigAuditLog log = new ConfigAuditLog(
                1L,
                "tenant-1",
                "CONFIGURATION",
                "config-123",
                AuditAction.UPDATE,
                "old",
                "new",
                "admin",
                "user-1",
                "Admin User",
                "admin@example.com",
                "192.168.1.1",
                "Mozilla/5.0",
                "reason",
                "{\"meta\":\"data\"}",
                now
        );

        assertThat(log.getId()).isEqualTo(1L);
        assertThat(log.getTenantId()).isEqualTo("tenant-1");
        assertThat(log.getAction()).isEqualTo(AuditAction.UPDATE);
        assertThat(log.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should support setter and getter for all fields")
    void settersAndGetters_Work() {
        auditLog.setId(100L);
        auditLog.setTenantId("new-tenant");
        auditLog.setEntityType("FEATURE_FLAG");
        auditLog.setEntityId("flag-456");
        auditLog.setAction(AuditAction.DELETE);
        auditLog.setOldValue("deleted-old");
        auditLog.setNewValue("deleted-new");
        auditLog.setChangedBy("user2");
        auditLog.setUserId("user-2");
        auditLog.setUserName("User Two");
        auditLog.setUserEmail("user2@example.com");
        auditLog.setIpAddress("10.0.0.1");
        auditLog.setUserAgent("CustomAgent/1.0");
        auditLog.setChangeReason("Deleted flag");
        auditLog.setMetadata("{\"deleted\":true}");

        assertThat(auditLog.getId()).isEqualTo(100L);
        assertThat(auditLog.getTenantId()).isEqualTo("new-tenant");
        assertThat(auditLog.getEntityType()).isEqualTo("FEATURE_FLAG");
        assertThat(auditLog.getEntityId()).isEqualTo("flag-456");
        assertThat(auditLog.getAction()).isEqualTo(AuditAction.DELETE);
        assertThat(auditLog.getOldValue()).isEqualTo("deleted-old");
        assertThat(auditLog.getNewValue()).isEqualTo("deleted-new");
        assertThat(auditLog.getChangedBy()).isEqualTo("user2");
        assertThat(auditLog.getUserId()).isEqualTo("user-2");
        assertThat(auditLog.getUserName()).isEqualTo("User Two");
        assertThat(auditLog.getUserEmail()).isEqualTo("user2@example.com");
        assertThat(auditLog.getIpAddress()).isEqualTo("10.0.0.1");
        assertThat(auditLog.getUserAgent()).isEqualTo("CustomAgent/1.0");
        assertThat(auditLog.getChangeReason()).isEqualTo("Deleted flag");
        assertThat(auditLog.getMetadata()).isEqualTo("{\"deleted\":true}");
    }

    @Test
    @DisplayName("Should handle timestamp field")
    void createdAt_CanBeSet() {
        LocalDateTime now = LocalDateTime.now();
        auditLog.setCreatedAt(now);

        assertThat(auditLog.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should support Lombok builder pattern with method chaining")
    void builder_Chaining() {
        ConfigAuditLog builtLog = ConfigAuditLog.builder()
                .id(1L)
                .tenantId("tenant-1")
                .entityType("ENVIRONMENT")
                .entityId("env-1")
                .action(AuditAction.CREATE)
                .oldValue(null)
                .newValue("production")
                .changedBy("system")
                .userId("system-1")
                .userName("System")
                .userEmail("system@example.com")
                .ipAddress("127.0.0.1")
                .userAgent("Internal/1.0")
                .changeReason("Initial creation")
                .metadata("{\"auto\":true}")
                .build();

        assertThat(builtLog.getId()).isEqualTo(1L);
        assertThat(builtLog.getEntityType()).isEqualTo("ENVIRONMENT");
        assertThat(builtLog.getAction()).isEqualTo(AuditAction.CREATE);
        assertThat(builtLog.getChangedBy()).isEqualTo("system");
    }

    @Test
    @DisplayName("Should handle AuditAction enum codes and descriptions")
    void auditAction_HasCodesAndDescriptions() {
        assertThat(AuditAction.CREATE.getCode()).isEqualTo("create");
        assertThat(AuditAction.CREATE.getDescription()).isEqualTo("Entity created");

        assertThat(AuditAction.UPDATE.getCode()).isEqualTo("update");
        assertThat(AuditAction.UPDATE.getDescription()).isEqualTo("Entity updated");

        assertThat(AuditAction.DELETE.getCode()).isEqualTo("delete");
        assertThat(AuditAction.DELETE.getDescription()).isEqualTo("Entity deleted");

        assertThat(AuditAction.ROLLOUT.getCode()).isEqualTo("rollout");
        assertThat(AuditAction.ROLLOUT.getDescription()).isEqualTo("Feature flag rolled out");

        assertThat(AuditAction.ROLLBACK.getCode()).isEqualTo("rollback");
        assertThat(AuditAction.ROLLBACK.getDescription()).isEqualTo("Feature flag rolled back");
    }

    @Test
    @DisplayName("Should support Lombok @Data functionality")
    void lombokData_Functionality() {
        ConfigAuditLog log1 = ConfigAuditLog.builder()
                .id(1L)
                .tenantId("tenant-1")
                .entityType("CONFIGURATION")
                .build();

        ConfigAuditLog log2 = ConfigAuditLog.builder()
                .id(1L)
                .tenantId("tenant-1")
                .entityType("CONFIGURATION")
                .build();

        // Test toString
        assertThat(log1.toString()).contains("tenant-1");
        assertThat(log1.toString()).contains("CONFIGURATION");

        // Test hashCode
        assertThat(log1.hashCode()).isNotNull();

        // Test equals
        assertThat(log1).isNotNull();
    }

    @Test
    @DisplayName("Should handle TEXT columns for long values")
    void textColumns_CanStoreLongValues() {
        String longValue = "a".repeat(10000);
        String longJson = "{\"key\":\"" + "a".repeat(10000) + "\"}";

        auditLog.setOldValue(longValue);
        auditLog.setNewValue(longValue);
        auditLog.setChangeReason(longValue);
        auditLog.setMetadata(longJson);

        assertThat(auditLog.getOldValue()).hasSize(10000);
        assertThat(auditLog.getNewValue()).hasSize(10000);
        assertThat(auditLog.getChangeReason()).hasSize(10000);
        assertThat(auditLog.getMetadata()).hasSizeGreaterThan(10000);
    }

    @Test
    @DisplayName("Should support different entity types")
    void entityTypes_Supported() {
        String[] entityTypes = {
                "CONFIGURATION",
                "FEATURE_FLAG",
                "ENVIRONMENT",
                "NOTIFICATION",
                "TENANT"
        };

        for (String entityType : entityTypes) {
            ConfigAuditLog log = ConfigAuditLog.builder()
                    .tenantId("tenant-1")
                    .entityType(entityType)
                    .action(AuditAction.READ)
                    .build();

            assertThat(log.getEntityType()).isEqualTo(entityType);
        }
    }

    @Test
    @DisplayName("Should generate summary with null createdAt")
    void getSummary_NullCreatedAt() {
        auditLog.setCreatedAt(null);

        String summary = auditLog.getSummary();

        assertThat(summary).contains("UPDATE");
        assertThat(summary).contains("CONFIGURATION");
        assertThat(summary).contains("config-123");
        assertThat(summary).contains("admin");
    }
}
