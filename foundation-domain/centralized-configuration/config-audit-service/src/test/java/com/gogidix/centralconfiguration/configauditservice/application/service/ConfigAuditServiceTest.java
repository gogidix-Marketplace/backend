package com.gogidix.centralconfiguration.configauditservice.application.service;

import com.gogidix.centralconfiguration.configauditservice.domain.model.AuditAction;
import com.gogidix.centralconfiguration.configauditservice.domain.model.ConfigAuditLog;
import com.gogidix.centralconfiguration.configauditservice.domain.repository.ConfigAuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigAuditService Tests")
class ConfigAuditServiceTest {

    @Mock
    private ConfigAuditLogRepository auditLogRepository;

    @InjectMocks
    private ConfigAuditService configAuditService;

    private ConfigAuditLog testAuditLog;

    @BeforeEach
    void setUp() {
        testAuditLog = ConfigAuditLog.builder()
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
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create audit log successfully")
    void createAuditLog_Success() {
        // Given
        when(auditLogRepository.save(any(ConfigAuditLog.class))).thenReturn(testAuditLog);

        // When
        ConfigAuditLog result = configAuditService.createAuditLog(
                "tenant-1",
                "CONFIGURATION",
                "config-123",
                AuditAction.UPDATE,
                "old-value",
                "new-value",
                "admin",
                "Admin User",
                "admin@example.com",
                "192.168.1.1",
                "Mozilla/5.0",
                "Configuration update",
                "{\"key\":\"value\"}"
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTenantId()).isEqualTo("tenant-1");
        assertThat(result.getEntityType()).isEqualTo("CONFIGURATION");
        assertThat(result.getAction()).isEqualTo(AuditAction.UPDATE);

        verify(auditLogRepository).save(any(ConfigAuditLog.class));
    }

    @Test
    @DisplayName("Should use default tenant when tenantId is null")
    void createAuditLog_NullTenantId_UsesDefault() {
        // Given
        when(auditLogRepository.save(any(ConfigAuditLog.class))).thenReturn(testAuditLog);

        // When
        configAuditService.createAuditLog(
                null,
                "CONFIGURATION",
                "config-123",
                AuditAction.CREATE,
                null,
                "new-value",
                "admin",
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Then
        verify(auditLogRepository).save(argThat(log ->
                "default".equals(log.getTenantId())
        ));
    }

    @Test
    @DisplayName("Should get all audit logs for tenant")
    void getAuditLogs_Success() {
        // Given
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("FEATURE_FLAG")
                        .action(AuditAction.CREATE)
                        .build()
        );

        when(auditLogRepository.findByTenantId("tenant-1")).thenReturn(logs);

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogs("tenant-1");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEntityType()).isEqualTo("CONFIGURATION");
        assertThat(result.get(1).getEntityType()).isEqualTo("FEATURE_FLAG");

        verify(auditLogRepository).findByTenantId("tenant-1");
    }

    @Test
    @DisplayName("Should use default tenant when tenantId is null for getAuditLogs")
    void getAuditLogs_NullTenantId_UsesDefault() {
        // Given
        when(auditLogRepository.findByTenantId("default")).thenReturn(List.of(testAuditLog));

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogs(null);

        // Then
        assertThat(result).hasSize(1);
        verify(auditLogRepository).findByTenantId("default");
    }

    @Test
    @DisplayName("Should get audit logs by entity type")
    void getAuditLogsByEntityType_Success() {
        // Given
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("CONFIGURATION")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditLogRepository.findByTenantIdAndEntityType("tenant-1", "CONFIGURATION"))
                .thenReturn(logs);

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogsByEntityType(
                "tenant-1", "CONFIGURATION");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(log -> "CONFIGURATION".equals(log.getEntityType()));

