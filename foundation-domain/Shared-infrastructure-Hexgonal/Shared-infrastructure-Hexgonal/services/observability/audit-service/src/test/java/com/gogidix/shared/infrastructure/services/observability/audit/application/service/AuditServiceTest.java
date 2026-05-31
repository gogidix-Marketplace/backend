package com.gogidix.shared.infrastructure.services.observability.audit.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.shared.infrastructure.services.observability.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.shared.infrastructure.services.observability.audit.domain.model.AuditLog;
import com.gogidix.shared.infrastructure.services.observability.audit.domain.port.out.AuditLogRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuditService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Audit Service Tests")
class AuditServiceTest {

    @Mock
    private AuditLogRepositoryPort auditLogRepository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @InjectMocks
    private AuditService auditService;

    private static final String TEST_TENANT_ID = "tenant-001";
    private AuditLog testAuditLog;
    private AuditLogResponseDto testResponseDto;

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TEST_TENANT_ID);

        Map<String, Object> changes = new HashMap<>();
        changes.put("status", "ACTIVE");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("ip", "192.168.1.1");

        testAuditLog = AuditLog.builder()
                .id("audit-123")
                .tenantId(TenantId.of(TEST_TENANT_ID))
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
                .changes(changes)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();

        testResponseDto = AuditLogResponseDto.builder()
                .id("audit-123")
                .tenantId(TEST_TENANT_ID)
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
                .changes(changes)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create audit log successfully")
    void shouldCreateAuditLogSuccessfully() {
        CreateAuditLogRequestDto request = new CreateAuditLogRequestDto(
                "user-123",
                "john.doe",
                AuditLog.AuditAction.CREATE,
                AuditLog.AuditEntityType.USER,
                "entity-123",
                "John Doe",
                "Created new user",
                "192.168.1.1",
                "Mozilla/5.0",
                true,
                null,
                new HashMap<>(),
                new HashMap<>()
        );

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        AuditLogResponseDto result = auditService.createAuditLog(request);

        assertNotNull(result);
        assertEquals("user-123", result.userId());
        assertEquals(AuditLog.AuditAction.CREATE, result.action());
        assertEquals(AuditLog.AuditEntityType.USER, result.entityType());
        verify(auditLogRepository).save(any(AuditLog.class));
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should get audit log by ID")
    void shouldGetAuditLogById() {
        when(auditLogRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(testAuditLog));

        AuditLogResponseDto result = auditService.getAuditLog("audit-123");

        assertNotNull(result);
        assertEquals("audit-123", result.id());
        assertEquals("user-123", result.userId());
        verify(auditLogRepository).findByIdAndTenantId("audit-123", TEST_TENANT_ID);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should return null when audit log not found")
    void shouldReturnNullWhenAuditLogNotFound() {
        when(auditLogRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        AuditLogResponseDto result = auditService.getAuditLog("non-existent");

        assertNull(result);
    }

    @Test
    @DisplayName("Should list audit logs with filters")
    void shouldListAuditLogsWithFilters() {
        List<AuditLog> auditLogs = Arrays.asList(testAuditLog);
        when(auditLogRepository.findByTenantIdWithFilters(anyString(), anyString(), any(), any(), anyInt()))
                .thenReturn(auditLogs);

        List<AuditLogResponseDto> result = auditService.listAuditLogs("user-123", null, null, 100);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user-123", result.get(0).userId());
        verify(auditLogRepository).findByTenantIdWithFilters(TEST_TENANT_ID, "user-123", null, null, 100);
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should use default limit when limit is null or zero")
    void shouldUseDefaultLimitWhenLimitIsNullOrZero() {
        when(auditLogRepository.findByTenantIdWithFilters(anyString(), anyString(), any(), any(), anyInt()))
                .thenReturn(Arrays.asList());

        auditService.listAuditLogs("user-123", null, null, null);
        auditService.listAuditLogs("user-123", null, null, 0);

        verify(auditLogRepository, times(2)).findByTenantIdWithFilters(TEST_TENANT_ID, "user-123", null, null, 100);
    }

    @Test
    @DisplayName("Should list audit logs with date range")
    void shouldListAuditLogsWithDateRange() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        lenient().when(auditLogRepository.findByTenantIdWithFilters(anyString(), isNull(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt()))
                .thenReturn(Arrays.asList(testAuditLog));

        List<AuditLogResponseDto> result = auditService.listAuditLogs(null, startDate, endDate, 50);

        assertNotNull(result);
        verify(auditLogRepository).findByTenantIdWithFilters(TEST_TENANT_ID, null, startDate, endDate, 50);
    }

    @Test
    @DisplayName("Should list entity audit logs")
    void shouldListEntityAuditLogs() {
        when(auditLogRepository.findByTenantIdAndEntity(anyString(), anyString(), anyString()))
                .thenReturn(Arrays.asList(testAuditLog));

        List<AuditLogResponseDto> result = auditService.listEntityAuditLogs("USER", "entity-123");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(auditLogRepository).findByTenantIdAndEntity(TEST_TENANT_ID, "USER", "entity-123");
        verify(tenantContextHolder).getRequiredTenantId();
    }

    @Test
    @DisplayName("Should create audit log with failed action")
    void shouldCreateAuditLogWithFailedAction() {
        CreateAuditLogRequestDto request = new CreateAuditLogRequestDto(
                "user-123",
                "john.doe",
                AuditLog.AuditAction.CREATE,
                AuditLog.AuditEntityType.USER,
                null,
                null,
                "Failed to create user",
                "192.168.1.1",
                "Mozilla/5.0",
                false,
                "Validation failed",
                null,
                null
        );

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        AuditLogResponseDto result = auditService.createAuditLog(request);

        assertNotNull(result);
        verify(auditLogRepository).save(argThat(log ->
                !log.isSuccess() &&
                "Validation failed".equals(log.getErrorMessage())
        ));
    }

    @Test
    @DisplayName("Should create audit log for LOGIN action")
    void shouldCreateAuditLogForLoginAction() {
        CreateAuditLogRequestDto request = new CreateAuditLogRequestDto(
                "user-123",
                "john.doe",
                AuditLog.AuditAction.LOGIN,
                null,
                null,
                null,
                "User logged in",
                "192.168.1.1",
                "Mozilla/5.0",
                true,
                null,
                null,
                null
        );

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        AuditLogResponseDto result = auditService.createAuditLog(request);

        assertNotNull(result);
        verify(auditLogRepository).save(argThat(log ->
                log.getAction() == AuditLog.AuditAction.LOGIN
        ));
    }

    @Test
    @DisplayName("Should create audit log for LOGOUT action")
    void shouldCreateAuditLogForLogoutAction() {
        CreateAuditLogRequestDto request = new CreateAuditLogRequestDto(
                "user-123",
                "john.doe",
                AuditLog.AuditAction.LOGOUT,
                null,
                null,
                null,
                "User logged out",
                "192.168.1.1",
                "Mozilla/5.0",
                true,
                null,
                null,
                null
        );

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        AuditLogResponseDto result = auditService.createAuditLog(request);

        assertNotNull(result);
        verify(auditLogRepository).save(argThat(log ->
                log.getAction() == AuditLog.AuditAction.LOGOUT
        ));
    }

    @Test
    @DisplayName("Should handle audit log with null optional fields")
    void shouldHandleAuditLogWithNullOptionalFields() {
        CreateAuditLogRequestDto request = new CreateAuditLogRequestDto(
                "user-123",
                null,
                AuditLog.AuditAction.READ,
                AuditLog.AuditEntityType.TENANT,
                null,
                null,
                null,
                null,
                null,
                true,
                null,
                null,
                null
        );

        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        AuditLogResponseDto result = auditService.createAuditLog(request);

        assertNotNull(result);
        verify(auditLogRepository).save(argThat(log ->
                log.getUserId() != null &&
                log.getUsername() == null &&
                log.getEntityId() == null &&
                log.getEntityName() == null &&
                log.getDescription() == null &&
                log.getIpAddress() == null &&
                log.getUserAgent() == null
        ));
    }

    @Test
    @DisplayName("Should return empty list when no audit logs found")
    void shouldReturnEmptyListWhenNoAuditLogsFound() {
        when(auditLogRepository.findByTenantIdWithFilters(anyString(), anyString(), any(), any(), anyInt()))
                .thenReturn(Arrays.asList());

        List<AuditLogResponseDto> result = auditService.listAuditLogs("user-123", null, null, 100);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should handle null tenant ID in response")
    void shouldHandleNullTenantIdInResponse() {
        AuditLog logWithNullTenant = AuditLog.builder()
                .id("audit-456")
                .tenantId(null)
                .userId("user-456")
                .action(AuditLog.AuditAction.READ)
                .entityType(AuditLog.AuditEntityType.USER)
                .timestamp(LocalDateTime.now())
                .build();

        when(auditLogRepository.findByIdAndTenantId(anyString(), anyString()))
                .thenReturn(Optional.of(logWithNullTenant));

        AuditLogResponseDto result = auditService.getAuditLog("audit-456");

        assertNotNull(result);
        assertNull(result.tenantId());
    }
}
