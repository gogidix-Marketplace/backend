package com.gogidix.transaction.audit.unit.infrastructure.persistence;

import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.infrastructure.persistence.postgres.AuditJpaRepository;
import com.gogidix.transaction.audit.infrastructure.persistence.postgres.PostgresAuditLogRepository;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PostgresAuditLogRepository Unit Tests")
class PostgresAuditLogRepositoryTest {

    @Mock
    private AuditJpaRepository jpaRepository;

    @InjectMocks
    private PostgresAuditLogRepository repository;

    private AuditLog auditLog;
    private UUID auditLogId;

    @BeforeEach
    void setUp() {
        auditLogId = UUID.randomUUID();
        auditLog = AuditLog.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .description("Transaction created")
                .build();
    }

    @Test
    @DisplayName("Should save audit log")
    void shouldSaveAuditLog() {
        // Given
        when(jpaRepository.save(auditLog)).thenReturn(auditLog);

        // When
        AuditLog result = repository.save(auditLog);

        // Then
        assertNotNull(result);
        assertEquals(auditLogId, result.getId());
        verify(jpaRepository).save(auditLog);
    }

    @Test
    @DisplayName("Should find audit log by UUID")
    void shouldFindAuditLogByUUID() {
        // Given
        when(jpaRepository.findById(auditLogId)).thenReturn(Optional.of(auditLog));

        // When
        Optional<AuditLog> result = repository.findById(auditLogId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(auditLogId, result.get().getId());
        verify(jpaRepository).findById(auditLogId);
    }

    @Test
    @DisplayName("Should find audit log by string ID")
    void shouldFindAuditLogByStringId() {
        // Given
        when(jpaRepository.findById(auditLogId)).thenReturn(Optional.of(auditLog));

        // When
        Optional<AuditLog> result = repository.findById(auditLogId.toString());

        // Then
        assertTrue(result.isPresent());
        assertEquals(auditLogId, result.get().getId());
        verify(jpaRepository).findById(auditLogId);
    }

    @Test
    @DisplayName("Should return empty for invalid UUID format")
    void shouldReturnEmptyForInvalidUUIDFormat() {
        // Given
        String invalidId = "not-a-uuid";

        // When
        Optional<AuditLog> result = repository.findById(invalidId);

        // Then
        assertFalse(result.isPresent());
        verify(jpaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Should delete audit log")
    void shouldDeleteAuditLog() {
        // Given
        doNothing().when(jpaRepository).delete(auditLog);

        // When
        repository.delete(auditLog);

        // Then
        verify(jpaRepository).delete(auditLog);
    }

    @Test
    @DisplayName("Should check if audit log exists by ID")
    void shouldCheckIfAuditLogExistsById() {
        // Given
        when(jpaRepository.existsById(auditLogId)).thenReturn(true);

        // When
        boolean result = repository.existsById(auditLogId);

        // Then
        assertTrue(result);
        verify(jpaRepository).existsById(auditLogId);
    }

    @Test
    @DisplayName("Should find audit logs by entity type and entity ID")
    void shouldFindAuditLogsByEntityTypeAndEntityId() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog);
        when(jpaRepository.findByEntityTypeAndEntityId("Transaction", "txn-12345"))
                .thenReturn(auditLogs);

        // When
        List<AuditLog> result = repository.findByEntityTypeAndEntityId("Transaction", "txn-12345");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Transaction", result.get(0).getEntityType());
        verify(jpaRepository).findByEntityTypeAndEntityId("Transaction", "txn-12345");
    }

    @Test
    @DisplayName("Should find audit logs by tenant, entity type and entity ID")
    void shouldFindAuditLogsByTenantAndEntityTypeAndEntityId() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog);
        when(jpaRepository.findByTenantIdAndEntityTypeAndEntityId("tenant-001", "Transaction", "txn-12345"))
                .thenReturn(auditLogs);