        verify(auditLogRepository).findByTenantIdAndEntityType("tenant-1", "CONFIGURATION");
    }

    @Test
    @DisplayName("Should use default tenant for getAuditLogsByEntityType")
    void getAuditLogsByEntityType_NullTenantId_UsesDefault() {
        // Given
        when(auditLogRepository.findByTenantIdAndEntityType("default", "CONFIGURATION"))
                .thenReturn(List.of(testAuditLog));

        // When
        configAuditService.getAuditLogsByEntityType(null, "CONFIGURATION");

        // Then
        verify(auditLogRepository).findByTenantIdAndEntityType("default", "CONFIGURATION");
    }

    @Test
    @DisplayName("Should get audit logs by entity")
    void getAuditLogsByEntity_Success() {
        // Given
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("CONFIGURATION")
                        .entityId("config-123")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditLogRepository.findByTenantIdAndEntityTypeAndEntityId(
                "tenant-1", "CONFIGURATION", "config-123")).thenReturn(logs);

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogsByEntity(
                "tenant-1", "CONFIGURATION", "config-123");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(log ->
                "CONFIGURATION".equals(log.getEntityType()) &&
                        "config-123".equals(log.getEntityId())
        );

        verify(auditLogRepository).findByTenantIdAndEntityTypeAndEntityId(
                "tenant-1", "CONFIGURATION", "config-123");
    }

    @Test
    @DisplayName("Should use default tenant for getAuditLogsByEntity")
    void getAuditLogsByEntity_NullTenantId_UsesDefault() {
        // Given
        when(auditLogRepository.findByTenantIdAndEntityTypeAndEntityId(
                "default", "CONFIGURATION", "config-123"))
                .thenReturn(List.of(testAuditLog));

        // When
        configAuditService.getAuditLogsByEntity(null, "CONFIGURATION", "config-123");

        // Then
        verify(auditLogRepository).findByTenantIdAndEntityTypeAndEntityId(
                "default", "CONFIGURATION", "config-123");
    }

    @Test
    @DisplayName("Should get audit logs by date range")
    void getAuditLogsByDateRange_Success() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .action(AuditAction.CREATE)
                        .build()
        );

        when(auditLogRepository.findByTenantIdAndDateRange("tenant-1", startDate, endDate))
                .thenReturn(logs);

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogsByDateRange(
                "tenant-1", startDate, endDate);

        // Then
        assertThat(result).hasSize(2);

        verify(auditLogRepository).findByTenantIdAndDateRange("tenant-1", startDate, endDate);
    }

    @Test
    @DisplayName("Should use default tenant for getAuditLogsByDateRange")
    void getAuditLogsByDateRange_NullTenantId_UsesDefault() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        when(auditLogRepository.findByTenantIdAndDateRange("default", startDate, endDate))
                .thenReturn(List.of(testAuditLog));

        // When
        configAuditService.getAuditLogsByDateRange(null, startDate, endDate);

        // Then
        verify(auditLogRepository).findByTenantIdAndDateRange("default", startDate, endDate);
    }

    @Test
    @DisplayName("Should get audit logs by user")
    void getAuditLogsByUser_Success() {
        // Given
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-2")
                        .changedBy("admin")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditLogRepository.findByChangedBy("admin")).thenReturn(logs);

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogsByUser("admin");

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(log -> "admin".equals(log.getChangedBy()));

        verify(auditLogRepository).findByChangedBy("admin");
    }

    @Test
    @DisplayName("Should create audit log with all action types")
    void createAuditLog_AllActionTypes() {
        AuditAction[] actions = AuditAction.values();

        when(auditLogRepository.save(any(ConfigAuditLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        for (AuditAction action : actions) {
            ConfigAuditLog result = configAuditService.createAuditLog(
                    "tenant-1",
                    "CONFIGURATION",
                    "config-123",
                    action,
                    null,
                    "value",
                    "admin",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            assertThat(result.getAction()).isEqualTo(action);
        }

        verify(auditLogRepository, times(actions.length)).save(any(ConfigAuditLog.class));
    }

    @Test
    @DisplayName("Should handle empty audit log list")
    void getAuditLogs_EmptyList() {
        // Given
        when(auditLogRepository.findByTenantId("tenant-1")).thenReturn(List.of());

        // When
        List<ConfigAuditLog> result = configAuditService.getAuditLogs("tenant-1");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should create audit log with null optional fields")
    void createAuditLog_NullOptionalFields() {
        // Given
        when(auditLogRepository.save(any(ConfigAuditLog.class))).thenReturn(testAuditLog);

        // When
        ConfigAuditLog result = configAuditService.createAuditLog(
                "tenant-1",
                "CONFIGURATION",
                "config-123",
                AuditAction.CREATE,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Then
        assertThat(result).isNotNull();
        verify(auditLogRepository).save(any(ConfigAuditLog.class));
    }

    @Test
    @DisplayName("Should support different entity types")
    void getAuditLogsByEntityType_DifferentEntityTypes() {
        // Given
        List<ConfigAuditLog> configLogs = List.of(testAuditLog);
        List<ConfigAuditLog> flagLogs = List.of(
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("FEATURE_FLAG")
                        .action(AuditAction.ROLLOUT)
                        .build()
        );

        when(auditLogRepository.findByTenantIdAndEntityType("tenant-1", "CONFIGURATION"))
                .thenReturn(configLogs);
        when(auditLogRepository.findByTenantIdAndEntityType("tenant-1", "FEATURE_FLAG"))
                .thenReturn(flagLogs);

        // When
        List<ConfigAuditLog> configs = configAuditService.getAuditLogsByEntityType(
                "tenant-1", "CONFIGURATION");
        List<ConfigAuditLog> flags = configAuditService.getAuditLogsByEntityType(
                "tenant-1", "FEATURE_FLAG");

        // Then
        assertThat(configs).hasSize(1);
        assertThat(flags).hasSize(1);
        assertThat(flags.get(0).getEntityType()).isEqualTo("FEATURE_FLAG");
    }

    @Test
    @DisplayName("Should preserve audit log data through save")
    void createAuditLog_PreservesData() {
        // Given
        when(auditLogRepository.save(any(ConfigAuditLog.class))).thenAnswer(invocation -> {
            ConfigAuditLog log = invocation.getArgument(0);
            log.setId(1L);
            return log;
        });

        // When
        ConfigAuditLog result = configAuditService.createAuditLog(
                "tenant-1",
                "ENVIRONMENT",
                "env-1",
                AuditAction.UPDATE,
                "old",
                "new",
                "user-1",
                "User One",
                "user@example.com",
                "10.0.0.1",
                "TestAgent/1.0",
                "Test reason",
                "{\"meta\":\"data\"}"
        );

        // Then
        assertThat(result.getTenantId()).isEqualTo("tenant-1");
        assertThat(result.getEntityType()).isEqualTo("ENVIRONMENT");
        assertThat(result.getEntityId()).isEqualTo("env-1");
        assertThat(result.getAction()).isEqualTo(AuditAction.UPDATE);
        assertThat(result.getOldValue()).isEqualTo("old");
        assertThat(result.getNewValue()).isEqualTo("new");
        assertThat(result.getChangedBy()).isEqualTo("user-1");
        assertThat(result.getUserName()).isEqualTo("User One");
        assertThat(result.getUserEmail()).isEqualTo("user@example.com");
        assertThat(result.getIpAddress()).isEqualTo("10.0.0.1");
        assertThat(result.getUserAgent()).isEqualTo("TestAgent/1.0");
        assertThat(result.getChangeReason()).isEqualTo("Test reason");
        assertThat(result.getMetadata()).isEqualTo("{\"meta\":\"data\"}");
    }
}