        // When
        List<AuditLog> result = repository.findByTenantIdAndEntityTypeAndEntityId(
                "tenant-001", "Transaction", "txn-12345");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("tenant-001", result.get(0).getTenantId());
        verify(jpaRepository).findByTenantIdAndEntityTypeAndEntityId("tenant-001", "Transaction", "txn-12345");
    }

    @Test
    @DisplayName("Should find audit logs by correlation ID")
    void shouldFindAuditLogsByCorrelationId() {
        // Given
        String correlationId = "corr-001";
        List<AuditLog> auditLogs = Arrays.asList(auditLog);
        when(jpaRepository.findByCorrelationId(correlationId)).thenReturn(auditLogs);

        // When
        List<AuditLog> result = repository.findByCorrelationId(correlationId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaRepository).findByCorrelationId(correlationId);
    }

    @Test
    @DisplayName("Should find audit logs by tenant ID and timestamp range")
    void shouldFindAuditLogsByTenantIdAndTimestampBetween() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);
        List<AuditLog> auditLogs = Arrays.asList(auditLog);
        when(jpaRepository.findByTenantIdAndTimestampBetween("tenant-001", startDate, endDate))
                .thenReturn(auditLogs);

        // When
        List<AuditLog> result = repository.findByTenantIdAndTimestampBetween("tenant-001", startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaRepository).findByTenantIdAndTimestampBetween("tenant-001", startDate, endDate);
    }

    @Test
    @DisplayName("Should find audit logs by actor ID")
    void shouldFindAuditLogsByActorId() {
        // Given
        String actorId = "user-001";
        List<AuditLog> auditLogs = Arrays.asList(auditLog);
        when(jpaRepository.findByActorId(actorId)).thenReturn(auditLogs);

        // When
        List<AuditLog> result = repository.findByActorId(actorId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaRepository).findByActorId(actorId);
    }

    @Test
    @DisplayName("Should count audit logs by tenant ID")
    void shouldCountAuditLogsByTenantId() {
        // Given
        Long expectedCount = 42L;
        when(jpaRepository.countByTenantId("tenant-001")).thenReturn(expectedCount);

        // When
        Long result = repository.countByTenantId("tenant-001");

        // Then
        assertEquals(expectedCount, result);
        verify(jpaRepository).countByTenantId("tenant-001");
    }

    @Test
    @DisplayName("Should search audit logs with filters")
    void shouldSearchAuditLogsWithFilters() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog);
        when(jpaRepository.searchAuditLogs(
                eq("tenant-001"), eq("Transaction"), eq("txn-12345"),
                eq("CREATE"), eq("user-001"), eq("INFO"), isNull(),
                eq("SUCCESS"), isNull(), isNull()
        )).thenReturn(auditLogs);

        // When
        List<AuditLog> result = repository.searchAuditLogs(
                "tenant-001", "Transaction", "txn-12345",
                "CREATE", "user-001", "INFO", null,
                "SUCCESS", null, null
        );

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(jpaRepository).searchAuditLogs(
                eq("tenant-001"), eq("Transaction"), eq("txn-12345"),
                eq("CREATE"), eq("user-001"), eq("INFO"), isNull(),
                eq("SUCCESS"), isNull(), isNull()
        );
    }

    @Test
    @DisplayName("Should delete audit logs older than specified date")
    void shouldDeleteOlderThan() {
        // Given
        LocalDateTime cutoffDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        Long expectedCount = 100L;
        when(jpaRepository.countByTimestampBefore(cutoffDate)).thenReturn(expectedCount);
        doNothing().when(jpaRepository).deleteByTimestampBefore(cutoffDate);

        // When
        Long result = repository.deleteOlderThan(cutoffDate);

        // Then
        assertEquals(expectedCount, result);
        verify(jpaRepository).countByTimestampBefore(cutoffDate);
        verify(jpaRepository).deleteByTimestampBefore(cutoffDate);
    }

    @Test
    @DisplayName("Should return empty list when no audit logs match entity")
    void shouldReturnEmptyListWhenNoAuditLogsMatchEntity() {
        // Given
        when(jpaRepository.findByEntityTypeAndEntityId("NonExistent", "id-999"))
                .thenReturn(List.of());

        // When
        List<AuditLog> result = repository.findByEntityTypeAndEntityId("NonExistent", "id-999");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepository).findByEntityTypeAndEntityId("NonExistent", "id-999");
    }

    @Test
    @DisplayName("Should return empty optional when audit log not found by ID")
    void shouldReturnEmptyOptionalWhenAuditLogNotFoundById() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(jpaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Optional<AuditLog> result = repository.findById(nonExistentId);

        // Then
        assertFalse(result.isPresent());
        verify(jpaRepository).findById(nonExistentId);
    }

    @Test
    @DisplayName("Should handle null parameters in search")
    void shouldHandleNullParametersInSearch() {
        // Given
        when(jpaRepository.searchAuditLogs(
                isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull()
        )).thenReturn(List.of());

        // When
        List<AuditLog> result = repository.searchAuditLogs(
                null, null, null, null, null, null, null, null, null, null
        );

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaRepository).searchAuditLogs(
                isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull(), isNull()
        );
    }

    @Test
    @DisplayName("Should return zero count for tenant with no audit logs")
    void shouldReturnZeroCountForTenantWithNoAuditLogs() {
        // Given
        when(jpaRepository.countByTenantId("non-existent-tenant")).thenReturn(0L);

        // When
        Long result = repository.countByTenantId("non-existent-tenant");

        // Then
        assertEquals(0L, result);
        verify(jpaRepository).countByTenantId("non-existent-tenant");
    }

    @Test
    @DisplayName("Should return zero count for delete older than when no records exist")
    void shouldReturnZeroCountForDeleteOlderThanWhenNoRecordsExist() {
        // Given
        LocalDateTime cutoffDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        when(jpaRepository.countByTimestampBefore(cutoffDate)).thenReturn(0L);
        doNothing().when(jpaRepository).deleteByTimestampBefore(cutoffDate);

        // When
        Long result = repository.deleteOlderThan(cutoffDate);

        // Then
        assertEquals(0L, result);
        verify(jpaRepository).countByTimestampBefore(cutoffDate);
        verify(jpaRepository).deleteByTimestampBefore(cutoffDate);
    }

    @Test
    @DisplayName("Should handle UUID parsing edge case")
    void shouldHandleUUIDParsingEdgeCase() {
        // Given - empty string
        String emptyId = "";

        // When
        Optional<AuditLog> result = repository.findById(emptyId);

        // Then
        assertFalse(result.isPresent());
        verify(jpaRepository, never()).findById(any());
    }
}
